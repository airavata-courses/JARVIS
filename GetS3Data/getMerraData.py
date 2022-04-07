import json
from datetime import datetime, timedelta
from timeit import default_timer

import numpy as np
from flask import Flask, request, json, send_file, jsonify
from flask_restful import Api, Resource, reqparse

import matplotlib.pyplot as plt
import cartopy.crs as ccrs
import os
from os.path import exists
from netCDF4 import Dataset

import requests
import warnings

warnings.filterwarnings("ignore")

app = Flask(__name__)
api = Api(app)


class MERRAData(Resource):
    def post(self):
        print(request.data)
        if len(request.data) == 0:
            data = json.loads(request.form['body'])
        else:
            data = json.loads(request.data)
        date, time = data['date_time'].split('T')
        d = date.split('-')
        print(d)
        x = get_merra(d[0],d[1])
        return x


class Session_Download(requests.Session):

    AUTH_HOST = 'urs.earthdata.nasa.gov'

    def __init__(self, username, password):
        super().__init__()
        self.auth = (username, password)

    def rebuild_auth(self, prepared_request, response):
        headers = prepared_request.headers
        url = prepared_request.url

        if 'Authorization' in headers:
            original_parsed = requests.utils.urlparse(response.request.url)
            redirect_parsed = requests.utils.urlparse(url)

            if (original_parsed.hostname != redirect_parsed.hostname) and redirect_parsed.hostname != self.AUTH_HOST and original_parsed.hostname != self.AUTH_HOST:
                del headers['Authorization']

        return


def get_merra(year,month):

    username = 'jarvis_2022'
    password = 'Jarvis@22'
    session = Session_Download(username, password)
    
    try:
        file = download_data(session, year, month)
        csv_files = convert_data(file, year, month)
        plot_files = plot_data(file, year, month)
        return jsonify({"img_urls": plot_files,"csv_urls": csv_files, "status":"success"})
    except ValueError as err:
        err_type, err_message = err.args
        return jsonify({"status":"error","message":err_message})
    
def download_data(session, year, month):
    domain = "https://goldsmr4.gesdisc.eosdis.nasa.gov/opendap/MERRA2_MONTHLY/M2TMNXCHM.5.12.4/"
    subdomain=f"{year}/MERRA2_400.tavgM_2d_chm_Nx.{year}{month}.nc4.nc4"
    url = domain + subdomain
    try:
        filename = f"{year}_{month}.nc4"
        if not exists(filename):
            response = session.get(url, stream=True)
            if response.status_code == 404:
                raise ValueError('date', 'Invalid year or month')
            with open(filename, 'wb') as fd:
                for chunk in response.iter_content(chunk_size=1024*1024):
                    fd.write(chunk)
        return filename
    except requests.exceptions.HTTPError as e:
        return "Connection error", 500
    except requests.exceptions.ConnectionError:
        return "Connection error", 500

def convert_data(file, year, month):

    files = []
    data = Dataset(file, more="r")
    lons = data.variables['lon'][:]
    lats = data.variables['lat'][:]
    time = data.variables['time'][:]
    COCL = data.variables['COCL'][:,:,:]
    COCL = COCL[0,:,:]
    COEM = data.variables['COEM'][:,:,:]
    COEM = COEM[0,:,:]
    COLS = data.variables['COLS'][:,:,:]
    COLS = COLS[0,:,:]
    TO3 =  data.variables['TO3'][:,:,:]
    TO3 =  TO3[0,:,:]
    
    filename = f"{year}_{month}_COCL.csv"
    temp = np.array(COCL)
    A = np.vstack([lons, temp])
    lats = np.append(0, lats)
    latsflat = np.reshape(lats, (-1, 1))
    csv_file = np.hstack((latsflat, A))
    np.savetxt(filename, csv_file, delimiter=",")
    files.append(filename)
    
    filename = f"{year}_{month}_COEM.csv"
    temp = np.array(COEM)
    A = np.vstack([lons, temp])
    csv_file = np.hstack((latsflat, A))
    np.savetxt(filename, csv_file, delimiter=",")
    files.append(filename)
    
    filename = f"{year}_{month}_COLS.csv"
    temp = np.array(COLS)
    A = np.vstack([lons, temp])
    csv_file = np.hstack((latsflat, A))
    np.savetxt(filename, csv_file, delimiter=",")
    files.append(filename)
    
    filename = f"{year}_{month}_TO3.csv"
    temp = np.array(TO3)
    A = np.vstack([lons, temp])
    csv_file = np.hstack((latsflat, A))
    np.savetxt(filename, csv_file, delimiter=",")
    files.append(filename)

    data.close()
    
    return files

def plot_data(file, year, month):
    
    files = []
    data = Dataset(file, more="r")
    lons = data.variables['lon'][:]
    lats = data.variables['lat'][:]
    time = data.variables['time'][:]
    COCL = data.variables['COCL'][:,:,:]
    COCL = COCL[0,:,:]
    COEM = data.variables['COEM'][:,:,:]
    COEM = COEM[0,:,:]
    COLS = data.variables['COLS'][:,:,:]
    COLS = COLS[0,:,:]
    TO3 =  data.variables['TO3'][:,:,:]
    TO3 =  TO3[0,:,:]
    d={'COCL':COCL,'COEM':COEM,'COLS':COLS,'TO3':TO3}
    
    for i in ['COCL','COEM','COLS','TO3']:
        fig = plt.figure(figsize=(16,9))
        ax = plt.axes(projection=ccrs.Robinson())
        ax.set_global()
        ax.coastlines(resolution="110m",linewidth=1)
        ax.gridlines(linestyle='--',color='black')
        plt.contourf(lons, lats, d[i], transform=ccrs.PlateCarree(),cmap=plt.cm.jet)
        plt.title(f'MERRA-2 {i} levels, {year} {month}', size=16)
        cb = plt.colorbar(ax=ax, orientation="vertical", pad=0.02, aspect=16, shrink=0.8)
        cb.set_label('Threshold',size=14,rotation=0,labelpad=15)
        cb.ax.tick_params(labelsize=12)
        fname = f"plot_{year}_{month}_{i}.png"
        plt.savefig("/opt/img/"+fname)
        files.append(fname)
    return files


api.add_resource(MERRAData, '/api/merradata')

if __name__ == '__main__':
    app.run(port="3080")
    #app.run(host="0.0.0.0", port="80")

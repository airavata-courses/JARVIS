# Code for reading S3 data is Referenced from https://nbviewer.org/gist/dopplershift/356f2e14832e9b676207
import json
from datetime import datetime, timedelta
from timeit import default_timer

import numpy as np
from flask import Flask, request, json, send_file, jsonify
from flask_restful import Api, Resource, reqparse
from siphon.radarserver import RadarServer

import metpy.plots as mpplots
import matplotlib.pyplot as plt
import cartopy.crs as ccrs
import cartopy.feature as cfeature

ref_norm, ref_cmap = mpplots.ctables.registry.get_with_steps('NWSReflectivity', 5, 5)

app = Flask(__name__)
api = Api(app)


class NexradData(Resource):
    def post(self):
        print(request.data)
        if len(request.data) == 0:
            data = json.loads(request.form['body'])
        else:
            data = json.loads(request.data)
        print(data)
        x = get_for_single_timestamp(data['station'], data['date_time'])
        return x


def raw_to_masked_float(var, data):
    # Values come back signed. If the _Unsigned attribute is set, we need to convert
    # from the range [-127, 128] to [0, 255].
    if var._Unsigned:
        data = data & 255

    # Mask missing points
    data = np.ma.array(data, mask=data == 0)

    # Convert to float using the scale and offset
    return data * var.scale_factor + var.add_offset


def polar_to_cartesian(az, rng):
    az_rad = np.deg2rad(az)[:, None]
    x = rng * np.sin(az_rad)
    y = rng * np.cos(az_rad)
    return x, y


def get_for_single_timestamp(station, date_time):
    start1 = default_timer()
    rs = RadarServer('http://tds-nexrad.scigw.unidata.ucar.edu/thredds/radarServer/nexrad/level2/S3/')

    query = rs.query()
    date, time = date_time.split('T')
    d = date.split('-')
    t = time.split(':')
    date__time = datetime(int(d[0]), int(d[1]), int(d[2]), int(t[0]), int(t[1]), 30)
    query.stations(station).time(date__time)

    d = {}

    f = rs.validate_query(query)

    if f:

        catalog = rs.get_catalog(query)

        if len(catalog.datasets) == 0:
            return jsonify({"status": "error", "message": "No data found for given date-time stamp"})

        data = catalog.datasets[0].remote_access()

        sweep = 0

        ref_var = data.variables['Reflectivity_HI']
        ref_data = ref_var[sweep]
        rng = data.variables['distanceR_HI'][:]
        az = data.variables['azimuthR_HI'][sweep]

        ref = raw_to_masked_float(ref_var, ref_data)
        d['ref_val'] = ref.tolist()
        x, y = polar_to_cartesian(az, rng)

        fig = plt.figure(figsize=(10, 10))
        ax = new_map(fig, data.StationLongitude, data.StationLatitude)
        ax.pcolormesh(x, y, ref, cmap=ref_cmap, norm=ref_norm, zorder=0)

        s = station + '_' + date_time

        fig.savefig("/opt/img/"+s)
        return jsonify({"img_url": s, "status":"success"})

    else:
        return jsonify({"status":"error","message":"No data found for given date-time stamp"})


def new_map(fig, lon, lat):
    # Create projection centered on the radar. This allows us to use x
    # and y relative to the radar.
    proj = ccrs.LambertConformal(central_longitude=lon, central_latitude=lat)

    # New axes with the specified projection
    ax = fig.add_axes([0.02, 0.02, 0.96, 0.96], projection=proj)

    # Add coastlines and states
    ax.add_feature(cfeature.COASTLINE.with_scale('50m'), linewidth=2)
    ax.add_feature(cfeature.STATES.with_scale('50m'))

    return ax


api.add_resource(NexradData, '/api/nexraddata')

if __name__ == '__main__':
    app.run(host="0.0.0.0", port="80")

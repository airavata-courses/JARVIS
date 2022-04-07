FROM continuumio/miniconda3

RUN apt update

RUN conda install -c anaconda flask
RUN conda install -c conda-forge flask-restful
RUN conda install -c conda-forge numpy
RUN conda install -c conda-forge metpy
RUN conda install -c conda-forge netcdf4
COPY ./getNexradData.py ./

CMD ["python", "getMerraData.py"]

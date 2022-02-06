FROM continuumio/anaconda3

RUN apt update

RUN conda install -c anaconda flask
RUN conda install -c conda-forge flask-restful
RUN conda install -c conda-forge numpy
RUN conda install -c conda-forge siphon
RUN conda install -c conda-forge metpy
COPY ./getNexradData.py ./

CMD ["python", "getNexradData.py"]
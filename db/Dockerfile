FROM postgres:12

COPY ./ads_workingv1.3.sql /docker-entrypoint-initdb.d/test.sql
ENV POSTGRES_USER=postgres 
ENV POSTGRES_PASSWORD=password

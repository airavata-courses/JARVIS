all:
	docker run -d -it --net=skynet -v $$(pwd)/dbaccess/ads_workingv1.3.sql:/docker-entrypoint-initdb.d/test.sql \
		--env POSTGRES_USER=postgres \
		--env POSTGRES_PASSWORD=password \
		--name postgres_db postgres:12
	docker build -t db_app ./dbaccess/.
	docker run -d --rm --net=skynet --name db_app db_app

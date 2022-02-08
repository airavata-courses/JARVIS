all:
	docker run -d --rm --net=skynet -v $$(pwd)/dbaccess/ads_workingv1.3.sql:/docker-entrypoint-initdb.d/test.sql \
		--env POSTGRES_USER=postgres \
		--env POSTGRES_PASSWORD=password \
		--name postgres_db postgres:12
	docker build -t dbapp ./dbaccess/.
	docker run -d --rm --net=skynet --name dbapp dbapp

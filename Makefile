all: images
	docker run -d --rm --net=skynet --name postgresdb postgresdb
	docker run -d --rm --net=skynet --name dbapp dbapp

images:
	docker build -t postgresdb ./db/.
	docker build -t dbapp ./dbaccess/.

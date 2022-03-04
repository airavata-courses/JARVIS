all: images
	docker run -d --net=skynet --rm --name staticwebserver staticwebserver

images:
	echo "Building docker for static webserver"
	docker build -t staticwebserver .

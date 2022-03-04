all: images
	# This is used for unit testing so it's fine to use volume mount here
	docker run -d --rm --net=skynet -v $$(pwd)/www:/var/www --name staticwebserver staticwebserver

images:
	echo "Building docker for static webserver"
	docker build -t staticwebserver .

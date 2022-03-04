all: images
	docker run -d --rm --net=skynet --name cacheserver cacheserver

images:
	echo "Building cache server"
	docker build -t cacheserver .

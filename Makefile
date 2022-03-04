all: images
	docker run -d --net=skynet --rm --name mongodb mongo
	docker run -d --net=skynet --rm --name authserver authserver

images:
	echo "Building docker for Auth server"
	docker build -t authserver .

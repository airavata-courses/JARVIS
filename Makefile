all: images
	docker run -d -v $$(pwd)/imgdump/:/opt/img/ --rm --net=skynet --name s3getserver s3getserver

images:
	echo "Building cache server"
	docker build -t s3getserver ./GetS3Data/.

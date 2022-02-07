all:
	echo "Building cache server"
	docker build -t s3get_server ./GetS3Data/.
	docker run -d -v $$(pwd)/imgdump/:/opt/img/ --rm --net=skynet --name s3get_server s3get_server

all: images
	docker run -d -v $$(pwd)/imgdump/:/opt/img/ --rm --net=skynet --name merragetserver merragetserver

images:
	echo "Building merra server"
	docker build -t merragetserver ./GetS3Data/.

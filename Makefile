all:
	echo "Building cache server"
	docker build -t cache_server .
	docker run -d --rm --net=skynet --name cache_server cache_server

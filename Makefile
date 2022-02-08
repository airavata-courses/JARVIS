all:
	echo "Building docker for Auth server"
	docker build -t auth_server .
	docker run -d --net=skynet --rm --name auth_server auth_server

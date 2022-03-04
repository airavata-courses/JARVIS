all: images
	docker run -d --rm --net=skynet --name apigateway -p 80:80 apigateway

images:
	echo "Building docker for api gateway"
	docker build -t apigateway .

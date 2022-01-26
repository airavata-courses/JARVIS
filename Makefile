all:
	echo "Building docker for api gateway"
	docker build -t api_gateway .
	docker run -d --name api_gateway -p 8000:80 api_gateway

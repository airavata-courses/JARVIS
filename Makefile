all: images
	-docker network create skynet
	-docker run -d --net=skynet --name zookeeper -p 2181:2181 --rm wurstmeister/zookeeper
	-docker run -d --net=skynet --name kafka -p 9092:9092 --rm \
		-e KAFKA_ADVERTISED_HOST_NAME=kafka \
		-e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
		-e KAFKA_CREATE_TOPICS="m1:1:3,m2:1:3" \
		wurstmeister/kafka
	-docker run -d --net=skynet --name sender --rm \
		-e DEPLOYMENT=sender \
		kafkasidecar
	-docker run -d --net=skynet --name receiver --rm \
		-e DEPLOYMENT=receiver \
		kafkasidecar
images:
	echo "Building docker for kafka sidecar"
	docker build -t kafkasidecar .

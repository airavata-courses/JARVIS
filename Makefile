netname := skynet
reponame := https://github.com/airavata-courses/JARVIS.git
microservices := staticwebserver mongodb authserver dbapp postgresdb s3getserver cacheserver apigateway
statelessmicroservices := staticwebserver authserver dbapp postgresdb s3getserver cacheserver apigateway
registry := jarvis-master:32000
# branches := a2-static-server a2-authserver a2-webserver-DBAccess a2-s3data a2-cache a2-api-gateway
# For dev test
branches := a2-static-server-dev a2-authserver-dev a2-webserver-DBAccess-dev a2-s3data-dev a2-cache-dev a2-api-gateway-dev
deployments := api-gateway-deployment.yml cache-deployment.yml s3data-deployment.yml authserver-deployment.yml dbaccess-deployment.yml static-server-deployment.yml 

all:
	echo "Options: kubernetes, local"

kubernetes: checkout_code build_docker_images push_docker_images deploy_kubernetes
	echo "Building to deploy to kubernetes"

local: checkout_code create_softlinks build_dockers
	echo "Building project"

checkout_code:
	echo "checking code out"
	mkdir -p build
	-for branch in ${branches} ; do \
		git clone -b $${branch} ${reponame} build/$${branch} ;\
	done

create_softlinks:
	-ln -s $$(pwd)/build/a2-static-server-dev/www/html/assets/img build/a2-s3data-dev/imgdump

build_docker_images:
	echo "Building docker images"
	-for branch in ${branches} ; do \
		make -C build/$${branch} images;\
	done

push_docker_images:
	echo "ReTagging docker images and pushing to registry"
	-for microservice in ${statelessmicroservices} ; do \
		docker tag $${microservice} ${registry}/$${microservice}${IMG_TAG};\
		docker push ${registry}/$${microservice}${IMG_TAG};\
	done

deploy_kubernetes:
	echo "Deploying images to kubernetes"
	-for deployment in ${deployments} ; do \
		kubectl apply -f deployments/$${deployment} ;\
	done

build_dockers:
	echo "Creating docker network"
	-docker network create ${netname};
	echo "Building and starting docker images"
	-for branch in ${branches} ; do \
		make -C build/$${branch} ;\
	done

cleanup_all: cleanup_local
	rm -rf build

cleanup_kube: delete_deployments rm_images
	echo "Cleaning up kubernetes deployments"

delete_deployments:
	-for deployment in ${deployments} ; do \
		kubectl delete -f deployments/$${deployment} ;\
	done

cleanup_local: rm_containers rm_images
	echo "Cleaning up local"
	-docker network rm ${netname}

rm_containers:
	-for mserv in ${microservices}; do\
		docker stop $${mserv};\
	done

rm_images:
	-for microservice in ${statelessmicroservices} ; do \
		docker image rm ${registry}/$${microservice}${IMG_TAG};\
	done
	-for mserv in ${microservices}; do\
		docker image rm $${mserv};\
	done

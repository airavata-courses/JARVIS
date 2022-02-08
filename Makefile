netname := skynet
reponame := git@github.com:airavata-courses/JARVIS.git
microservices := static_webserver s3get_server cache_server api_gateway
branches := a1-static-server a1-s3data a1-cache a1-api-gateway
# For dev test
# branches := a1-static-server-dev a1-s3data-dev a1-cache-dev a1-api-gateway-dev

all: checkout_code create_softlinks build_dockers
	echo "Building project"

checkout_code:
	echo "checking code out"
	mkdir -p build
	-for branch in ${branches} ; do \
		git clone -b $${branch} ${reponame} build/$${branch} ;\
	done

create_softlinks:
	-ln -s $$(pwd)/build/a1-static-server-dev/www/html/assets/img build/a1-s3data-dev/imgdump
	
build_dockers:
	echo "Creating docker network"
	-docker network create ${netname};
	echo "Building and starting docker images"
	-for branch in ${branches} ; do \
		make -C build/$${branch} ;\
	done

cleanup_all: cleanup
	rm -rf build

cleanup: rm_containers rm_images
	echo "Cleaning up"
	-docker network rm ${netname}

rm_containers:
	-for mserv in ${microservices}; do\
		docker stop $${mserv};\
	done

rm_images:
	-for mserv in ${microservices}; do\
		docker image rm $${mserv};\
	done

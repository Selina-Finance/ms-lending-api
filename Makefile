SERVICE := ms-lending-api
DEV_DOCKERFILE := Dockerfile.dev
PORT := 8080

.PHONY: help build

help: ## This help.
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

build_docker:
	docker build -t $(SERVICE) -f $(DEV_DOCKERFILE) .

run_bash:
	docker run -v $$(pwd)/dev/:/spring/cloud/ -e "SPRING_CLOUD_BOOTSTRAP_LOCATION=classpath:/,/spring/cloud/bootstrap-cloud-v2.properties" -p $(PORT):$(PORT) -it $(SERVICE) /bin/bash

run_start:
	docker run -v $$(pwd)/src:/app/src  -p $(PORT):$(PORT) -it $(SERVICE) yarn start

run_ui:
	docker run -p $(PORT):$(PORT) -it $(SERVICE) 
	#yarn storybook

develop:
	docker compose up 

build_story:
	docker build -t $(SERVICE) -f Dockerfile.nginx .


preview:
	helmfile --file helmfile.yaml template --validate --include-crds --output-dir-template /tmp/generate/ 

build: sonarcube
	gradle test
	gradle build --no-daemon
	rm -f build/libs/*-plain.jar  

# Section for Java Lib
build_lib:
	#gradle test
	gradle build -Dversion=${VERSION} --no-daemon

publish:
	gradle publish -Dversion=${VERSION}

sonarcube:
	gradle sonarqube -Dsonar.pullrequest.key=${PULL_NUMBER} -Dsonar.pullrequest.branch=${PR_HEAD_REF}
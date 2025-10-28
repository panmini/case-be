ENV ?= dev
VERSION ?= latest
IMAGE_NAME ?= thy

REGISTRY :=

BASE_COMPOSE = docker/common/docker-compose.base.yml
ENV_COMPOSE = docker/$(ENV).docker-compose.yml
DC = docker-compose -f $(BASE_COMPOSE) -f $(ENV_COMPOSE)

IMAGE_TAG = $(REGISTRY)/$(IMAGE_NAME):$(VERSION)
IMAGE_LATEST = $(REGISTRY)/$(IMAGE_NAME):latest

.PHONY: up down build restart logs build-image buildx-image build-and-push-image clean tag

up:
	$(DC) up -d

down:
	$(DC) down

build:
	$(DC) build

restart:
	$(MAKE) down ENV=$(ENV)
	$(MAKE) up ENV=$(ENV)

logs:
	$(DC) logs -f

build-image:
	docker build -t $(IMAGE_TAG) -f Dockerfile .

buildx-image:
	docker buildx build --platform linux/amd64 -t $(IMAGE_TAG) -f Dockerfile .

build-and-push-image:
	$(MAKE) buildx-image ENV=$(ENV) VERSION=$(VERSION)
	$(MAKE) tag ENV=$(ENV) VERSION=$(VERSION)
	docker push $(IMAGE_TAG)
	docker push $(IMAGE_LATEST)

tag:
	docker tag $(IMAGE_TAG) $(IMAGE_LATEST)

clean:
	docker system prune -f
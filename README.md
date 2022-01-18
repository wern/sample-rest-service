# sample-rest-service
Simple SpringBoot REST service to play around with Docker and K8s. Uses [`wern/sample-time-service`](https://github.com/wern/sample-time-service) to illustrate cross container communication.

Corresponding container image on Docker Hub: [`werneb/sample-rest-service`](https://hub.docker.com/repository/docker/werneb/sample-rest-service)

## Get it up and running with Docker only
1. Install Docker and Docker-Compose
1. Clone Repository
1. Start everything via `docker-compose up -d`
1. Access service using `curl http://localhost:8080/sampleservice` (sample-rest-service only)
1. Access service using `curl http://localhost:8080/sampletimeservice` (sample-rest-service with cascading call to sample-time-service)


## Get it up and running with Docker and Kubernetes (e.g. minikube)
1. Install Docker and minikube
1. Clone Repository
1. Start minikube via `minikube start`
1. Apply Kubernetes config via `kubectl apply -f k8s.yml`
1. Start network tunnel via `minikube service --url sample-rest-service`
1. Access service using `curl http://localhost:{tunnelPort}/sampleservice` (sample-rest-service only)
1. Access service using `curl http://localhost:{tunnelPort}/sampletimeservice` (sample-rest-service with cascading call to sample-time-service)

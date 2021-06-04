To build the docker:

DOCKER_SCAN_SUGGEST=false docker-compose build -q --parallel

if some error occur on building:
DOCKER_SCAN_SUGGEST=false docker-compose build 


To start the docker with integrated tests:
COMPOSE_PROFILES=test docker-compose up

Abbort if test fails:
COMPOSE_PROFILES=test docker compose up --abort-on-container-exit

without test:
docker-compose up
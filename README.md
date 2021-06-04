To build the docker without tests:
DOCKER_SCAN_SUGGEST=false docker-compose build -q --parallel

if some error occur on building: (not quiet to see the log)
DOCKER_SCAN_SUGGEST=false docker-compose build 

Run without test:
docker-compose up


To build with tests: (docker compose (1.26+ is needed))

DOCKER_SCAN_SUGGEST=false COMPOSE_PROFILES=test docker-compose build -q --parallel
To start the docker with integrated tests:
COMPOSE_PROFILES=test docker-compose up

Abbort if test fails: (fot gitlab ci)
COMPOSE_PROFILES=test docker compose up --abort-on-container-exit


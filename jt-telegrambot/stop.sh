#!/bin/bash

# Ensure, that docker-compose stopped
docker-compose stop

# Ensure, that the old application won't be deployd again
mvn clean

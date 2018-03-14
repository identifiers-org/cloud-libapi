# This Makefile helps con managing environments and deployment of this library

#														#
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>	#
#														#

# Environment
docker_compose_development_file = docker-compose-development.yml
tag_version = `cat VERSION`

# Targets
development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker-compose -f $(docker_compose_development_file) up -d
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_down
	@touch development_env_up

development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker-compose -f $(docker_compose_development_file) down
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_up
	@touch development_env_down

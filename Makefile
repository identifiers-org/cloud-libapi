# This Makefile helps con managing environments and deployment of this library

#														#
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>	#
#														#

# Environment
docker_compose_development_file = docker-compose-development.yml
tag_version = $(shell cat VERSION)

# Targets
all: deploy

release: deploy set_next_development_version
	@echo "<===|DEVOPS|===> [DEPLOY] Deploying library to Maven Respository"
	@git add pom.xml
	@git commit -am "Next project development version prepared"
	@git push

sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${tag_version}'"
	@mvn versions:set -DnewVersion=${tag_version}

set_next_development_version:
	@echo "<===|DEVOPS|===> [SYNC] Setting the new development version, current ${tag_version}"
	@mvn versions:set -DnewVersion=$(shell ./increment_version.sh -p ${tag_version})-SNAPSHOT

deploy: clean
	@echo "<===|DEVOPS|===> [DEPLOY] Deploying library to Maven Respository"
	@mvn clean deploy -P ossrh -DskipTests

development_env_up: tmp
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

development_run_tests: development_env_up
	@echo "<===|DEVOPS|===> [TESTS] Running Unit Tests"
	@mvn clean test

clean:
	@echo "<===|DEVOPS|===> [CLEAN] Cleaning the space"
	@mvn clean > /dev/null
	@mvn versions:commit

# Folders
tmp:
	@echo "<===|DEVOPS|===> [FOLDER] Creating temporary folders"
	@mkdir -p tmp/fakesmtp

clean_tmp:
	@echo "<===|DEVOPS|===> [HOUSEKEEPING] Cleaning temporary folders"
	@rm -rf tmp

.PHONY: all clean clean_tmp deploy release sync_project_version set_next_development_version development_run_tests

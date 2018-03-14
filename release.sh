#!/bin/bash
#
# This script is a helper in the release cycle of the containerized service
#
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Defaults
verb=nothing
message=""
version=$(cat VERSION)

if [ $# -lt 1 ]
then
  echo "usage: $(basename $0) patch|minor|major major.minor.patch"
  exit 1
fi

# Read verb
verb=$1
# Read the possible message
if [ $# -gt 1 ]
then
    message=$2
fi
# Everything ok?
ok=false

# Process release
if [ "${verb}" == "patch" ]
then
    echo "<===|DEVOPS|===> [RELEASE] PATCH"
    echo -e "\tCurrent version '${version}'"
    version=$(./increment_version.sh -p ${version})
    echo -e "\tNew version '${version}'"
    echo "${version}" > VERSION
    ok=true
fi
if [ "${verb}" == "minor" ]
then
    echo "<===|DEVOPS|===> [RELEASE] MINOR"
    echo -e "\tCurrent version '${version}'"
    version=$(./increment_version.sh -m ${version})
    echo -e "\tNew version '${version}'"
    echo "${version}" > VERSION
    ok=true
fi
if [ "${verb}" == "major" ]
then
    echo "<===|DEVOPS|===> [RELEASE] MAJOR"
    echo -e "\tCurrent version '${version}'"
    version=$(./increment_version.sh -M ${version})
    echo -e "\tNew version '${version}'"
    echo "${version}" > VERSION
    ok=true
fi

# Should we publish the changes?
if $ok ; then
    echo -e "\tCommit, push and tag version"
    git add VERSION
    if [ "${message}" != "" ] ; then
        echo -e "\tVersion Tag message: ${message}"
    else
        echo -e "\tNO VERSION tag message included"
        message="New release ${version}"
    fi
    git commit -m "${message}"
    git tag ${version} -m "${message}"
    git push origin ${version}
    # Pack the new release
    make
else
    echo -e "\t--- ABORT --- Something went wrong"
fi

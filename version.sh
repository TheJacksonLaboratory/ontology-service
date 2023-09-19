#!/bin/bash
OS=`uname`
# This script allows up to quickly update versions where they are needed.
PROJECT_VERSION=$(./gradlew properties -q | awk '/^version:/ {print $2}')
if [ "$OS" = 'Darwin' ]; then
        # for MacOS
        sed -i '' "s/version = .*/version = \"${PROJECT_VERSION}\",/g" src/main/java/org/jacksonlaboratory/Application.java

    else
        # for Linux and Windows
        sed -i "s/version = .*/version = \"${PROJECT_VERSION}\"/g" src/main/java/org/jacksonlaboratory/Application.java
fi



#!/bin/bash

# API Launcher
cd "$( dirname "${BASH_SOURCE[0]}" )"
java -cp "./api.rest.jar:./api.rest.properties:./" com.api.rest.main.App

#!/bin/bash

rm -R dist
mkdir dist
mvn -X compile
mvn -X package
cp ./target/api.rest*with-dependencies.jar ./dist
cp ./config/* ./dist
mv ./dist/api.rest-*.jar ./dist/api.rest.jar

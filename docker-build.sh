#!/bin/bash
lein uberjar
docker build . -t rest-demo-app

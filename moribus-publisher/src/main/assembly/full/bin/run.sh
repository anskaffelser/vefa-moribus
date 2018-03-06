#!/bin/sh

APP=$(dirname $(readlink -f "$0"))/..

java -classpath $APP:$APP/lib/* no.difi.vefa.moribus.Main $@
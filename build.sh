#!/bin/bash

BUILD=build/classes
SRC=src/main/java/xss

javac -d $BUILD `find $SRC -name *.java`

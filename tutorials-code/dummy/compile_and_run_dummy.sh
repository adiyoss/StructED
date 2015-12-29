#!/bin/bash
if [ -d bin ]; then
	rm -rf bin 
fi
mkdir bin
javac -d bin -sourcepath . -cp ../../bin/StructED_1.0.1.jar DummyTutorial.java
java -cp bin:../../bin/StructED_1.0.1.jar com.tutorials.DummyTutorial
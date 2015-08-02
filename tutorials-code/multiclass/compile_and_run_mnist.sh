#!/bin/bash
if [ -d bin ]; then
	rm -rf bin 
fi
mkdir bin
javac -d bin -sourcepath . -cp ../../bin/StructED_1.0.1.jar MulticlassMNIST.java
java -Xms2048m -Xmx2048m -cp bin:../../bin/StructED_1.0.1.jar com.tutorials.MulticlassMNIST
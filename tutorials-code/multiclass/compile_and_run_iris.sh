#!/bin/bash
mkdir bin
javac -d bin -sourcepath . -cp ../../bin/StructED_1.0.1.jar MulticlassIRIS.java
java -Xms2048m -Xmx2048m -cp bin:../../bin/StructED_1.0.1.jar com.tutorials.MulticlassIRIS
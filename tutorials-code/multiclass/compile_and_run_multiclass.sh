#!/bin/bash
mkdir bin
javac -d bin -sourcepath . -cp ../../bin/StructED_1.0.1.jar MulticlassTutorial.java
java -cp bin:../../bin/StructED_1.0.1.jar com.tutorials.MulticlassTutorial
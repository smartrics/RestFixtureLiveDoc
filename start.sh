#!/bin/bash
CLASSPATH=''
for f in target/dependencies/*.jar
do
 CLASSPATH="${f}:${CLASSPATH}"
done

echo ${CLASSPATH}
 
java -cp ${CLASSPATH} fitnesseMain.FitNesseMain -v -p 9090 -d src/main/resources -e 0 -b output.log


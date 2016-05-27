setLocal EnableDelayedExpansion
set CLASSPATH="
for /R target\dependencies %%a in (*.jar) do (
   set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"
echo !CLASSPATH!
 
java -cp !CLASSPATH! fitnesseMain.FitNesseMain -v -p 9090 -d src\main\resources -e 0 -b output.log


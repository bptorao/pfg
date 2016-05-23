rem Modo uso: ETL_PreProceso_MNIST.sh


@echo off
setLocal EnableDelayedExpansion
set CLASSPATH=.
for /R lib %%a in (*.jar) do (
	set CLASSPATH=!CLASSPATH!;"%%a"
)
set CLASSPATH=!CLASSPATH!

java -cp %CLASSPATH% es.uem.etl.PreProceso

pause
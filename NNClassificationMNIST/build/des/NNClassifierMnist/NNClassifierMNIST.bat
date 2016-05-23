rem Modo uso: Weka Classifiers - NeuralNetwork


@echo off
setLocal EnableDelayedExpansion
set CLASSPATH=.
for /R lib %%a in (*.jar) do (
	set CLASSPATH=!CLASSPATH!;"%%a"
)
set CLASSPATH=!CLASSPATH!

java -Xmx2048M -cp %CLASSPATH% bptg.uem.pfg.wclassifiers.NNClassificationMNIST

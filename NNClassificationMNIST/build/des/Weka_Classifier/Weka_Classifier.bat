rem Modo uso: Weka Classifiers - NeuralNetwork


@echo off
setLocal EnableDelayedExpansion
set CLASSPATH=.
for /R lib %%a in (*.jar) do (
	set CLASSPATH=!CLASSPATH!;"%%a"
)
set CLASSPATH=!CLASSPATH!

java -cp %CLASSPATH% bptg.uem.pfg.wclassifiers.NNClassificationMNIST

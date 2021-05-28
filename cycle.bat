@echo off

call gradlew.bat jar
call update.bat %1
call build\nodes\runnodes.bat



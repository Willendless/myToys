@echo off


mkdir .\build
pushd .\build
cl /Zi ..\donkeyDB.c
.\donkeyDB.exe
popd .\build

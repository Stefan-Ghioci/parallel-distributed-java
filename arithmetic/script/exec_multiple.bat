@echo off  
for /L %%N in (1,1,%6) do "C:\Program Files\Java\jdk-11.0.2\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.5\lib\idea_rt.jar=51060:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.5\bin" -Dfile.encoding=UTF-8 -classpath %1 %2 %3 %4 %5 %%N
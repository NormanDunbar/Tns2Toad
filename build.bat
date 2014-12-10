call antlr4 tnsnames.g4
javac *.java

jar -cvf test\tns2toad.jar *.class > nul

del *.class
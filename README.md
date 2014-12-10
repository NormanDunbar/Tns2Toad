# Tns2Toad

Convert a tnsnames.ora into a file suitable for Toad to import into its connections grid.


## File List

  - LICENSE - The MIT License file. Basically, you do what you like!
  - README.md - This file in Markdown format.
  - README.html - This file, in HTML format.


### Windows Only Files

  - antlr4.bat - A batch file to compile a *.g4 file into various Java classes, lexers and parsers.
  - grun.bat - A file to test run parser rules. Read the ANTLR4 docs for details on the [ANTLR][antlr] website. You might need to use this in debugging your grammar.
  - build.bat - A batch file to compile the grammar, compile the Java files, build a jarfile (in the test subdirectory) and clean up after itself. 
  - antlr_shell.cmd - A batch file to set the Java and ALTLR4 environment. Run this first, before you try to do anything. You will need to edit the various folder names to match your system before doing anything though!

### Linux/Unix Only Files

  - build.sh - Carries out a full compile and build from the grammar onwards. Creates a jar file in the test direcory.
  - antlr_shell.sh - You need to "source" this file to set up the ANTLR4 environment *before* attempting to build anything. Edit this to set your own JAVA_HOME before use. See build.sh.

### Something for Everyone!

  - tnsnames.g4 - this is the ANTLR4 grammar that defines the structure and semantics of a tnsnames.ora file as per the Oracle docs for 11gR2. However, it may be valid for previous, and subsequent versions too.
  - tns2toad.java - This is the small, but almost perfectly formed Java class that kicks off and controls everything when the final build is complete.
  - tns2toadListener.java - This class extends one of the Java classes created by the ANTLR4 build process and carries out all the hard work of creating your Toad sessions import file by parsing your tnsnames.ora file.


## The Build

### Dependencies
  - Java 1.6 (aka Java 6) minimum. Has been built and tested with Java 6 and Java 7.
  - antlr-4.4-complete.jar - downloaded from the [ANTLR][antlr] Web site. There may be a later version.
  - Toad 11.x or higher. This utility has been tested with Toad 11.6.


### Windows
  - Open a cmd window.
  - cd to wherever the source is to be found.
  - Execute the antlr_shell.cmd program.
  - Execute the build.bat program. A tns2toad.jar file will be created in the test folder.
  - Test - see below.

### Linus and Unix.
  - Open a shell session.
  - cd to wherever the source lives.
  - ./build.sh to perform a full build. A tns2toad.jar will be created in the test directory.
  - Test. See below.

## Testing

From the build directory:

  - cd test
  - On Windows, execute the tns2toad.cmd passing parameters as described below.
  - On Linux and Unix, execute the tns2toad.sh script passing parameters as described below.

### Parameters
Output from the checker is passed to stdout. You can either ignore this and simply watch it whizz up the screen at high speed, or you can catch everything that would potentially whizz by, in a file by redirecting stdout to a file, as follows:

#### Everything to the Screen

Windows:
```
tns2toad tnsnames.ora
```

Linux/Unix:

```
./tns2toad.sh tnsnames.ora
```

#### Everyting to a file

Windows:

```
tns2toad tnsnames.ora >myconnections.txt
```

Linux/Unix:
```
./tns2toad.sh tnsnames.ora >myconnections.txt
```


The file, myconnections.txt is now ready to be imported into Toad.



[antlr]: http://www.antlr.org

echo " "
echo "Setting build environment ..."
. ./antlr_shell.sh

echo "Compiling grammar ..."
antlr4 tnsnames.g4

echo "Compiling java ..."
javac *.java

echo "Creating jar ..."
jar -cvf tns2toad.jar *.class > /dev/null

echo "Cleaning up ..."
rm *.class

echo "Done."
echo " "

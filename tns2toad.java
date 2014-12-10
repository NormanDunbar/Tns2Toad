//--------------------------------------------------------------------
// The MIT License (MIT) 
// 
// Copyright (c) 2014 by Norman Dunbar 
// 
// Permission is hereby granted, free of charge, to any person 
// obtaining a copy of this software and associated documentation 
// files (the "Software"), to deal in the Software without 
// restriction, including without limitation the rights to use, 
// copy, modify, merge, publish, distribute, sublicense, and/or sell 
// copies of the Software, and to permit persons to whom the 
// Software is furnished to do so, subject to the following 
// conditions: 
//
// The above copyright notice and this permission notice shall be 
// included in all copies or substantial portions of the Software. 
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
// OTHER DEALINGS IN THE SOFTWARE. 
//
// Project      : Oracle Tnsnames.ora to TOAD Connections.ini.
// Developed by : Norman Dunbar, norman@dunbar-it.co.uk
//--------------------------------------------------------------------


// Import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

// Import some Java "stuff".
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;


public class tns2toad 
{
    public static void main(String[] args) throws Exception 
    {

        // A bit of "sign on and blow my own trumpet stuff" ;-)
        String thisVersion = "0.1";   // Version of this utility.
 
        // Assume tnsnames.ora will be piped via stdin, unless we get a parameter passed.
        String tnsnamesFilename = null;
        InputStream iStream = System.in;

        // How many positional options are we expecting?
        int expectedPositionalArgs = 1;

        // These are collected from the command line.
        File inputFile = null;
        String oracleHome="";
        String userName="";

        // These are used to process the command line.
        int i = 0;
        String thisArg;

        //---------------------------------------------------------
        // Let's scan the command line and see what needs doing ...
        //---------------------------------------------------------

        // Scan along the args array, looking at all the options. These
        // are all  prefixed by "--" and must all be before any of the
        // positional arguments.
        // When we find one, we zap it!
        // Each option takes a parameter - they get zapped also.
        // ThisArg holds the argument, i points at the parameter for it.
        while (i < args.length && args[i].startsWith("--")) {
            thisArg = args[i].toLowerCase();
            args[i++] = "";

            // Oracle Home...
            if (thisArg.equals("--oracle_home")) {
                if (i < args.length) {
                    oracleHome = args[i];
                    args[i++] = "";
                }
                else {
                    usage("ERROR: --oracle_home requires a path name");
                }
            }
            // User name...
            else if (thisArg.equals("--user")) {
                if (i < args.length) {
                    userName = args[i];
                    args[i++] = "";
                }
                else {
                    usage("ERROR: --user requires a username");
                }
            }
            // Something else? Not permitted.
            else {
                usage("Invalid option '" + thisArg + "'");
            }
                
        }

        // At this point we should be sitting with i pointing at the first
        // positional argument. Scan those next. All the options have been
        // extracted now, and zapped.

        // However, just exactly how many positional args do we want? This will
        // also catch any --options mingling within the positional args.
        if (i != args.length - expectedPositionalArgs) {
            usage("Unexpected or insufficient positional parameter(s) supplied.");
        }
        
        // We should only have a single parameter here, the tnsnames.ora file.
        tnsnamesFilename = args[i];

        //---------------------------------------------------------
        // Well, we got here, args on the command line must be ok
        // Check if we can open and/or read the tnsnames.ora file.
        //---------------------------------------------------------
        inputFile = new File(tnsnamesFilename);
        if (inputFile.isFile()) {
            iStream = new FileInputStream(tnsnamesFilename);
            //tnsnamesFilename = inputFile.getCanonicalPath();
        } 
        else {
            System.out.println("\nERROR 1: '" + tnsnamesFilename + 
                               "' is not a valid filename.\n");
            System.exit(1);  // Error exit.
        }
        
        //---------------------------------------------------------
        // Everything is fine, let's JFDI! :-)
        //---------------------------------------------------------

        // Feed the tnsnames.ora file into the lexer and get a
        // token stream from the lexer...
        ANTLRInputStream input = new ANTLRInputStream(iStream);
        tnsnamesLexer lexer = new tnsnamesLexer(input);

        // Feed the lexer's token stream to the parser and get
        // a parse tree out in return...
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tnsnamesParser parser = new tnsnamesParser(tokens);
        ParseTree tree = parser.tnsnames();

        // Feed the parse tree to the tree walker & the listener
        // and get a load of text on stdout as a final result.
        // That is your import file, redirect it to a file and
        // let Toad import it for you.
        ParseTreeWalker tnsWalker = new ParseTreeWalker();
        tns2toadListener tnsListener = new tns2toadListener(parser, userName, oracleHome);
        tnsWalker.walk(tnsListener, tree);
    }

    public static void usage(String message) {
        // Something was wrong with the command line.
        System.err.println();
        System.err.println(message);
        System.err.println();
        System.err.println("Usage: tns2toad <options> filename");
        System.err.println("Options:   --oracle_home <path to Oracle home> The default oracle home to be used.");
        System.err.println("           --user <username>                   The default user for all connections.");
        System.err.println("Parameter: filename.                           The tnsnames.ora file to be parsed.");
        System.err.println();
        System.exit(1);
    }

}

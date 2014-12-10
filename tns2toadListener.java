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

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.io.File;
import java.util.*;

public class tns2toadListener extends tnsnamesBaseListener 
{
    tnsnamesParser parser;                  // The Parser we are listening for.
    int loginNumber = 1;                    // The Login Number in the TOAD file.
    String oracleHome;                      // Default Oracle Home to use
    String user;                            // If supplied, use this user for all connections.
    String connectAs = "ConnectAs=Normal";  // How to connect. (Normal, SYSDBA, SYSOPER)

    //---------------------------------------------------------------- 
    // CONSTRUCTOR
    //---------------------------------------------------------------- 
    public tns2toadListener(tnsnamesParser parser, String user, String oracleHome) 
    {
        this.parser = parser;

        // Default user to connect as, may be null.
        this.user = user.toUpperCase();

        // If connecting as SYS, we need to connect as SYSDBA, otherwise, Normal.
        // Don't be fooled - this doesn't work ...
        // if (this.user == "SYS") {...}
        // Effing Java!
        if (this.user.equals("SYS"))
        {
            this.connectAs="ConnectAs=SYSDBA";
        }

        this.user = "User=" + this.user;

        // We might get passed an Oracle Home to use, otherwise, default.
        this.oracleHome = "OracleHome=" + oracleHome;
    }


    //---------------------------------------------------------------- 
    // TNS_ENTRY Entry.
    //---------------------------------------------------------------- 
    // We have parsed a database alias entry, write out all the stuff
    // for the entry in CONNECTIONS.INI. Much of which is not needed
    // at least in Toad 11.6.
    //---------------------------------------------------------------- 
    @Override
    public void enterTns_entry(tnsnamesParser.Tns_entryContext ctx)
    {
        String thisAlias;

        // Add each alias to the found aliases list, if it hasn't already been added.
        for (int i = 0; i < ctx.alias_list().alias().size(); i++)
        {
            // Get the next Alias from any lists.
            thisAlias = ctx.alias_list().alias(i).getText();
            
            // Write out the "stuff".
            System.out.println("[LOGIN" + (this.loginNumber) + "]");
            System.out.println(this.user);
            System.out.println("Server=" + thisAlias);
            System.out.println("AutoConnect=0");
            System.out.println(this.oracleHome);
            System.out.println("SavePassword=0");
            System.out.println("Favorite=0");          // Yes, it is spelt incorrectly! ;-)      
            System.out.println("SessionReadOnly=0");
            System.out.println("Alias=");
            System.out.println("Host=");
            System.out.println("InstanceName=");
            System.out.println("ServiceName=");
            System.out.println("SID=");
            System.out.println("Port=");
            System.out.println("LDAP=");
            System.out.println("Method=0");
            System.out.println("Protocol=TNS");
            System.out.println("ProtocolName=TCP");
            System.out.println("Color=8421376");       // TEAL - Yuck! Reminds user to fix it!
            System.out.println(this.connectAs);
            System.out.println("LastConnect=19600407031549");
            System.out.println("RelativePosition=" + (this.loginNumber -1));
            System.out.println("GUID=");
            System.out.println();

            this.loginNumber++;
        }
    }


    //---------------------------------------------------------------- 
    // TNS_NAMES Exit. (At end of file basically.)
    //---------------------------------------------------------------- 
    @Override
    public void exitTnsnames(tnsnamesParser.TnsnamesContext ctx)
    {
        System.out.println("[NumberCustomField]");
        System.out.println("NUMBER=0");
        System.out.println();
        System.out.println("<<Split file here. CONNECTIONS.INI above, CONNECTIONPWDS.INI below>>");
    }
}
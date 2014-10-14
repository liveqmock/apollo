/* --------------------------Usercode Section------------------------ */
package cn.com.youtong.apollo.script;

import java.io.*;

%%
/* -----------------Options and Declarations Section----------------- */

/*
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java.
*/
%class	Script2Lowwer
%unicode
%public
%int

/*
  Declarations

  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.
*/
%{
	char charQuote;
	private Writer output;
    public void setOutput(Writer output)
	{
		this.output= output;
	}

	public static void main(String[] args)
	{
		String script=
			"//**************************Script Test***************\n"
			+ "<%\n"
			+ "function Test()\n"
			+ "{\n"
			+ "     Return 'String should not CHANGED!';\n"
			+ "}\n"
			+ "%>\n"
			+ "A1 > A2 +A3 //a1  lt a2 + 'a3' \\\\²âÊÔºº×Ö////\n"
			+ "A1 == B2 +B3+ B6\n"
			+ "A1 == B2 +100\n"
			+ "A1 == B2 +1000\n";
		try
		{
			System.out.println("==================script before=============");
			System.out.println(script);

			StringReader reader= new StringReader(script);
			StringWriter out= new StringWriter();
			Script2Lowwer js= new Script2Lowwer(reader);
			js.setOutput(out);
			js.yylex() ;

			String result= out.toString();
			out.close() ;
			System.out.println("==================script translated=============");
			System.out.println(result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
%}

/*
  Macro Declarations

  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.
*/

%state QUOTE
%state CNOTE

%%

/* ------------------------Lexical Rules Section---------------------- */

<YYINITIAL> {
	"//".*  {
			output.write(yytext());
		}

	"/*"	{
			output.write(yytext());
			yybegin(CNOTE);
		}

	\"|\'	{
			charQuote= yytext().charAt(0);
			output.write(yytext());
			yybegin(QUOTE);
		}

	.|\n	{
				output.write(yytext().toLowerCase());
			}
}

<CNOTE> {
	"*/"	{
			output.write(yytext());
			yybegin(0);
		}

	.|\n	{
			output.write(yytext());
		}
}

<QUOTE> {
	\n		{
				output.write(yytext());
				yybegin(0);
			}

	\\.		{
				output.write(yytext());
			}

	.		{
				output.write(yytext());
				if(yytext().charAt(0) == charQuote)
					yybegin(0);
			}
}
<<EOF>>		{
				return 0;
			}
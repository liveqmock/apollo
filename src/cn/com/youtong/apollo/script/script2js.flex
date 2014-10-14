/* --------------------------Usercode Section------------------------ */
package cn.com.youtong.apollo.script;

import java.io.*;

%%
/* -----------------Options and Declarations Section----------------- */

/*
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java.
*/
%class	Script2Js
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
	private Writer output;
    public void setOutput(Writer output)
	{
		this.output= output;
	}

	public static void main(String[] args)
	{
		String script=
			"//**************************script test***************\n"
			+ "<%\n"
			+ "function test()\n"
			+ "{\n"
			+ "     return 100 +2;\n"
			+ "}\n"
			+ "%>\n"
			+ "a1 > a2 +a3 //a1  lt a2 + 'a3' \\\\²âÊÔºº×Ö////\n"
			+ "a1 == b2 +b3+ b6\n"
			+ "a1 == a2 +100\n"
			+ "a1 == a2 +1000\n";
		try
		{
			System.out.println("==================script before=============");
			System.out.println(script);

			StringReader reader= new StringReader(script);
			StringWriter out= new StringWriter();
			Script2Js js= new Script2Js(reader);
			js.setOutput(out);
			js.yylex() ;

			String result= out.toString();
			out.close() ;
			System.out.println("==================script translated=============");
			System.out.println(result);
		}
		catch(Exception e)
		{

		}
	}
%}

/*
  Macro Declarations

  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.
*/
space	=	[ \t]

%state SCRIPT

%%

/* ------------------------Lexical Rules Section---------------------- */

<YYINITIAL> {
	^{space}*"<%".*		{
							output.write("//Started script");
							yybegin(SCRIPT);
						}

	^{space}*			{
							output.write(yytext());
						}


	^{space}*"//".*		{
							output.write(yytext());
						}

	^.+					{
							output.write("a(");
							boolean findNotes= false;
							int i;
							for(i=0; i< yylength(); i++)
							{
								char p= yycharat(i);
								if( p == '/' && (i+1)< yylength() && yycharat(i+1)== '/')
								{
									findNotes= true;
									break;
								}
								else
									output.write(p);
							}
							output.write(", '");
							if(findNotes)
								i+=2;	//skip //
							else
								i= 0; //Êä³ö¹«Ê½Îª×¢ÊÍ
							for(; i< yylength(); i++)
							{
								char p= yycharat(i);
								if( p == '\'' || p == '\\')
									output.write('\\');
								output.write(p);
							}
							output.write("');");
						}

	\n					{
							output.write(yytext());
						}

	<<EOF>>				{
							output.write("//end");
							return 0;
						}
}

<SCRIPT> {
	^{space}*"%>".*		{
							output.write("//End script");
							yybegin(YYINITIAL);
						}
	(.*)|\n				{
							output.write(yytext());
						}
}
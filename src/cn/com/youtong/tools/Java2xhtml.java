package cn.com.youtong.tools;

/* Copyright (C) 2003 Shayne Steele
 * License and warranty are under the terms of the GNU General Public License
 * Version 2.0.  See http://www.gnu.org for license and warranty details.
 */

/** Java2xhtml.java  Version 0.9
 *  Produces an XHTML file from Java source code with syntax highlighting,
 *  includes additional options (line numbering, tab spacing, etc.)
 * <P>
 * NOTE: Common java naming structure is assumed
 *       Capitalize the first letter that appears in a class or interface name
 *       Use lowercase for the first letter in a method or variable name
 *       Use only uppercase letters when naming constants
 *
 * @version     0.9, March 2003
 * @author      Shayne Steele
 */
import java.io.*;
import java.util.*;

public class Java2xhtml
{
	// collect various sets of keywords
	Collection keywordCollection;
	Collection primitiveTypeCollection;
	Collection primitiveLiteralCollection;
	Collection javadocTagCollection;

	// all these variables are changeable by a options file
	int extraIndentation = 0;
	int tabSize = 4;
	int lineModulus = 5;
	boolean hasLegend = false;
	boolean hasLineNumbers = false;
	boolean hasLineModulusDrawnLines = false;
	boolean hasLineModulusCodeBlocks = false;
	boolean hasFooter = false;
	boolean hasFooterIcons = false;
	boolean hasFooterDate = true;
	boolean isCodeSnippet = false;
	boolean isXHTML_1_1 = true;
	boolean hasTitle = false;
	boolean hasAllBoldSourceCode = false;
	boolean hasInternalStyleSheet = true;
	boolean hasExternalStyleSheet = true;
	String externalStyleSheetName = "style.css";

	// create the various keyword collections
	// parse the html options file
	public Java2xhtml(Properties htmlProperties)
	{
		// collection type is Hashset for unique elements and fast retieval
		String keywordArray[] =
			{
			"abstract", "default", "if", "private", "do", "implements", "protected", "throws", "break", "import", "public", "transient", "else", "instanceof", "return", "try", "case", "extends",
			"throw", "static", "catch", "final", "interface", "while", "volatile", "finally", "super", "synchronized", "class", "native", "switch", "package", "const", "for", "new", "goto",
			"continue", "this", "assert", "strictfp"
		};
		keywordCollection = new HashSet(Arrays.asList(keywordArray));
		String primitiveTypeArray[] =
			{
			"boolean", "char", "byte", "short", "int", "long", "float", "double", "void"
		};
		primitiveTypeCollection = new HashSet(Arrays.asList(primitiveTypeArray));
		String primitiveLiteralArray[] =
			{
			"false", "null", "true"
		};
		primitiveLiteralCollection = new HashSet(Arrays.asList(primitiveLiteralArray));
		String javadocTagArray[] =
			{
			"see", "author", "version", "param", "return", "exception", "deprecated", "throws", "link", "since", "serial", "serialField", "serialData", "beaninfo"
		};
		javadocTagCollection = new HashSet(Arrays.asList(javadocTagArray));
		// get html properties (use defaults if necessary)
		if(htmlProperties != null)
		{
			// html properies file exist try parsing it
			hasLegend = Boolean.valueOf(htmlProperties.getProperty("hasLegend", "false")).booleanValue();
			extraIndentation = Integer.parseInt(htmlProperties.getProperty("extraIndentation", "0"));
			tabSize = Integer.parseInt(htmlProperties.getProperty("tabSize", "4"));
			hasLineNumbers = Boolean.valueOf(htmlProperties.getProperty("hasLineNumbers", "false")).booleanValue();
			lineModulus = Integer.parseInt(htmlProperties.getProperty("lineModulus", "5"));
			hasLineModulusDrawnLines = Boolean.valueOf(htmlProperties.getProperty("hasLineModulusDrawnLines", "false")).booleanValue();
			hasLineModulusCodeBlocks = Boolean.valueOf(htmlProperties.getProperty("hasLineModulusCodeBlocks", "false")).booleanValue();
			hasFooter = Boolean.valueOf(htmlProperties.getProperty("hasFooter", "false")).booleanValue();
			hasFooterIcons = Boolean.valueOf(htmlProperties.getProperty("hasFooterIcons", "false")).booleanValue();
			hasFooterDate = Boolean.valueOf(htmlProperties.getProperty("hasFooterDate", "true")).booleanValue();
			isXHTML_1_1 = Boolean.valueOf(htmlProperties.getProperty("isXHTML_1_1", "true")).booleanValue();
			isCodeSnippet = Boolean.valueOf(htmlProperties.getProperty("isCodeSnippet", "false")).booleanValue();
			hasTitle = Boolean.valueOf(htmlProperties.getProperty("hasTitle", "false")).booleanValue();
			hasAllBoldSourceCode = Boolean.valueOf(htmlProperties.getProperty("hasAllBoldSourceCode", "false")).booleanValue();
			hasInternalStyleSheet = Boolean.valueOf(htmlProperties.getProperty("hasInternalStyleSheet", "true")).booleanValue();
			hasExternalStyleSheet = Boolean.valueOf(htmlProperties.getProperty("hasExternalStyleSheet", "true")).booleanValue();
			externalStyleSheetName = htmlProperties.getProperty("externalStyleSheetName", "style.css");
		}
	}


	// constant 'States' java source code can be in
	public final static class State
	{
		public final static State TEXT = new State();
		public final static State IMPORT_NAME = new State();
		public final static State PARAM_VARIABLE = new State();
		public final static State JAVADOC = new State();
		public final static State PACKAGE_NAME = new State();
		public final static State DOUBLE_QUOTE = new State();
		public final static State SINGLE_QUOTE = new State();
		public final static State TRADITIONAL_COMMENT = new State();
		public final static State LINE_COMMENT = new State();

		// empty constructor
		private State()
		{
			// empty body
		}
	}

	// Convert java source code StringBufffer into colorized (and tab spaced)
	// HTML String .
	// Assumes that Java naming convention is used
	// Uses a very basic state machine design.
	public String makeHTML(StringBuffer bufferIn, String title)
	{
		int codeLineNumber = 0;
		boolean isNewLine = true;
		boolean isNewBlock = true;
		int identifierLength = 0;
		int qualifiedIdentifierLength = 0;
		int presentIndex = -1;
		int spaceLength = 0;
		int saveIndex = 0;
		char presentChar = 0;
		State presentState = State.TEXT;
		StringBuffer bufferOut = new StringBuffer();
		if(!isCodeSnippet)
		{
			bufferOut.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
			if(isXHTML_1_1)
			{
				bufferOut.append("<!DOCTYPE html PUBLIC " + "\"-//W3C//DTD XHTML 1.1//EN\"\r\n");
				bufferOut.append("    \"http://www.w3.org/TR/xhtml11/DTD/" + "xhtml11.dtd\">\r\n");
				bufferOut.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"" + " xml:lang=\"en\">\r\n");
			}
			else
			{
				bufferOut.append("<!DOCTYPE html PUBLIC " + "\"-//W3C//DTD XHTML 1.0 Strict//EN\"\r\n");
				bufferOut.append("    \"http://www.w3.org/TR/xhtml1/DTD/" + "xhtml1-strict.dtd\">\r\n");
				bufferOut.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"" + " xml:lang=\"en\" lang=\"en\">\r\n");
			}
			bufferOut.append(" <head>\r\n");
			bufferOut.append("  <title>\r\n");
			bufferOut.append("   " + title + "\r\n");
			bufferOut.append("  </title>\r\n");
			bufferOut.append("  <meta name=\"generator\"\r\n");
			bufferOut.append("        content=\"Java2xhtml 0.9\" />\r\n");
			if(hasInternalStyleSheet)
			{
				bufferOut.append("  <style type=\"text/css\">\r\n");
				bufferOut.append("   <!-- /* <![CDATA[ */\r\n");
				bufferOut.append("    .sourceCodeStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #000000;\r\n");
				bufferOut.append("       background-color: #FFFFFF;\r\n");
				if(hasAllBoldSourceCode)
				{
					bufferOut.append("       font-weight: bold;\r\n");
				}
				bufferOut.append("     }\r\n");
				bufferOut.append("    .lineNumberStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       font-weight: normal;\r\n");
				bufferOut.append("       color: #000000;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				if(lineModulus > 0)
				{
					bufferOut.append("    .modulusLineNumberStyle\r\n");
					bufferOut.append("     {\r\n");
					bufferOut.append("       font-weight: bold;\r\n");
					bufferOut.append("       color: #000000;\r\n");
					bufferOut.append("       background-color: ");
					bufferOut.append("transparent;\r\n");
					bufferOut.append("     }\r\n");
					if(hasLineModulusDrawnLines)
					{
						bufferOut.append("    .modulusLineStyle\r\n");
						bufferOut.append("     {\r\n");
						bufferOut.append("       text-decoration: ");
						bufferOut.append("line-through;\r\n");
						bufferOut.append("       color: #000000;\r\n");
						bufferOut.append("       background-color: ");
						bufferOut.append("transparent;\r\n");
						bufferOut.append("     }\r\n");
					}
					if(hasLineModulusCodeBlocks)
					{
						bufferOut.append("    .modulusBlockPREStyle\r\n");
						bufferOut.append("     {\r\n");
						bufferOut.append("       margin: 0em\r\n");
						bufferOut.append("     }\r\n");
						bufferOut.append("    .modulusBlockStyle\r\n");
						bufferOut.append("     {\r\n");
						bufferOut.append("       color: #000000;\r\n");
						bufferOut.append("       background-color: ");
						bufferOut.append("#CCCCCC;\r\n");
						bufferOut.append("     }\r\n");
					}
				}
				bufferOut.append("    .keywordStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #9900FF;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .methodStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #0000FF;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .variableStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #CC9933;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .singleLineCommentStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #CC3333;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .traditionalCommentStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #FF0000;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .javadocCommentStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #CC0033;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .javadocTagStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #0099CC;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .importNameStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #33CCCC;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .packageNameStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #339999;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .primitiveTypeStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #009900;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .nonPrimitiveTypeStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #009966;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .constructorStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #3300CC;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .constantStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #666666;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .doubleQuoteStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #996633;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("       font-style: italic;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .singleQuoteStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #663333;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("       font-style: oblique;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .numericalLiteralStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #333300;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				bufferOut.append("    .primitiveLiteralStyle\r\n");
				bufferOut.append("     {\r\n");
				bufferOut.append("       color: #006600;\r\n");
				bufferOut.append("       background-color: transparent;\r\n");
				bufferOut.append("     }\r\n");
				if(hasFooterIcons)
				{
					bufferOut.append("    .iconStyle\r\n");
					bufferOut.append("     {\r\n");
					bufferOut.append("       border-style: none;\r\n");
					bufferOut.append("     }\r\n");
				}
				if(hasTitle)
				{
					bufferOut.append("    #title\r\n");
					bufferOut.append("     {\r\n");
					bufferOut.append("       text-align: center;\r\n");
					bufferOut.append("       font-size: xx-large;\r\n");
					bufferOut.append("     }\r\n");
				}
				if(hasLegend)
				{
					bufferOut.append("    #legendTitle\r\n");
					bufferOut.append("     {\r\n");
					bufferOut.append("       text-align: center;\r\n");
					bufferOut.append("       font-size: x-large;\r\n");
					bufferOut.append("     }\r\n");
					bufferOut.append("    #legend\r\n");
					bufferOut.append("     {\r\n");
					bufferOut.append("       font-family: monospace;\r\n");
					bufferOut.append("       font-size: large;\r\n");
					bufferOut.append("     }\r\n");
				}
				if(hasFooter)
				{
					bufferOut.append("    #footer\r\n");
					bufferOut.append("     {\r\n");
					bufferOut.append("       font-size: xx-small;\r\n");
					bufferOut.append("     }\r\n");
				}
				bufferOut.append("   /* ]]> */ -->\r\n");
				bufferOut.append("  </style>\r\n");
			}
			if(hasExternalStyleSheet)
			{
				bufferOut.append("  <link rel=\"stylesheet\" " + "type=\"text/css\" href=\"" + externalStyleSheetName + "\" />\r\n");
			}
			bufferOut.append(" </head>\r\n");
			bufferOut.append(" <body>\r\n");
		}
		if(hasTitle)
		{
			bufferOut.append("  <div id=\"title\">\r\n");
			bufferOut.append("   " + title + "\r\n");
			bufferOut.append("  </div>\r\n");
			bufferOut.append("  <hr />\r\n");
		}
		if(hasLegend)
		{
			bufferOut.append("  <div id=\"legendTitle\">\r\n");
			bufferOut.append("   Legend\r\n");
			bufferOut.append("  </div>\r\n");
			bufferOut.append("  <div class=\"sourceCodeStyle\">\r\n");
			bufferOut.append("   <div id=\"legend\">\r\n");
			bufferOut.append("    <span class=\"keywordStyle\">");
			bufferOut.append("keyword</span>\r\n");
			bufferOut.append("    <span class=\"methodStyle\">");
			bufferOut.append("method</span>\r\n");
			bufferOut.append("    <span class=\"variableStyle\">variable" + "</span>\r\n");
			bufferOut.append("    <span class=\"singleLineCommentStyle\">" + "singleLineComment</span>\r\n");
			bufferOut.append("    <span class=\"traditionalCommentStyle\">" + "traditionalComment</span>\r\n");
			bufferOut.append("    <span class=\"javadocCommentStyle\">" + "javadocComment</span>\r\n");
			bufferOut.append("    <span class=\"javadocTagStyle\">javadocTag" + "</span>\r\n");
			bufferOut.append("    <span class=\"importNameStyle\">" + "importName</span>\r\n");
			bufferOut.append("    <span class=\"packageNameStyle\">" + "packageName</span>\r\n");
			bufferOut.append("    <span class=\"primitiveTypeStyle\">" + "primitiveType</span>\r\n");
			bufferOut.append("    <span class=\"nonPrimitiveTypeStyle\">" + "nonPrimitiveType</span>\r\n");
			bufferOut.append("    <span class=\"constructorStyle\">" + "constructor</span>\r\n");
			bufferOut.append("    <span class=\"constantStyle\">" + "constant</span>\r\n");
			bufferOut.append("    <span class=\"doubleQuoteStyle\">" + "doubleQuote</span>\r\n");
			bufferOut.append("    <span class=\"singleQuoteStyle\">" + "singleQuote</span>\r\n");
			bufferOut.append("    <span class=\"numericalLiteralStyle\">" + "numericalLiteral</span>\r\n");
			bufferOut.append("    <span class=\"primitiveLiteralStyle\">" + "primitiveLiteral</span>\r\n");
			bufferOut.append("   </div>\r\n");
			bufferOut.append("  </div>\r\n");
			bufferOut.append("  <hr />\r\n");
		}
		bufferOut.append("  <div class=\"sourceCodeStyle\">\r\n");
		if(hasLineModulusCodeBlocks)
		{
			bufferOut.append("<pre class=\"modulusBlockPREStyle\">\r\n");
		}
		else
		{
			bufferOut.append("<pre>\r\n");
		}
		// process the input Java code Stringbuffer
		// subtract 2 from the bufferIn.length() to get EOF marker
		while(presentIndex++ != (bufferIn.length() - 2))
		{
			for(int i = 0; i < extraIndentation; i++)
			{
				bufferOut.append(" ");
			}
			if((hasLineNumbers || hasLineModulusCodeBlocks) && isNewLine)
			{
				// add line numbers if desired
				// line numbers are 1 - 9999 then rotate line numbers
				codeLineNumber = (++codeLineNumber) % 10000;
				if((lineModulus > 0) && hasLineModulusCodeBlocks && (codeLineNumber % lineModulus == 1))
				{
					if(isNewBlock)
					{
						if((State.TRADITIONAL_COMMENT == presentState) || (State.JAVADOC == presentState))
						{
							bufferOut.insert((bufferOut.length() - ("\r\n").length()), "</span>");
						}
						bufferOut.append("</pre>\r\n");
						bufferOut.append("   <div class=");
						bufferOut.append("\"modulusBlockStyle\">");
						bufferOut.append("\r\n<pre class=\"");
						bufferOut.append("modulusBlockPREStyle\">\r\n");
						if(State.TRADITIONAL_COMMENT == presentState)
						{
							bufferOut.append("<span class=" + "\"traditionalCommentStyle\">");
						}
						if(State.JAVADOC == presentState)
						{
							bufferOut.append("<span class=" + "\"javadocCommentStyle\">");
						}
					}
					isNewBlock = !isNewBlock;
				}
				// make straight columns of line numbers
				if(codeLineNumber < 1000)
				{
					bufferOut.append(" ");
				}
				if(codeLineNumber < 100)
				{
					bufferOut.append(" ");
				}
				if(codeLineNumber < 10)
				{
					bufferOut.append(" ");
				}
				if(hasLineNumbers)
				{
					if((lineModulus > 0) && (codeLineNumber % lineModulus == 0))
					{
						bufferOut.append("<span class=" + "\"modulusLineNumberStyle\">");
						bufferOut.append(codeLineNumber);
						bufferOut.append(": </span>");
						if(hasLineModulusDrawnLines)
						{
							// compute spaceLength so a line can be drawn
							while((presentIndex != (bufferIn.length() - 1)) && ((Character.isSpaceChar(bufferIn.charAt(presentIndex))) || (bufferIn.charAt(presentIndex) == '\t')))
							{
								// for each tab, insert tabSize spaces
								if(bufferIn.charAt(presentIndex) == '\t')
								{
									for(int i = 0; i < tabSize; i++)
									{
										bufferIn.insert(presentIndex + 1, " ");
									}
									presentIndex++;
									continue;
								}
								if(' ' == bufferIn.charAt(presentIndex))
								{
									// read a space so place a space
									bufferOut.append(" ");
									spaceLength += (" ").length();
								}
								else
								{
									// a white space character was read
									bufferOut.append(bufferIn.charAt(presentIndex));
									++spaceLength;
								}
								presentIndex++;
							}
							// check if line is empty
							// (no printable characters on line)
							if((presentIndex == (bufferIn.length() - 1)) || (Character.isWhitespace(bufferIn.charAt(presentIndex))))
							{
								spaceLength = 0;
							}
							// draw the line
							if(spaceLength > 1)
							{
								bufferOut.insert((bufferOut.length() - spaceLength), "<span class=" + "\"modulusLineStyle\">");
								bufferOut.insert((bufferOut.length() - (" ").length()), "</span>");
							}
							spaceLength = 0;
						}
					}
					else
					{
						// line numbers are in lineNumberColor
						bufferOut.append("<span class=\"lineNumberStyle\">");
						bufferOut.append(codeLineNumber);
						bufferOut.append(":</span> ");
					}
				}
				isNewLine = false;
			}
			// a state machine
			presentChar = bufferIn.charAt(presentIndex);
			if((Character.isJavaIdentifierPart(presentChar)) || ((State.IMPORT_NAME == presentState) && (presentChar == '*')))
			{
				// this is an identifier
				bufferOut.append(presentChar);
				identifierLength++;
				continue; // keep adding characters until identifier is done
			}
			if(identifierLength > 0)
			{
				// identifier
				qualifiedIdentifierLength = qualifiedIdentifierLength + identifierLength;
				if(bufferIn.charAt(presentIndex) == '.')
				{
					// qualified identifier
					bufferOut.append(presentChar);
					qualifiedIdentifierLength++;
					identifierLength = 0;
					continue; // keep adding characters to qualified identifier
				}
				String identifier = bufferOut.toString().substring(bufferOut.length() - identifierLength);
				if((State.PARAM_VARIABLE == presentState))
				{
					// any identifier after a param in a javadoc is assumed to
					// be a variable
					bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"variableStyle\">");
					bufferOut.append("</span>");
					presentState = State.JAVADOC;
				}
				else if(State.JAVADOC == presentState)
				{
					// in javadoc state
					if((javadocTagCollection.contains(identifier)) && (bufferIn.charAt(presentIndex - (identifierLength + 1)) == '@'))
					{
						// identifier is a javadocTag
						bufferOut.insert(bufferOut.length() - identifierLength, "<span class=\"javadocTagStyle\">");
						bufferOut.append("</span>");
						if(("param").equals(identifier))
						{
							// any identifier after a param is assumed to
							// be a variable, get into a state to do this
							presentState = State.PARAM_VARIABLE;
						}
					}
				}
				else if(State.IMPORT_NAME == presentState)
				{
					// import identifier
					bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"importNameStyle\">");
					bufferOut.append("</span>");
					presentState = State.TEXT;
				}
				else if(State.PACKAGE_NAME == presentState)
				{
					// package identifier
					bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"packageNameStyle\">");
					bufferOut.append("</span>");
					presentState = State.TEXT;
				}
				else if(State.TEXT == presentState)
				{
					if(keywordCollection.contains(identifier))
					{
						// identifier is a keyword
						bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"keywordStyle\">");
						bufferOut.append("</span>");
						if(("import").equals(identifier))
						{
							// anything after an import in text mode must be
							// an import name, so enter state to process this
							presentState = State.IMPORT_NAME;
						}
						else if(("package").equals(identifier))
						{
							// anything after an package in text mode must be
							// an package name, so enter state to process this
							presentState = State.PACKAGE_NAME;
						}
					}
					else if(primitiveTypeCollection.contains(identifier))
					{
						// identifier is a primitive type
						bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"primitiveTypeStyle\">");
						bufferOut.append("</span>");
					}
					else if((identifier.equals(identifier.toUpperCase())) && (!(Character.isDigit(identifier.charAt(0)))))
					{
						// identifier is a constant
						bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"constantStyle\">");
						bufferOut.append("</span>");
					}
					else if(Character.isUpperCase(identifier.charAt(0)))
					{
						// identifier is a constructor or non-primitive type
						// eat white space
						saveIndex = presentIndex;
						while(Character.isWhitespace(bufferIn.charAt(saveIndex++)))
						{
							//empty body
						}
						if(bufferIn.charAt(--saveIndex) == '(')
						{ // identifier is a constructor
							bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=" + "\"constructorStyle\">");
							bufferOut.append("</span>");
						}
						else
						{
							// identifier is a non-primitive type
							bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=" + "\"nonPrimitiveTypeStyle\">");
							bufferOut.append("</span>");
						}
					}
					else if(!(Character.isDigit(identifier.charAt(0)) || primitiveLiteralCollection.contains(identifier)))
					{
						// identifier is a method or a variable
						// eat white space
						saveIndex = presentIndex;
						while(Character.isWhitespace(bufferIn.charAt(saveIndex++)))
						{
							// empty body
						}
						--saveIndex;
						// identifier is a method
						if(bufferIn.charAt(saveIndex) == '(')
						{
							bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"methodStyle\">");
							bufferOut.append("</span>");
						}
						else if(bufferIn.charAt(saveIndex) == ',')
						{
							// comma seperated variables
							bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"variableStyle\">");
							bufferOut.append("</span>");
						}
						else
						{
							// a variable
							// take care of cases such as array[index].variable
							if(bufferIn.charAt(presentIndex - (qualifiedIdentifierLength + 1)) == '.')
							{
								qualifiedIdentifierLength++;
							}
							bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=\"variableStyle\">");
							bufferOut.append("</span>");
						}
					}
					else
					{
						if(primitiveLiteralCollection.contains(identifier))
						{
							// primitiveLiteral (boolean or null)
							bufferOut.insert(bufferOut.length() - identifierLength, "<span class=" + "\"primitiveLiteralStyle\">");
							bufferOut.append("</span>");
						}
						// a numerical literal
						else
						{
							if(((presentIndex - (qualifiedIdentifierLength + 1)) > 0) && (bufferIn.charAt(presentIndex - (qualifiedIdentifierLength + 1)) == '.'))
							{
								qualifiedIdentifierLength++;
							}
							bufferOut.insert(bufferOut.length() - qualifiedIdentifierLength, "<span class=" + "\"numericalLiteralStyle\">");
							bufferOut.append("</span>");
						}
					}
				}
				qualifiedIdentifierLength = 0;
				identifierLength = 0;
			}
			// process characters NOT in identifiers
			switch(presentChar)
			{
				case '&': //ampersand
					bufferOut.append("&amp;"); // HTMLify character
					break;
				case '<': // less than sign
					bufferOut.append("&lt;"); // HTMLify character
					break;
				case '>': // greater than sign
					bufferOut.append("&gt;"); // HTMLify character
					break;
				case '\"': // double quote
					bufferOut.append("&quot;"); // HTMLify character
					if(State.TEXT == presentState)
					{
						presentState = State.DOUBLE_QUOTE;
						bufferOut.insert(bufferOut.length() - ("&quot;").length(), "<span class=\"doubleQuoteStyle\">");
					}
					else if(State.DOUBLE_QUOTE == presentState)
					{
						presentState = State.TEXT;
						bufferOut.append("</span>");
					}
					break;
				case '\'': // single quote
					bufferOut.append("\'");
					if(State.TEXT == presentState)
					{
						presentState = State.SINGLE_QUOTE;
						bufferOut.insert(bufferOut.length() - ("\'").length(), "<span class=\"singleQuoteStyle\">");
					}
					else if(State.SINGLE_QUOTE == presentState)
					{
						presentState = State.TEXT;
						bufferOut.append("</span>");
					}
					break;
				case '\\': // backslash
					bufferOut.append("\\");
					if((State.DOUBLE_QUOTE == presentState) || (State.SINGLE_QUOTE == presentState))
					{
						// treat as a character escape sequence
						bufferOut.append(bufferIn.charAt(++presentIndex));
					}
					break;
				case '\t': // tab

					// replace tabs with tabsize number of spaces
					for(int i = 0; i < tabSize; i++)
					{
						bufferOut.append(' ');
					}
					break;
				case '*': // star
					bufferOut.append("*");
					if((State.TEXT == presentState) && (bufferIn.charAt(presentIndex - 1) == '/'))
					{
						if(((bufferIn.length() - 1) > presentIndex) && (bufferIn.charAt(presentIndex + 1) == '*'))
						{
							presentState = State.JAVADOC;
							bufferOut.insert(bufferOut.length() - ("/*").length(), "<span class=" + "\"javadocCommentStyle\">");
						}
						else
						{
							presentState = State.TRADITIONAL_COMMENT;
							bufferOut.insert(bufferOut.length() - ("/*").length(), "<span class=" + "\"traditionalCommentStyle\">");
						}
					}
					break;
				case '/': // foward slash
					bufferOut.append("/");
					if(((State.TRADITIONAL_COMMENT == presentState) || (State.JAVADOC == presentState)) && (bufferIn.charAt(presentIndex - 1) == '*'))
					{
						bufferOut.append("</span>");
						presentState = State.TEXT;
					}
					if((State.TEXT == presentState) && (presentIndex > 0) && (bufferIn.charAt(presentIndex - 1) == '/'))
					{
						bufferOut.insert(bufferOut.length() - ("//").length(), "<span class=" + "\"singleLineCommentStyle\">");
						presentState = State.LINE_COMMENT;
					}
					break;
				case '\r': // carriage return
					// fall through
				case '\n': // line feed

					// all HTML lines end in \r\n
					if((bufferIn.charAt(presentIndex) == '\r') && ((bufferIn.length() - 1) > presentIndex) && (bufferIn.charAt(presentIndex + 1) == '\n'))
					{
						++presentIndex;
					}

					// end single line comments
					if(State.LINE_COMMENT == presentState)
					{
						bufferOut.append("</span>");
						presentState = State.TEXT;
					}

					// end of block
					if((lineModulus > 0) && hasLineModulusCodeBlocks && ((codeLineNumber % lineModulus == 0) && !isNewBlock))
					{
						// end multi-line spanning states
						if((State.TRADITIONAL_COMMENT == presentState) || (State.JAVADOC == presentState))
						{
							bufferOut.append("</span>");
						}
						bufferOut.append("\r\n");
						bufferOut.append("</pre>\r\n");
						bufferOut.append("   </div>\r\n");
						bufferOut.append("<pre class=\"");
						bufferOut.append("modulusBlockPREStyle\">\r\n");
						// restart multi-line spanning states
						if(State.TRADITIONAL_COMMENT == presentState)
						{
							bufferOut.append("<span class=" + "\"traditionalCommentStyle\">");
						}
						if(State.JAVADOC == presentState)
						{
							bufferOut.append("<span class=" + "\"javadocCommentStyle\">");
						}
					}
					else
					{
						// div automatically starts new line
						bufferOut.append("\r\n");
					}
					isNewLine = true;
					break;
				case 0: // nul character
					if((State.LINE_COMMENT == presentState) && (presentIndex == (bufferIn.length() - 1)))
					{
						bufferOut.append("</span>");
					}
					break;
				default: // everything else
					bufferOut.append(presentChar);
			}
			qualifiedIdentifierLength = 0;
		}
		bufferOut.append("</pre>\r\n");
		// end block early if no more source code
		if((lineModulus > 0) && hasLineModulusCodeBlocks && !isNewBlock && (codeLineNumber % lineModulus != 0))
		{
			bufferOut.append("   </div>\r\n");
		}
		bufferOut.append("  </div>\r\n"); // end div of sourceCodeStyle
		// if code snippet then don't add ending tags of xhtml page
		if(!isCodeSnippet)
		{
			// if footer mode then add a footer
			if(hasFooter)
			{
				bufferOut.append("  <hr />\r\n");
				bufferOut.append("  <div id=\"footer\">\r\n");
				if(hasFooterIcons)
				{
					if(hasFooterDate)
					{
						bufferOut.append("   <script type=\"text/javaScript\"");
						bufferOut.append(">\r\n");
						bufferOut.append("    <!-- // <![CDATA[\r\n");
						bufferOut.append("     document.write(\"Document last");
						bufferOut.append(" modified on \"");
						bufferOut.append(" + document.lastModified + ");
						bufferOut.append("\"<br />\");\r\n");
						bufferOut.append("    // ]]> -->\r\n");
						bufferOut.append("   </script>\r\n");
					}
					bufferOut.append("   <a href=\"");
					bufferOut.append("http://validator.w3.org/check/referer");
					bufferOut.append("\">\r\n");
					bufferOut.append("    <img class=\"iconStyle\" src=\"");
					bufferOut.append("http://www.w3.org/Icons/");
					if(isXHTML_1_1)
					{
						bufferOut.append("valid-xhtml11\"\r\n");
						bufferOut.append("         alt=\"Valid XHTML 1.1!\"");
					}
					else
					{
						bufferOut.append("valid-xhtml10\"\r\n");
						bufferOut.append("         alt=\"Valid XHTML 1.0!\"");
					}
					bufferOut.append(" height=\"31\" ");
					bufferOut.append("width=\"88\" />\r\n");
					bufferOut.append("   </a>\r\n");
					bufferOut.append("   &#160;\r\n");
					bufferOut.append("   <a href=\"");
					bufferOut.append("http://jigsaw.w3.org");
					bufferOut.append("/css-validator/check/referer");
					bufferOut.append("\">\r\n");
					bufferOut.append("    <img class=\"iconStyle\" src=\"");
					bufferOut.append("http://jigsaw.w3.org/");
					bufferOut.append("css-validator/images/vcss");
					bufferOut.append("\"\r\n");
					bufferOut.append("         alt=\"Valid CSS!\"");
					bufferOut.append(" height=\"31\" width=\"88\" />\r\n");
					bufferOut.append("   </a>\r\n");
				}
				else
				{
					bufferOut.append("   This is a valid\r\n");
					bufferOut.append("   <a href=\"http://");
					bufferOut.append("validator.w3.org/check/referer");
					if(isXHTML_1_1)
					{
						bufferOut.append("\">XHTML 1.1</a>\r\n");
					}
					else
					{
						bufferOut.append("\">XHTML 1.0</a>\r\n");
					}
					bufferOut.append("   with\r\n");
					bufferOut.append("   <a href=\"http://");
					bufferOut.append("jigsaw.w3.org");
					bufferOut.append("/css-validator/check/referer");
					bufferOut.append("\">CSS</a>\r\n");
					bufferOut.append("   document \r\n");
					if(hasFooterDate)
					{
						bufferOut.append("   <script type=\"text/javaScript\"");
						bufferOut.append(">\r\n");
						bufferOut.append("    <!-- // <![CDATA[\r\n");
						bufferOut.append("     document.write(\"last modified");
						bufferOut.append(" on \" + document.lastModified);");
						bufferOut.append("\r\n");
						bufferOut.append("    // ]]> -->\r\n");
						bufferOut.append("   </script>\r\n");
					}
				}
				bufferOut.append("  </div>\r\n");
			}
			bufferOut.append(" </body>\r\n");
			bufferOut.append("</html>\r\n");
		}
		return bufferOut.toString();
	}

	public static void main(String []args)
	{
		Java2xhtml test= new Java2xhtml(null);
		StringBuffer bufIn= new StringBuffer();
		bufIn.append("public static void main(String []args)\n"
					 + "{\n"
					 + "	Java2xhtml test= new Java2xhtml(null);\n"
					 + "	StringBuffer bufIn= new StringBuffer();\n"
					 + "	bufIn.append(\"\");\n"
					 + "	System.out.print(test.makeHTML(bufIn, \"Test\"));\n"
					 +"}\n");
		System.out.print(test.makeHTML(bufIn, "Test"));
	}
}
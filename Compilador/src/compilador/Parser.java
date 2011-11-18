//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.txt"
package compilador;
import compilador.AnalizadorLexico;
import compilador.Token;
import compilador.Entrada;
import compilador.ElementoPolaca;
import java.util.ArrayList;
import java.util.Stack;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//#line 28 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short ELSE=259;
public final static short ID=260;
public final static short CTE_INT=261;
public final static short FOR=262;
public final static short PRINT=263;
public final static short CADENA=264;
public final static short ULONGINT=265;
public final static short INTEGER=266;
public final static short ASIGN=267;
public final static short COMP_IGUAL=268;
public final static short COMP_MENOR_IGUAL=269;
public final static short COMP_MAYOR_IGUAL=270;
public final static short COMP_DISTINTO=271;
public final static short BEGIN=272;
public final static short END=273;
public final static short STRUCT=274;
public final static short CTE_ULON=275;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    1,    1,    2,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    5,    5,    8,    7,    9,
   10,    9,    9,    9,    6,    6,   11,   11,   11,   11,
    4,    4,   12,   12,   12,   12,   19,   13,   13,   21,
   18,   18,   18,   18,   20,   20,   20,   20,   20,   20,
   17,   17,   17,   17,   17,   17,   23,   17,   24,   17,
   25,   17,   26,   17,   27,   17,   28,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   29,   17,   30,   17,
   14,   14,   14,   14,   34,   31,   31,   31,   31,   31,
   31,   31,   36,   33,   33,   33,   38,   33,   33,   39,
   35,   35,   32,   32,   32,   32,   32,   15,   15,   15,
   15,   15,   15,   16,   16,   16,   16,   16,   22,   22,
   22,   22,   22,   41,   41,   41,   41,   41,   37,   37,
   42,   42,   42,   42,   40,   40,   40,   40,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    3,    3,    2,    2,
    5,    2,    2,    2,    3,    1,    1,    0,    5,    3,
    0,    5,    2,    3,    1,    3,    1,    3,    2,    2,
    1,    2,    1,    1,    1,    1,    0,    4,    3,    0,
    4,    3,    2,    2,    3,    3,    2,    2,    1,    2,
    5,    5,    5,    5,    5,    5,    0,    5,    0,    5,
    0,    5,    0,    5,    0,    5,    0,    5,    4,    4,
    4,    4,    4,    4,    4,    4,    4,    4,    4,    4,
    3,    3,    3,    3,    3,    3,    0,    4,    0,    3,
    4,    3,    4,    3,    0,    7,    5,    5,    5,    6,
    6,    6,    0,    6,    4,    4,    0,    6,    5,    0,
    3,    1,    1,    1,    1,    2,    1,    5,    4,    4,
    4,    4,    5,    4,    4,    3,    3,    4,    3,    3,
    1,    2,    2,    3,    3,    1,    2,    2,    1,    1,
    1,    1,    1,    2,    3,    2,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   17,   16,   18,    0,    0,
    0,    0,    3,    4,    6,    0,    0,    0,    0,   33,
   34,   35,   36,    0,    0,    0,    0,  143,  142,    0,
    0,   37,    0,  136,  140,    0,  139,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  148,
    2,    5,    0,    9,    0,   12,    0,   10,    0,   32,
    0,   15,    0,    0,   39,    0,  146,    0,  144,   87,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  127,    0,   26,    0,  126,    0,    0,    0,
    0,   94,    0,   92,    0,    0,    0,    0,    0,    0,
    0,    0,    7,    8,    0,    0,    0,    0,   49,    0,
   44,   40,   43,   90,  145,    0,    0,    0,    0,    0,
    0,    0,   38,    0,    0,    0,    0,    0,    0,    0,
    0,  134,  135,  128,  124,    0,  115,  114,    0,    0,
  117,    0,    0,    0,    0,    0,   29,   93,   91,  121,
  122,    0,  120,    0,    0,  119,    0,  125,   48,    0,
   50,   47,    0,   42,    0,   88,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   77,   79,
   80,   78,   75,   76,  116,    0,    0,    0,    0,    0,
    0,   28,  123,  118,    0,   19,   11,   46,   45,   41,
   53,   62,   55,   66,   56,   68,   54,   64,   51,   58,
   52,   60,    0,    0,   97,    0,    0,   99,    0,   95,
   98,    0,   24,    0,  107,    0,  102,  100,  101,    0,
    0,    0,    0,    0,    0,   96,   22,  112,  105,    0,
    0,  106,    0,  103,    0,    0,  109,    0,  111,  108,
  104,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   95,  154,   17,   18,   48,  155,  231,
   96,   19,   20,   21,   22,   23,   32,   65,   72,  111,
  165,   42,  176,  178,  168,  174,  170,  172,  116,   66,
   45,  214,  215,  230,  239,  248,   34,  233,  240,   35,
   36,   37,
};
final static short yysindex[] = {                        42,
 -249,  -22,   81,  -18,  -12,    0,    0,    0, -233, -223,
    0,   42,    0,    0,    0,  -59,   -4,  -53,  237,    0,
    0,    0,    0, -199,   13, -110,    5,    0,    0, -168,
  -43,    0,  184,    0,    0,   69,    0,    5,  -25, -152,
 -147,   -1, -146, -176,   72,  106,   -8,   31,  118,    0,
    0,    0,  128,    0,  114,    0,  -27,    0,   99,    0,
  169,    0,   61,   61,    0,  138,    0,  -77,    0,    0,
  307, -110,  169,  169,  169,  169,   97,   97,  169,  169,
   97,   97,    0,   74,    0,    0,    0,  176,  -79, -218,
  176,    0,  237,    0,  -73,  -46,  237,  157,  -16,  238,
 -220,  254,    0,    0, -152,   18,  -32,   83,    0,   28,
    0,    0,    0,    0,    0,  171,  169,  169,  169,  169,
  169,  169,    0,   87,   96,  135,  154,   69,   69,  358,
  445,    0,    0,    0,    0,    5,    0,    0,   35,  266,
    0,  176,  176,   -6,  272,   54,    0,    0,    0,    0,
    0,   14,    0,    8,  212,    0,  288,    0,    0,  294,
    0,    0,  295,    0,   28,    0,  461,  334,  466,  335,
  467,  341,  472,  348,  485,  369,  486,  375,    0,    0,
    0,    0,    0,    0,    0,   40,  368,  376,   40,   24,
   40,    0,    0,    0,   44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  176,   37,    0,   40,   40,    0,   40,    0,
    0,    0,    0,  379,    0,   51,    0,    0,    0,   40,
 -220,  393,  169,  393,   50,    0,    0,    0,    0,  176,
  387,    0,  393,    0,  406,  393,    0,  393,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  423,    0,    0,    0,    0,    0,    0,    0,
    0,  365,    0,    0,    0,    0,    0,    0,   15,    0,
    0,    0,    0,    0,    0,  451,   93,    0,    0,    0,
    0,    0,    0,    0,    0,  149,    0,   47,    0,    0,
  229,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,   16,    0,   29,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  115,    0,    0,
    0,    0,    0,    0,    0,    0,  200,  336,    0,    0,
  122,  144,    0,    0,    0,  430,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -54,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  460,  475,  480,  493,
  495,  500,    0,  -90,   80,  112,  153,  363,  383,  183,
  204,    0,    0,    0,    0,   -5,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   63,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  209,    0,  218,    0,
  225,    0,  235,    0,  264,    0,  277,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  380,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -95,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  203,    0,  203,    0,    0,    0,    0,    0,    0,
    0,    0,  203,    0,    0,  203,    0,  203,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,  530,  533,  421,   66,   19,    0,    0, -183,    0,
  324,  416,    0,    0,    0,    0,    0,  476,    0,  -60,
    0,  464,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  306,  268,    0, -167,    0,  -72,    0,    0,    7,
  -13,    0,
};
final static int YYTABLESIZE=697;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   25,   30,   10,  113,   27,   58,   24,    9,  132,  133,
   25,  223,  149,   10,   31,   13,  105,   31,   24,   30,
   10,   44,   30,   10,  152,   24,  159,   47,   14,   20,
   49,  104,  100,   83,   55,  113,   50,  143,  139,   10,
   25,   78,  151,   77,    6,    7,   25,  237,  144,  164,
   68,   24,  189,  113,   56,   13,  113,   87,   85,   25,
   78,   13,   77,  128,  129,   16,  242,   61,   14,   24,
   24,   62,  194,   31,   14,  247,  158,   16,  250,   89,
  251,    9,  220,   90,  139,   10,  109,   10,  141,  141,
   91,  141,   69,  141,  141,  139,   10,  141,  226,   24,
    9,  213,  222,   24,  200,  141,   10,   53,  244,  234,
   81,    9,   86,   24,   24,   82,   78,   10,   77,  109,
   88,   30,    9,  157,   40,   30,   41,  179,   10,   78,
   94,   77,  135,  141,  141,  141,  180,  141,   78,  141,
   77,  162,   10,   30,   41,   63,   98,   64,  141,  141,
  141,  141,  141,  101,  141,  147,  147,  147,  102,  147,
  241,  147,  137,  137,  137,   83,  137,   83,  137,   21,
   21,   40,  103,  147,  147,  181,  147,   78,  114,   77,
  137,  137,  115,  137,  138,  138,  138,  142,  138,  131,
  138,  131,  141,  131,  182,  141,   78,  141,   77,  147,
   53,   27,  138,  138,   27,  138,   57,  131,  131,  148,
  131,  166,   70,   30,   10,  150,   27,   28,   31,  141,
  139,   10,  141,  141,    2,  141,   78,   59,   77,    4,
    5,   29,  141,   26,   27,   28,  141,   27,   28,   93,
  133,   43,  133,   80,  133,   79,  141,  110,  110,   29,
  113,   46,   29,  136,  137,   99,   25,   25,  133,  133,
   25,  133,   25,   25,   67,   25,   25,  195,  138,  193,
   31,   13,   13,   31,   25,   13,    9,   13,   13,  219,
   13,   13,   10,  107,   14,   14,  108,   31,   14,   13,
   14,   14,  225,   14,   14,  185,  153,    1,    2,  136,
  137,    3,   14,    4,    5,  243,    6,    7,    6,    7,
  136,  137,  156,  146,  138,    8,  107,    2,   30,  108,
   59,   30,    4,    5,  186,  138,  192,   92,    2,  134,
  191,   59,   93,    4,    5,   85,  196,   85,  161,    2,
   38,   28,   59,   93,    4,    5,  197,   39,  141,   78,
  141,   77,  198,  199,   93,   29,   27,   28,   38,   28,
  141,  141,  141,  141,    1,   39,  122,   86,  121,   86,
  147,   29,  147,   29,  202,  204,  132,  137,  132,  137,
  132,  206,  147,  147,  147,  147,  110,  112,  208,  137,
  137,  137,  137,  140,  132,  132,  145,  132,  183,  138,
   78,  138,   77,  130,  131,  130,  131,  130,   84,  210,
   84,  138,  138,  138,  138,  212,  131,  131,  131,  131,
   15,  130,  130,  129,  130,  129,  216,  129,   27,   28,
  160,  163,   15,  238,  217,  136,  137,  232,   81,   60,
   81,  129,  129,   29,  129,  246,  249,  187,  188,  190,
  138,   73,   74,   75,   76,  133,  218,  133,  221,   82,
   97,   82,  110,  110,   71,   33,   71,  133,  133,  133,
  133,  148,  148,   73,  148,   73,  148,  110,   97,   97,
   74,   25,   74,  227,  228,  184,  229,   78,  148,   77,
   72,   89,   72,    2,   71,  147,   59,  236,    4,    5,
   61,  201,   84,   78,   23,   77,  203,  205,   78,   78,
   77,   77,  207,  146,   78,   65,   77,   60,  224,   69,
   67,   69,   97,   97,  106,  209,  211,   78,   78,   77,
   77,  235,   70,   63,   70,   57,  124,  125,  126,  127,
   59,   51,  130,  131,   52,  245,    0,  123,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  117,  118,  119,  120,    0,    0,
  167,  169,  171,  173,  175,  177,    0,    0,    0,    0,
    0,  132,    0,  132,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  132,  132,  132,  132,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  130,    0,
  130,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  130,  130,  130,  130,    0,    0,    0,    0,  129,    0,
  129,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  129,  129,  129,  129,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  145,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   45,   46,   64,   59,   59,    0,   40,   81,   82,
  260,  195,   59,   46,    0,    0,   44,   40,   12,   45,
   46,   40,   45,   46,   41,   19,   59,   40,    0,  125,
  264,   59,   41,   59,   16,   41,  260,  256,   45,   46,
   40,   43,   59,   45,  265,  266,   46,  231,  267,  110,
   46,   45,   59,   59,   59,   40,   62,   59,   40,   59,
   43,   46,   45,   77,   78,    0,  234,  267,   40,   63,
   64,   59,   59,   59,   46,  243,   59,   12,  246,  256,
  248,   40,   59,  260,   45,   46,   59,   46,   42,   43,
  267,   45,  261,   47,   88,   45,   46,   91,   62,   93,
   40,   62,   59,   97,  165,   59,   46,  260,   59,   59,
   42,   40,  260,  107,  108,   47,   43,   46,   45,   59,
  267,   59,   40,  105,   44,   45,   46,   41,   46,   43,
   59,   45,   59,   41,   42,   43,   41,   45,   43,   47,
   45,   59,   46,   45,   46,  256,   41,  258,  142,  143,
  144,   59,   60,  123,   62,   41,   42,   43,   41,   45,
  233,   47,   41,   42,   43,  256,   45,  258,   47,  265,
  266,   44,   59,   59,   60,   41,   62,   43,   41,   45,
   59,   60,  260,   62,   41,   42,   43,  267,   45,   41,
   47,   43,  186,   45,   41,  189,   43,  191,   45,  273,
  260,  256,   59,   60,  259,   62,  260,   59,   60,  256,
   62,   41,  256,   45,   46,   59,  260,  261,  273,  213,
   45,   46,  216,  217,  257,  219,   43,  260,   45,  262,
  263,  275,  226,  256,  260,  261,  230,  260,  261,  272,
   41,  260,   43,   60,   45,   62,  240,   45,   46,  275,
  256,  264,  275,  260,  261,  264,  256,  257,   59,   60,
  260,   62,  262,  263,  260,  265,  266,  260,  275,  256,
  256,  256,  257,  259,  274,  260,   40,  262,  263,  256,
  265,  266,   46,  256,  256,  257,  259,  273,  260,  274,
  262,  263,  256,  265,  266,  261,   59,  256,  257,  260,
  261,  260,  274,  262,  263,  256,  265,  266,  265,  266,
  260,  261,   59,  267,  275,  274,  256,  257,  256,  259,
  260,  259,  262,  263,   59,  275,  273,  256,  257,  256,
   59,  260,  272,  262,  263,  256,  125,  258,  256,  257,
  260,  261,  260,  272,  262,  263,   59,  267,  256,   43,
  258,   45,   59,   59,  272,  275,  260,  261,  260,  261,
  268,  269,  270,  271,    0,  267,   60,  256,   62,  258,
  256,  275,  258,  275,   41,   41,   41,  256,   43,  258,
   45,   41,  268,  269,  270,  271,   63,   64,   41,  268,
  269,  270,  271,   88,   59,   60,   91,   62,   41,  256,
   43,  258,   45,   41,  256,   43,  258,   45,  256,   41,
  258,  268,  269,  270,  271,   41,  268,  269,  270,  271,
    0,   59,   60,   41,   62,   43,   59,   45,  260,  261,
  107,  108,   12,   41,   59,  260,  261,   59,  256,   19,
  258,   59,   60,  275,   62,   59,   41,  142,  143,  144,
  275,  268,  269,  270,  271,  256,  189,  258,  191,  256,
   45,  258,  260,  261,  256,    2,  258,  268,  269,  270,
  271,   42,   43,  256,   45,  258,   47,  275,   63,   64,
  256,   59,  258,  216,  217,   41,  219,   43,   59,   45,
  256,   41,  258,  257,   31,  267,  260,  230,  262,  263,
   41,   41,   39,   43,  125,   45,   41,   41,   43,   43,
   45,   45,   41,   93,   43,   41,   45,   97,  213,  256,
   41,  258,  107,  108,   61,   41,   41,   43,   43,   45,
   45,  226,  256,   41,  258,   41,   73,   74,   75,   76,
   41,   12,   79,   80,   12,  240,   -1,   72,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  268,  269,  270,  271,   -1,   -1,
  117,  118,  119,  120,  121,  122,   -1,   -1,   -1,   -1,
   -1,  256,   -1,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  268,  269,  270,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  268,  269,  270,  271,   -1,   -1,   -1,   -1,  256,   -1,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  268,  269,  270,  271,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  267,
};
}
final static short YYFINAL=11;
final static short YYMAXTOKEN=275;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","THEN","ELSE","ID","CTE_INT","FOR",
"PRINT","CADENA","ULONGINT","INTEGER","ASIGN","COMP_IGUAL","COMP_MENOR_IGUAL",
"COMP_MAYOR_IGUAL","COMP_DISTINTO","BEGIN","END","STRUCT","CTE_ULON",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque_declaracion",
"programa : bloque_declaracion bloque_sentencias",
"programa : bloque_sentencias",
"bloque_declaracion : declaracion",
"bloque_declaracion : bloque_declaracion declaracion",
"bloque_sentencias : conjunto_sentencias",
"declaracion : tipo lista_variables ';'",
"declaracion : estructura ID ';'",
"declaracion : tipo ';'",
"declaracion : estructura ';'",
"declaracion : estructura ID ',' lista_variables ';'",
"declaracion : lista_variables ';'",
"declaracion : tipo lista_variables",
"declaracion : estructura ID",
"declaracion : error ID ';'",
"tipo : INTEGER",
"tipo : ULONGINT",
"$$1 :",
"estructura : STRUCT $$1 '{' cuerpo_estructura '}'",
"cuerpo_estructura : tipo ID ';'",
"$$2 :",
"cuerpo_estructura : tipo ID ';' $$2 cuerpo_estructura",
"cuerpo_estructura : tipo ID",
"cuerpo_estructura : tipo ID cuerpo_estructura",
"lista_variables : ID",
"lista_variables : ID ',' lista_variables",
"bloque : sentencia",
"bloque : BEGIN conjunto_sentencias END",
"bloque : conjunto_sentencias END",
"bloque : BEGIN conjunto_sentencias",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : sentencia conjunto_sentencias",
"sentencia : seleccion",
"sentencia : bucle",
"sentencia : impresion",
"sentencia : asignacion",
"$$3 :",
"seleccion : IF condicion $$3 seleccionThen",
"seleccion : IF error seleccionThen",
"$$4 :",
"seleccionThen : THEN bloque $$4 seleccionElse",
"seleccionThen : error bloque seleccionElse",
"seleccionThen : THEN seleccionElse",
"seleccionThen : error seleccionElse",
"seleccionElse : ELSE bloque ';'",
"seleccionElse : error bloque ';'",
"seleccionElse : ELSE ';'",
"seleccionElse : error ';'",
"seleccionElse : ';'",
"seleccionElse : ELSE error",
"condicion : '(' expresion '>' expresion ')'",
"condicion : '(' expresion '<' expresion ')'",
"condicion : '(' expresion COMP_IGUAL expresion ')'",
"condicion : '(' expresion COMP_DISTINTO expresion ')'",
"condicion : '(' expresion COMP_MENOR_IGUAL expresion ')'",
"condicion : '(' expresion COMP_MAYOR_IGUAL expresion ')'",
"$$5 :",
"condicion : '(' expresion '>' $$5 ')'",
"$$6 :",
"condicion : '(' expresion '<' $$6 ')'",
"$$7 :",
"condicion : '(' expresion COMP_IGUAL $$7 ')'",
"$$8 :",
"condicion : '(' expresion COMP_DISTINTO $$8 ')'",
"$$9 :",
"condicion : '(' expresion COMP_MENOR_IGUAL $$9 ')'",
"$$10 :",
"condicion : '(' expresion COMP_MAYOR_IGUAL $$10 ')'",
"condicion : '(' expresion '>' expresion",
"condicion : '(' expresion '<' expresion",
"condicion : '(' expresion COMP_IGUAL expresion",
"condicion : '(' expresion COMP_DISTINTO expresion",
"condicion : '(' expresion COMP_MENOR_IGUAL expresion",
"condicion : '(' expresion COMP_MAYOR_IGUAL expresion",
"condicion : expresion '>' expresion ')'",
"condicion : expresion '<' expresion ')'",
"condicion : expresion COMP_IGUAL expresion ')'",
"condicion : expresion COMP_DISTINTO expresion ')'",
"condicion : expresion COMP_MENOR_IGUAL expresion ')'",
"condicion : expresion COMP_MAYOR_IGUAL expresion ')'",
"condicion : expresion '>' expresion",
"condicion : expresion '<' expresion",
"condicion : expresion COMP_IGUAL expresion",
"condicion : expresion COMP_DISTINTO expresion",
"condicion : expresion COMP_MENOR_IGUAL expresion",
"condicion : expresion COMP_MAYOR_IGUAL expresion",
"$$11 :",
"condicion : '(' error $$11 ')'",
"$$12 :",
"condicion : error $$12 ')'",
"bucle : FOR condicion_for bloque ';'",
"bucle : FOR condicion_for ';'",
"bucle : FOR condicion_for bloque error",
"bucle : FOR condicion_for error",
"$$13 :",
"condicion_for : '(' ID ASIGN factor_for ';' $$13 comparacion_for",
"condicion_for : ID ASIGN factor_for ';' comparacion_for",
"condicion_for : '(' ASIGN factor_for ';' comparacion_for",
"condicion_for : '(' ID ASIGN ';' comparacion_for",
"condicion_for : '(' ID error factor_for ';' comparacion_for",
"condicion_for : '(' ID ASIGN factor_for error comparacion_for",
"condicion_for : '(' error ASIGN factor_for ';' comparacion_for",
"$$14 :",
"comparacion_for : factor_for '>' factor_for ';' $$14 decremento_for",
"comparacion_for : '>' factor_for ';' decremento_for",
"comparacion_for : factor_for '>' ';' decremento_for",
"$$15 :",
"comparacion_for : factor_for error $$15 factor ';' decremento_for",
"comparacion_for : factor_for '>' factor_for error decremento_for",
"$$16 :",
"decremento_for : $$16 factor_for ')'",
"decremento_for : ')'",
"factor_for : ID",
"factor_for : CTE_ULON",
"factor_for : CTE_INT",
"factor_for : '-' CTE_INT",
"factor_for : elemento_estructura",
"impresion : PRINT '(' CADENA ')' ';'",
"impresion : '(' CADENA ')' ';'",
"impresion : PRINT '(' ')' ';'",
"impresion : PRINT CADENA ')' ';'",
"impresion : PRINT '(' CADENA ';'",
"impresion : PRINT '(' CADENA ')' error",
"asignacion : ID ASIGN expresion ';'",
"asignacion : elemento_estructura ASIGN expresion ';'",
"asignacion : ID expresion ';'",
"asignacion : ID ASIGN ';'",
"asignacion : ID ASIGN expresion error",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : expresion '+'",
"expresion : expresion '-'",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : termino '*'",
"termino : termino '/'",
"factor : factor_comun",
"factor : elemento_estructura",
"factor_comun : ID",
"factor_comun : CTE_ULON",
"factor_comun : CTE_INT",
"factor_comun : '-' CTE_INT",
"elemento_estructura : ID '.' ID",
"elemento_estructura : ID ID",
"elemento_estructura : ID '.'",
"elemento_estructura : '.' ID",
};

//#line 364 "gramatica.txt"

private AnalizadorLexico anaLex;
private ArrayList<String> errores;
private ArrayList<String> tokens;
private ArrayList<String> salida;
private ArrayList<String> elemento_estructuras;
private String error_anterior;
private int linea_error_anterior;

// para manejo de los tipos de datos
private Tipo tipo_dato;
private ArrayList<Entrada> identificadores;

// para manejo de errores semanticos
private ArrayList<String> erroresSemanticos;
private Tipo tipoExpresion;

// para control de tipos en el for
private ArrayList<Entrada> elementosDelFor;

//para manejo de polaca inversa
private ArrayList<ElementoPolaca> polacaInversa;
private Stack<Integer> pila;
private Stack<Integer> retornos;
private boolean almacenarDecremento;
private Entrada estructuraPolaca;
private Stack<ElementoPolaca> decrementosFor;
private Stack<Entrada> indicesFor;

public static final int MAX_INTEGER_NEG = 32767;
public static final int MAX_INTEGER = 32768;
public static final float MAX_ULONGINTEGER = 4294967295f;

public Parser(AnalizadorLexico analLex){
  anaLex=analLex;
  error_anterior = "";
  errores = new ArrayList<String>();
  tokens = new ArrayList<String>();
  salida = new ArrayList<String>();
  polacaInversa = new ArrayList<ElementoPolaca>();
  elementosDelFor = new ArrayList<Entrada>();
  erroresSemanticos = new ArrayList<String>();
  identificadores = new ArrayList<Entrada>();
  tipoExpresion = Tipo.INTEGER;
  almacenarDecremento = false;
  decrementosFor=new Stack<ElementoPolaca>();
  pila=new Stack<Integer>();
  retornos=new Stack<Integer>();
  indicesFor=new Stack<Entrada>();
}

int yylex() {
        Token t = this.anaLex.getToken();
        tokens.add(t.getNombre());
        yylval = t.getAtributoYacc();
        return t.getCodigoYacc();
}

void yyout(String texto) {
  salida.add("En linea " + anaLex.getNumeroLinea() + " " + texto);

}

void yyerror(String texto) {
     if(!texto.equals("syntax error"))
          errores.add("En linea " + anaLex.getNumeroLinea() + ": se encontro el error: " + texto);
     else{
         if(error_anterior.equals(texto))
              errores.add("En linea " + linea_error_anterior + ": ocurrio un error sintactico");
     }
     linea_error_anterior = anaLex.getNumeroLinea();
     error_anterior = texto;
}

public ArrayList<String> getErrores(){
	return this.errores;
}

public ArrayList<String> getTokens(){
        return this.tokens;
}

public ArrayList<String> getSalida(){
   ArrayList<String> retorno = new ArrayList<String>();
   retorno.add("Analizador lï¿½xico: ");
   retorno.addAll(this.tokens);
   retorno.add("Analizador sintï¿½ctico: ");
   retorno.addAll(this.salida);
   return retorno;
}

private void fueraRangoIntPositivo(Entrada e){
    int val = (Integer) (e.getValor());
    if ((0 <= val) && (val > MAX_INTEGER-1))
       yyerror("entero fuera de rango [-32768;32767]");
}

private void controlarTiposFor(){
    Tipo t = elementosDelFor.get(0).getTipo_dato();
    int i=0;
    while ((i<this.elementosDelFor.size()) && (t==this.elementosDelFor.get(i).getTipo_dato())) {
         i++;
    }
    if (i<this.elementosDelFor.size()) {
        this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": Se esperaba un identificador/constante de tipo " 
                                       + t + " pero se encontro " + this.elementosDelFor.get(i).getTipo_dato());
        
    }
    this.elementosDelFor.clear();
}

private void controlarTipoAsignacion(Entrada id){
	if((id.getTipo_dato() == Tipo.INTEGER) && (tipoExpresion == Tipo.ULONGINT))
		this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": Incopatibilidad de tipo, se esperaba un entero");
	else if (id.getTipo_dato() == Tipo.STRUCT)
			this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": no se pueden realizar asignaciones a estructuras");
}

private void setearTipoElementosEstructuras(Entrada e) {
    elemento_estructuras.add(e.getNombre());
    e = anaLex.getTablaSimbolos().get(e.getNombre());
    e.setTipo_dato(this.tipo_dato);
    this.tipo_dato = null;
}

private boolean estaDeclarada(Entrada e) {
    if (e.getTipo_dato()==null) {
        this.erroresSemanticos.add("En linea: " + anaLex.getNumeroLinea() + " - La variable " + e.getNombre() + " no esta declarada.");
		return false;
    }
	return true;
}


private boolean estaRedeclarada(Entrada e) {
    if (e.getTipo_dato()!=null) {
        this.erroresSemanticos.add("En linea: " + anaLex.getNumeroLinea() + " - La variable " + e.getNombre() + " ya esta declarada.");
        return true;
    }
    return false;
}

public ArrayList<String> getErroresSemanticos() {
    return this.erroresSemanticos;
}

public ArrayList<ElementoPolaca> getPolacaInversa(){
    return this.polacaInversa;
}

public boolean isCompilable(){
       return (erroresSemanticos.isEmpty() && errores.isEmpty());
}

//metodos para controlar la polaca
private void agregarAPolacaId(Entrada e){
    Tipo t = anaLex.getTablaSimbolos().get(e.getNombre()).getTipo_dato();
    ElementoPolaca ep = new ElementoPolaca(ElementoPolaca.VARIABLE, t,e.getNombre());
    if (almacenarDecremento)
       decrementosFor.push(ep);
    else
        polacaInversa.add(ep);
}

private void agregarAPolacaConstante(Entrada e){
    ElementoPolaca ep;
    if(e.getTipo() == Tipo.CONSTANTE_INTEGER)
    ep = new ElementoPolaca(ElementoPolaca.CONSTANTE, Tipo.INTEGER,e.getValor().toString());
    else
      if(e.getTipo() == Tipo.CONSTANTE_ULONGINT)
        ep = new ElementoPolaca(ElementoPolaca.CONSTANTE, Tipo.ULONGINT,e.getValor().toString().replace(".0", ""));
      else
        ep = new ElementoPolaca(ElementoPolaca.CADENA,e.getValor().toString());
    if(almacenarDecremento)
        decrementosFor.push(ep);
    else 
        polacaInversa.add(ep);
}

private void agregarAPolaca(int t){
     polacaInversa.add(new ElementoPolaca(t));
}

private void agregarAPolacaDecremento(){
    Entrada indice = indicesFor.pop();
    agregarAPolacaId(indice);
    polacaInversa.add(decrementosFor.pop());
    polacaInversa.add(new ElementoPolaca(ElementoPolaca.RESTA));
    agregarAPolacaId(indice);
    polacaInversa.add(new ElementoPolaca(ElementoPolaca.ASIGNACION));
}

private void guardarIndiceFor(Entrada e){
    this.indicesFor.push(e);
}


private void apilar(int s) {
        pila.push(polacaInversa.size());
        polacaInversa.add(new ElementoPolaca(s));
}

private void desapilar(int desplazamiento) {
        int pos = pila.pop().intValue();
        int direccion = polacaInversa.size()+desplazamiento;
        polacaInversa.get(pos).setNombre("LABEL_"+Integer.toString(direccion));
}

private void agregarRotulo(){
    polacaInversa.add(new ElementoPolaca(ElementoPolaca.ROTULO,"LABEL_"+Integer.toString(polacaInversa.size())));
}

private void apilarDireccion() {
        retornos.push(polacaInversa.size()-1);
}

private void desapilarDireccion() {
        int pos=pila.pop().intValue();
        int direccion=retornos.pop();
        polacaInversa.get(pos).setNombre("LABEL_"+Integer.toString(direccion));
}
//#line 799 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
//    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 22 "gramatica.txt"
{yyout("PROGRAMA.");}
break;
case 2:
//#line 23 "gramatica.txt"
{yyout("PROGRAMA.");}
break;
case 3:
//#line 24 "gramatica.txt"
{yyout("PROGRAMA.");}
break;
case 4:
//#line 28 "gramatica.txt"
{yyout("declaracion.");}
break;
case 5:
//#line 29 "gramatica.txt"
{yyout("Bloque de declaraciones.");}
break;
case 6:
//#line 32 "gramatica.txt"
{yyout("Bloque de sentencias.");}
break;
case 7:
//#line 36 "gramatica.txt"
{
                                         for (int i=0;i<this.identificadores.size();i++) {
                                             this.identificadores.get(i).setTipo_dato(this.tipo_dato);
                                         }
                                         this.tipo_dato = null;
                                         this.identificadores.clear();
                                       }
break;
case 8:
//#line 43 "gramatica.txt"
{    /* Recupero la tabla de simbolos*/
                                    TablaSimbolos t = anaLex.getTablaSimbolos();
                                    /* Recupero la entrada de la estructura padre*/
                                    Entrada padre = t.get(((Entrada)val_peek(1).obj).getNombre());
                                    padre.setTipo_dato(Tipo.STRUCT);
                                    int distancia_acumulada = 0;
                                    for (int i=0;i<elemento_estructuras.size();i++) {
                                        Entrada e = t.get(elemento_estructuras.get(i));
                                        t.removeEntrada(e.getNombre());
                                        Entrada eNueva = new EntradaEstructura(e.getNombre(), e.getTipo(), e.getValor(),padre,distancia_acumulada);
                                        eNueva.setTipo_dato(e.getTipo_dato());
                                        t.addNuevaEntrada(eNueva);
                                        /* Actualizo la distancia*/
                                        if (e.getTipo_dato()==Tipo.INTEGER)
                                            distancia_acumulada = distancia_acumulada + 2;
                                        else if (e.getTipo_dato()==Tipo.ULONGINT)
                                            distancia_acumulada = distancia_acumulada + 4;
                                    }
                                    padre.setTamanio(distancia_acumulada);
                                  }
break;
case 9:
//#line 64 "gramatica.txt"
{ yyerror("falta nombre de variable en declaracion.");}
break;
case 10:
//#line 65 "gramatica.txt"
{yyerror("falta nombre de la estructura en declaraciï¿½n.");}
break;
case 11:
//#line 66 "gramatica.txt"
{yyerror("declaracion multiple en estructuras esta prohibida.");}
break;
case 12:
//#line 67 "gramatica.txt"
{ yyerror("declaracion de variable sin tipo definido.");}
break;
case 13:
//#line 68 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 14:
//#line 69 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 15:
//#line 70 "gramatica.txt"
{yyerror("tipo primitivo no admitido por el lenguaje.");}
break;
case 16:
//#line 74 "gramatica.txt"
{this.tipo_dato=Tipo.INTEGER;}
break;
case 17:
//#line 75 "gramatica.txt"
{this.tipo_dato=Tipo.ULONGINT;}
break;
case 18:
//#line 78 "gramatica.txt"
{yyout("Sentencia de declaracion de estructura"); elemento_estructuras = new ArrayList<String>();}
break;
case 20:
//#line 81 "gramatica.txt"
{
                                      setearTipoElementosEstructuras((Entrada)val_peek(1).obj);
                                }
break;
case 21:
//#line 84 "gramatica.txt"
{
                                      setearTipoElementosEstructuras((Entrada)val_peek(1).obj);
                                }
break;
case 23:
//#line 88 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 24:
//#line 89 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 25:
//#line 92 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                       if (!(this.estaRedeclarada(e))) {
                         this.identificadores.add(e);
                       }
                      }
break;
case 26:
//#line 97 "gramatica.txt"
{ Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(2).obj).getNombre());
                                            if (!(this.estaRedeclarada(e))) {
	                                        this.identificadores.add(e);
                                            }
                                         }
break;
case 27:
//#line 104 "gramatica.txt"
{yyout("Una sola sentencia.");}
break;
case 28:
//#line 105 "gramatica.txt"
{yyout("Bloque de sentencias.");}
break;
case 29:
//#line 106 "gramatica.txt"
{ yyerror("se esperaba un 'begin' al inicio de un bloque de sentencias ejecutables.");}
break;
case 30:
//#line 107 "gramatica.txt"
{ yyerror("se esperaba un 'end' al final de un bloque de sentencias ejecutables.");}
break;
case 37:
//#line 121 "gramatica.txt"
{apilar(ElementoPolaca.BF);}
break;
case 38:
//#line 121 "gramatica.txt"
{yyout("Condicional.");}
break;
case 39:
//#line 123 "gramatica.txt"
{ yyerror("la condicion de seleccion no es valida.");}
break;
case 40:
//#line 126 "gramatica.txt"
{desapilar(1); apilar(ElementoPolaca.BI);agregarRotulo();}
break;
case 42:
//#line 128 "gramatica.txt"
{yyerror("se esperaba un 'then'.");}
break;
case 43:
//#line 129 "gramatica.txt"
{yyerror("se esperaba un bloque luego de la palabra reservada 'then'.");}
break;
case 44:
//#line 130 "gramatica.txt"
{yyerror("se esperaba un 'then'.");}
break;
case 45:
//#line 133 "gramatica.txt"
{desapilar(0); agregarRotulo();}
break;
case 46:
//#line 135 "gramatica.txt"
{yyerror("se esperaba un 'else'.");}
break;
case 47:
//#line 136 "gramatica.txt"
{yyerror("se esperaba un bloque luego de la palabra reservada 'else'.");}
break;
case 48:
//#line 137 "gramatica.txt"
{yyerror("falta rama del else");}
break;
case 49:
//#line 138 "gramatica.txt"
{yyerror("falta rama del else");}
break;
case 50:
//#line 139 "gramatica.txt"
{yyerror("falta punto y coma");}
break;
case 51:
//#line 142 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYOR);}
break;
case 52:
//#line 143 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENOR);}
break;
case 53:
//#line 144 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.IGUAL);}
break;
case 54:
//#line 145 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.DISTINTO);}
break;
case 55:
//#line 146 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENORIGUAL);}
break;
case 56:
//#line 147 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYORIGUAL);}
break;
case 57:
//#line 149 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 59:
//#line 150 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 61:
//#line 151 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 63:
//#line 152 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 65:
//#line 153 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 67:
//#line 154 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 69:
//#line 156 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 70:
//#line 157 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 71:
//#line 158 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 72:
//#line 159 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 73:
//#line 160 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 74:
//#line 161 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 75:
//#line 163 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 76:
//#line 164 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 77:
//#line 165 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 78:
//#line 166 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 79:
//#line 167 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 80:
//#line 168 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 81:
//#line 170 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 82:
//#line 171 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 83:
//#line 172 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 84:
//#line 173 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 85:
//#line 174 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 86:
//#line 175 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 87:
//#line 177 "gramatica.txt"
{yyerror("Se encontro un error en la condicion.");}
break;
case 89:
//#line 178 "gramatica.txt"
{yyerror("Se encontro un error en la condicion");}
break;
case 91:
//#line 182 "gramatica.txt"
{agregarAPolacaDecremento();
                                         apilar(ElementoPolaca.BI);
                                         desapilarDireccion();
                                         desapilar(0);
                                         agregarRotulo();
                                         yyout("Bucle for.");}
break;
case 92:
//#line 189 "gramatica.txt"
{yyerror("se esperaba un bloque de sentencias, pero se encontro ';'.");}
break;
case 93:
//#line 190 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 94:
//#line 191 "gramatica.txt"
{yyerror("se esperaba finalizacion de bloque del for.");}
break;
case 95:
//#line 195 "gramatica.txt"
{ Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(3).obj).getNombre());
                                              estaDeclarada(e);
                                              this.elementosDelFor.add(0,e); /* agrego la entrada del ID a los elementos del for en la posicion 0*/

                                              guardarIndiceFor((Entrada)val_peek(3).obj);
                                              agregarAPolacaId((Entrada)val_peek(3).obj);
                                              agregarAPolaca(ElementoPolaca.ASIGNACION);
                                              agregarRotulo();
                                              apilarDireccion();
                                              }
break;
case 97:
//#line 206 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condicion.");}
break;
case 98:
//#line 207 "gramatica.txt"
{ yyerror("se esperaba un identificador antes del operador de asignacion.");}
break;
case 99:
//#line 208 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues del operador de asignacion.");}
break;
case 100:
//#line 209 "gramatica.txt"
{ yyerror("se esperaba operador de asignacion.");}
break;
case 101:
//#line 210 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 102:
//#line 211 "gramatica.txt"
{ yyerror("se encontro un error antes del operador de asignacion.");}
break;
case 103:
//#line 214 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MAYOR);
                                                 apilar(ElementoPolaca.BF);}
break;
case 105:
//#line 217 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante antes de '>'.");}
break;
case 106:
//#line 218 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues '>'.");}
break;
case 107:
//#line 219 "gramatica.txt"
{ yyerror("se esperaba '>'.");}
break;
case 109:
//#line 220 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 110:
//#line 223 "gramatica.txt"
{almacenarDecremento = true;}
break;
case 111:
//#line 223 "gramatica.txt"
{almacenarDecremento = false; controlarTiposFor();}
break;
case 112:
//#line 225 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues de ';'.");}
break;
case 113:
//#line 229 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);
                 this.elementosDelFor.add(e);
				 if(e.getTipo_dato() == Tipo.STRUCT) this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": no se puede utilizar el nombre de una estructura en la condicion del for");
                 agregarAPolacaId(e);}
break;
case 114:
//#line 234 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                          estaDeclarada(e);
                          this.elementosDelFor.add(e);
                          agregarAPolacaConstante(e);}
break;
case 115:
//#line 238 "gramatica.txt"
{ Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                             this.elementosDelFor.add(e);
                             e.visitar();
                             fueraRangoIntPositivo((Entrada)val_peek(0).obj);
                             agregarAPolacaConstante((Entrada)val_peek(0).obj); }
break;
case 116:
//#line 243 "gramatica.txt"
{Entrada actual = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
    				if (!(actual).isVisitado()) {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(), (Integer) actual.getValor() * -1);
        			e.setTipo_dato(Tipo.INTEGER);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
        			anaLex.getTablaSimbolos().removeEntrada(((Entrada)(val_peek(0).obj)).getNombre());
        			elementosDelFor.add(e);
 				agregarAPolacaConstante(e);
    				} else {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(),(Integer)actual.getValor() * -1);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
        			elementosDelFor.add(e);
 				agregarAPolacaConstante(e);
 				}
			    }
break;
case 117:
//#line 258 "gramatica.txt"
{this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": no se puede utilizar estructuras en la condicion del for");}
break;
case 118:
//#line 262 "gramatica.txt"
{yyout("Sentencia de impresion.");
                                      agregarAPolacaConstante((Entrada)val_peek(2).obj);
                                      agregarAPolaca(ElementoPolaca.PRINT);}
break;
case 119:
//#line 266 "gramatica.txt"
{ yyerror("se esperaba 'print'");}
break;
case 120:
//#line 267 "gramatica.txt"
{ yyerror("se esperaba una cadena dentro de la instruccion print.");}
break;
case 121:
//#line 268 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la cadena en sentencia print.");}
break;
case 122:
//#line 269 "gramatica.txt"
{ yyerror("sentencia print sin parentesis de cierre.");}
break;
case 123:
//#line 271 "gramatica.txt"
{ yyerror("se esperaba ';.'");}
break;
case 124:
//#line 274 "gramatica.txt"
{
                                        Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(3).obj).getNombre());
                                        estaDeclarada(e);
										System.out.println("tipo token de la entrada: " + e.getTipo());
										controlarTipoAsignacion((Entrada)val_peek(3).obj);
										tipoExpresion = Tipo.INTEGER;
                                        agregarAPolacaId((Entrada)val_peek(3).obj);
                                        agregarAPolaca(ElementoPolaca.ASIGNACION);
                                    }
break;
case 125:
//#line 283 "gramatica.txt"
{yyout("Asignacion a elemento de estructura.");
														 controlarTipoAsignacion((Entrada)val_peek(3).obj);
														 tipoExpresion = Tipo.INTEGER;
                                                         agregarAPolacaId((Entrada)val_peek(3).obj);
                                                         agregarAPolaca(ElementoPolaca.ASIGNACION);}
break;
case 126:
//#line 289 "gramatica.txt"
{ yyerror("se esperaba el operador de asignacion ':='.");}
break;
case 127:
//#line 290 "gramatica.txt"
{ yyerror("se esperaba una expresion del lado derecho de la asignacion.");}
break;
case 128:
//#line 291 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 129:
//#line 294 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.SUMA);yyout("Sentencia de suma.");}
break;
case 130:
//#line 295 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.RESTA);yyout("Sentencia de resta.");}
break;
case 132:
//#line 298 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '+'.");}
break;
case 133:
//#line 299 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '-'.");}
break;
case 134:
//#line 303 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MULTIPLICACION);yyout("Sentencia de producto.");}
break;
case 135:
//#line 304 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.DIVISION);yyout("Sentencia de division.");}
break;
case 137:
//#line 307 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '*'.");}
break;
case 138:
//#line 308 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '/'.");}
break;
case 140:
//#line 312 "gramatica.txt"
{agregarAPolacaId((Entrada)val_peek(0).obj);}
break;
case 141:
//#line 315 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);
				 if(e.getTipo_dato() == Tipo.ULONGINT) tipoExpresion = Tipo.ULONGINT;
				 if(e.getTipo_dato() == Tipo.STRUCT) this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": no se puede utilizar el nombre de una estructura como operando en una expresion");
                 agregarAPolacaId(e);}
break;
case 142:
//#line 320 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                          estaDeclarada(e);
						  tipoExpresion = Tipo.ULONGINT;
                          agregarAPolacaConstante(e);}
break;
case 143:
//#line 324 "gramatica.txt"
{ Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                             e.visitar();
                             fueraRangoIntPositivo((Entrada)val_peek(0).obj);
                             agregarAPolacaConstante((Entrada)val_peek(0).obj); }
break;
case 144:
//#line 328 "gramatica.txt"
{Entrada actual = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
    				if (!(actual).isVisitado()) {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(), (Integer) actual.getValor() * -1);
        			e.setTipo_dato(Tipo.INTEGER);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
        			anaLex.getTablaSimbolos().removeEntrada(((Entrada)(val_peek(0).obj)).getNombre());
 				agregarAPolacaConstante(e);
    				} else {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(),(Integer)actual.getValor() * -1);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
 				agregarAPolacaConstante(e);
 				}
			    }
break;
case 145:
//#line 343 "gramatica.txt"
{yyout("Elemento de estructura.");
                                  Entrada e = (Entrada)val_peek(0).obj;
                                  Entrada ePadre = anaLex.getTablaSimbolos().get(((Entrada)val_peek(2).obj).getNombre());
                                  if(estaDeclarada(e)){
	                                 EntradaEstructura eHijo = (EntradaEstructura) (anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()));
	                                 if (!ePadre.getNombre().equals(eHijo.getEstructura().getNombre())) {
	                                    this.erroresSemanticos.add("En linea: " + anaLex.getNumeroLinea() + " - El identificador " + eHijo.getNombre() + " no pertenece a la estructura " + ePadre.getNombre());
	                                 }
                                  } 
                                  yyval = val_peek(0);
                                  }
break;
case 146:
//#line 355 "gramatica.txt"
{ yyerror("se esperaba un punto.");}
break;
case 147:
//#line 356 "gramatica.txt"
{ yyerror("se esperaba identificador de la variable mientro de la estructura.");}
break;
case 148:
//#line 357 "gramatica.txt"
{ yyerror("se esperaba identificador de la estructura antes del punto.");}
break;
//#line 1571 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################

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
import java.util.Vector;
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
    3,    3,    3,    3,    5,    5,    5,    8,    7,    9,
   10,    9,    9,    9,    6,    6,   11,   11,   11,   11,
    4,    4,   12,   12,   12,   12,   19,   13,   13,   21,
   18,   18,   18,   20,   20,   20,   17,   17,   17,   17,
   17,   17,   23,   17,   24,   17,   25,   17,   26,   17,
   27,   17,   28,   17,   17,   17,   17,   17,   17,   17,
   29,   17,   30,   17,   22,   22,   22,   22,   22,   16,
   16,   16,   16,   16,   31,   31,   31,   31,   31,   33,
   33,   32,   32,   32,   32,   14,   14,   14,   35,   35,
   35,   35,   35,   35,   35,   35,   35,   36,   35,   35,
   35,   35,   34,   34,   34,   34,   15,   15,   15,   15,
   15,   15,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    3,    3,    2,    2,
    5,    2,    2,    2,    1,    1,    1,    0,    5,    3,
    0,    5,    2,    3,    1,    3,    1,    3,    2,    2,
    1,    2,    1,    1,    1,    1,    0,    4,    3,    0,
    4,    4,    3,    3,    3,    2,    5,    5,    5,    5,
    5,    5,    0,    5,    0,    5,    0,    5,    0,    5,
    0,    5,    0,    5,    4,    4,    4,    4,    4,    4,
    0,    4,    0,    3,    3,    3,    1,    2,    2,    4,
    4,    3,    3,    4,    3,    3,    1,    2,    2,    1,
    1,    3,    2,    2,    2,    4,    3,    4,   11,   10,
   10,   10,   10,   10,   10,   11,   11,    0,   12,   11,
   11,    1,    1,    1,    1,    2,    5,    4,    4,    4,
    4,    5,
};
final static short yydefred[] = {                         0,
   17,    0,    0,    0,    0,   16,   15,   18,    0,    0,
    0,    0,    3,    4,    6,    0,    0,    0,    0,   33,
   34,   35,   36,    0,    0,    0,   37,    0,  115,    0,
  114,    0,    0,    0,    0,    0,   91,   87,   90,  112,
    0,    0,    0,    0,    0,    0,    0,   95,    2,    5,
    0,    9,    0,   12,    0,   10,    0,   32,    0,    0,
    0,   39,    0,   71,    0,    0,    0,   93,    0,   83,
    0,  116,   26,    0,    0,    0,   82,    0,    0,    0,
    0,    0,    0,   97,    0,    0,    0,    0,    0,    0,
    0,    0,    7,    8,    0,    0,    0,    0,    0,   40,
    0,   74,    0,    0,    0,    0,    0,    0,    0,   38,
   92,   84,   80,    0,    0,   85,   86,  113,    0,    0,
    0,    0,    0,   29,   98,   96,  120,  121,    0,  119,
    0,    0,  118,    0,   81,    0,    0,   46,    0,    0,
   43,   72,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   28,
  122,  117,    0,   19,   11,   42,   45,   44,   41,   49,
   58,   51,   62,   52,   64,   50,   60,   47,   54,   48,
   56,    0,    0,    0,    0,    0,    0,    0,   24,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  108,    0,    0,   22,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  100,    0,  102,    0,  103,
    0,  104,    0,  105,    0,  101,  106,  107,    0,  110,
  111,   99,  109,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   85,  131,   17,   18,   46,  132,  197,
   86,   19,   20,   21,   22,   23,   27,   62,   67,  101,
  140,   35,  152,  154,  144,  150,  146,  148,  103,   63,
   36,   37,   38,   39,   43,  212,
};
final static short yysindex[] = {                       -40,
    0,  -38,  -21,   -4,  -14,    0,    0,    0, -247, -207,
    0,  -40,    0,    0,    0,  -54,   -2,  -50,  162,    0,
    0,    0,    0, -196, -165,    9,    0,  -36,    0,  -13,
    0, -176, -137, -130,   22,   59,    0,    0,    0,    0,
 -105, -148,  -19,  164,    4,   86,  171,    0,    0,    0,
  169,    0,  172,    0,  -16,    0,   96,    0,  142,   44,
  -27,    0,  210,    0,  -36,  373, -165,    0,   23,    0,
   -1,    0,    0,    0,  153,  153,    0,  153,  153,  115,
 -245,  115,  162,    0,   14,  -41,  162,  201,   18,  238,
 -177,  241,    0,    0, -137,   49, -101,   44,   36,    0,
  243,    0,  264,  142,  142,  142,  142,  142,  142,    0,
    0,    0,    0,   59,   59,    0,    0,    0,  253,  115,
   54,  263,   61,    0,    0,    0,    0,    0,   -7,    0,
   67,  203,    0,  286,    0,  287,  289,    0,  311, -101,
    0,    0,   25,  332,   75,  342,  124,  348,  148,  367,
  296,  374,  386,  380,  115,  364,  115,   21,  115,    0,
    0,    0,  135,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  368,  115,  372,  115,   83,  378,    0,    0,  115,
  379,  115,  382,  115,  -59,  115, -177,  383,  115,  387,
  115,  388,    0,   90,  389,    0,  115,  390,  115,  391,
  115,  142,  115,   24,  115,  410,  115,  411,  115,  412,
  395,  414,  115,  -33,  415,    0,  416,    0,  417,    0,
  142,    0,  418,    0,  -37,    0,    0,    0,  419,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  402,    0,    0,    0,    0,    0,    0,    0,
    0,  464,    0,    0,    0,    0,    0,    0,   15,    0,
    0,    0,    0,    0,  424,    0,    0,   -8,    0,    0,
    0,    0,    0,  199,    0,  111,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,   16,    0,   29,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   55,    0,    0,    0,   62,    0,
    0,    0,    0,  121,  116,  136,    0,   84,   91,    0,
    0,    0,    0,    0,    0,    0,  -52,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  427,  428,  429,  433,  436,  437,    0,
    0,    0,    0,  141,  276,    0,    0,    0,    0,    0,
    0,    0,  -45,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -119,    0,  -84,    0,  -73,    0,  -66,    0,
  108,    0,  137,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  354,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  112,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  455,  470,  426,   38,   45,    0,    0, -133,    0,
   12,  -12,    0,    0,    0,    0,    0,  421,    0,  -77,
    0,  446,    0,    0,    0,    0,    0,    0,    0,    0,
   72,  420,  231,  316,    0,    0,
};
final static int YYTABLESIZE=644;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          9,
   25,   26,  204,  242,   52,   10,   27,  234,   56,   69,
  120,   32,    9,   30,   31,   13,   47,  126,   10,  136,
    9,  121,   33,   32,   34,   45,   10,   95,   14,  189,
   87,   32,   10,  113,  113,   42,  113,   16,  113,   84,
   25,   76,   94,   75,   90,   70,   25,   87,   87,   16,
  113,  162,   48,   32,   10,   13,   54,  113,  129,   25,
   53,   13,  169,  206,   76,  170,   75,   76,   14,   75,
   59,   97,  100,   31,   14,    9,  128,   73,    1,  186,
   77,   10,  224,    9,   72,   87,   87,    6,    7,   10,
   60,   76,   61,   75,  138,  113,  113,  113,   32,  113,
   78,  113,   94,   94,   94,   79,   94,  135,   94,  137,
  139,   81,  157,  113,  113,  172,  113,   76,   82,   75,
   94,   94,   51,   94,   88,   88,   88,   32,   88,   74,
   88,   89,   89,   89,   32,   89,   67,   89,   67,  134,
   32,   34,   88,   88,  194,   88,  114,  115,  213,   89,
   89,   77,   89,   77,   98,   77,   79,   99,   79,   32,
   79,   80,   95,   95,  174,   95,   76,   95,   75,   77,
   77,   69,   77,   69,   79,   79,   78,   79,   78,   95,
   78,   76,   70,   76,   70,   76,   32,   10,  176,   68,
   76,   68,   75,  188,   78,   78,  203,   78,   10,   76,
   76,    9,   76,   27,   88,   51,   27,   10,   91,   55,
   30,   92,   33,   30,  125,    1,    2,   25,  241,    3,
   31,    4,    5,   68,    6,    7,  118,   29,   98,    2,
   93,   99,   57,    8,    4,    5,   20,    2,   28,   29,
   57,   31,    4,    5,   83,   30,   65,   29,  161,   44,
  102,   40,   83,   31,  112,   41,   25,   25,   93,  127,
   25,   31,   25,   25,   64,   25,   25,   89,   65,   29,
   31,   13,   13,   31,   25,   13,  185,   13,   13,  223,
   13,   13,  111,   31,   14,   14,  124,   31,   14,   13,
   14,   14,    2,   14,   14,   57,  130,    4,    5,  133,
    2,  141,   14,   57,  142,    4,    5,   83,  116,  117,
  113,  155,  113,  118,   29,   83,   75,   94,   75,   94,
   75,  159,  113,  113,  113,  113,  163,  164,   31,   94,
   94,   94,   94,  160,   75,   75,  178,   75,   76,   88,
   75,   88,  118,   29,  165,  166,   89,  167,   89,  118,
   29,   88,   88,   88,   88,   28,   29,   31,   89,   89,
   89,   89,   30,   65,   31,   65,   77,   21,   77,  168,
   31,   79,  171,   79,  118,   29,   21,   21,   77,   77,
   77,   77,  173,   79,   79,   79,   79,   92,  175,   31,
    1,   78,   66,   78,   66,  119,   76,  122,   76,    6,
    7,   65,   29,   78,   78,   78,   78,  177,   76,   76,
   76,   76,   65,   29,  179,   76,   31,   75,    2,   24,
  181,   57,  183,    4,    5,   15,  180,   31,   76,  190,
   75,   24,  109,  192,  108,  156,  158,   15,   24,  196,
  199,  207,  221,  201,   58,  209,  211,  215,  217,  219,
  226,  228,  230,  231,  232,  236,  237,  238,  240,  243,
   25,  239,   24,    1,   73,   94,   49,   57,   61,   63,
  182,   66,  184,   59,  187,   71,   53,   55,   23,   24,
   24,   50,    0,    0,    0,    0,    0,  110,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  191,    0,
  193,  195,   24,    0,   96,  198,   24,  200,  123,  202,
    0,  205,   58,    0,  208,    0,  210,   24,   24,  214,
    0,    0,  216,    0,  218,    0,  220,    0,  222,    0,
  225,   75,  227,   75,  229,    0,    0,    0,  233,  235,
    0,    0,    0,   75,   75,   75,   75,    0,    0,  143,
  145,  147,  149,  151,  153,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  104,  105,  106,  107,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   62,   41,   59,   46,   59,   41,   59,   46,
  256,   45,   40,   59,    0,    0,  264,   59,   46,   97,
   40,  267,   44,   45,   46,   40,   46,   44,    0,  163,
   43,   45,   46,   42,   43,   40,   45,    0,   47,   59,
   40,   43,   59,   45,   41,   59,   46,   60,   61,   12,
   59,   59,  260,   45,   46,   40,   59,   59,   41,   59,
   16,   46,  140,  197,   43,   41,   45,   43,   40,   45,
  267,   60,   61,   59,   46,   40,   59,   33,  256,   59,
   59,   46,   59,   40,  261,   98,   99,  265,  266,   46,
  256,   43,  258,   45,   59,   41,   42,   43,   45,   45,
   42,   47,   41,   42,   43,   47,   45,   59,   47,   98,
   99,  260,   59,   59,   60,   41,   62,   43,  267,   45,
   59,   60,  260,   62,   41,   42,   43,   45,   45,  260,
   47,   41,   42,   43,   45,   45,  256,   47,  258,   95,
   45,   46,   59,   60,   62,   62,   75,   76,   59,   59,
   60,   41,   62,   43,  256,   45,   41,  259,   43,   45,
   45,  267,   42,   43,   41,   45,   43,   47,   45,   59,
   60,  256,   62,  258,   59,   60,   41,   62,   43,   59,
   45,   41,  256,   43,  258,   45,   45,   46,   41,  256,
   43,  258,   45,   59,   59,   60,  256,   62,   46,   59,
   60,   40,   62,  256,   41,  260,  259,   46,  123,  260,
  256,   41,   44,  259,  256,  256,  257,  256,  256,  260,
  273,  262,  263,  260,  265,  266,  260,  261,  256,  257,
   59,  259,  260,  274,  262,  263,  125,  257,  260,  261,
  260,  275,  262,  263,  272,  267,  260,  261,  256,  264,
   41,  256,  272,  275,  256,  260,  256,  257,  267,   59,
  260,  275,  262,  263,  256,  265,  266,  264,  260,  261,
  256,  256,  257,  259,  274,  260,  256,  262,  263,  256,
  265,  266,  260,  275,  256,  257,  273,  273,  260,  274,
  262,  263,  257,  265,  266,  260,   59,  262,  263,   59,
  257,   59,  274,  260,   41,  262,  263,  272,   78,   79,
  256,   59,  258,  260,  261,  272,   41,  256,   43,  258,
   45,   59,  268,  269,  270,  271,  260,  125,  275,  268,
  269,  270,  271,  273,   59,   60,   41,   62,   43,  256,
   45,  258,  260,  261,   59,   59,  256,   59,  258,  260,
  261,  268,  269,  270,  271,  260,  261,  275,  268,  269,
  270,  271,  267,  256,  275,  258,  256,  256,  258,   59,
  275,  256,   41,  258,  260,  261,  265,  266,  268,  269,
  270,  271,   41,  268,  269,  270,  271,  267,   41,  275,
  256,  256,  256,  258,  258,   80,  256,   82,  258,  265,
  266,  260,  261,  268,  269,  270,  271,   41,  268,  269,
  270,  271,  260,  261,   41,   43,  275,   45,  257,    0,
   41,  260,   59,  262,  263,    0,   41,  275,   43,   62,
   45,   12,   60,   62,   62,  120,  121,   12,   19,   62,
   62,   59,  212,   62,   19,   59,   59,   59,   59,   59,
   41,   41,   41,   59,   41,   41,   41,   41,   41,   41,
   59,  231,   43,    0,   41,  267,   12,   41,   41,   41,
  155,   26,  157,   41,  159,   30,   41,   41,  125,   60,
   61,   12,   -1,   -1,   -1,   -1,   -1,   67,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  183,   -1,
  185,  186,   83,   -1,   59,  190,   87,  192,   83,  194,
   -1,  196,   87,   -1,  199,   -1,  201,   98,   99,  204,
   -1,   -1,  207,   -1,  209,   -1,  211,   -1,  213,   -1,
  215,  256,  217,  258,  219,   -1,   -1,   -1,  223,  224,
   -1,   -1,   -1,  268,  269,  270,  271,   -1,   -1,  104,
  105,  106,  107,  108,  109,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  268,  269,  270,  271,
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
"tipo : INTEGER",
"tipo : ULONGINT",
"tipo : error",
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
"seleccionThen : error bloque seleccionElse ';'",
"seleccionThen : THEN seleccionElse ';'",
"seleccionElse : ELSE bloque ';'",
"seleccionElse : error bloque ';'",
"seleccionElse : ELSE ';'",
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
"$$11 :",
"condicion : '(' error $$11 ')'",
"$$12 :",
"condicion : error $$12 ')'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : expresion '+'",
"expresion : expresion '-'",
"asignacion : ID ASIGN expresion ';'",
"asignacion : elemento_estructura ASIGN expresion ';'",
"asignacion : ID expresion ';'",
"asignacion : ID ASIGN ';'",
"asignacion : ID ASIGN expresion error",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : termino '*'",
"termino : termino '/'",
"factor : factor_for",
"factor : elemento_estructura",
"elemento_estructura : ID '.' ID",
"elemento_estructura : ID ID",
"elemento_estructura : ID '.'",
"elemento_estructura : '.' ID",
"bucle : FOR condicion_for bloque ';'",
"bucle : FOR condicion_for ';'",
"bucle : FOR condicion_for bloque error",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for '>' factor_for ';' factor_for ')'",
"condicion_for : ID ASIGN factor_for ';' factor_for '>' factor_for ';' factor_for ')'",
"condicion_for : '(' ASIGN factor_for ';' factor_for '>' factor_for ';' factor_for ')'",
"condicion_for : '(' ID ASIGN ';' factor_for '>' factor_for ';' factor_for ')'",
"condicion_for : '(' ID ASIGN factor_for ';' '>' factor_for ';' factor_for ')'",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for '>' ';' factor_for ')'",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for '>' factor_for ';' ')'",
"condicion_for : '(' ID error factor_for ';' factor_for '>' factor_for ';' factor_for ')'",
"condicion_for : '(' ID ASIGN factor_for error factor_for '>' factor_for ';' factor_for ')'",
"$$13 :",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for error $$13 factor ';' factor ')'",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for '>' factor_for error factor_for ')'",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for '>' factor_for ';' factor_for error",
"condicion_for : error",
"factor_for : ID",
"factor_for : CTE_ULON",
"factor_for : CTE_INT",
"factor_for : '-' CTE_INT",
"impresion : PRINT '(' CADENA ')' ';'",
"impresion : '(' CADENA ')' ';'",
"impresion : PRINT '(' ')' ';'",
"impresion : PRINT CADENA ')' ';'",
"impresion : PRINT '(' CADENA ';'",
"impresion : PRINT '(' CADENA ')' error",
};

//#line 255 "gramatica.txt"

private AnalizadorLexico anaLex;
private Vector<String> errores;
private Vector<String> tokens;
private Vector<String> salida;
private Vector<String> elemento_estructuras;
private String error_anterior;
private int linea_error_anterior;

// para manejo de los tipos de datos
private Tipo tipo_dato;
private Vector<Entrada> identificadores = new Vector<Entrada>();

// para manejo de errores semanticos
private Vector<String> erroresSemanticos = new Vector<String>();

//para manejo de polaca inversa
private Vector<ElementoPolaca> polacaInversa = new Vector<ElementoPolaca>();
Stack<Integer> pila=new Stack();
Stack<Integer> retornos=new Stack();

public static final int MAX_INTEGER_NEG = 32767;
public static final int MAX_INTEGER = 32768;
public static final float MAX_ULONGINTEGER = 4294967295f;

public Parser(AnalizadorLexico analLex){
  anaLex=analLex;
  error_anterior = "";
  errores = new Vector<String>();
  tokens = new Vector<String>();
  salida = new Vector<String>();
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
          errores.add("En linea: " + anaLex.getNumeroLinea() + " se encontro el error: " + texto);
     else{
         if(error_anterior.equals(texto))
              errores.add("En linea: " + linea_error_anterior + " ocurrio un error sintactico");
     }
     linea_error_anterior = anaLex.getNumeroLinea();
     error_anterior = texto;
}

public Vector<String> getErrores(){
	return this.errores;
}

public Vector<String> getTokens(){
        return this.tokens;
}

public Vector<String> getSalida(){
   Vector<String> retorno = new Vector<String>();
   retorno.add("Analizador léxico: ");
   retorno.addAll(this.tokens);
   retorno.add("Analizador sintáctico: ");
   retorno.addAll(this.salida);
   return retorno;
}

private void fueraRangoIntPositivo(Entrada e){
    int val = (Integer) (e.getValor());
    if ((0 <= val) && (val > MAX_INTEGER-1))
       yyerror("entero fuera de rango [-32768;32767]");
}

private void setearTipoElementosEstructuras(Entrada e) {
    elemento_estructuras.add(e.getNombre());
    e = anaLex.getTablaSimbolos().get(e.getNombre());
    e.setTipo_dato(this.tipo_dato);
    this.tipo_dato = null;
}

private void estaDeclarada(Entrada e) {
    if (e.getTipo_dato()==null) {
        this.erroresSemanticos.add("La variable " + e.getNombre() + " no está declarada.");
    }
}

public Vector<String> getErroresSemanticos() {
    return this.erroresSemanticos;
}

public Vector<ElementoPolaca> getPolacaInversa(){
    return this.polacaInversa;
}



//metodos para controlar la polaca
void agregarAPolacaId(Entrada e){
    Tipo t = anaLex.getTablaSimbolos().get(e.getNombre()).getTipo_dato();
    System.out.println("tipo de la entrada: " + t);
    if(t == Tipo.INTEGER)
       polacaInversa.add(new ElementoPolaca(ElementoPolaca.VAR_INT,e.getNombre()));
    else
       polacaInversa.add(new ElementoPolaca(ElementoPolaca.VAR_ULONG,e.getNombre()));
}

void agregarAPolacaConstante(Entrada e){
    if(e.getTipo() == Tipo.CONSTANTE_INTEGER)
    polacaInversa.add(new ElementoPolaca(ElementoPolaca.CONS_INT,e.getValor().toString()));
    else
      if(e.getTipo() == Tipo.CONSTANTE_ULONGINT)
        polacaInversa.add(new ElementoPolaca(ElementoPolaca.CONS_ULONG,e.getValor().toString()));
      else
        polacaInversa.add(new ElementoPolaca(ElementoPolaca.CADENA,e.getValor().toString()));
}

void agregarAPolaca(int t){
		  polacaInversa.add(new ElementoPolaca(t));
}

void apilar(int s) {
        pila.push(polacaInversa.size());
        polacaInversa.add(new ElementoPolaca(s));
}

void desapilar(int desplazamiento) {
        int pos = pila.pop().intValue();
        int direccion = polacaInversa.size()+desplazamiento;
        polacaInversa.get(pos).nombre = "LABEL_"+Integer.toString(direccion);
}

void agregarRotulo(){
    polacaInversa.add(new ElementoPolaca(ElementoPolaca.ROTULO,"LABEL_"+Integer.toString(polacaInversa.size())));
}

void apilarDireccion() {
        retornos.push(polacaInversa.size());
}

void desapilarDireccion() {
        int pos=pila.pop().intValue();
        int direccion=retornos.pop();
        polacaInversa.get(pos).nombre="LABEL_"+Integer.toString(direccion);
}
//#line 683 "Parser.java"
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
                                  }}
break;
case 9:
//#line 62 "gramatica.txt"
{ yyerror("falta nombre de variable en declaracion.");}
break;
case 10:
//#line 63 "gramatica.txt"
{yyerror("falta nombre de la estructura en declaraciï¿½n.");}
break;
case 11:
//#line 64 "gramatica.txt"
{yyerror("declaracion multiple en estructuras esta prohibida.");}
break;
case 12:
//#line 65 "gramatica.txt"
{ yyerror("declaracion de variable sin tipo definido.");}
break;
case 13:
//#line 66 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 14:
//#line 67 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 15:
//#line 70 "gramatica.txt"
{this.tipo_dato=Tipo.INTEGER;}
break;
case 16:
//#line 71 "gramatica.txt"
{this.tipo_dato=Tipo.ULONGINT;}
break;
case 17:
//#line 72 "gramatica.txt"
{yyerror("tipo primitivo no admitido por el lenguaje.");}
break;
case 18:
//#line 75 "gramatica.txt"
{yyout("Sentencia de declaracion de estructura"); elemento_estructuras = new Vector<String>();}
break;
case 20:
//#line 78 "gramatica.txt"
{
                                      setearTipoElementosEstructuras((Entrada)val_peek(1).obj);
                                }
break;
case 21:
//#line 81 "gramatica.txt"
{
                                      setearTipoElementosEstructuras((Entrada)val_peek(1).obj);
                                }
break;
case 23:
//#line 85 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 24:
//#line 86 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 25:
//#line 89 "gramatica.txt"
{this.identificadores.add(anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()));}
break;
case 26:
//#line 90 "gramatica.txt"
{this.identificadores.add(anaLex.getTablaSimbolos().get(((Entrada)val_peek(2).obj).getNombre()));}
break;
case 27:
//#line 93 "gramatica.txt"
{yyout("Una sola sentencia.");}
break;
case 28:
//#line 94 "gramatica.txt"
{yyout("Bloque de sentencias.");}
break;
case 29:
//#line 95 "gramatica.txt"
{ yyerror("se esperaba un 'begin' al inicio de un bloque de sentencias ejecutables.");}
break;
case 30:
//#line 96 "gramatica.txt"
{ yyerror("se esperaba un 'end' al final de un bloque de sentencias ejecutables.");}
break;
case 37:
//#line 110 "gramatica.txt"
{apilar(ElementoPolaca.BF);}
break;
case 38:
//#line 110 "gramatica.txt"
{yyout("Condicional.");}
break;
case 39:
//#line 112 "gramatica.txt"
{ yyerror("la condicion de seleccion no es valida.");}
break;
case 40:
//#line 115 "gramatica.txt"
{desapilar(1); apilar(ElementoPolaca.BI);agregarRotulo();}
break;
case 42:
//#line 117 "gramatica.txt"
{yyerror("se esperaba un 'then'.");}
break;
case 43:
//#line 118 "gramatica.txt"
{yyerror("se esperaba un bloque luego de la palabra reservada 'then'.");}
break;
case 44:
//#line 121 "gramatica.txt"
{desapilar(0); agregarRotulo();}
break;
case 45:
//#line 123 "gramatica.txt"
{yyerror("se esperaba un 'else'.");}
break;
case 46:
//#line 124 "gramatica.txt"
{yyerror("se esperaba un bloque luego de la palabra reservada 'else'.");}
break;
case 47:
//#line 127 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYOR);}
break;
case 48:
//#line 128 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENOR);}
break;
case 49:
//#line 129 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.IGUAL);}
break;
case 50:
//#line 130 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.DISTINTO);}
break;
case 51:
//#line 131 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENORIGUAL);}
break;
case 52:
//#line 132 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYORIGUAL);}
break;
case 53:
//#line 134 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 55:
//#line 135 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 57:
//#line 136 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 59:
//#line 137 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 61:
//#line 138 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 63:
//#line 139 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 65:
//#line 141 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 66:
//#line 142 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 67:
//#line 143 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 68:
//#line 144 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 69:
//#line 145 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 70:
//#line 146 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 71:
//#line 148 "gramatica.txt"
{yyerror("Falta la expresion izquierda de la condicion.");}
break;
case 73:
//#line 149 "gramatica.txt"
{yyerror("Falta '(' al comienzo de la condicion.");}
break;
case 75:
//#line 153 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.SUMA);yyout("Sentencia de suma.");}
break;
case 76:
//#line 154 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.RESTA);yyout("Sentencia de resta.");}
break;
case 78:
//#line 157 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '+'.");}
break;
case 79:
//#line 158 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '-'.");}
break;
case 80:
//#line 162 "gramatica.txt"
{
                                        Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(3).obj).getNombre());
                                        estaDeclarada(e);
                                        agregarAPolacaId((Entrada)val_peek(3).obj); 
                                        agregarAPolaca(ElementoPolaca.ASIGNACION);
                                    }
break;
case 81:
//#line 168 "gramatica.txt"
{yyout("Asignacion a elemento de estructura.");}
break;
case 82:
//#line 170 "gramatica.txt"
{ yyerror("se esperaba el operador de asignacion ':='.");}
break;
case 83:
//#line 171 "gramatica.txt"
{ yyerror("se esperaba una expresion del lado derecho de la asignacion.");}
break;
case 84:
//#line 172 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 85:
//#line 175 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MULTIPLICACION);yyout("Sentencia de producto.");}
break;
case 86:
//#line 176 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.DIVISION);yyout("Sentencia de division.");}
break;
case 88:
//#line 179 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '*'.");}
break;
case 89:
//#line 180 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '/'.");}
break;
case 92:
//#line 187 "gramatica.txt"
{yyout("Elemento de estructura.");}
break;
case 93:
//#line 189 "gramatica.txt"
{ yyerror("se esperaba un punto.");}
break;
case 94:
//#line 190 "gramatica.txt"
{ yyerror("se esperaba identificador de la variable mientro de la estructura.");}
break;
case 95:
//#line 191 "gramatica.txt"
{ yyerror("se esperaba identificador de la estructura antes del punto.");}
break;
case 96:
//#line 194 "gramatica.txt"
{yyout("Bucle for.");}
break;
case 97:
//#line 196 "gramatica.txt"
{yyerror("se esperaba un bloque de sentencias, pero se encontro ';'.");}
break;
case 98:
//#line 197 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 99:
//#line 200 "gramatica.txt"
{
                                                                                            Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(9).obj).getNombre());
                                                                                            estaDeclarada(e);
                                                                                         }
break;
case 100:
//#line 205 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condiciï¿½n.");}
break;
case 101:
//#line 206 "gramatica.txt"
{ yyerror("se esperaba un identificador antes del operador de asignaciï¿½n.");}
break;
case 102:
//#line 207 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues del operador de asignaciï¿½n.");}
break;
case 103:
//#line 208 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante antes de '>'.");}
break;
case 104:
//#line 209 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues '>'.");}
break;
case 105:
//#line 210 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues de ';'.");}
break;
case 106:
//#line 212 "gramatica.txt"
{ yyerror("se esperaba operador de asignaciï¿½n.");}
break;
case 107:
//#line 213 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 108:
//#line 214 "gramatica.txt"
{ yyerror("se esperaba '>'.");}
break;
case 110:
//#line 215 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 111:
//#line 216 "gramatica.txt"
{ yyerror("se esperaba ')' despues de la condiciï¿½n.");}
break;
case 112:
//#line 217 "gramatica.txt"
{yyerror("error en la sentencia for.");}
break;
case 113:
//#line 221 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);
                 agregarAPolacaId((Entrada)val_peek(0).obj);}
break;
case 114:
//#line 224 "gramatica.txt"
{agregarAPolacaConstante((Entrada)val_peek(0).obj);}
break;
case 115:
//#line 225 "gramatica.txt"
{ anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()).visitar();
                             fueraRangoIntPositivo((Entrada)val_peek(0).obj);
                             agregarAPolacaConstante((Entrada)val_peek(0).obj); }
break;
case 116:
//#line 228 "gramatica.txt"
{Entrada actual = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
    				if (!(actual).isVisitado()) {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(), (Integer) actual.getValor() * -1);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
        			anaLex.getTablaSimbolos().removeEntrada(((Entrada)(val_peek(0).obj)).getNombre());
    				} else {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(),(Integer)actual.getValor() * -1);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
 				}
 				agregarAPolacaConstante((Entrada)val_peek(1).obj);
                                 /*tal vez convenga pasar directamente e, que es la nueva entrada con el signo negativo puesto*/
			    }
break;
case 117:
//#line 242 "gramatica.txt"
{yyout("Sentencia de impresion.");
                                      agregarAPolacaConstante((Entrada)val_peek(2).obj);
                                      agregarAPolaca(ElementoPolaca.PRINT);}
break;
case 118:
//#line 246 "gramatica.txt"
{ yyerror("se esperaba 'print'");}
break;
case 119:
//#line 247 "gramatica.txt"
{ yyerror("se esperaba una cadena dentro de la instruccion print.");}
break;
case 120:
//#line 248 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la cadena en sentencia print.");}
break;
case 121:
//#line 249 "gramatica.txt"
{ yyerror("sentencia print sin parentesis de cierre.");}
break;
case 122:
//#line 251 "gramatica.txt"
{ yyerror("se esperaba ';.'");}
break;
//#line 1284 "Parser.java"
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

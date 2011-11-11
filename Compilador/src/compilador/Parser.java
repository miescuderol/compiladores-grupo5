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
   18,   18,   18,   20,   20,   20,   20,   17,   17,   17,
   17,   17,   17,   23,   17,   24,   17,   25,   17,   26,
   17,   27,   17,   28,   17,   17,   17,   17,   17,   17,
   17,   29,   17,   30,   17,   22,   22,   22,   22,   22,
   16,   16,   16,   16,   16,   31,   31,   31,   31,   31,
   33,   33,   32,   32,   32,   32,   14,   14,   14,   35,
   35,   35,   35,   35,   35,   35,   35,   35,   36,   35,
   35,   35,   35,   34,   34,   34,   34,   15,   15,   15,
   15,   15,   15,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    3,    3,    2,    2,
    5,    2,    2,    2,    1,    1,    1,    0,    5,    3,
    0,    5,    2,    3,    1,    3,    1,    3,    2,    2,
    1,    2,    1,    1,    1,    1,    0,    4,    3,    0,
    4,    4,    3,    3,    3,    2,    2,    5,    5,    5,
    5,    5,    5,    0,    5,    0,    5,    0,    5,    0,
    5,    0,    5,    0,    5,    4,    4,    4,    4,    4,
    4,    0,    4,    0,    3,    3,    3,    1,    2,    2,
    4,    4,    3,    3,    4,    3,    3,    1,    2,    2,
    1,    1,    3,    2,    2,    2,    4,    3,    4,   11,
   10,   10,   10,   10,   10,   10,   11,   11,    0,   12,
   11,   11,    1,    1,    1,    1,    2,    5,    4,    4,
    4,    4,    5,
};
final static short yydefred[] = {                         0,
   17,    0,    0,    0,    0,   16,   15,   18,    0,    0,
    0,    0,    3,    4,    6,    0,    0,    0,    0,   33,
   34,   35,   36,    0,    0,    0,   37,    0,  116,    0,
  115,    0,    0,    0,    0,    0,   92,   88,   91,  113,
    0,    0,    0,    0,    0,    0,    0,   96,    2,    5,
    0,    9,    0,   12,    0,   10,    0,   32,    0,    0,
    0,   39,    0,   72,    0,    0,    0,   94,    0,   84,
    0,  117,   26,    0,    0,    0,   83,    0,    0,    0,
    0,    0,    0,   98,    0,    0,    0,    0,    0,    0,
    0,    0,    7,    8,    0,    0,    0,    0,    0,   40,
    0,   75,    0,    0,    0,    0,    0,    0,    0,   38,
   93,   85,   81,    0,    0,   86,   87,  114,    0,    0,
    0,    0,    0,   29,   99,   97,  121,  122,    0,  120,
    0,    0,  119,    0,   82,    0,   47,    0,   46,    0,
    0,   43,   73,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   28,  123,  118,    0,   19,   11,   42,   45,   44,   41,
   50,   59,   52,   63,   53,   65,   51,   61,   48,   55,
   49,   57,    0,    0,    0,    0,    0,    0,    0,   24,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  109,    0,    0,   22,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  101,    0,  103,    0,
  104,    0,  105,    0,  106,    0,  102,  107,  108,    0,
  111,  112,  100,  110,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   85,  131,   17,   18,   46,  132,  198,
   86,   19,   20,   21,   22,   23,   27,   62,   67,  101,
  141,   35,  153,  155,  145,  151,  147,  149,  103,   63,
   36,   37,   38,   39,   43,  213,
};
final static short yysindex[] = {                       -40,
    0,  -37,  -21,   -4,  -14,    0,    0,    0, -242, -232,
    0,  -40,    0,    0,    0,  -29,  -15,   -9,  172,    0,
    0,    0,    0, -208, -157,    9,    0,  -39,    0,  -13,
    0, -174, -164, -158,   20,   26,    0,    0,    0,    0,
 -151, -190,  -19,   79,    4,    5,   83,    0,    0,    0,
   87,    0,   89,    0,    8,    0,  104,    0,  150,   52,
  -27,    0,   97,    0,  -39,  381, -157,    0,  -97,    0,
  -41,    0,    0,    0,  161,  161,    0,  161,  161,  123,
 -214,  123,  172,    0, -107,  -42,  172,  117,    2,  134,
 -147,  146,    0,    0, -164,  132, -165,   36,   44,    0,
  151,    0,  196,  150,  150,  150,  150,  150,  150,    0,
    0,    0,    0,   26,   26,    0,    0,    0,  154,  123,
   62,  201,    7,    0,    0,    0,    0,    0,   -1,    0,
   17,  162,    0,  243,    0,  251,    0,  254,    0,  258,
 -165,    0,    0,  129,  277,  304,  279,  331,  289,  399,
  295,  410,  301,  415,  312,  123,  297,  123,   27,  123,
    0,    0,    0,  143,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  316,  123,  319,  123,   91,  323,    0,    0,
  123,  324,  123,  329,  123,  -57,  123, -147,  337,  123,
  344,  123,  347,    0,   98,  368,    0,  123,  369,  123,
  386,  123,  150,  123,   55,  123,  407,  123,  421,  123,
  425,  408,  427,  123,  -33,  428,    0,  429,    0,  430,
    0,  150,    0,  431,    0,  -32,    0,    0,    0,  432,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  422,    0,    0,    0,    0,    0,    0,    0,
    0,  474,    0,    0,    0,    0,    0,    0,   15,    0,
    0,    0,    0,    0,  441,    0,    0,   -8,    0,    0,
    0,    0,    0,  216,    0,  119,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,   16,    0,   29,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   63,    0,    0,    0,   70,    0,
    0,    0,    0,   38,  124,  144,    0,   92,   99,    0,
    0,    0,    0,    0,    0,    0,  190,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  443,  444,  445,  447,  448,  449,    0,
    0,    0,    0,  149,  284,    0,    0,    0,    0,    0,
    0,    0,   41,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -111,    0,  -85,    0,  -76,    0,  175,
    0,  181,    0,  203,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  370,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  482,  484,  438,   66,   -2,    0,    0, -126,    0,
  -50,   28,    0,    0,    0,    0,    0,  433,    0,  -77,
    0,  371,    0,    0,    0,    0,    0,    0,    0,    0,
   80,  404,  122,  372,    0,    0,
};
final static int YYTABLESIZE=652;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          9,
   25,   76,   26,   75,  205,   10,   69,  235,  243,   97,
  100,   32,    9,   53,   31,   13,  126,  113,   10,  136,
    9,   47,   33,   32,   34,   45,   10,   48,   14,   52,
   73,   32,   10,  114,  114,   42,  114,  190,  114,   84,
   25,  120,  129,   54,   90,   70,   25,  138,  140,   56,
  114,   95,  121,   32,   10,   13,   20,  163,   59,   25,
  128,   13,   76,  170,   75,   16,   94,   78,   14,   81,
   87,  207,   79,   31,   14,    9,   82,   16,   77,   96,
   96,   10,   96,    9,   96,  187,   72,   87,   87,   10,
   98,    9,  134,   99,  137,   51,   96,   10,   60,   30,
   61,   74,  139,  114,  114,  114,   32,  114,    1,  114,
   95,   95,   95,  225,   95,   80,   95,    6,    7,   88,
  158,  114,  114,   92,  114,   87,   87,   91,   95,   95,
   33,   95,   89,   89,   89,   32,   89,  102,   89,   90,
   90,   90,   32,   90,   68,   90,   68,   93,   32,   34,
   89,   89,  195,   89,  114,  115,  214,   90,   90,   78,
   90,   78,  111,   78,   80,  124,   80,   32,   80,  171,
   70,   76,   70,   75,   76,  127,   75,   78,   78,   71,
   78,   71,   80,   80,   79,   80,   79,   21,   79,   77,
  135,   77,  130,   77,   32,   10,   21,   21,  204,  116,
  117,  189,   79,   79,  133,   79,   10,   77,   77,  142,
   77,    9,  156,  125,  112,    1,    2,   10,   25,    3,
   68,    4,    5,  242,    6,    7,  118,   29,   98,    2,
   51,   99,   57,    8,    4,    5,  143,    2,   28,   29,
   57,   31,    4,    5,   83,   30,   65,   29,   27,   44,
   55,   40,   83,   31,  162,   41,   25,   25,   94,  160,
   25,   31,   25,   25,   64,   25,   25,   89,   65,   29,
   31,   13,   13,   31,   25,   13,  164,   13,   13,  161,
   13,   13,  186,   31,   14,   14,  165,   31,   14,   13,
   14,   14,    2,   14,   14,   57,   30,    4,    5,   30,
    2,  166,   14,   57,   93,    4,    5,   83,    2,  167,
  224,   57,  168,    4,    5,   83,  169,  172,  114,  174,
  114,  118,   29,   83,   76,   95,   76,   95,   76,  176,
  114,  114,  114,  114,  222,  178,   31,   95,   95,   95,
   95,  180,   76,   76,  173,   76,   76,   89,   75,   89,
  118,   29,  182,  240,   90,  184,   90,  118,   29,   89,
   89,   89,   89,   28,   29,   31,   90,   90,   90,   90,
   30,  175,   31,   76,   78,   75,   78,  191,   31,   80,
  193,   80,  118,   29,  197,  200,   78,   78,   78,   78,
  202,   80,   80,   80,   80,  208,   66,   31,    1,   79,
   71,   79,  210,   24,   77,  212,   77,    6,    7,   65,
   29,   79,   79,   79,   79,   24,   77,   77,   77,   77,
   65,   29,   24,   76,   31,   75,  216,  218,    2,   96,
   69,   57,   69,    4,    5,   31,   66,   15,   66,  177,
  109,   76,  108,   75,  220,   27,   24,  227,   27,   15,
  179,  119,   76,  122,   75,  181,   58,   76,   67,   75,
   67,  229,   31,   24,   24,  231,  232,  233,  237,  238,
  239,  241,  244,    1,  144,  146,  148,  150,  152,  154,
   25,   74,   95,   58,   62,   64,   24,   60,   54,   56,
   24,  157,  159,   49,   23,   50,    0,    0,    0,  110,
    0,   24,   24,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  123,    0,    0,    0,   58,    0,    0,  183,    0,  185,
    0,  188,    0,    0,    0,    0,    0,    0,    0,   76,
    0,   76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   76,   76,   76,   76,  192,    0,  194,  196,    0,
    0,    0,  199,    0,  201,    0,  203,    0,  206,    0,
    0,  209,    0,  211,    0,    0,  215,    0,    0,  217,
    0,  219,    0,  221,    0,  223,    0,  226,    0,  228,
    0,  230,    0,    0,    0,  234,  236,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  104,  105,
  106,  107,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   43,   40,   45,   62,   46,   46,   41,   41,   60,
   61,   45,   40,   16,    0,    0,   59,   59,   46,   97,
   40,  264,   44,   45,   46,   40,   46,  260,    0,   59,
   33,   45,   46,   42,   43,   40,   45,  164,   47,   59,
   40,  256,   41,   59,   41,   59,   46,   98,   99,   59,
   59,   44,  267,   45,   46,   40,  125,   59,  267,   59,
   59,   46,   43,  141,   45,    0,   59,   42,   40,  260,
   43,  198,   47,   59,   46,   40,  267,   12,   59,   42,
   43,   46,   45,   40,   47,   59,  261,   60,   61,   46,
  256,   40,   95,  259,   59,  260,   59,   46,  256,   59,
  258,  260,   59,   41,   42,   43,   45,   45,  256,   47,
   41,   42,   43,   59,   45,  267,   47,  265,  266,   41,
   59,   59,   60,   41,   62,   98,   99,  123,   59,   60,
   44,   62,   41,   42,   43,   45,   45,   41,   47,   41,
   42,   43,   45,   45,  256,   47,  258,   59,   45,   46,
   59,   60,   62,   62,   75,   76,   59,   59,   60,   41,
   62,   43,  260,   45,   41,  273,   43,   45,   45,   41,
  256,   43,  258,   45,   43,   59,   45,   59,   60,  256,
   62,  258,   59,   60,   41,   62,   43,  256,   45,   41,
   59,   43,   59,   45,   45,   46,  265,  266,  256,   78,
   79,   59,   59,   60,   59,   62,   46,   59,   60,   59,
   62,   40,   59,  256,  256,  256,  257,   46,  256,  260,
  260,  262,  263,  256,  265,  266,  260,  261,  256,  257,
  260,  259,  260,  274,  262,  263,   41,  257,  260,  261,
  260,  275,  262,  263,  272,  267,  260,  261,   59,  264,
  260,  256,  272,  275,  256,  260,  256,  257,  267,   59,
  260,  275,  262,  263,  256,  265,  266,  264,  260,  261,
  256,  256,  257,  259,  274,  260,  260,  262,  263,  273,
  265,  266,  256,  275,  256,  257,  125,  273,  260,  274,
  262,  263,  257,  265,  266,  260,  256,  262,  263,  259,
  257,   59,  274,  260,  267,  262,  263,  272,  257,   59,
  256,  260,   59,  262,  263,  272,   59,   41,  256,   41,
  258,  260,  261,  272,   41,  256,   43,  258,   45,   41,
  268,  269,  270,  271,  213,   41,  275,  268,  269,  270,
  271,   41,   59,   60,   41,   62,   43,  256,   45,  258,
  260,  261,   41,  232,  256,   59,  258,  260,  261,  268,
  269,  270,  271,  260,  261,  275,  268,  269,  270,  271,
  267,   41,  275,   43,  256,   45,  258,   62,  275,  256,
   62,  258,  260,  261,   62,   62,  268,  269,  270,  271,
   62,  268,  269,  270,  271,   59,   26,  275,  256,  256,
   30,  258,   59,    0,  256,   59,  258,  265,  266,  260,
  261,  268,  269,  270,  271,   12,  268,  269,  270,  271,
  260,  261,   19,   43,  275,   45,   59,   59,  257,   59,
  256,  260,  258,  262,  263,  275,  256,    0,  258,   41,
   60,   43,   62,   45,   59,  256,   43,   41,  259,   12,
   41,   80,   43,   82,   45,   41,   19,   43,  256,   45,
  258,   41,  273,   60,   61,   41,   59,   41,   41,   41,
   41,   41,   41,    0,  104,  105,  106,  107,  108,  109,
   59,   41,  267,   41,   41,   41,   83,   41,   41,   41,
   87,  120,  121,   12,  125,   12,   -1,   -1,   -1,   67,
   -1,   98,   99,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   83,   -1,   -1,   -1,   87,   -1,   -1,  156,   -1,  158,
   -1,  160,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
   -1,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  268,  269,  270,  271,  184,   -1,  186,  187,   -1,
   -1,   -1,  191,   -1,  193,   -1,  195,   -1,  197,   -1,
   -1,  200,   -1,  202,   -1,   -1,  205,   -1,   -1,  208,
   -1,  210,   -1,  212,   -1,  214,   -1,  216,   -1,  218,
   -1,  220,   -1,   -1,   -1,  224,  225,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  268,  269,
  270,  271,
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
"seleccionElse : error ';'",
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

//#line 258 "gramatica.txt"

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

public boolean isCompilable(){
       return (erroresSemanticos.size() == 0);
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
//#line 688 "Parser.java"
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
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
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
//#line 125 "gramatica.txt"
{yyerror("falta rama del else");}
break;
case 48:
//#line 128 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYOR);}
break;
case 49:
//#line 129 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENOR);}
break;
case 50:
//#line 130 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.IGUAL);}
break;
case 51:
//#line 131 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.DISTINTO);}
break;
case 52:
//#line 132 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENORIGUAL);}
break;
case 53:
//#line 133 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYORIGUAL);}
break;
case 54:
//#line 135 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 56:
//#line 136 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 58:
//#line 137 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 60:
//#line 138 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 62:
//#line 139 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 64:
//#line 140 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
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
//#line 147 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 72:
//#line 149 "gramatica.txt"
{yyerror("Falta la expresion izquierda de la condicion.");}
break;
case 74:
//#line 150 "gramatica.txt"
{yyerror("Falta '(' al comienzo de la condicion.");}
break;
case 76:
//#line 154 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.SUMA);yyout("Sentencia de suma.");}
break;
case 77:
//#line 155 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.RESTA);yyout("Sentencia de resta.");}
break;
case 79:
//#line 158 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '+'.");}
break;
case 80:
//#line 159 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '-'.");}
break;
case 81:
//#line 163 "gramatica.txt"
{
                                        Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(3).obj).getNombre());
                                        estaDeclarada(e);
                                        agregarAPolacaId((Entrada)val_peek(3).obj);
                                        agregarAPolaca(ElementoPolaca.ASIGNACION);
                                    }
break;
case 82:
//#line 169 "gramatica.txt"
{yyout("Asignacion a elemento de estructura.");
                                                         agregarAPolaca(ElementoPolaca.ASIGNACION);}
break;
case 83:
//#line 172 "gramatica.txt"
{ yyerror("se esperaba el operador de asignacion ':='.");}
break;
case 84:
//#line 173 "gramatica.txt"
{ yyerror("se esperaba una expresion del lado derecho de la asignacion.");}
break;
case 85:
//#line 174 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 86:
//#line 177 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MULTIPLICACION);yyout("Sentencia de producto.");}
break;
case 87:
//#line 178 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.DIVISION);yyout("Sentencia de division.");}
break;
case 89:
//#line 181 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '*'.");}
break;
case 90:
//#line 182 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '/'.");}
break;
case 93:
//#line 189 "gramatica.txt"
{yyout("Elemento de estructura.");
                                  agregarAPolacaId((Entrada)val_peek(0).obj);}
break;
case 94:
//#line 192 "gramatica.txt"
{ yyerror("se esperaba un punto.");}
break;
case 95:
//#line 193 "gramatica.txt"
{ yyerror("se esperaba identificador de la variable mientro de la estructura.");}
break;
case 96:
//#line 194 "gramatica.txt"
{ yyerror("se esperaba identificador de la estructura antes del punto.");}
break;
case 97:
//#line 197 "gramatica.txt"
{yyout("Bucle for.");}
break;
case 98:
//#line 199 "gramatica.txt"
{yyerror("se esperaba un bloque de sentencias, pero se encontro ';'.");}
break;
case 99:
//#line 200 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 100:
//#line 203 "gramatica.txt"
{
                                                                                            Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(9).obj).getNombre());
                                                                                            estaDeclarada(e);
                                                                                         }
break;
case 101:
//#line 208 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condicion.");}
break;
case 102:
//#line 209 "gramatica.txt"
{ yyerror("se esperaba un identificador antes del operador de asignacion.");}
break;
case 103:
//#line 210 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues del operador de asignacion.");}
break;
case 104:
//#line 211 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante antes de '>'.");}
break;
case 105:
//#line 212 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues '>'.");}
break;
case 106:
//#line 213 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues de ';'.");}
break;
case 107:
//#line 215 "gramatica.txt"
{ yyerror("se esperaba operador de asignaciï¿½n.");}
break;
case 108:
//#line 216 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 109:
//#line 217 "gramatica.txt"
{ yyerror("se esperaba '>'.");}
break;
case 111:
//#line 218 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 112:
//#line 219 "gramatica.txt"
{ yyerror("se esperaba ')' despues de la condiciï¿½n.");}
break;
case 113:
//#line 220 "gramatica.txt"
{yyerror("error en la sentencia for.");}
break;
case 114:
//#line 224 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);
                 agregarAPolacaId((Entrada)val_peek(0).obj);}
break;
case 115:
//#line 227 "gramatica.txt"
{agregarAPolacaConstante((Entrada)val_peek(0).obj);}
break;
case 116:
//#line 228 "gramatica.txt"
{ anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()).visitar();
                             fueraRangoIntPositivo((Entrada)val_peek(0).obj);
                             agregarAPolacaConstante((Entrada)val_peek(0).obj); }
break;
case 117:
//#line 231 "gramatica.txt"
{Entrada actual = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
    				if (!(actual).isVisitado()) {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(), (Integer) actual.getValor() * -1);
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
case 118:
//#line 245 "gramatica.txt"
{yyout("Sentencia de impresion.");
                                      agregarAPolacaConstante((Entrada)val_peek(2).obj);
                                      agregarAPolaca(ElementoPolaca.PRINT);}
break;
case 119:
//#line 249 "gramatica.txt"
{ yyerror("se esperaba 'print'");}
break;
case 120:
//#line 250 "gramatica.txt"
{ yyerror("se esperaba una cadena dentro de la instruccion print.");}
break;
case 121:
//#line 251 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la cadena en sentencia print.");}
break;
case 122:
//#line 252 "gramatica.txt"
{ yyerror("sentencia print sin parentesis de cierre.");}
break;
case 123:
//#line 254 "gramatica.txt"
{ yyerror("se esperaba ';.'");}
break;
//#line 1295 "Parser.java"
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

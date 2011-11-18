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
   18,   18,   18,   20,   20,   20,   20,   17,   17,   17,
   17,   17,   17,   23,   17,   24,   17,   25,   17,   26,
   17,   27,   17,   28,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   29,   17,   30,   17,   16,   16,   16,
   16,   16,   22,   22,   22,   22,   22,   32,   32,   32,
   32,   32,   33,   33,   34,   34,   34,   34,   31,   31,
   31,   31,   14,   14,   14,   38,   35,   35,   35,   35,
   35,   35,   35,   40,   37,   37,   37,   41,   37,   37,
   42,   39,   39,   36,   36,   36,   36,   36,   15,   15,
   15,   15,   15,   15,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    3,    3,    2,    2,
    5,    2,    2,    2,    3,    1,    1,    0,    5,    3,
    0,    5,    2,    3,    1,    3,    1,    3,    2,    2,
    1,    2,    1,    1,    1,    1,    0,    4,    3,    0,
    4,    4,    3,    3,    3,    2,    2,    5,    5,    5,
    5,    5,    5,    0,    5,    0,    5,    0,    5,    0,
    5,    0,    5,    0,    5,    4,    4,    4,    4,    4,
    4,    4,    4,    4,    4,    4,    4,    3,    3,    3,
    3,    3,    3,    0,    4,    0,    3,    4,    4,    3,
    3,    4,    3,    3,    1,    2,    2,    3,    3,    1,
    2,    2,    1,    1,    1,    1,    1,    2,    3,    2,
    2,    2,    4,    3,    4,    0,    7,    5,    5,    5,
    6,    6,    6,    0,    6,    4,    4,    0,    6,    5,
    0,    3,    1,    1,    1,    1,    2,    1,    5,    4,
    4,    4,    4,    5,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   17,   16,   18,    0,    0,
    0,    0,    3,    4,    6,    0,    0,    0,    0,   33,
   34,   35,   36,    0,    0,    0,    0,  107,  106,    0,
    0,   37,    0,  104,    0,  100,  103,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  112,
    2,    5,    0,    9,    0,   12,    0,   10,    0,   32,
    0,   15,    0,    0,   39,    0,  110,    0,  108,   84,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   91,    0,   26,    0,   90,    0,    0,    0,
    0,    0,  114,    0,    0,    0,    0,    0,    0,    0,
    0,    7,    8,    0,    0,    0,    0,    0,   40,    0,
   87,  109,    0,    0,    0,    0,    0,    0,    0,   38,
    0,    0,    0,    0,    0,    0,    0,    0,   98,   99,
   92,   88,    0,  136,  135,    0,  138,    0,    0,    0,
    0,    0,    0,   29,  115,  113,  142,  143,    0,  141,
    0,    0,  140,    0,   89,    0,   47,    0,   46,    0,
    0,   43,   85,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   74,   76,   77,   75,   72,
   73,  137,    0,    0,    0,    0,    0,    0,   28,  144,
  139,    0,   19,   11,   42,   45,   44,   41,   50,   59,
   52,   63,   53,   65,   51,   61,   48,   55,   49,   57,
    0,    0,  118,    0,    0,  120,    0,  116,  119,    0,
   24,    0,  128,    0,  123,  121,  122,    0,    0,    0,
    0,    0,    0,  117,   22,  133,  126,    0,    0,  127,
    0,  124,    0,    0,  130,    0,  132,  129,  125,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   94,  151,   17,   18,   48,  152,  229,
   95,   19,   20,   21,   22,   23,   32,   65,   72,  110,
  161,   42,  173,  175,  165,  171,  167,  169,  113,   66,
   34,   35,   36,   37,   45,  212,  213,  228,  237,  246,
  231,  238,
};
final static short yysindex[] = {                        42,
 -223,  -28,  -41,  -18,  -12,    0,    0,    0, -209, -197,
    0,   42,    0,    0,    0,  -53,   22,  -51,  247,    0,
    0,    0,    0, -195,   25, -167,  -37,    0,    0, -164,
  -10,    0,  110,    0,   45,    0,    0,  -37,   -6, -159,
 -148,   14, -145, -102,   60,  162,  -39,    2,  195,    0,
    0,    0,  107,    0,  153,    0,  -13,    0,  138,    0,
  214,    0,   78,  -19,    0,  215,    0,    8,    0,    0,
  302, -167,  214,  214,  214,  214,  -46,  -46,  214,  214,
  -46,  -46,    0,   21,    0,    0,    0,  238,   47, -177,
  238,  247,    0,   52,  -29,  247,  259,  -16,  260, -233,
  270,    0,    0, -159,   84, -205,   64,   71,    0,  278,
    0,    0,  316,  214,  214,  214,  214,  214,  214,    0,
  156,  157,  331,  416,   45,   45,  428,  436,    0,    0,
    0,    0,  -37,    0,    0,  102,    0,  310,  238,  238,
  171,  327,  167,    0,    0,    0,    0,    0,   24,    0,
  132,  275,    0,  359,    0,  370,    0,  395,    0,  399,
 -205,    0,    0,  442,  431,  473,  435,  474,  439,  479,
  452,  480,  461,  486,  462,    0,    0,    0,    0,    0,
    0,    0,   40,  403,  446,   40,   54,   40,    0,    0,
    0,   46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  238,  -52,    0,   40,   40,    0,   40,    0,    0,    0,
    0,  469,    0,  203,    0,    0,    0,   40, -233,  489,
  214,  489,   57,    0,    0,    0,    0,  238,  475,    0,
  489,    0,  491,  489,    0,  489,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  482,    0,    0,    0,    0,    0,    0,    0,
    0,  533,    0,    0,    0,    0,    0,    0,   15,    0,
    0,    0,    0,    0,    0,  501,   90,    0,    0,    0,
    0,    0,    0,    0,  146,    0,    0,  391,    0,    0,
  276,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,   16,    0,   29,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   97,    0,    0,
    0,    0,    0,    0,    0,    0,  151,  311,    0,    0,
  119,  126,    0,    0,    0,  406,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  211,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  503,  504,  505,  506,  507,  508,    0,
 -130, -122,  -76,   93,  365,  385,  127,  135,    0,    0,
    0,    0,  -17,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   50,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  145,    0,  179,    0,  185,    0,  210,
    0,  230,    0,  250,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  425,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -91,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  251,
    0,  251,    0,    0,    0,    0,    0,    0,    0,    0,
  251,    0,    0,  251,    0,  251,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  539,  540,   49,   38,    4,    0,    0, -181,    0,
  348,   13,    0,    0,    0,    0,    0,  481,    0,  -83,
    0,  421,    0,    0,    0,    0,    0,    0,    0,    0,
    7,   99,  -68,    0,    0,  351, -121,    0,   98,    0,
    0,    0,
};
final static int YYTABLESIZE=673;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         10,
   25,   99,   40,   30,   41,   54,   24,   58,   68,  224,
  221,   31,  129,  130,   31,   13,   30,   10,   24,   55,
    9,   44,  156,  134,  149,   24,   10,   47,   14,  146,
  104,    6,    7,   20,   30,   10,   25,   16,   30,   10,
   25,  134,  148,   85,  134,  103,   25,  235,   15,   16,
  107,   24,   83,  108,   49,   13,   78,   96,   77,   25,
   15,   13,   50,   78,  216,   77,  219,   60,   14,   24,
   24,   61,   87,   31,   14,   96,   96,  198,  140,  132,
   56,    9,  191,   62,  136,   10,   81,   10,   63,  141,
   64,   82,  225,  226,  137,  227,   69,  137,   24,    9,
   53,  211,   24,    9,  220,   10,  234,  154,   30,   10,
    9,   86,  218,   24,   24,  242,   10,    9,   93,   96,
   96,   88,  157,   10,  100,   80,   78,   80,   77,  159,
  105,  105,  105,   82,  105,   82,  105,  111,  111,  111,
  143,  111,  155,  111,   60,  137,  137,  137,  105,  105,
   40,  105,   78,   89,   77,  111,  111,   90,  111,  101,
  101,  101,  239,  101,   91,  101,  102,  102,  102,   80,
  102,   79,  102,   21,   21,  125,  126,  101,  101,   83,
  101,   83,   30,   41,  102,  102,   95,  102,   95,  137,
   95,   97,  137,   97,  137,   97,  176,  177,   78,   78,
   77,   77,   97,  223,   95,   95,   53,   95,   57,   97,
   97,  102,   97,   27,   28,  136,   10,  137,   38,   28,
  137,  137,   67,  137,   98,   39,  145,   26,   29,  186,
  137,   27,   28,   29,  137,  101,  107,    2,  134,  108,
   59,   43,    4,    5,  137,   70,   29,  136,   10,   27,
   28,   46,   92,   27,   28,  111,   25,   25,   30,   10,
   25,  232,   25,   25,   29,   25,   25,  112,   29,   27,
   31,   13,   13,   31,   25,   13,  131,   13,   13,  190,
   13,   13,  136,   10,   14,   14,    9,   31,   14,   13,
   14,   14,   10,   14,   14,  131,  131,    1,    2,  133,
  134,    3,   14,    4,    5,   30,    6,    7,   30,  217,
    6,    7,  241,  139,  135,    8,    2,  147,  150,   59,
    2,    4,    5,   59,  144,    4,    5,    2,  153,  240,
   59,   92,    4,    5,    2,   92,  162,   59,  245,    4,
    5,  248,   92,  249,   78,  105,   77,  105,   81,   92,
   81,   96,  111,   96,  111,   96,  163,  105,  105,  105,
  105,  119,  182,  118,  111,  111,  111,  111,  183,   96,
   96,  178,   96,   78,  101,   77,  101,   73,   74,   75,
   76,  102,   78,  102,   78,  188,  101,  101,  101,  101,
   79,  192,   79,  102,  102,  102,  102,   38,   28,  193,
   68,   95,   68,   95,   39,   94,   97,   94,   97,   94,
  106,  109,   29,   95,   95,   95,   95,  194,   97,   97,
   97,   97,   33,   94,   94,   93,   94,   93,  195,   93,
  133,  134,  105,  105,   70,  105,   70,  105,  138,  189,
   71,  142,   71,   93,   93,  135,   93,  112,  112,  105,
  112,   71,  112,  196,  158,  160,  179,  197,   78,   84,
   77,  214,  133,  134,  112,   69,   27,   69,  180,   27,
   78,  200,   77,   27,   28,  202,  181,  135,   78,  204,
   77,  105,  199,   31,   78,   66,   77,   66,   29,  184,
  185,  187,  206,  121,  122,  123,  124,  133,  134,  127,
  128,  208,  210,    2,  215,   67,   59,   67,    4,    5,
  131,  131,  135,  201,  203,   78,   78,   77,   77,  205,
  207,   78,   78,   77,   77,  131,  209,  230,   78,  236,
   77,  247,    1,  244,  164,  166,  168,  170,  172,  174,
   25,   86,  111,   58,   62,   64,   60,   54,   56,   23,
   51,   52,  120,    0,    0,    0,    0,    0,    0,    0,
    0,  222,    0,    0,    0,    0,   96,    0,   96,  114,
  115,  116,  117,    0,  233,    0,    0,    0,   96,   96,
   96,   96,    0,    0,    0,    0,    0,    0,  243,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   94,    0,   94,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   94,   94,   94,   94,    0,    0,    0,    0,
   93,    0,   93,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   93,   93,   93,   93,    0,  110,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  109,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         46,
    0,   41,   44,   45,   46,   59,    0,   59,   46,   62,
  192,   40,   81,   82,    0,    0,   45,   46,   12,   16,
   40,   40,  106,   41,   41,   19,   46,   40,    0,   59,
   44,  265,  266,  125,   45,   46,  260,    0,   45,   46,
   40,   59,   59,   40,   62,   59,   46,  229,    0,   12,
  256,   45,   59,  259,  264,   40,   43,   45,   45,   59,
   12,   46,  260,   43,  186,   45,  188,   19,   40,   63,
   64,  267,   59,   59,   46,   63,   64,  161,  256,   59,
   59,   40,   59,   59,   45,   46,   42,   46,  256,  267,
  258,   47,  214,  215,   88,  217,  261,   91,   92,   40,
  260,   62,   96,   40,   59,   46,  228,  104,   59,   46,
   40,  260,   59,  107,  108,   59,   46,   40,   59,  107,
  108,  267,   59,   46,  123,  256,   43,  258,   45,   59,
   41,   42,   43,  256,   45,  258,   47,   41,   42,   43,
   92,   45,   59,   47,   96,  139,  140,  141,   59,   60,
   44,   62,   43,  256,   45,   59,   60,  260,   62,   41,
   42,   43,  231,   45,  267,   47,   41,   42,   43,   60,
   45,   62,   47,  265,  266,   77,   78,   59,   60,  256,
   62,  258,   45,   46,   59,   60,   41,   62,   43,  183,
   45,   41,  186,   43,  188,   45,   41,   41,   43,   43,
   45,   45,   41,  256,   59,   60,  260,   62,  260,   59,
   60,   59,   62,  260,  261,   45,   46,  211,  260,  261,
  214,  215,  260,  217,  264,  267,  256,  256,  275,   59,
  224,  260,  261,  275,  228,   41,  256,  257,  256,  259,
  260,  260,  262,  263,  238,  256,  275,   45,   46,  260,
  261,  264,  272,  260,  261,   41,  256,  257,   45,   46,
  260,   59,  262,  263,  275,  265,  266,  260,  275,   59,
  256,  256,  257,  259,  274,  260,  256,  262,  263,  256,
  265,  266,   45,   46,  256,  257,   40,  273,  260,  274,
  262,  263,   46,  265,  266,   45,   46,  256,  257,  260,
  261,  260,  274,  262,  263,  256,  265,  266,  259,  256,
  265,  266,  256,  267,  275,  274,  257,   59,   59,  260,
  257,  262,  263,  260,  273,  262,  263,  257,   59,  232,
  260,  272,  262,  263,  257,  272,   59,  260,  241,  262,
  263,  244,  272,  246,   43,  256,   45,  258,  256,  272,
  258,   41,  256,   43,  258,   45,   41,  268,  269,  270,
  271,   60,  261,   62,  268,  269,  270,  271,   59,   59,
   60,   41,   62,   43,  256,   45,  258,  268,  269,  270,
  271,  256,  256,  258,  258,   59,  268,  269,  270,  271,
  256,  260,  258,  268,  269,  270,  271,  260,  261,  125,
  256,  256,  258,  258,  267,   41,  256,   43,  258,   45,
   63,   64,  275,  268,  269,  270,  271,   59,  268,  269,
  270,  271,    2,   59,   60,   41,   62,   43,   59,   45,
  260,  261,   42,   43,  256,   45,  258,   47,   88,  273,
  256,   91,  258,   59,   60,  275,   62,   42,   43,   59,
   45,   31,   47,   59,  107,  108,   41,   59,   43,   39,
   45,   59,  260,  261,   59,  256,  256,  258,   41,  259,
   43,   41,   45,  260,  261,   41,   41,  275,   43,   41,
   45,   61,   41,  273,   43,  256,   45,  258,  275,  139,
  140,  141,   41,   73,   74,   75,   76,  260,  261,   79,
   80,   41,   41,  257,   59,  256,  260,  258,  262,  263,
  260,  261,  275,   41,   41,   43,   43,   45,   45,   41,
   41,   43,   43,   45,   45,  275,   41,   59,   43,   41,
   45,   41,    0,   59,  114,  115,  116,  117,  118,  119,
   59,   41,  267,   41,   41,   41,   41,   41,   41,  125,
   12,   12,   72,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  211,   -1,   -1,   -1,   -1,  256,   -1,  258,  268,
  269,  270,  271,   -1,  224,   -1,   -1,   -1,  268,  269,
  270,  271,   -1,   -1,   -1,   -1,   -1,   -1,  238,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,   -1,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  268,  269,  270,  271,   -1,   -1,   -1,   -1,
  256,   -1,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  268,  269,  270,  271,   -1,  267,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  267,
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
"bucle : FOR condicion_for bloque ';'",
"bucle : FOR condicion_for ';'",
"bucle : FOR condicion_for bloque error",
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
};

//#line 358 "gramatica.txt"

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
		this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": Incopatibilidad de tipo, se eperaba un entero");
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
//#line 788 "Parser.java"
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
//#line 132 "gramatica.txt"
{desapilar(0); agregarRotulo();}
break;
case 45:
//#line 134 "gramatica.txt"
{yyerror("se esperaba un 'else'.");}
break;
case 46:
//#line 135 "gramatica.txt"
{yyerror("se esperaba un bloque luego de la palabra reservada 'else'.");}
break;
case 47:
//#line 136 "gramatica.txt"
{yyerror("falta rama del else");}
break;
case 48:
//#line 139 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYOR);}
break;
case 49:
//#line 140 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENOR);}
break;
case 50:
//#line 141 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.IGUAL);}
break;
case 51:
//#line 142 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.DISTINTO);}
break;
case 52:
//#line 143 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MENORIGUAL);}
break;
case 53:
//#line 144 "gramatica.txt"
{yyout("CONDICION");agregarAPolaca(ElementoPolaca.MAYORIGUAL);}
break;
case 54:
//#line 146 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 56:
//#line 147 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 58:
//#line 148 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 60:
//#line 149 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 62:
//#line 150 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 64:
//#line 151 "gramatica.txt"
{yyerror("Falta la expresion derecha de la condicion.");}
break;
case 66:
//#line 153 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 67:
//#line 154 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
break;
case 68:
//#line 155 "gramatica.txt"
{yyerror("Falta ')' al final de la condicion.");}
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
//#line 160 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 73:
//#line 161 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 74:
//#line 162 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
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
//#line 167 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 79:
//#line 168 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 80:
//#line 169 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
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
//#line 174 "gramatica.txt"
{yyerror("Se encontro un error en la condicion.");}
break;
case 86:
//#line 175 "gramatica.txt"
{yyerror("Se encontro un error en la condicion");}
break;
case 88:
//#line 180 "gramatica.txt"
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
case 89:
//#line 189 "gramatica.txt"
{yyout("Asignacion a elemento de estructura.");
														 controlarTipoAsignacion((Entrada)val_peek(3).obj);
														 tipoExpresion = Tipo.INTEGER;
                                                         agregarAPolacaId(estructuraPolaca);
                                                         agregarAPolaca(ElementoPolaca.ASIGNACION);}
break;
case 90:
//#line 195 "gramatica.txt"
{ yyerror("se esperaba el operador de asignacion ':='.");}
break;
case 91:
//#line 196 "gramatica.txt"
{ yyerror("se esperaba una expresion del lado derecho de la asignacion.");}
break;
case 92:
//#line 197 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 93:
//#line 200 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.SUMA);yyout("Sentencia de suma.");}
break;
case 94:
//#line 201 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.RESTA);yyout("Sentencia de resta.");}
break;
case 96:
//#line 204 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '+'.");}
break;
case 97:
//#line 205 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '-'.");}
break;
case 98:
//#line 209 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MULTIPLICACION);yyout("Sentencia de producto.");}
break;
case 99:
//#line 210 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.DIVISION);yyout("Sentencia de division.");}
break;
case 101:
//#line 213 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '*'.");}
break;
case 102:
//#line 214 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '/'.");}
break;
case 105:
//#line 221 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);
				 if(e.getTipo_dato() == Tipo.ULONGINT) tipoExpresion = Tipo.ULONGINT;
				 if(e.getTipo_dato() == Tipo.STRUCT) this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": no se puede utilizar el nombre de una estructura como operando en una expresion");
                 agregarAPolacaId(e);}
break;
case 106:
//#line 226 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                          estaDeclarada(e);
						  tipoExpresion = Tipo.ULONGINT;
                          agregarAPolacaConstante(e);}
break;
case 107:
//#line 230 "gramatica.txt"
{ Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                             e.visitar();
                             fueraRangoIntPositivo((Entrada)val_peek(0).obj);
                             agregarAPolacaConstante((Entrada)val_peek(0).obj); }
break;
case 108:
//#line 234 "gramatica.txt"
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
case 109:
//#line 249 "gramatica.txt"
{yyout("Elemento de estructura.");
                                  Entrada e = (Entrada)val_peek(0).obj;
                                  estructuraPolaca = e;
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
case 110:
//#line 262 "gramatica.txt"
{ yyerror("se esperaba un punto.");}
break;
case 111:
//#line 263 "gramatica.txt"
{ yyerror("se esperaba identificador de la variable mientro de la estructura.");}
break;
case 112:
//#line 264 "gramatica.txt"
{ yyerror("se esperaba identificador de la estructura antes del punto.");}
break;
case 113:
//#line 267 "gramatica.txt"
{agregarAPolacaDecremento();
                                         apilar(ElementoPolaca.BI);
                                         desapilarDireccion();
                                         desapilar(0);
                                         agregarRotulo();
                                         yyout("Bucle for.");}
break;
case 114:
//#line 274 "gramatica.txt"
{yyerror("se esperaba un bloque de sentencias, pero se encontro ';'.");}
break;
case 115:
//#line 275 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 116:
//#line 279 "gramatica.txt"
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
case 118:
//#line 290 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condicion.");}
break;
case 119:
//#line 291 "gramatica.txt"
{ yyerror("se esperaba un identificador antes del operador de asignacion.");}
break;
case 120:
//#line 292 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues del operador de asignacion.");}
break;
case 121:
//#line 293 "gramatica.txt"
{ yyerror("se esperaba operador de asignacion.");}
break;
case 122:
//#line 294 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 123:
//#line 295 "gramatica.txt"
{ yyerror("se encontro un error antes del operador de asignacion.");}
break;
case 124:
//#line 298 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MAYOR);
                                                 apilar(ElementoPolaca.BF);}
break;
case 126:
//#line 301 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante antes de '>'.");}
break;
case 127:
//#line 302 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues '>'.");}
break;
case 128:
//#line 303 "gramatica.txt"
{ yyerror("se esperaba '>'.");}
break;
case 130:
//#line 304 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 131:
//#line 307 "gramatica.txt"
{almacenarDecremento = true;}
break;
case 132:
//#line 307 "gramatica.txt"
{almacenarDecremento = false; controlarTiposFor();}
break;
case 133:
//#line 309 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues de ';'.");}
break;
case 134:
//#line 313 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);
                 this.elementosDelFor.add(e);
				 if(e.getTipo_dato() == Tipo.STRUCT) this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": no se puede utilizar el nombre de una estructura en la condicion del for");
                 agregarAPolacaId(e);}
break;
case 135:
//#line 318 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                          estaDeclarada(e);
                          this.elementosDelFor.add(e);
                          agregarAPolacaConstante(e);}
break;
case 136:
//#line 322 "gramatica.txt"
{ Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                             this.elementosDelFor.add(e);
                             e.visitar();
                             fueraRangoIntPositivo((Entrada)val_peek(0).obj);
                             agregarAPolacaConstante((Entrada)val_peek(0).obj); }
break;
case 137:
//#line 327 "gramatica.txt"
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
case 138:
//#line 342 "gramatica.txt"
{this.erroresSemanticos.add("En linea " + anaLex.getNumeroLinea() + ": no se puede utilizar estructuras en la condicion del for");}
break;
case 139:
//#line 345 "gramatica.txt"
{yyout("Sentencia de impresion.");
                                      agregarAPolacaConstante((Entrada)val_peek(2).obj);
                                      agregarAPolaca(ElementoPolaca.PRINT);}
break;
case 140:
//#line 349 "gramatica.txt"
{ yyerror("se esperaba 'print'");}
break;
case 141:
//#line 350 "gramatica.txt"
{ yyerror("se esperaba una cadena dentro de la instruccion print.");}
break;
case 142:
//#line 351 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la cadena en sentencia print.");}
break;
case 143:
//#line 352 "gramatica.txt"
{ yyerror("sentencia print sin parentesis de cierre.");}
break;
case 144:
//#line 354 "gramatica.txt"
{ yyerror("se esperaba ';.'");}
break;
//#line 1541 "Parser.java"
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

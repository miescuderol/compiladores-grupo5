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
   17,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   29,   17,   30,   17,   22,   22,   22,
   22,   22,   16,   16,   16,   16,   16,   31,   31,   31,
   31,   31,   33,   33,   32,   32,   32,   32,   14,   14,
   14,   37,   35,   35,   35,   35,   35,   35,   39,   36,
   36,   36,   40,   36,   36,   41,   38,   38,   34,   34,
   34,   34,   15,   15,   15,   15,   15,   15,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    3,    3,    2,    2,
    5,    2,    2,    2,    1,    1,    1,    0,    5,    3,
    0,    5,    2,    3,    1,    3,    1,    3,    2,    2,
    1,    2,    1,    1,    1,    1,    0,    4,    3,    0,
    4,    4,    3,    3,    3,    2,    2,    5,    5,    5,
    5,    5,    5,    0,    5,    0,    5,    0,    5,    0,
    5,    0,    5,    0,    5,    4,    4,    4,    4,    4,
    4,    4,    4,    4,    4,    4,    4,    3,    3,    3,
    3,    3,    3,    0,    4,    0,    3,    3,    3,    1,
    2,    2,    4,    4,    3,    3,    4,    3,    3,    1,
    2,    2,    1,    1,    3,    2,    2,    2,    4,    3,
    4,    0,    7,    5,    5,    5,    6,    6,    0,    6,
    4,    4,    0,    6,    5,    0,    3,    1,    1,    1,
    1,    2,    5,    4,    4,    4,    4,    5,
};
final static short yydefred[] = {                         0,
   17,    0,    0,    0,    0,   16,   15,   18,    0,    0,
    0,    0,    3,    4,    6,    0,    0,    0,    0,   33,
   34,   35,   36,    0,    0,    0,  131,  130,    0,    0,
   37,    0,    0,  104,  100,  103,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  108,    2,
    5,    0,    9,    0,   12,    0,   10,    0,   32,    0,
    0,    0,   39,    0,  106,    0,  132,   84,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   96,    0,   26,    0,   95,    0,    0,    0,    0,  110,
    0,    0,    0,    0,    0,    0,    0,    0,    7,    8,
    0,    0,    0,    0,    0,   40,    0,   87,  105,    0,
    0,    0,    0,    0,    0,    0,   38,    0,    0,    0,
    0,    0,    0,    0,    0,   98,   99,   97,   93,  129,
    0,    0,    0,    0,    0,   29,  111,  109,  136,  137,
    0,  135,    0,    0,  134,    0,   94,    0,   47,    0,
   46,    0,    0,   43,   85,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   74,   76,   77,
   75,   72,   73,    0,    0,    0,    0,    0,   28,  138,
  133,    0,   19,   11,   42,   45,   44,   41,   50,   59,
   52,   63,   53,   65,   51,   61,   48,   55,   49,   57,
    0,    0,  114,    0,  116,    0,  112,  115,    0,   24,
    0,  123,    0,  117,  118,    0,    0,    0,    0,    0,
    0,  113,   22,  128,  121,    0,    0,  122,    0,  119,
    0,    0,  125,    0,  127,  124,  120,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   91,  143,   17,   18,   47,  144,  217,
   92,   19,   20,   21,   22,   23,   31,   63,   70,  107,
  153,   41,  165,  167,  157,  163,  159,  161,  110,   64,
   33,   34,   35,   36,   44,  203,  216,  225,  234,  219,
  226,
};
final static short yysindex[] = {                       -38,
    0,  -40,   60,  -37,  -22,    0,    0,    0, -231, -182,
    0,  -38,    0,    0,    0,  -55,   53,  -52,   -8,    0,
    0,    0,    0, -149, -188,  -29,    0,    0, -173,    9,
    0,  152,   86,    0,    0,    0,  -29,  121, -125, -119,
   -6, -114, -203,   36,  107,  -18,   35,  140,    0,    0,
    0,  142,    0,  147,    0,  -13,    0,   62,    0,   84,
  -12,  -19,    0,  176,    0,  -34,    0,    0,  382, -188,
   84,   84,   84,   84,   50,   50,   84,   84,   50,   50,
    0,  -23,    0,    0,    0,  156, -237,  156,   -8,    0,
  -14,  -46,   -8,  218,  -16,  224, -193,  228,    0,    0,
 -125,  133, -208,   44,   52,    0,  243,    0,    0,  272,
   84,   84,   84,   84,   84,   84,    0,   46,  343,  431,
  437,   86,   86,  440,  447,    0,    0,    0,    0,    0,
  260,  156,   57,  267,   61,    0,    0,    0,    0,    0,
   -9,    0,   79,  221,    0,  288,    0,  293,    0,  297,
    0,  304, -208,    0,    0,  448,  328,  453,  342,  456,
  353,  473,  354,  474,  360,  479,  365,    0,    0,    0,
    0,    0,    0,   93,  350,   93,    6,   93,    0,    0,
    0,  180,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  156,   24,    0,   93,    0,   93,    0,    0,    0,    0,
  375,    0,  101,    0,    0,   93, -193,  383,   84,  383,
   12,    0,    0,    0,    0,  156,  376,    0,  383,    0,
  402,  383,    0,  383,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  392,    0,    0,    0,    0,    0,    0,    0,
    0,  454,    0,    0,    0,    0,    0,    0,   15,    0,
    0,    0,    0,    0,  414,   72,    0,    0,    0,    0,
    0,    0,  129,    0,    0,    0,  -33,    0,    0,  190,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    0,   16,    0,   29,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   80,    0,    0,    0,    0,
    0,    0,    0,    0,  134,  170,    0,    0,  102,  109,
    0,    0,    0,   38,    0,    0,    0,    0,    0,    0,
    0,    0,  197,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  419,  420,  421,  422,  425,  427,    0, -179, -157, -132,
  -99,  348,  370,  -93,  -71,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -58,    0,  -49,    0,   75,
    0,   99,    0,  108,    0,  192,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  344,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -83,    0,
    0,    0,    0,    0,    0,    0,    0,  158,    0,  158,
    0,    0,    0,    0,    0,    0,    0,    0,  158,    0,
    0,  158,    0,  158,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  472,  491,  452,   40,   19,    0,    0, -158,    0,
   32,    5,    0,    0,    0,    0,    0,  451,    0,  -92,
    0,  435,    0,    0,    0,    0,    0,    0,    0,    0,
  299,  467,  -35,  326,    0,  271,    0,  -30,    0,    0,
    0,
};
final static int YYTABLESIZE=653;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         30,
   25,    9,   43,   53,   29,   10,   57,   10,  129,  129,
  148,  129,  138,  129,   31,   13,   66,   46,  132,   76,
    9,   75,   96,  210,  141,  129,   10,    9,   14,  133,
  101,    9,   48,   10,   54,  129,   76,   10,   75,   16,
   25,   20,  140,  126,  127,  100,   25,  104,   93,  181,
  105,   16,   85,   29,   10,   13,   87,   83,  223,   25,
  188,   13,    1,   88,  207,   93,   93,   61,   14,   62,
  230,    6,    7,   31,   14,    9,   80,   49,   80,  108,
  108,   10,  108,    9,  108,  213,  168,   67,   76,   10,
   75,    9,  103,  106,   90,   10,  108,   10,   82,   30,
   82,   29,  149,   39,   29,   40,   29,   40,   93,   93,
  151,   55,  129,  129,  129,  176,  129,   60,  129,  146,
  107,  107,  107,   83,  107,   83,  107,   79,   29,   10,
  129,  129,   80,  129,   52,  150,  152,   29,  107,  107,
   84,  107,  101,  101,  101,   29,  101,   94,  101,  102,
  102,  102,   86,  102,  201,  102,   81,   97,   81,  220,
  101,  101,   78,  101,   78,   29,   10,  102,  102,   90,
  102,   90,   21,   90,   92,   76,   92,   75,   92,   81,
   98,   21,   21,  227,   79,   39,   79,   90,   90,  228,
   90,  147,   92,   92,   76,   92,   75,   68,  233,   68,
   29,  236,  126,  237,   52,   99,   70,   56,   70,  137,
   91,   78,   91,   77,   91,   25,  108,    1,    2,   26,
   27,    3,   42,    4,    5,  109,    6,    7,   91,   91,
   65,   91,  128,  106,   28,    8,  104,    2,  209,  105,
   58,   45,    4,    5,    2,   95,  180,   58,    2,    4,
    5,   58,   89,    4,    5,   27,   25,   25,  136,   89,
   25,  206,   25,   25,   68,   25,   25,  229,   26,   27,
   31,   13,   13,   31,   25,   13,  139,   13,   13,  212,
   13,   13,  142,   28,   14,   14,  145,   31,   14,   13,
   14,   14,    2,   14,   14,   58,   30,    4,    5,   30,
    2,  154,   14,   58,  105,    4,    5,   89,    2,   26,
   27,   58,  155,    4,    5,   89,  130,   27,  174,   37,
   27,   37,   27,   89,   28,  178,   38,  129,   38,  129,
   71,   28,   71,  179,   28,  107,   28,  107,  182,  129,
  129,  129,  129,   26,   27,  183,  184,  107,  107,  107,
  107,  185,  130,   27,   69,  186,   69,  101,   28,  101,
  130,   27,  187,   66,  102,   66,  102,   28,  190,  101,
  101,  101,  101,  122,  123,   28,  102,  102,  102,  102,
   26,   27,  192,  169,   90,   76,   90,   75,   89,   92,
   89,   92,   89,  194,  196,   28,   90,   90,   90,   90,
  198,   92,   92,   92,   92,  200,   89,   89,  204,   89,
   88,  131,   88,  134,   88,  130,   27,  126,  126,   71,
   72,   73,   74,  224,   76,   91,   75,   91,   88,   88,
   28,   88,  126,  218,  232,    1,   32,   91,   91,   91,
   91,  116,  235,  115,    6,    7,  205,   67,  208,   67,
   25,   15,   27,    1,   86,   27,  107,  175,  177,   58,
   62,   64,   60,   15,   69,   54,   24,   56,   23,   31,
   59,  170,   82,   76,  214,   75,  215,  171,   24,   76,
  172,   75,   76,   50,   75,   24,  222,  173,  189,   76,
   76,   75,   75,  191,  102,   76,  193,   75,   76,  202,
   75,  202,   51,  202,    0,  118,  119,  120,  121,    0,
   24,  124,  125,  195,  197,   76,   76,   75,   75,  199,
  117,   76,    0,   75,    0,    0,  211,   24,   24,  202,
    0,  202,    0,    0,    0,    0,    0,    0,  221,    0,
  135,  202,    0,    0,   59,  156,  158,  160,  162,  164,
  166,  231,    0,    0,    0,   24,    0,    0,    0,   24,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   24,   24,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   89,    0,   89,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   89,   89,   89,   89,    0,
    0,    0,    0,    0,    0,   88,    0,   88,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   88,   88,   88,
   88,    0,    0,    0,    0,    0,    0,    0,    0,  111,
  112,  113,  114,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   40,   59,   45,   46,   59,   46,   42,   43,
  103,   45,   59,   47,    0,    0,   46,   40,  256,   43,
   40,   45,   41,  182,   41,   59,   46,   40,    0,  267,
   44,   40,  264,   46,   16,   59,   43,   46,   45,    0,
   40,  125,   59,   79,   80,   59,   46,  256,   44,   59,
  259,   12,   59,   45,   46,   40,  260,   39,  217,   59,
  153,   46,  256,  267,   59,   61,   62,  256,   40,  258,
   59,  265,  266,   59,   46,   40,  256,  260,  258,   42,
   43,   46,   45,   40,   47,   62,   41,  261,   43,   46,
   45,   40,   61,   62,   59,   46,   59,   46,  256,   59,
  258,   45,   59,   44,   45,   46,   45,   46,  104,  105,
   59,   59,   41,   42,   43,   59,   45,  267,   47,  101,
   41,   42,   43,  256,   45,  258,   47,   42,   45,   46,
   59,   60,   47,   62,  260,  104,  105,   45,   59,   60,
  260,   62,   41,   42,   43,   45,   45,   41,   47,   41,
   42,   43,  267,   45,   62,   47,  256,  123,  258,   59,
   59,   60,  256,   62,  258,   45,   46,   59,   60,   41,
   62,   43,  256,   45,   41,   43,   43,   45,   45,   59,
   41,  265,  266,  219,  256,   44,  258,   59,   60,  220,
   62,   59,   59,   60,   43,   62,   45,  256,  229,  258,
   45,  232,   45,  234,  260,   59,  256,  260,  258,  256,
   41,   60,   43,   62,   45,  256,   41,  256,  257,  260,
  261,  260,  260,  262,  263,  260,  265,  266,   59,   60,
  260,   62,  256,  267,  275,  274,  256,  257,   59,  259,
  260,  264,  262,  263,  257,  264,  256,  260,  257,  262,
  263,  260,  272,  262,  263,   59,  256,  257,  273,  272,
  260,  256,  262,  263,  256,  265,  266,  256,  260,  261,
  256,  256,  257,  259,  274,  260,   59,  262,  263,  256,
  265,  266,   59,  275,  256,  257,   59,  273,  260,  274,
  262,  263,  257,  265,  266,  260,  256,  262,  263,  259,
  257,   59,  274,  260,  267,  262,  263,  272,  257,  260,
  261,  260,   41,  262,  263,  272,  260,  261,   59,  260,
  261,  260,  261,  272,  275,   59,  267,  256,  267,  258,
  256,  275,  258,  273,  275,  256,  275,  258,  260,  268,
  269,  270,  271,  260,  261,  125,   59,  268,  269,  270,
  271,   59,  260,  261,  256,   59,  258,  256,  275,  258,
  260,  261,   59,  256,  256,  258,  258,  275,   41,  268,
  269,  270,  271,   75,   76,  275,  268,  269,  270,  271,
  260,  261,   41,   41,  256,   43,  258,   45,   41,  256,
   43,  258,   45,   41,   41,  275,  268,  269,  270,  271,
   41,  268,  269,  270,  271,   41,   59,   60,   59,   62,
   41,   86,   43,   88,   45,  260,  261,  260,  261,  268,
  269,  270,  271,   41,   43,  256,   45,  258,   59,   60,
  275,   62,  275,   59,   59,  256,    2,  268,  269,  270,
  271,   60,   41,   62,  265,  266,  176,  256,  178,  258,
   59,    0,  256,    0,   41,  259,  267,  132,  133,   41,
   41,   41,   41,   12,   30,   41,    0,   41,  125,  273,
   19,   41,   38,   43,  204,   45,  206,   41,   12,   43,
   41,   45,   43,   12,   45,   19,  216,   41,   41,   43,
   43,   45,   45,   41,   60,   43,   41,   45,   43,  174,
   45,  176,   12,  178,   -1,   71,   72,   73,   74,   -1,
   44,   77,   78,   41,   41,   43,   43,   45,   45,   41,
   70,   43,   -1,   45,   -1,   -1,  201,   61,   62,  204,
   -1,  206,   -1,   -1,   -1,   -1,   -1,   -1,  213,   -1,
   89,  216,   -1,   -1,   93,  111,  112,  113,  114,  115,
  116,  226,   -1,   -1,   -1,   89,   -1,   -1,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  104,  105,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,   -1,  258,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  268,  269,  270,  271,   -1,
   -1,   -1,   -1,   -1,   -1,  256,   -1,  258,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  268,  269,  270,
  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  268,
  269,  270,  271,
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
"$$13 :",
"condicion_for : '(' ID ASIGN factor_for ';' $$13 comparacion_for",
"condicion_for : ID ASIGN factor_for ';' comparacion_for",
"condicion_for : '(' ASIGN factor_for ';' comparacion_for",
"condicion_for : '(' ID ASIGN ';' comparacion_for",
"condicion_for : '(' ID error factor_for ';' comparacion_for",
"condicion_for : '(' ID ASIGN factor_for error comparacion_for",
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
"impresion : PRINT '(' CADENA ')' ';'",
"impresion : '(' CADENA ')' ';'",
"impresion : PRINT '(' ')' ';'",
"impresion : PRINT CADENA ')' ';'",
"impresion : PRINT '(' CADENA ';'",
"impresion : PRINT '(' CADENA ')' error",
};

//#line 289 "gramatica.txt"

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
private Stack<Integer> pila=new Stack();
private Stack<Integer> retornos=new Stack();
private Entrada indiceFor;
private ElementoPolaca decrementoFor;
private boolean almacenarDecremento = false;
private Entrada estructuraPolaca;

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
       return (erroresSemanticos.isEmpty());
}

//metodos para controlar la polaca
private void agregarAPolacaId(Entrada e){
    Tipo t = anaLex.getTablaSimbolos().get(e.getNombre()).getTipo_dato();
    ElementoPolaca ep = new ElementoPolaca(ElementoPolaca.VARIABLE, t,e.getNombre());
    if (almacenarDecremento)
       decrementoFor = ep;
    else
        polacaInversa.add(ep);
}

private void agregarAPolacaConstante(Entrada e){
    ElementoPolaca ep;
    if(e.getTipo() == Tipo.CONSTANTE_INTEGER)
    ep = new ElementoPolaca(ElementoPolaca.CONSTANTE, Tipo.INTEGER,e.getValor().toString());
    else
      if(e.getTipo() == Tipo.CONSTANTE_ULONGINT)
        ep = new ElementoPolaca(ElementoPolaca.CONSTANTE, Tipo.ULONGINT,e.getValor().toString());
      else
        ep = new ElementoPolaca(ElementoPolaca.CADENA,e.getValor().toString());
    if(almacenarDecremento)
        decrementoFor = ep;
    else 
        polacaInversa.add(ep);
}

private void agregarAPolaca(int t){
     polacaInversa.add(new ElementoPolaca(t));
}

private void agregarAPolacaDecremento(){
    agregarAPolacaId(indiceFor);
    polacaInversa.add(decrementoFor);
    polacaInversa.add(new ElementoPolaca(ElementoPolaca.RESTA));
    agregarAPolacaId(indiceFor);
    polacaInversa.add(new ElementoPolaca(ElementoPolaca.ASIGNACION));
}

private void guardarIndiceFor(Entrada e){
    this.indiceFor = e;
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
//#line 726 "Parser.java"
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
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 73:
//#line 150 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 74:
//#line 151 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 75:
//#line 152 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 76:
//#line 153 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 77:
//#line 154 "gramatica.txt"
{yyerror("Falta '(' al principio de la condicion.");}
break;
case 78:
//#line 156 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 79:
//#line 157 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 80:
//#line 158 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 81:
//#line 159 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 82:
//#line 160 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 83:
//#line 161 "gramatica.txt"
{yyerror("Faltan parentesis en condicion.");}
break;
case 84:
//#line 163 "gramatica.txt"
{yyerror("Se encontro un error en la condicion.");}
break;
case 86:
//#line 164 "gramatica.txt"
{yyerror("Se encontro un error en la condicion");}
break;
case 88:
//#line 168 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.SUMA);yyout("Sentencia de suma.");}
break;
case 89:
//#line 169 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.RESTA);yyout("Sentencia de resta.");}
break;
case 91:
//#line 172 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '+'.");}
break;
case 92:
//#line 173 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '-'.");}
break;
case 93:
//#line 177 "gramatica.txt"
{
                                        Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(3).obj).getNombre());
                                        estaDeclarada(e);
                                        agregarAPolacaId((Entrada)val_peek(3).obj);
                                        agregarAPolaca(ElementoPolaca.ASIGNACION);
                                    }
break;
case 94:
//#line 183 "gramatica.txt"
{yyout("Asignacion a elemento de estructura.");
                                                         agregarAPolacaId(estructuraPolaca);
                                                         agregarAPolaca(ElementoPolaca.ASIGNACION);}
break;
case 95:
//#line 187 "gramatica.txt"
{ yyerror("se esperaba el operador de asignacion ':='.");}
break;
case 96:
//#line 188 "gramatica.txt"
{ yyerror("se esperaba una expresion del lado derecho de la asignacion.");}
break;
case 97:
//#line 189 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 98:
//#line 192 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MULTIPLICACION);yyout("Sentencia de producto.");}
break;
case 99:
//#line 193 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.DIVISION);yyout("Sentencia de division.");}
break;
case 101:
//#line 196 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '*'.");}
break;
case 102:
//#line 197 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '/'.");}
break;
case 105:
//#line 204 "gramatica.txt"
{yyout("Elemento de estructura.");
                                  estructuraPolaca = (Entrada)val_peek(0).obj;}
break;
case 106:
//#line 207 "gramatica.txt"
{ yyerror("se esperaba un punto.");}
break;
case 107:
//#line 208 "gramatica.txt"
{ yyerror("se esperaba identificador de la variable mientro de la estructura.");}
break;
case 108:
//#line 209 "gramatica.txt"
{ yyerror("se esperaba identificador de la estructura antes del punto.");}
break;
case 109:
//#line 212 "gramatica.txt"
{agregarAPolacaDecremento();
                                         apilar(ElementoPolaca.BI);
                                         desapilarDireccion();
                                         desapilar(0);
                                         agregarRotulo();
                                         yyout("Bucle for.");}
break;
case 110:
//#line 219 "gramatica.txt"
{yyerror("se esperaba un bloque de sentencias, pero se encontro ';'.");}
break;
case 111:
//#line 220 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 112:
//#line 224 "gramatica.txt"
{ Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(3).obj).getNombre());
                                              estaDeclarada(e);
                                              guardarIndiceFor((Entrada)val_peek(3).obj);
                                              agregarAPolacaId((Entrada)val_peek(3).obj);
                                              agregarAPolaca(ElementoPolaca.ASIGNACION);
                                              agregarRotulo();
                                              apilarDireccion();
                                              }
break;
case 114:
//#line 233 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condicion.");}
break;
case 115:
//#line 234 "gramatica.txt"
{ yyerror("se esperaba un identificador antes del operador de asignacion.");}
break;
case 116:
//#line 235 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues del operador de asignacion.");}
break;
case 117:
//#line 236 "gramatica.txt"
{ yyerror("se esperaba operador de asignacion.");}
break;
case 118:
//#line 237 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 119:
//#line 240 "gramatica.txt"
{agregarAPolaca(ElementoPolaca.MAYOR);
                                                 apilar(ElementoPolaca.BF);}
break;
case 121:
//#line 243 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante antes de '>'.");}
break;
case 122:
//#line 244 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues '>'.");}
break;
case 123:
//#line 245 "gramatica.txt"
{ yyerror("se esperaba '>'.");}
break;
case 125:
//#line 246 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 126:
//#line 249 "gramatica.txt"
{almacenarDecremento = true;}
break;
case 127:
//#line 249 "gramatica.txt"
{almacenarDecremento = false;}
break;
case 128:
//#line 251 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues de ';'.");}
break;
case 129:
//#line 255 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);
                 agregarAPolacaId((Entrada)val_peek(0).obj);}
break;
case 130:
//#line 258 "gramatica.txt"
{agregarAPolacaConstante((Entrada)val_peek(0).obj);}
break;
case 131:
//#line 259 "gramatica.txt"
{ anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()).visitar();
                             fueraRangoIntPositivo((Entrada)val_peek(0).obj);
                             agregarAPolacaConstante((Entrada)val_peek(0).obj); }
break;
case 132:
//#line 262 "gramatica.txt"
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
case 133:
//#line 276 "gramatica.txt"
{yyout("Sentencia de impresion.");
                                      agregarAPolacaConstante((Entrada)val_peek(2).obj);
                                      agregarAPolaca(ElementoPolaca.PRINT);}
break;
case 134:
//#line 280 "gramatica.txt"
{ yyerror("se esperaba 'print'");}
break;
case 135:
//#line 281 "gramatica.txt"
{ yyerror("se esperaba una cadena dentro de la instruccion print.");}
break;
case 136:
//#line 282 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la cadena en sentencia print.");}
break;
case 137:
//#line 283 "gramatica.txt"
{ yyerror("sentencia print sin parentesis de cierre.");}
break;
case 138:
//#line 285 "gramatica.txt"
{ yyerror("se esperaba ';.'");}
break;
//#line 1396 "Parser.java"
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

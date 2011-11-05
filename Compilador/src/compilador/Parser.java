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
import java.util.Vector;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//#line 26 "Parser.java"




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
    4,    4,   12,   12,   12,   12,   13,   13,   13,   13,
   17,   17,   17,   17,   17,   17,   19,   19,   19,   19,
   19,   19,   18,   18,   18,   18,   18,   16,   16,   16,
   16,   16,   20,   20,   20,   20,   20,   22,   22,   21,
   21,   21,   21,   14,   14,   14,   24,   24,   24,   24,
   24,   24,   24,   24,   24,   25,   24,   24,   24,   24,
   23,   23,   23,   23,   15,   15,   15,   15,   15,   15,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    3,    3,    2,    2,
    5,    2,    2,    2,    1,    1,    1,    0,    5,    3,
    0,    5,    2,    3,    1,    3,    1,    3,    2,    2,
    1,    2,    1,    1,    1,    1,    7,    7,    7,    7,
    5,    4,    5,    5,    5,    5,    1,    1,    1,    1,
    1,    1,    3,    3,    1,    2,    2,    4,    4,    3,
    3,    4,    3,    3,    1,    2,    2,    1,    1,    3,
    2,    2,    2,    4,    3,    4,   11,   10,   10,   10,
   10,   10,   10,   11,   11,    0,   12,   11,   11,    1,
    1,    1,    1,    2,    5,    4,    4,    4,    4,    5,
};
final static short yydefred[] = {                         0,
   17,    0,    0,    0,    0,   16,   15,   18,    0,    0,
    0,    0,    3,    4,    6,    0,    0,    0,    0,   33,
   34,   35,   36,    0,    0,    0,   93,   92,    0,    0,
    0,    0,    0,   69,   65,   68,    0,    0,    0,    0,
    0,   90,    0,    0,    0,    0,    0,    0,    0,   73,
    2,    5,    0,    9,    0,   12,    0,   10,    0,   32,
    0,    0,   71,    0,   94,    0,    0,    0,    0,   49,
   51,   52,   50,    0,    0,   47,   48,    0,    0,    0,
   61,    0,   26,    0,   60,    0,    0,    0,    0,   75,
    0,    0,    0,    0,    0,    0,    0,    0,    7,    8,
    0,    0,    0,   70,    0,    0,    0,    0,    0,    0,
    0,    0,   63,   64,   62,   58,   91,    0,    0,    0,
    0,    0,   29,   76,   74,   98,   99,    0,   97,    0,
    0,   96,    0,   59,    0,    0,    0,    0,    0,    0,
    0,    0,   42,    0,    0,    0,    0,    0,   28,  100,
   95,    0,   19,   11,    0,   43,   44,   45,   46,   41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   24,   40,   38,   39,   37,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   86,    0,
    0,   22,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   78,    0,   80,    0,   81,    0,   82,    0,   83,
    0,   79,   84,   85,    0,   88,   89,   77,   87,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   91,  130,   17,   18,   48,  131,  183,
   92,   93,   20,   21,   22,   23,   31,   41,   78,   33,
   34,   35,   36,   45,  198,
};
final static short yysindex[] = {                       -38,
    0,  -40,   37,   27,  -33,    0,    0,    0, -221, -194,
    0,  -38,    0,    0,    0,  -55,   17,  -47,  166,    0,
    0,    0,    0, -182, -162,  -37,    0,    0, -163,    9,
 -186,  149,   42,    0,    0,    0,  -37,  -19, -154, -147,
  -13,    0, -142, -215,  -23,   79,  -21,    7,   88,    0,
    0,    0,  104,    0,   78,    0,    5,    0,   57,    0,
   -7,  -12,    0, -121,    0,   95,  296,  -12,  -12,    0,
    0,    0,    0,  -28,  -28,    0,    0,   -7,  -28,  -28,
    0,   -1,    0,    0,    0,   93, -199,   93,  166,    0,
 -123,  -46,  166,   94,  -22,   99, -165,  100,    0,    0,
 -154,   18,  -97,    0,   -7,   -7,  137,  -95, -208,   42,
   42,  345,    0,    0,    0,    0,    0,  108,   93,   73,
  116,  -93,    0,    0,    0,    0,    0,    6,    0,  -82,
   61,    0,  138,    0,  -12,  394,  400,  158,  -10,  -12,
  -12,  -12,    0,   93,  143,   93,   49,   93,    0,    0,
    0,  129,    0,    0,  145,    0,    0,    0,    0,    0,
  148,  155,  179,  146,   93,  190,   93,   65,  197,    0,
    0,    0,    0,    0,    0,   93,  231,   93,  234,   93,
  -41,   93, -165,  240,   93,  243,   93,  252,    0,   86,
  260,    0,   93,  270,   93,  272,   93,   -7,   93,   50,
   93,  266,   93,  294,   93,  295,  279,  318,   93,  -31,
  319,    0,  332,    0,  337,    0,   -7,    0,  367,    0,
  -30,    0,    0,    0,  375,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  330,    0,    0,    0,    0,    0,    0,    0,
    0,  422,    0,    0,    0,    0,    0,    0,   15,    0,
    0,    0,    0,    0,    0,   45,    0,    0,    0,    0,
    0,    0,  101,    0,    0,    0,  142,    0,    0,  157,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,   16,    0,   29,    0,    0,    0,
    0,    0,    0,   52,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  106,  111,    0,    0,    0,   74,   81,
    0,    0,    0,  368,    0,    0,    0,    0,    0,    0,
    0,    0,  -56,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  131,
  136,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   21,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  300,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   44,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  420,  426,  384,   59,   34,    0,    0, -130,    0,
  433,  430,    0,    0,    0,    0,    0,  353,  -42,    4,
  421,  248,  348,    0,    0,
};
final static int YYTABLESIZE=635;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         30,
   25,    9,   27,   54,   29,   10,   47,   10,   64,  220,
  228,   58,  125,   29,   31,   13,    9,   10,  128,   96,
  190,  171,   10,  105,  107,   29,   10,    9,   14,   75,
  160,   74,   75,   10,   74,   90,  127,   29,   10,   81,
   25,   75,   49,   74,   87,   85,   25,  141,  101,   55,
  142,   88,  192,   29,   10,   13,  119,  116,   16,   25,
   75,   13,   74,  100,  151,   50,   44,  120,   14,   68,
   16,   69,   83,   31,   14,   56,  134,  110,  111,   30,
   39,   29,   40,   79,   61,   91,   91,   91,   80,   91,
    1,   91,   72,   72,   72,   62,   72,   65,   72,    6,
    7,   29,   40,   91,   91,   53,   91,  168,  210,   29,
   72,   72,   84,   72,   66,   66,   66,   29,   66,   94,
   66,   67,   67,   67,   86,   67,  180,   67,   98,   97,
   29,  146,   66,   66,  133,   66,   99,   29,  104,   67,
   67,   55,   67,   55,  199,   55,   57,   39,   57,  123,
   57,   56,  126,   56,   77,   56,   76,  129,  132,   55,
   55,  135,   55,  140,   57,   57,  144,   57,   20,   56,
   56,   54,   56,   54,  148,   54,   53,  152,   53,  149,
   53,   29,   10,   91,   91,  153,   91,  170,   91,   54,
   54,   75,   54,   74,   53,   53,  154,   53,  158,   27,
   91,  165,   27,  172,   53,    9,  173,  176,   77,  124,
   76,   10,   57,  174,  189,   25,   31,    1,    2,   26,
   27,    3,   63,    4,    5,  227,    6,    7,  117,   27,
   46,   26,   27,    2,   28,    8,   59,  175,    4,    5,
   26,   27,   95,   28,    2,  159,   28,   59,   89,    4,
    5,  178,   26,   27,  115,   28,   25,   25,  182,   89,
   25,  150,   25,   25,   66,   25,   25,   28,   26,   27,
   31,   13,   13,   31,   25,   13,   30,   13,   13,   30,
   13,   13,   42,   28,   14,   14,   43,   31,   14,   13,
   14,   14,  185,   14,   14,  187,   37,   27,  193,   21,
   91,  195,   14,   38,  167,  209,  212,   72,   21,   21,
  197,   28,   91,   91,   91,   91,   37,   27,  201,   72,
   72,   72,   72,   38,  117,   27,  113,  114,  203,   66,
  205,   28,  117,   27,  214,  216,   67,  217,   75,   28,
   74,   66,   66,   66,   66,  117,   27,   28,   67,   67,
   67,   67,  117,   27,   32,   77,   55,   76,  218,  222,
   28,   57,   70,   71,   72,   73,   56,   28,   55,   55,
   55,   55,  223,   57,   57,   57,   57,  224,   56,   56,
   56,   56,   67,   15,    1,  143,   54,   75,   25,   74,
   82,   53,  138,    6,    7,   15,   26,   27,   54,   54,
   54,   54,   60,   53,   53,   53,   53,  226,   71,   73,
   73,   28,   73,  102,   73,  229,   70,   71,   72,   73,
   24,    1,    2,   72,   23,   59,   73,    4,    5,   19,
  112,   51,   24,  118,  156,  121,   75,   52,   74,   24,
  157,   19,   75,    0,   74,  207,    0,    0,   19,    0,
    0,    0,    0,    0,    0,    0,    0,  136,  137,  139,
    0,    0,    0,    0,  225,   24,  145,  147,    0,    0,
    0,    0,  122,    0,    0,    0,   60,    0,    0,    0,
    0,    0,   24,    0,    0,    0,    0,    0,   24,   24,
    0,  164,    0,  166,  103,  169,    0,    0,    0,    0,
  108,  109,    0,    0,    0,    0,    0,    0,    0,   24,
    0,    0,  177,   24,  179,  181,    0,    0,   19,    0,
    0,    0,   19,  184,    0,  186,    0,  188,    0,  191,
    0,    0,  194,    0,  196,    0,    0,  200,    0,    0,
  202,    0,  204,    0,  206,    0,  208,    0,  211,    0,
  213,  106,  215,    0,    0,   24,  219,  221,    0,    0,
   24,   24,   24,   70,   71,   72,   73,  155,    0,    0,
    0,    0,  161,  162,  163,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   70,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   59,   59,   45,   46,   40,   46,   46,   41,
   41,   59,   59,   45,    0,    0,   40,   46,   41,   41,
   62,  152,   46,   66,   67,   45,   46,   40,    0,   43,
   41,   45,   43,   46,   45,   59,   59,   45,   46,   59,
   40,   43,  264,   45,  260,   59,   46,  256,   44,   16,
  259,  267,  183,   45,   46,   40,  256,   59,    0,   59,
   43,   46,   45,   59,   59,  260,   40,  267,   40,  256,
   12,  258,   39,   59,   46,   59,   59,   74,   75,   59,
   44,   45,   46,   42,  267,   41,   42,   43,   47,   45,
  256,   47,   41,   42,   43,  258,   45,  261,   47,  265,
  266,   45,   46,   59,   60,  260,   62,   59,   59,   45,
   59,   60,  260,   62,   41,   42,   43,   45,   45,   41,
   47,   41,   42,   43,  267,   45,   62,   47,   41,  123,
   45,   59,   59,   60,  101,   62,   59,   45,  260,   59,
   60,   41,   62,   43,   59,   45,   41,   44,   43,  273,
   45,   41,   59,   43,   60,   45,   62,   59,   59,   59,
   60,  259,   62,  259,   59,   60,   59,   62,  125,   59,
   60,   41,   62,   43,   59,   45,   41,  260,   43,  273,
   45,   45,   46,   42,   43,  125,   45,   59,   47,   59,
   60,   43,   62,   45,   59,   60,   59,   62,   41,  256,
   59,   59,  259,   59,  260,   40,   59,   62,   60,  256,
   62,   46,  260,   59,  256,  256,  273,  256,  257,  260,
  261,  260,  260,  262,  263,  256,  265,  266,  260,  261,
  264,  260,  261,  257,  275,  274,  260,   59,  262,  263,
  260,  261,  264,  275,  257,  256,  275,  260,  272,  262,
  263,   62,  260,  261,  256,  275,  256,  257,   62,  272,
  260,  256,  262,  263,  256,  265,  266,  275,  260,  261,
  256,  256,  257,  259,  274,  260,  256,  262,  263,  259,
  265,  266,  256,  275,  256,  257,  260,  273,  260,  274,
  262,  263,   62,  265,  266,   62,  260,  261,   59,  256,
  256,   59,  274,  267,  256,  256,   41,  256,  265,  266,
   59,  275,  268,  269,  270,  271,  260,  261,   59,  268,
  269,  270,  271,  267,  260,  261,   79,   80,   59,  256,
   59,  275,  260,  261,   41,   41,  256,   59,   43,  275,
   45,  268,  269,  270,  271,  260,  261,  275,  268,  269,
  270,  271,  260,  261,    2,   60,  256,   62,   41,   41,
  275,  256,  268,  269,  270,  271,  256,  275,  268,  269,
  270,  271,   41,  268,  269,  270,  271,   41,  268,  269,
  270,  271,   30,    0,  256,   41,  256,   43,   59,   45,
   38,  256,  256,  265,  266,   12,  260,  261,  268,  269,
  270,  271,   19,  268,  269,  270,  271,   41,  267,   42,
   43,  275,   45,   61,   47,   41,  268,  269,  270,  271,
    0,    0,  257,  267,  125,  260,   59,  262,  263,    0,
   78,   12,   12,   86,   41,   88,   43,   12,   45,   19,
   41,   12,   43,   -1,   45,  198,   -1,   -1,   19,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  105,  106,  107,
   -1,   -1,   -1,   -1,  217,   45,  119,  120,   -1,   -1,
   -1,   -1,   89,   -1,   -1,   -1,   93,   -1,   -1,   -1,
   -1,   -1,   62,   -1,   -1,   -1,   -1,   -1,   68,   69,
   -1,  144,   -1,  146,   62,  148,   -1,   -1,   -1,   -1,
   68,   69,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   89,
   -1,   -1,  165,   93,  167,  168,   -1,   -1,   89,   -1,
   -1,   -1,   93,  176,   -1,  178,   -1,  180,   -1,  182,
   -1,   -1,  185,   -1,  187,   -1,   -1,  190,   -1,   -1,
  193,   -1,  195,   -1,  197,   -1,  199,   -1,  201,   -1,
  203,  256,  205,   -1,   -1,  135,  209,  210,   -1,   -1,
  140,  141,  142,  268,  269,  270,  271,  135,   -1,   -1,
   -1,   -1,  140,  141,  142,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  267,
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
"seleccion : IF condicion THEN bloque ELSE bloque ';'",
"seleccion : IF condicion error bloque ELSE bloque ';'",
"seleccion : IF condicion THEN bloque error bloque ';'",
"seleccion : IF error THEN bloque ELSE bloque ';'",
"condicion : '(' expresion comparador expresion ')'",
"condicion : expresion comparador expresion ')'",
"condicion : '(' error comparador expresion ')'",
"condicion : '(' expresion error expresion ')'",
"condicion : '(' expresion comparador error ')'",
"condicion : '(' expresion comparador expresion error",
"comparador : '>'",
"comparador : '<'",
"comparador : COMP_IGUAL",
"comparador : COMP_DISTINTO",
"comparador : COMP_MENOR_IGUAL",
"comparador : COMP_MAYOR_IGUAL",
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
"$$3 :",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for error $$3 factor ';' factor ')'",
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

//#line 225 "gramatica.txt"

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

// para manejo de errores semÃ¡nticos
private Vector<String> erroresSemanticos = new Vector<String>();

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

Vector<String> getErrores(){
	return this.errores;
}

Vector<String> getTokens(){
        return this.tokens;
}

Vector<String> getSalida(){
   Vector<String> retorno = new Vector<String>();
   retorno.add("Analizador lï¿½xico: ");
   retorno.addAll(this.tokens);
   retorno.add("Analizador sintï¿½ctico: ");
   retorno.addAll(this.salida);
   return retorno;
}

boolean fueraRangoIntPositivo(Entrada e){
    int val = (Integer) (e.getValor());
    return ((0 <= val) && (val > MAX_INTEGER-1));
}

void setearTipoElementosEstructuras(Entrada e) {
    elemento_estructuras.add(e.getNombre());
    e = anaLex.getTablaSimbolos().get(e.getNombre());
    e.setTipo_dato(this.tipo_dato);
    this.tipo_dato = null;
}

void estaDeclarada(Entrada e) {
    if (e.getTipo_dato()==null) {
        this.erroresSemanticos.add("La variable " + e.getNombre() + " no estÃ¡ declarada.");
    }
}
//#line 578 "Parser.java"
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
    //yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 20 "gramatica.txt"
{yyout("PROGRAMA.");}
break;
case 2:
//#line 21 "gramatica.txt"
{yyout("PROGRAMA.");}
break;
case 3:
//#line 22 "gramatica.txt"
{yyout("PROGRAMA.");}
break;
case 4:
//#line 26 "gramatica.txt"
{yyout("declaraciï¿½n.");}
break;
case 5:
//#line 27 "gramatica.txt"
{yyout("Bloque de declaraciones.");}
break;
case 6:
//#line 30 "gramatica.txt"
{yyout("Bloque de sentencias.");}
break;
case 7:
//#line 34 "gramatica.txt"
{
                                         for (int i=0;i<this.identificadores.size();i++) {
                                             this.identificadores.get(i).setTipo_dato(this.tipo_dato);
                                         }
                                         this.tipo_dato = null;
                                         this.identificadores.clear();
                                       }
break;
case 8:
//#line 41 "gramatica.txt"
{    /* Recupero la tabla de sï¿½mbolos*/
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
//#line 60 "gramatica.txt"
{ yyerror("falta nombre de variable en declaraciï¿½n.");}
break;
case 10:
//#line 61 "gramatica.txt"
{yyerror("falta nombre de la estructura en declaraciï¿½n.");}
break;
case 11:
//#line 62 "gramatica.txt"
{yyerror("declaraciï¿½n mï¿½ltiple en estructuras estï¿½ prohibida.");}
break;
case 12:
//#line 63 "gramatica.txt"
{ yyerror("declaraciï¿½n de variable sin tipo definido.");}
break;
case 13:
//#line 64 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 14:
//#line 65 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 15:
//#line 68 "gramatica.txt"
{this.tipo_dato=Tipo.INTEGER;}
break;
case 16:
//#line 69 "gramatica.txt"
{this.tipo_dato=Tipo.ULONGINT;}
break;
case 17:
//#line 70 "gramatica.txt"
{yyerror("tipo primitivo no admitido por el lenguaje.");}
break;
case 18:
//#line 73 "gramatica.txt"
{yyout("Sentencia de declaraciï¿½n de estructura"); elemento_estructuras = new Vector<String>();}
break;
case 20:
//#line 76 "gramatica.txt"
{
                                      setearTipoElementosEstructuras((Entrada)val_peek(1).obj);
                                }
break;
case 21:
//#line 79 "gramatica.txt"
{
                                      setearTipoElementosEstructuras((Entrada)val_peek(1).obj);
                                }
break;
case 23:
//#line 83 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 24:
//#line 84 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 25:
//#line 87 "gramatica.txt"
{this.identificadores.add(anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()));}
break;
case 26:
//#line 88 "gramatica.txt"
{this.identificadores.add(anaLex.getTablaSimbolos().get(((Entrada)val_peek(2).obj).getNombre()));}
break;
case 27:
//#line 91 "gramatica.txt"
{yyout("Una sola sentencia.");}
break;
case 28:
//#line 92 "gramatica.txt"
{yyout("Bloque de sentencias.");}
break;
case 29:
//#line 93 "gramatica.txt"
{ yyerror("se esperaba un 'begin' al inicio de un bloque de sentencias ejecutables.");}
break;
case 30:
//#line 94 "gramatica.txt"
{ yyerror("se esperaba un 'end' al final de un bloque de sentencias ejecutables.");}
break;
case 37:
//#line 108 "gramatica.txt"
{yyout("Condicional.");}
break;
case 38:
//#line 110 "gramatica.txt"
{ yyerror("se esperaba un 'then'.");}
break;
case 39:
//#line 111 "gramatica.txt"
{ yyerror("se esperaba un 'else'.");}
break;
case 40:
//#line 112 "gramatica.txt"
{ yyerror("la condiciï¿½n de selecciï¿½n no es vï¿½lida.");}
break;
case 42:
//#line 117 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condiciï¿½n.");}
break;
case 43:
//#line 118 "gramatica.txt"
{ yyerror("se esperaba una expresion antes del comparador.");}
break;
case 44:
//#line 119 "gramatica.txt"
{ yyerror("se esperaba un comparador vï¿½lido.");}
break;
case 45:
//#line 120 "gramatica.txt"
{ yyerror("se esperaba una expresion despuï¿½s del comparador");}
break;
case 46:
//#line 121 "gramatica.txt"
{ yyerror("se esperaba ')' despuï¿½s de la condiciï¿½n.");}
break;
case 53:
//#line 132 "gramatica.txt"
{yyout("Sentencia de suma.");}
break;
case 54:
//#line 133 "gramatica.txt"
{yyout("Sentencia de resta.");}
break;
case 56:
//#line 136 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '+'.");}
break;
case 57:
//#line 137 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '-'.");}
break;
case 58:
//#line 141 "gramatica.txt"
{
                                        Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(3).obj).getNombre());
                                        estaDeclarada(e);
                                    }
break;
case 59:
//#line 145 "gramatica.txt"
{yyout("Asignacion a elemento de estructura.");}
break;
case 60:
//#line 147 "gramatica.txt"
{ yyerror("se esperaba el operador de asignaciï¿½n ':='.");}
break;
case 61:
//#line 148 "gramatica.txt"
{ yyerror("se esperaba una expresiï¿½n del lado derecho de la asignaciï¿½n.");}
break;
case 62:
//#line 149 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 63:
//#line 152 "gramatica.txt"
{yyout("Sentencia de producto.");}
break;
case 64:
//#line 153 "gramatica.txt"
{yyout("Sentencia de divisiï¿½n.");}
break;
case 66:
//#line 156 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '*'.");}
break;
case 67:
//#line 157 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '/'.");}
break;
case 70:
//#line 164 "gramatica.txt"
{yyout("Elemento de estructura.");}
break;
case 71:
//#line 166 "gramatica.txt"
{ yyerror("se esperaba un punto.");}
break;
case 72:
//#line 167 "gramatica.txt"
{ yyerror("se esperaba identificador de la variable mientro de la estructura.");}
break;
case 73:
//#line 168 "gramatica.txt"
{ yyerror("se esperaba identificador de la estructura antes del punto.");}
break;
case 74:
//#line 171 "gramatica.txt"
{yyout("Bucle for.");}
break;
case 75:
//#line 173 "gramatica.txt"
{yyerror("se esperaba un bloque de sentencias, pero se encontro ';'.");}
break;
case 76:
//#line 174 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 77:
//#line 177 "gramatica.txt"
{
                                                                                            Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(9).obj).getNombre());
                                                                                            estaDeclarada(e);
                                                                                         }
break;
case 78:
//#line 182 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condiciï¿½n.");}
break;
case 79:
//#line 183 "gramatica.txt"
{ yyerror("se esperaba un identificador antes del operador de asignaciï¿½n.");}
break;
case 80:
//#line 184 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues del operador de asignaciï¿½n.");}
break;
case 81:
//#line 185 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante antes de '>'.");}
break;
case 82:
//#line 186 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues '>'.");}
break;
case 83:
//#line 187 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues de ';'.");}
break;
case 84:
//#line 189 "gramatica.txt"
{ yyerror("se esperaba operador de asignaciï¿½n.");}
break;
case 85:
//#line 190 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 86:
//#line 191 "gramatica.txt"
{ yyerror("se esperaba '>'.");}
break;
case 88:
//#line 192 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 89:
//#line 193 "gramatica.txt"
{ yyerror("se esperaba ')' despues de la condiciï¿½n.");}
break;
case 90:
//#line 194 "gramatica.txt"
{yyerror("error en la sentencia for.");}
break;
case 91:
//#line 198 "gramatica.txt"
{Entrada e = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
                 estaDeclarada(e);}
break;
case 93:
//#line 201 "gramatica.txt"
{ anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()).visitar(); if (fueraRangoIntPositivo((Entrada)val_peek(0).obj)) yyerror("entero fuera de rango [-32768;32767]");}
break;
case 94:
//#line 202 "gramatica.txt"
{Entrada actual = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
    				if (!(actual).isVisitado()) {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(), (Integer) actual.getValor() * -1);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
        			anaLex.getTablaSimbolos().removeEntrada(((Entrada)(val_peek(0).obj)).getNombre());
    				} else {
        			Entrada e = new Entrada("-" + actual.getNombre(), actual.getTipo(),(Integer)actual.getValor() * -1);
        			anaLex.getTablaSimbolos().addNuevaEntrada(e);
 				}
			    }
break;
case 95:
//#line 214 "gramatica.txt"
{yyout("Sentencia de impresiï¿½n.");}
break;
case 96:
//#line 216 "gramatica.txt"
{ yyerror("se esperaba 'print'");}
break;
case 97:
//#line 217 "gramatica.txt"
{ yyerror("se esperaba una cadena dentro de la instrucciï¿½n print.");}
break;
case 98:
//#line 218 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la cadena en sentencia print.");}
break;
case 99:
//#line 219 "gramatica.txt"
{ yyerror("sentencia print sin parentesis de cierre.");}
break;
case 100:
//#line 221 "gramatica.txt"
{ yyerror("se esperaba ';.'");}
break;
//#line 1086 "Parser.java"
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

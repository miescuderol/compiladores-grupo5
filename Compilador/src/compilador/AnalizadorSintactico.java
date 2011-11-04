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
//#line 26 "AnalizadorSintactico.java"




public class AnalizadorSintactico
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
//public class AnalizadorSintacticoVal is defined in AnalizadorSintacticoVal.java


String   yytext;//user variable to return contextual strings
AnalizadorSintacticoVal yyval; //used to return semantic vals from action routines
AnalizadorSintacticoVal yylval;//the 'lval' (result) I got from yylex()
AnalizadorSintacticoVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new AnalizadorSintacticoVal[YYSTACKSIZE];
  yyval=new AnalizadorSintacticoVal();
  yylval=new AnalizadorSintacticoVal();
  valptr=-1;
}
void val_push(AnalizadorSintacticoVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
AnalizadorSintacticoVal val_pop()
{
  if (valptr<0)
    return new AnalizadorSintacticoVal();
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
AnalizadorSintacticoVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new AnalizadorSintacticoVal();
  return valstk[ptr];
}
final AnalizadorSintacticoVal dup_yyval(AnalizadorSintacticoVal val)
{
  AnalizadorSintacticoVal dup = new AnalizadorSintacticoVal();
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
    9,    9,    9,    6,    6,   10,   10,   10,   10,    4,
    4,   11,   11,   11,   11,   12,   12,   12,   12,   16,
   16,   16,   16,   16,   16,   18,   18,   18,   18,   18,
   18,   17,   17,   17,   17,   17,   15,   15,   15,   15,
   15,   19,   19,   19,   19,   19,   21,   21,   20,   20,
   20,   20,   13,   13,   13,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   24,   23,   23,   23,   23,   22,
   22,   22,   22,   14,   14,   14,   14,   14,   14,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    3,    3,    2,    2,
    5,    2,    2,    2,    1,    1,    1,    0,    5,    3,
    4,    2,    3,    1,    3,    1,    3,    2,    2,    1,
    2,    1,    1,    1,    1,    7,    7,    7,    7,    5,
    4,    5,    5,    5,    5,    1,    1,    1,    1,    1,
    1,    3,    3,    1,    2,    2,    4,    4,    3,    3,
    4,    3,    3,    1,    2,    2,    1,    1,    3,    2,
    2,    2,    4,    3,    4,   11,   10,   10,   10,   10,
   10,   10,   11,   11,    0,   12,   11,   11,    1,    1,
    1,    1,    2,    5,    4,    4,    4,    4,    5,
};
final static short yydefred[] = {                         0,
   17,    0,    0,    0,    0,   16,   15,   18,    0,    0,
    0,    0,    3,    4,    6,    0,    0,    0,    0,   32,
   33,   34,   35,    0,    0,    0,   92,   91,    0,    0,
    0,    0,    0,   68,   64,   67,    0,    0,    0,    0,
    0,   89,    0,    0,    0,    0,    0,    0,    0,   72,
    2,    5,    0,    9,    0,   12,    0,   10,    0,   31,
    0,    0,   70,    0,   93,    0,    0,    0,    0,   48,
   50,   51,   49,    0,    0,   46,   47,    0,    0,    0,
   60,    0,   25,    0,   59,    0,    0,    0,    0,   74,
    0,    0,    0,    0,    0,    0,    0,    0,    7,    8,
    0,    0,    0,   69,    0,    0,    0,    0,    0,    0,
    0,    0,   62,   63,   61,   57,   90,    0,    0,    0,
    0,    0,   28,   75,   73,   97,   98,    0,   96,    0,
    0,   95,    0,   58,    0,    0,    0,    0,    0,    0,
    0,    0,   41,    0,    0,    0,    0,    0,   27,   99,
   94,    0,   19,   11,    0,   42,   43,   44,   45,   40,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   23,   39,   37,   38,   36,    0,    0,    0,    0,    0,
    0,    0,   21,    0,    0,    0,    0,    0,   85,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   77,    0,   79,    0,   80,    0,   81,    0,   82,    0,
   78,   83,   84,    0,   87,   88,   76,   86,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   91,  130,   17,   18,   48,  131,   92,
   93,   20,   21,   22,   23,   31,   41,   78,   33,   34,
   35,   36,   45,  197,
};
final static short yysindex[] = {                       -38,
    0,  -40,   37,   27,  -33,    0,    0,    0, -211, -194,
    0,  -38,    0,    0,    0,  -55,   11,  -47,  166,    0,
    0,    0,    0, -195, -182,  -37,    0,    0, -176,    9,
 -234,  149,   42,    0,    0,    0,  -37,  -19, -164, -162,
  -13,    0, -161, -215,  -23,   72,  -21,   -3,   84,    0,
    0,    0,  104,    0,   78,    0,    5,    0,   57,    0,
   -7,  -12,    0, -121,    0,   95,  296,  -12,  -12,    0,
    0,    0,    0,  -28,  -28,    0,    0,   -7,  -28,  -28,
    0,   -1,    0,    0,    0,   93, -199,   93,  166,    0,
 -123,  -46,  166,   94,  -22,   99, -165,  100,    0,    0,
 -164,   18,  -97,    0,   -7,   -7,  137,  -95, -208,   42,
   42,  266,    0,    0,    0,    0,    0,  108,   93,   73,
  110,  -98,    0,    0,    0,    0,    0,    6,    0,  -82,
   61,    0,  121,    0,  -12,  345,  411,  156,  -10,  -12,
  -12,  -12,    0,   93,  140,   93,   49,   93,    0,    0,
    0,  129,    0,    0,  143,    0,    0,    0,    0,    0,
  145,  148,  155,  146,   93,  176,   93,   65,  190, -165,
    0,    0,    0,    0,    0,   93,  197,   93,  231,   93,
  -41,   93,    0,  237,   93,  240,   93,  241,    0,   86,
  243,   93,  251,   93,  260,   93,   -7,   93,   50,   93,
  288,   93,  290,   93,  294,  277,  297,   93,  -31,  318,
    0,  319,    0,  332,    0,   -7,    0,  337,    0,  -30,
    0,    0,    0,  348,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  349,    0,    0,    0,    0,    0,    0,    0,
    0,  416,    0,    0,    0,    0,    0,    0,   15,    0,
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
    0,    0,    0,    0,    0,    0,    0,    0,    0,  305,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  420,  423,  384,   59,   34,    0,    0, -127,  382,
  422,    0,    0,    0,    0,    0,  353,   12,   55,  421,
  248,  351,    0,    0,
};
final static int YYTABLESIZE=635;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         30,
   24,    9,   26,   54,   29,   10,   47,   10,   64,  219,
  227,   58,  125,   29,   30,   13,    9,   10,  128,   96,
  190,   68,   10,   69,  171,   29,   10,    9,   14,   75,
  160,   74,   75,   10,   74,   90,  127,   29,   10,   81,
   24,   75,  183,   74,   87,   85,   24,  141,  101,   55,
  142,   88,   49,   29,   10,   13,  119,  116,   16,   24,
   75,   13,   74,  100,  151,   50,   44,  120,   14,   56,
   16,   61,   83,   30,   14,   62,  134,  105,  107,   29,
   39,   29,   40,   79,   65,   90,   90,   90,   80,   90,
    1,   90,   71,   71,   71,   53,   71,   84,   71,    6,
    7,   29,   40,   90,   90,   86,   90,  168,  209,   29,
   71,   71,   94,   71,   65,   65,   65,   29,   65,   97,
   65,   66,   66,   66,   98,   66,  180,   66,  110,  111,
   29,  146,   65,   65,  133,   65,   99,   29,  104,   66,
   66,   54,   66,   54,  198,   54,   56,   39,   56,  123,
   56,   55,  126,   55,   77,   55,   76,  129,  132,   54,
   54,  135,   54,  140,   56,   56,  144,   56,  148,   55,
   55,   53,   55,   53,  149,   53,   52,  152,   52,  154,
   52,   29,   10,   90,   90,  153,   90,  170,   90,   53,
   53,   75,   53,   74,   52,   52,  158,   52,  165,   26,
   90,  172,   26,  173,   53,    9,  174,  176,   77,  124,
   76,   10,   57,  175,  189,   25,   30,    1,    2,   26,
   27,    3,   63,    4,    5,  226,    6,    7,  117,   27,
   46,   26,   27,    2,   28,    8,   59,  178,    4,    5,
   26,   27,   95,   28,    2,  159,   28,   59,   89,    4,
    5,  182,   26,   27,  115,   28,   24,   24,  185,   89,
   24,  150,   24,   24,   66,   24,   24,   28,   26,   27,
   30,   13,   13,   30,   24,   13,   29,   13,   13,   29,
   13,   13,   42,   28,   14,   14,   43,   30,   14,   13,
   14,   14,  187,   14,   14,  192,   37,   27,  194,  196,
   90,  200,   14,   38,  167,  208,  143,   71,   75,  202,
   74,   28,   90,   90,   90,   90,   37,   27,  204,   71,
   71,   71,   71,   38,  117,   27,  113,  114,  211,   65,
  213,   28,  117,   27,  215,  216,   66,  217,   75,   28,
   74,   65,   65,   65,   65,  117,   27,   28,   66,   66,
   66,   66,  117,   27,   32,   77,   54,   76,  221,  222,
   28,   56,   70,   71,   72,   73,   55,   28,   54,   54,
   54,   54,  223,   56,   56,   56,   56,  225,   55,   55,
   55,   55,   67,   15,    1,  156,   53,   75,  228,   74,
   82,   52,  138,    6,    7,   15,   26,   27,   53,   53,
   53,   53,   60,   52,   52,   52,   52,   24,   70,   72,
   72,   28,   72,  102,   72,    1,   70,   71,   72,   73,
   24,   19,    2,   71,   22,   59,   72,    4,    5,   20,
  112,   51,   24,   19,   52,    0,  118,    0,  121,   24,
   19,    0,    0,  103,  206,    0,    0,    0,    0,  108,
  109,  157,    0,   75,    0,   74,    0,  136,  137,  139,
    0,    0,    0,  224,    0,   24,    0,    0,    0,  145,
  147,    0,  122,    0,    0,    0,   60,    0,    0,    0,
    0,    0,   24,    0,    0,    0,    0,    0,   24,   24,
    0,    0,    0,    0,  164,    0,  166,    0,  169,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   24,
   19,    0,    0,   24,   19,  177,  155,  179,  181,    0,
    0,  161,  162,  163,    0,    0,  184,    0,  186,    0,
  188,    0,  191,    0,    0,  193,    0,  195,    0,    0,
  199,    0,  201,    0,  203,    0,  205,    0,  207,    0,
  210,  106,  212,    0,  214,   24,    0,    0,  218,  220,
   24,   24,   24,   70,   71,   72,   73,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   69,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   59,   59,   45,   46,   40,   46,   46,   41,
   41,   59,   59,   45,    0,    0,   40,   46,   41,   41,
   62,  256,   46,  258,  152,   45,   46,   40,    0,   43,
   41,   45,   43,   46,   45,   59,   59,   45,   46,   59,
   40,   43,  170,   45,  260,   59,   46,  256,   44,   16,
  259,  267,  264,   45,   46,   40,  256,   59,    0,   59,
   43,   46,   45,   59,   59,  260,   40,  267,   40,   59,
   12,  267,   39,   59,   46,  258,   59,   66,   67,   59,
   44,   45,   46,   42,  261,   41,   42,   43,   47,   45,
  256,   47,   41,   42,   43,  260,   45,  260,   47,  265,
  266,   45,   46,   59,   60,  267,   62,   59,   59,   45,
   59,   60,   41,   62,   41,   42,   43,   45,   45,  123,
   47,   41,   42,   43,   41,   45,   62,   47,   74,   75,
   45,   59,   59,   60,  101,   62,   59,   45,  260,   59,
   60,   41,   62,   43,   59,   45,   41,   44,   43,  273,
   45,   41,   59,   43,   60,   45,   62,   59,   59,   59,
   60,  259,   62,  259,   59,   60,   59,   62,   59,   59,
   60,   41,   62,   43,  273,   45,   41,  260,   43,   59,
   45,   45,   46,   42,   43,  125,   45,   59,   47,   59,
   60,   43,   62,   45,   59,   60,   41,   62,   59,  256,
   59,   59,  259,   59,  260,   40,   59,   62,   60,  256,
   62,   46,  260,   59,  256,  256,  273,  256,  257,  260,
  261,  260,  260,  262,  263,  256,  265,  266,  260,  261,
  264,  260,  261,  257,  275,  274,  260,   62,  262,  263,
  260,  261,  264,  275,  257,  256,  275,  260,  272,  262,
  263,   62,  260,  261,  256,  275,  256,  257,   62,  272,
  260,  256,  262,  263,  256,  265,  266,  275,  260,  261,
  256,  256,  257,  259,  274,  260,  256,  262,  263,  259,
  265,  266,  256,  275,  256,  257,  260,  273,  260,  274,
  262,  263,   62,  265,  266,   59,  260,  261,   59,   59,
  256,   59,  274,  267,  256,  256,   41,  256,   43,   59,
   45,  275,  268,  269,  270,  271,  260,  261,   59,  268,
  269,  270,  271,  267,  260,  261,   79,   80,   41,  256,
   41,  275,  260,  261,   41,   59,  256,   41,   43,  275,
   45,  268,  269,  270,  271,  260,  261,  275,  268,  269,
  270,  271,  260,  261,    2,   60,  256,   62,   41,   41,
  275,  256,  268,  269,  270,  271,  256,  275,  268,  269,
  270,  271,   41,  268,  269,  270,  271,   41,  268,  269,
  270,  271,   30,    0,  256,   41,  256,   43,   41,   45,
   38,  256,  256,  265,  266,   12,  260,  261,  268,  269,
  270,  271,   19,  268,  269,  270,  271,   59,  267,   42,
   43,  275,   45,   61,   47,    0,  268,  269,  270,  271,
    0,    0,  257,  267,  125,  260,   59,  262,  263,  125,
   78,   12,   12,   12,   12,   -1,   86,   -1,   88,   19,
   19,   -1,   -1,   62,  197,   -1,   -1,   -1,   -1,   68,
   69,   41,   -1,   43,   -1,   45,   -1,  105,  106,  107,
   -1,   -1,   -1,  216,   -1,   45,   -1,   -1,   -1,  119,
  120,   -1,   89,   -1,   -1,   -1,   93,   -1,   -1,   -1,
   -1,   -1,   62,   -1,   -1,   -1,   -1,   -1,   68,   69,
   -1,   -1,   -1,   -1,  144,   -1,  146,   -1,  148,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   89,
   89,   -1,   -1,   93,   93,  165,  135,  167,  168,   -1,
   -1,  140,  141,  142,   -1,   -1,  176,   -1,  178,   -1,
  180,   -1,  182,   -1,   -1,  185,   -1,  187,   -1,   -1,
  190,   -1,  192,   -1,  194,   -1,  196,   -1,  198,   -1,
  200,  256,  202,   -1,  204,  135,   -1,   -1,  208,  209,
  140,  141,  142,  268,  269,  270,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
"cuerpo_estructura : tipo ID ';' cuerpo_estructura",
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
"$$2 :",
"condicion_for : '(' ID ASIGN factor_for ';' factor_for error $$2 factor ';' factor ')'",
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

//#line 180 "gramatica.txt"

private AnalizadorLexico anaLex;
private Vector<String> errores;
private Vector<String> tokens;
private Vector<String> salida;
private Vector<String> elemento_estructuras;
private String error_anterior;
private int linea_error_anterior;

public static final int MAX_INTEGER_NEG = 32767;
public static final int MAX_INTEGER = 32768;
public static final float MAX_ULONGINTEGER = 4294967295f;

public AnalizadorSintactico(AnalizadorLexico analLex){
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
   retorno.add("Analizador léxico: ");
   retorno.addAll(this.tokens);
   retorno.add("Analizador sintáctico: ");
   retorno.addAll(this.salida);
   return retorno;
}

boolean fueraRangoIntPositivo(Entrada e){
    int val = (Integer) (e.getValor());
    return ((0 <= val) && (val > MAX_INTEGER-1));
}
//#line 557 "AnalizadorSintactico.java"
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
//    yyval = dup_yyval(yyval); //duplicate yyval if AnalizadorSintacticoVal is used as semantic value
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
{yyout("declaración.");}
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
{yyout("Sentencia de declaración de variables.");}
break;
case 8:
//#line 35 "gramatica.txt"
{//anaLex.getTablaSimbolos().get(((Entrada)val_peek(1).obj).getNombre()).setValor(elemento_estructuras);}
    // Recupero la tabla de símbolos
    TablaSimbolos t = anaLex.getTablaSimbolos(); 
    // Recupero la entrada de la estructura padre
    Entrada padre = t.get(((Entrada)val_peek(1).obj).getNombre());
    int distancia_acumulada = 0;
    for (int i=0;i<elemento_estructuras.size();i++) {
        Entrada e = t.get(elemento_estructuras.get(i));
        t.removeEntrada(e.getNombre());
        Entrada eNueva = new EntradaEstructura(e.getNombre(), e.getTipo(), e.getValor(),padre,distancia_acumulada);
        t.addNuevaEntrada(eNueva);
        // Actualizo la distancia
        if (e.getTipo_dato()==Tipo.INTEGER) 
            distancia_acumulada = distancia_acumulada + 2;
        else if (e.getTipo_dato()==Tipo.ULONGINT)
            distancia_acumulada = distancia_acumulada + 4;
    }
}
break;
case 9:
//#line 36 "gramatica.txt"
{ yyerror("falta nombre de variable en declaración.");}
break;
case 10:
//#line 37 "gramatica.txt"
{yyerror("falta nombre de la estructura en declaración.");}
break;
case 11:
//#line 38 "gramatica.txt"
{yyerror("declaración múltiple en estructuras está prohibida.");}
break;
case 12:
//#line 39 "gramatica.txt"
{ yyerror("declaración de variable sin tipo definido.");}
break;
case 13:
//#line 40 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 14:
//#line 41 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 17:
//#line 46 "gramatica.txt"
{yyerror("tipo primitivo no admitido por el lenguaje.");}
break;
case 18:
//#line 49 "gramatica.txt"
{yyout("Sentencia de declaración de estructura"); elemento_estructuras = new Vector<String>();}
break;
case 20:
//#line 52 "gramatica.txt"
{elemento_estructuras.add(((Entrada)val_peek(1).obj).getNombre());}
break;
case 21:
//#line 53 "gramatica.txt"
{elemento_estructuras.add(((Entrada)val_peek(2).obj).getNombre());}
break;
case 22:
//#line 54 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 23:
//#line 55 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 26:
//#line 62 "gramatica.txt"
{yyout("Una sola sentencia.");}
break;
case 27:
//#line 63 "gramatica.txt"
{yyout("Bloque de sentencias.");}
break;
case 28:
//#line 64 "gramatica.txt"
{ yyerror("se esperaba un 'begin' al inicio de un bloque de sentencias ejecutables.");}
break;
case 29:
//#line 65 "gramatica.txt"
{ yyerror("se esperaba un 'end' al final de un bloque de sentencias ejecutables.");}
break;
case 36:
//#line 79 "gramatica.txt"
{yyout("Condicional.");}
break;
case 37:
//#line 81 "gramatica.txt"
{ yyerror("se esperaba un 'then'.");}
break;
case 38:
//#line 82 "gramatica.txt"
{ yyerror("se esperaba un 'else'.");}
break;
case 39:
//#line 83 "gramatica.txt"
{ yyerror("la condición de selección no es válida.");}
break;
case 41:
//#line 88 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condición.");}
break;
case 42:
//#line 89 "gramatica.txt"
{ yyerror("se esperaba una expresion antes del comparador.");}
break;
case 43:
//#line 90 "gramatica.txt"
{ yyerror("se esperaba un comparador válido.");}
break;
case 44:
//#line 91 "gramatica.txt"
{ yyerror("se esperaba una expresion después del comparador");}
break;
case 45:
//#line 92 "gramatica.txt"
{ yyerror("se esperaba ')' después de la condición.");}
break;
case 52:
//#line 103 "gramatica.txt"
{yyout("Sentencia de suma.");}
break;
case 53:
//#line 104 "gramatica.txt"
{yyout("Sentencia de resta.");}
break;
case 55:
//#line 107 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '+'.");}
break;
case 56:
//#line 108 "gramatica.txt"
{ yyerror("se esperaba un termino luego del '-'.");}
break;
case 57:
//#line 112 "gramatica.txt"
{yyout("ASIGNACION."); }
break;
case 58:
//#line 113 "gramatica.txt"
{yyout("Asignacion a elemento de estructura.");}
break;
case 59:
//#line 115 "gramatica.txt"
{ yyerror("se esperaba el operador de asignación ':='.");}
break;
case 60:
//#line 116 "gramatica.txt"
{ yyerror("se esperaba una expresión del lado derecho de la asignación.");}
break;
case 61:
//#line 117 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 62:
//#line 120 "gramatica.txt"
{yyout("Sentencia de producto.");}
break;
case 63:
//#line 121 "gramatica.txt"
{yyout("Sentencia de división.");}
break;
case 65:
//#line 124 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '*'.");}
break;
case 66:
//#line 125 "gramatica.txt"
{ yyerror("se esperaba un factor luego de '/'.");}
break;
case 69:
//#line 132 "gramatica.txt"
{yyout("Elemento de estructura.");}
break;
case 70:
//#line 134 "gramatica.txt"
{ yyerror("se esperaba un punto.");}
break;
case 71:
//#line 135 "gramatica.txt"
{ yyerror("se esperaba identificador de la variable mientro de la estructura.");}
break;
case 72:
//#line 136 "gramatica.txt"
{ yyerror("se esperaba identificador de la estructura antes del punto.");}
break;
case 73:
//#line 139 "gramatica.txt"
{yyout("Bucle for.");}
break;
case 74:
//#line 141 "gramatica.txt"
{yyerror("se esperaba un bloque de sentencias, pero se encontro ';'.");}
break;
case 75:
//#line 142 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 77:
//#line 147 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la condición.");}
break;
case 78:
//#line 148 "gramatica.txt"
{ yyerror("se esperaba un identificador antes del operador de asignación.");}
break;
case 79:
//#line 149 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues del operador de asignación.");}
break;
case 80:
//#line 150 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante antes de '>'.");}
break;
case 81:
//#line 151 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues '>'.");}
break;
case 82:
//#line 152 "gramatica.txt"
{ yyerror("se esperaba un identificador o constante despues de ';'.");}
break;
case 83:
//#line 154 "gramatica.txt"
{ yyerror("se esperaba operador de asignación.");}
break;
case 84:
//#line 155 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 85:
//#line 156 "gramatica.txt"
{ yyerror("se esperaba '>'.");}
break;
case 87:
//#line 157 "gramatica.txt"
{ yyerror("se esperaba ';'.");}
break;
case 88:
//#line 158 "gramatica.txt"
{ yyerror("se esperaba ')' despues de la condición.");}
break;
case 89:
//#line 159 "gramatica.txt"
{yyerror("error en la sentencia for.");}
break;
case 92:
//#line 165 "gramatica.txt"
{ anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre()).visitar(); if (fueraRangoIntPositivo((Entrada)val_peek(0).obj)) yyerror("entero fuera de rango [-32768;32767]");}
break;
case 93:
//#line 166 "gramatica.txt"
{ 
    Entrada actual = anaLex.getTablaSimbolos().get(((Entrada)val_peek(0).obj).getNombre());
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
case 94:
//#line 169 "gramatica.txt"
{yyout("Sentencia de impresión.");}
break;
case 95:
//#line 171 "gramatica.txt"
{ yyerror("se esperaba 'print'");}
break;
case 96:
//#line 172 "gramatica.txt"
{ yyerror("se esperaba una cadena dentro de la instrucción print.");}
break;
case 97:
//#line 173 "gramatica.txt"
{ yyerror("se esperaba '(' antes de la cadena en sentencia print.");}
break;
case 98:
//#line 174 "gramatica.txt"
{ yyerror("sentencia print sin parentesis de cierre.");}
break;
case 99:
//#line 176 "gramatica.txt"
{ yyerror("se esperaba ';.'");}
break;
//#line 998 "AnalizadorSintactico.java"
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
 * A default run method, used for operating this AnalizadorSintactico
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
public AnalizadorSintactico()
{
  //nothing to do
}


/**
 * Create a AnalizadorSintactico, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public AnalizadorSintactico(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################

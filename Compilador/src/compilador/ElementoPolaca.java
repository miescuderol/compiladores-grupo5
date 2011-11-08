/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Nacha
 */
public class ElementoPolaca {
    //operadores unarios:
    public static final int PRINT=0;
    public static final int CONVERSION=5;

    //operadores binarios:
    public static final int SUMA=1;
    public static final int RESTA=2;
    public static final int MULTIPLICACION=3;
    public static final int DIVISION=4;
    public static  final int ASIGNACION=21;

    //operandos:
    public static final int VAR_INT=6;
    public static final int VAR_ULONG=7;
    public static final int CONS_INT=8;
    public static final int CONS_ULONG=9;
    public static final int CADENA=10;

    //bifurcaciones:
    public static final int BF=11;
    public static final int BI=12;
    public static final int BV=13;
    public static final int ROTULO=14;

    //condiciones:
    public static  final int MAYOR=15;
    public static  final int MENOR=16;
    public static  final int MAYORIGUAL=17;
    public static  final int MENORIGUAL=18;
    public static  final int DISTINTO=19;
    public static  final int IGUAL=20;


    public int tipo;
    public String nombre;
    
    public ElementoPolaca(int t, String n) {
        tipo=t;
        nombre=n;
    }
    public ElementoPolaca(int t) {
        tipo=t;
        nombre=null;
    }
}

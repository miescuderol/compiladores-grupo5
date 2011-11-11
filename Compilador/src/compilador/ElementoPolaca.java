/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Nacha
 */
class ElementoPolaca {
    //operadores unarios:
    public static final int PRINT=0;
    public static final int CONVERSION=5;

    //operadores binarios:
    public static final int SUMA=1;
    public static final int RESTA=2;
    public static final int MULTIPLICACION=3;
    public static final int DIVISION=4;
    public static  final int ASIGNACION=20;

    //operandos:
    public static final int VAR_INT=6;
    public static final int VAR_ULONG=7;
    public static final int CONS_INT=8;
    public static final int CONS_ULONG=9;
    public static final int CADENA=10;

    //bifurcaciones:
    public static final int BF=11;
    public static final int BI=12;
    public static final int ROTULO=13;

    //condiciones:
    public static  final int MAYOR=14;
    public static  final int MENOR=15;
    public static  final int MAYORIGUAL=16;
    public static  final int MENORIGUAL=17;
    public static  final int DISTINTO=18;
    public static  final int IGUAL=19;

    private static final String[] scodigos=new String[]{"PRINT","SUMA","RESTA","MULTIPLICACION","DIVISION","CONVERSION TOULONG",
                                                        "variable int: ","variable ulong: ",
                                                        "constante int: ","constante ulong: ","cadena: ",
                                                        "bifurcacion por falso a direccion ",
                                                        "bifurcacion incondicional a direccion ",
                                                        "ROTULO ","MAYOR","MENOR", "MAYORIGUAL","MENORIGUAL","DISTINTO","IGUAL","ASIGNACION"};

    private int tipo;
    private String nombre;

    public ElementoPolaca(int t, String n) {
        tipo=t;
        nombre=n;
    }
    public ElementoPolaca(int t) {
        tipo=t;
        nombre=null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString(){
        if(nombre==null)
                return scodigos[tipo];
        else
            return scodigos[tipo]+nombre;
    }
    
}

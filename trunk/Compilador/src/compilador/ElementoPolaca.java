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

    //operadores binarios:
    public static final int SUMA=1;
    public static final int RESTA=2;
    public static final int MULTIPLICACION=3;
    public static final int DIVISION=4;
    public static  final int ASIGNACION=19;

    //operandos:
    public static final int VAR_INT=5;
    public static final int VAR_ULONG=6;
    public static final int CONS_INT=7;
    public static final int CONS_ULONG=8;
    public static final int CADENA=9;

    //bifurcaciones:
    public static final int BF=10;
    public static final int BI=11;
    public static final int ROTULO=12;

    //condiciones:
    public static  final int MAYOR=13;
    public static  final int MENOR=14;
    public static  final int MAYORIGUAL=15;
    public static  final int MENORIGUAL=16;
    public static  final int DISTINTO=17;
    public static  final int IGUAL=18;

    private static final String[] scodigos=new String[]{"PRINT","SUMA","RESTA","MULTIPLICACION","DIVISION",
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

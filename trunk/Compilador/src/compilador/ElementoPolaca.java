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

    //operadores binarios:
    public static final int SUMA=1;
    public static final int RESTA=2;
    public static final int MULTIPLICACION=3;
    public static final int DIVISION=4;
    public static  final int ASIGNACION=5;

    //operandos:
    public static final int VARIABLE=6;
    public static final int CONSTANTE=7;
    public static final int CADENA=8;

    //bifurcaciones:
    public static final int BF=9;
    public static final int BI=10;
    public static final int ROTULO=11;

    //condiciones:
    public static  final int MAYOR=12;
    public static  final int MENOR=13;
    public static  final int MAYORIGUAL=14;
    public static  final int MENORIGUAL=15;
    public static  final int DISTINTO=16;
    public static  final int IGUAL=17;
    
    public static  final int REGISTRO = 18;

    private static final String[] scodigos=new String[]{"PRINT","SUMA","RESTA","MULTIPLICACION","DIVISION","ASIGNACION",
                                                        "variable: ","constante: ","cadena: ",
                                                        "bifurcacion por falso a direccion ",
                                                        "bifurcacion incondicional a direccion ",
                                                        "ROTULO ","MAYOR","MENOR", "MAYORIGUAL","MENORIGUAL","DISTINTO","IGUAL"};

    private int tipo;
    private Tipo tipo_dato;
    private String nombre;

    public ElementoPolaca(int t, Tipo td, String n) {
        tipo=t;
        tipo_dato = td;
        nombre=n;
    }
    
    public ElementoPolaca(int t, String n){
        tipo = t;
        tipo_dato = null;
        nombre = n;
    }
    
    public ElementoPolaca(int t) {
        tipo=t;
        tipo_dato = null;
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
    
    public Tipo getTipo_dato() {
        return tipo_dato;
    }
    
    public void setTipo_dato(Tipo tipo_dato) {
        this.tipo_dato = tipo_dato;
    }
    
    @Override
    public String toString(){
        if(nombre==null)
                return scodigos[tipo];
        else if(tipo_dato == null)
            return scodigos[tipo]+nombre;
        else
            return scodigos[tipo]+tipo_dato.toString()+" "+nombre;
    }
    
}

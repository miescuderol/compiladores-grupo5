/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author José Ignacio Orlando
 */
public class TablaSimbolos implements Iterable<String> {
    
    private Hashtable<String,Integer> palabrasReservadas;
    private Hashtable<String,Entrada> tablaIdConst;
    
    public final static int IF=257;
    public final static int THEN=258;
    public final static int ELSE=259;
    public final static int ID=260;
    public final static int CTE_INT=261;
    public final static int FOR=262;
    public final static int PRINT=263;
    public final static int CADENA=264;
    public final static int ULONGINT=265;
    public final static int INTEGER=266;
    public final static int ASIGN=267;
    public final static int COMP_IGUAL=268;
    public final static int COMP_MENOR_IGUAL=269;
    public final static int COMP_MAYOR_IGUAL=270;
    public final static int COMP_DISTINTO=271;
    public final static int BEGIN=272;
    public final static int END=273;
    public final static int STRUCT=274;
    public final static int CTE_ULON=275;
    public final static int ERROR=276;
    
    public TablaSimbolos() {
        // Inicializamos la lista de palabras reservadas
        this.palabrasReservadas = new Hashtable<String,Integer>();
        this.palabrasReservadas.put("if",TablaSimbolos.IF);
        this.palabrasReservadas.put("then",TablaSimbolos.THEN);
        this.palabrasReservadas.put("else",TablaSimbolos.ELSE);
        this.palabrasReservadas.put("begin",TablaSimbolos.BEGIN);
        this.palabrasReservadas.put("end",TablaSimbolos.END);
        this.palabrasReservadas.put("print",TablaSimbolos.PRINT);
        this.palabrasReservadas.put("struct",TablaSimbolos.STRUCT);
        this.palabrasReservadas.put("for",TablaSimbolos.FOR);
        this.palabrasReservadas.put("integer",TablaSimbolos.INTEGER);
        this.palabrasReservadas.put("ulongint",TablaSimbolos.ULONGINT);
        // Inicializamos la tabla de identificadores y de constantes
        this.tablaIdConst = new Hashtable<String,Entrada>();
    }
    
    public boolean isPalabraReservada(String palabra) {
        return (this.palabrasReservadas.get(palabra)!=null);
    }
    
    public int getCodigoYacc(String palabra) {
        if (this.isPalabraReservada(palabra)) {
            return this.palabrasReservadas.get(palabra);
        } else {
            Entrada e = this.tablaIdConst.get(palabra);
            if (e!=null) { // chequeamos si es un elemento que esta en tabla de simbolos
                Tipo t = e.getTipo();
                if (t==Tipo.CADENA) {
                    return TablaSimbolos.CADENA;
                } else if (t==Tipo.CONSTANTE_INTEGER){
                        return TablaSimbolos.CTE_INT;
                    }else if (t==Tipo.CONSTANTE_ULONGINT){
                            return TablaSimbolos.CTE_ULON;
                        } else if (t==Tipo.ID) {
                                return TablaSimbolos.ID;
                          }
            } else { // chequeamos si es alguno de los operadores de comparación
                if (palabra.equals("==")) {
                    return TablaSimbolos.COMP_IGUAL;
                } else if (palabra.equals("!=")) {
                    return TablaSimbolos.COMP_DISTINTO;
                } else if (palabra.equals(">=")) {
                    return TablaSimbolos.COMP_MAYOR_IGUAL;
                } else if (palabra.equals("<=")) {
                    return TablaSimbolos.COMP_MENOR_IGUAL;
                } else if (palabra.equals(":=")) {
                    return TablaSimbolos.ASIGN;
                }
            }
        }
        return -1;
    }
    
    public Entrada get(String nombre) {
        return (this.tablaIdConst.get(nombre));
    }
    
    @Override
    public String toString() {
        Enumeration e = this.tablaIdConst.keys();
        String retorno = "";
        while (e.hasMoreElements()) {
            String clave = (String) (e.nextElement());
            Entrada entrada = this.tablaIdConst.get(clave);
            String imprimible = "Nombre: " + clave + " - " + entrada.toString() + "\n";
            retorno = retorno + imprimible;
        }
        return retorno;
    }
    
    public void addNuevaEntrada(Entrada e) {
        this.tablaIdConst.put(e.getNombre(), e);
    }
    
    public void removeEntrada(String nombre) {
        this.tablaIdConst.remove(nombre);
    }

    public String getPalabrasReservadas() {
        String ret = "";
        Enumeration e = this.palabrasReservadas.keys();
        while (e.hasMoreElements()) {
            ret = ret + ((String) (e.nextElement())) + "\n";
        }
        return ret;
    }

    @Override
    public IteradorTablaSimbolos iterator() {
        return new IteradorTablaSimbolos(this.tablaIdConst);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author José Ignacio Orlando
 */
public class Token {
    
    private String nombre;
    private int codigoYacc;
    
    public Token(String t, int codigo) {
        this.nombre = t;
        this.codigoYacc = codigo;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    @Override
    public String toString() {
        return "Token " + nombre + codigoYacc;
    }
    
    public AnalizadorSintacticoVal getAtributoYacc() {
        return null;
    }
    
    public int getCodigoYacc() {
        return this.codigoYacc;
    }
    
}

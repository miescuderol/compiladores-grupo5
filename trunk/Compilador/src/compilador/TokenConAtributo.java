/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;


/**
 *
 * @author José Ignacio Orlando
 */
public class TokenConAtributo extends Token {
    
    private Entrada entradaTablaSimbolos;
    
    public TokenConAtributo(String nombre, int codigo, Entrada e) {
        super(nombre,codigo);
        this.entradaTablaSimbolos = e;
    }
    
    public Entrada getEntradaTabla() {
        return this.entradaTablaSimbolos;
    }
    
    
    @Override
    public ParserVal getAtributoYacc() {
        return new ParserVal(this.entradaTablaSimbolos);
    }
    
    @Override
    public String toString(){
        return super.toString() + " " + this.entradaTablaSimbolos.toString();
    }
    
}

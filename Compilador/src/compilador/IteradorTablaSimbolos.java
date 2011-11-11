/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author José Ignacio Orlando
 */
public class IteradorTablaSimbolos implements Iterator<String> {

    private Iterator<String> it;
    private Hashtable<String,Entrada> tabla;
    
    public IteradorTablaSimbolos(Hashtable<String,Entrada> t) {
        this.it = t.keySet().iterator();
        this.tabla = t;
    }
    
    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    public Entrada nextEntrada() {
        String clave = it.next();
        return this.tabla.get(clave);
    }
    
    @Override
    public void remove() {
        this.it.remove();
    }

    @Override
    public String next() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

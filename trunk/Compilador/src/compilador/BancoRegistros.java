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
public class BancoRegistros {
    
    private Hashtable<String,Registro> registros;
    
    public BancoRegistros() {
        this.registros = new Hashtable<String,Registro>();
        this.registros.put("EAX", new Registro("EAX"));
        this.registros.put("EBX", new Registro("EBX"));
        this.registros.put("ECX", new Registro("ECX"));
        this.registros.put("EDX", new Registro("EDX"));
    }
    
    public Registro ocuparRegistroLibre() {
        Enumeration e = this.registros.keys();
        while (e.hasMoreElements()){
            Registro r = (Registro) (e.nextElement());
            if (!(r.getNombre().equals("EAX")) && !(r.isOcupado())) {
                r.ocupar();
                return r;
            }
        }
        return null;
    }
    
    public Registro getRegistro(String nombre) {
        return this.registros.get(nombre);
    }
    
    public void desocuparRegistro(String nombre) {
        this.registros.get(nombre).desocupar();
    }
    
    public boolean ocuparRegistro(String nombre) {
        if (!this.registros.get(nombre).isOcupado()) {
            this.registros.get(nombre).ocupar();
            return true;
        }
        return false;
    }
    
}

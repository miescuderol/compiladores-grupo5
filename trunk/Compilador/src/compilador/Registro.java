/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;


/**
 *
 * @author José Ignacio Orlando
 */
public class Registro {
    
    private boolean ocupado;
    private String nombre;
    
    public Registro(String nombre) {
        this.nombre = nombre;
        this.ocupado = false;
    }
    
    public void ocupar() {
        this.ocupado = true;
    }
    
    public void desocupar() {
        this.ocupado = false;
    }
    
    public boolean isOcupado() {
        return this.ocupado;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    
}

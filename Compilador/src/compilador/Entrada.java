/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author José Ignacio Orlando
 */
public class Entrada {
    
    protected String nombre;
    protected Tipo tipo_token;
    protected Object valor;
    protected boolean visitado;
    protected boolean isElementoEstructura;
    protected Tipo tipo_dato;
    protected int tamanio;
    
    public Entrada(String nom, Tipo t, Object v) {
        this.nombre = nom;
        this.tipo_token = t;
        this.valor = v;
        this.visitado = false;
        this.isElementoEstructura = false;
    }
    
    public Entrada(Entrada e){
        this.isElementoEstructura = e.isElementoEstructura();
        this.nombre = e.getNombre();
        this.tipo_dato = e.getTipo_dato();
        this.tipo_token = e.getTipo();
        this.valor = e.getValor();
        this.visitado = e.isVisitado();
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public Tipo getTipo() {
        return this.tipo_token;
    }
    
    public Object getValor() {
        return this.valor;
    }
    
    public void setValor(Object v){
        this.valor = v;
    }

    public boolean isElementoEstructura() {
        return isElementoEstructura;
    }

    public void setTipo_dato(Tipo tipo_dato) {
        this.tipo_dato = tipo_dato;
    }

    public Tipo getTipo_dato() {
        return tipo_dato;
    }
    
    @Override
    public String toString() {
        String retorno = "";
        if (this.tipo_token!=null) {
            retorno = retorno + "Tipo de token: " + tipo_token.toString();
        }
        retorno = retorno + " - Tipo de dato: " + tipo_dato;
        if (this.valor!=null) {
            retorno = retorno + " - Valor: " + valor.toString();
        }
        return retorno;
    }
    
    public void visitar() {
        this.visitado = true;
    }
    
    public boolean isVisitado() {
        return this.visitado;
    }
    
    public void setTamanio(int t) {
        this.tamanio = t;
    }
    
    public int getTamanio() {
        return this.tamanio;
    }
    
}

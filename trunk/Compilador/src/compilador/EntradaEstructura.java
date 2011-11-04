/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author José Ignacio Orlando
 */
public class EntradaEstructura extends Entrada {
    
    private Entrada estructura;
    private int bytesDesplazamiento;
    
    public EntradaEstructura(String nom, Tipo t, Object v, Entrada estructuraPadre, int despl) {
        super(nom,t,v);
        super.isElementoEstructura = true;
        this.estructura = estructuraPadre;
        this.bytesDesplazamiento = despl;
    }

    public int getBytesDesplazamiento() {
        return bytesDesplazamiento;
    }

    public Entrada getEstructura() {
        return estructura;
    }
    
    @Override
    public String toString() {
        String aRetornar = super.toString();
        if (estructura!=null) {
            aRetornar = aRetornar + " - Padre: " + estructura.getNombre();
        }
        aRetornar = aRetornar + " - Desplazamiento: " + bytesDesplazamiento;
        return aRetornar;
    }
    
}

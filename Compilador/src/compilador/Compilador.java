/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Vector;

/**
 *
 * @author José Ignacio Orlando
 */
public class Compilador {
    
    private TablaSimbolos tablaSimbolos;
    private AnalizadorLexico analLex;
    private AnalizadorSintactico analSint;
    
    public Compilador() {
        this.tablaSimbolos = new TablaSimbolos();
    }
    
    public String compilar(Archivo arch) {
        this.analLex = new AnalizadorLexico(arch, this.tablaSimbolos);
        this.analSint = new AnalizadorSintactico(this.analLex);
        // se corre la compilación
        //try{
            this.analSint.run();
            Vector<String> salida = new Vector<String>();
            salida.add("Compilando código fuente");
            salida.add("---------------------------------");
            salida.addAll(this.analSint.getSalida());
            String out = "";
            for (int i=0;i<salida.size();i++) {
                out = out + salida.get(i) + "\n";
            }
            return out;
        /*}catch(RuntimeException re){
            return ("El programa tiene errores");
        }*/
        
    }

    public String getContenidoTablaSimbolos() {
        return this.tablaSimbolos.toString();
    }

    public String getErrores() {
        Vector<String> salida = new Vector<String>();
        Vector<String> erroresLexicos = this.analLex.getErrores();
        if (!erroresLexicos.isEmpty()) {
            salida.add("ERRORES LÉXICOS:");
            salida.addAll(erroresLexicos);
        }
        salida.add("---------------------------------");
        Vector<String> erroresSintacticos = this.analSint.getErrores(); // harcodeo
        if (!erroresSintacticos.isEmpty()) {
            salida.add("ERRORES SINTÁCTICOS:");
            salida.addAll(erroresSintacticos);
        }
        String out = "";
        for (int i=0;i<salida.size();i++) {
            out = out + salida.get(i) + "\n";
        }
        return out;
    }

    public String getPalabrasReservadas() {
        return this.tablaSimbolos.getPalabrasReservadas();
    }
    
    
}

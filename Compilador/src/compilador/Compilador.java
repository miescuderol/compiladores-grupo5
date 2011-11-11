/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author JosÃ© Ignacio Orlando
 */
public class Compilador {
    
    private TablaSimbolos tablaSimbolos;
    private AnalizadorLexico analLex;
    private Parser analSint;
    private String assembler;
    
    public Compilador() {
        this.tablaSimbolos = new TablaSimbolos();
    }
    
    public String compilar(Archivo arch) {
        this.analLex = new AnalizadorLexico(arch, this.tablaSimbolos);
        this.analSint = new Parser(this.analLex);
        // se corre la compilaciÃ³n
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
        Vector<String> erroresSemanticos = this.analSint.getErroresSemanticos();
        if (!erroresSemanticos.isEmpty()) {
            salida.add("ERRORES SEMÁNTICOS:");
            salida.addAll(erroresSemanticos);
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
    
    public String getPolacaInversa(){
        Vector<ElementoPolaca> p = this.analSint.getPolacaInversa();
        String polacaInversa = "";
        for(int i = 0; i < p.size();i++){
            polacaInversa = polacaInversa + p.get(i) + "\n";
        }
        return polacaInversa;
    }
    
    private String generarHeaderAssembler() {
        return ".MODEL small\n.386\n.STACK 100h\n";
    }
    
    private String generarData() {
        String data=".DATA\n";
        // Generación de errores
        data = data + "msjErrorNegativo DB 'Error: No puede asignarse un resultado negativo a una variable ULONGINT.\n"
                + "msjWarningConversion DB 'Warning: Conversión implícita generó pérdida de precisión'\n";
        // Generación de elementos de la tabla de símbolos
        
        return "";
    }
    
    private String generarCode() {
        return "";
    }
    
    private String generarCuerpo() {
        return "";
    }
    
    private void generarAssembler() {
        if (this.analSint.isCompilable()) { // si el código no tiene error
            this.assembler = generarHeaderAssembler() + generarData() + generarCode() + generarCuerpo();
        }

    }
    
}

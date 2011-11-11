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
    private Vector<String> assembler;
    
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
    
    private void generarHeaderAssembler() {
        this.assembler.add(".386");
        this.assembler.add(".model flat, stdcall");
        this.assembler.add(".STACK 200h");
        this.assembler.add("option casemap :none");
        this.assembler.add("include \\masm32\\include\\windows.inc");
        this.assembler.add("include \\masm32\\include\\kernel32.inc");
        this.assembler.add("include \\masm32\\include\\user32.inc");
        this.assembler.add("includelib \\masm32\\lib\\kernel32.lib");
        this.assembler.add("includelib \\masm32\\lib\\user32.lib");
    }
    
    private void generarData() {
        // Generación del encabezado
        this.assembler.add(".DATA");
        // Generación de errores
        this.assembler.add("msjErrorNegativo DB 'Error: No puede asignarse un resultado negativo a una variable ULONGINT.");
        this.assembler.add("msjWarningConversion DB 'Warning: Conversión implícita generó pérdida de precisión'");
        // Generación de elementos de la tabla de símbolos
        IteradorTablaSimbolos it = this.analLex.getTablaSimbolos().iterator();
        while (it.hasNext()) {
            Entrada e = it.nextEntrada();
            // De acuerdo al tipo, reservamos el espacio que corresponda
            if (e.getTipo()==Tipo.ID) {
                Tipo t = e.getTipo_dato();
                if (t==Tipo.INTEGER) {
                    this.assembler.add("_"+ e.getNombre() + " dd 0");
                } else if (t==Tipo.ULONGINT) {
                    this.assembler.add("_"+ e.getNombre() + " dd 0");
                } else if (t==Tipo.STRUCT) {
                    // ¿¿¿¿CÓMO MIERDA?????
                }                  
            } else if (e.getTipo()==Tipo.CADENA) {
                this.assembler.add("_" + e.getNombre() + " db '" + e.getNombre()+ "', 0");
            }
        }
    }
    
    private void generarCode() {
        
    }
    
    private void generarCuerpo() {
    }
    
    private void generarAssembler() {
        if (this.analSint.isCompilable()) { // si el código no tiene error
            this.assembler = new Vector<String>();
            this.generarHeaderAssembler();
            this.generarData();
            this.generarCode();
            this.generarCuerpo();
        }
    }
    
    public String getAssembler() {
        String acum = "";
        for (int i=0;i<this.assembler.size();i++) {
            acum = acum + this.assembler.get(i) + "\n";
        }
        return acum;
    }
    
}

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
    private Vector<ElementoPolaca> polaca;

    
    // atributos auxiliares para generación de código
    String condicionFalsa;
    private BancoRegistros bancoRegistros;
    private int punteroPolaca;
    
    public Compilador() {
        this.tablaSimbolos = new TablaSimbolos();
    }
    
    public String compilar(Archivo arch) {
        this.analLex = new AnalizadorLexico(arch, this.tablaSimbolos);
        this.analSint = new Parser(this.analLex);
        // se corre la compilaciÃ³n
        //try{
            this.analSint.run();
            this.generarAssembler();
            Vector<String> salida = new Vector<String>();
            salida.add("Compilando código fuente");
            salida.add("---------------------------------");
            salida.add(this.getAssembler());
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
        Vector<ElementoPolaca> pol = this.analSint.getPolacaInversa();
        String polacaInversa = "";
        for(int i = 0; i < pol.size();i++){
            polacaInversa = polacaInversa + pol.get(i) + "\n";
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
        this.assembler.add("ERROR_UNDERFLOW_RESTA DB 'Error: No puede asignarse un resultado negativo a una variable ULONGINT.");
        this.assembler.add("ERROR_PERDIDA_PRECISION DB 'Error: Conversión implícita generó pérdida de precisión'");
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
                    this.assembler.add("_"+ e.getNombre() + " dd " + e.getTamanio() + "dup(?)");
                }                  
            } else if (e.getTipo()==Tipo.CADENA) {
                this.assembler.add("_" + e.getNombre() + " db '" + e.getNombre()+ "', 0");
            }
        }
    }
    
    private void generarCuerpo() {
        // Generación del encabezado
        this.assembler.add(".CODE");
        this.assembler.add(".start:");
        for (punteroPolaca=0;punteroPolaca<this.polaca.size();punteroPolaca++) {
            ElementoPolaca el = this.polaca.get(punteroPolaca);
            switch (el.getTipo()) {
                case ElementoPolaca.PRINT :
                    this.getAssemblerPrint(this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.ASIGNACION :
                    this.getAssemblerAsignacion(this.polaca.get(punteroPolaca-1),this.polaca.get(punteroPolaca-2));
                    break;
                case ElementoPolaca.BF :
                    this.getAssemblerSalto(condicionFalsa, this.polaca.get(punteroPolaca));
                    break;
                case ElementoPolaca.BI :
                    this.getAssemblerSalto("JMP", this.polaca.get(punteroPolaca));
                    break;
                case ElementoPolaca.DISTINTO :
                    this.getAssemblerCondiciones("JE",this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.DIVISION :
                    this.getAssemblerDivision(this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.IGUAL :
                    this.getAssemblerCondiciones("JNE",this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.MAYOR :
                    this.getAssemblerCondiciones("JNG",this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.MAYORIGUAL :
                    this.getAssemblerCondiciones("JL",this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.MENOR :
                    this.getAssemblerCondiciones("JNL",this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.MENORIGUAL :
                    this.getAssemblerCondiciones("JG",this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.MULTIPLICACION :
                    this.getAssemblerMultiplicacion(this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.RESTA :
                    this.getAssemblerResta(this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
                case ElementoPolaca.SUMA :
                    this.getAssemblerSuma(this.polaca.get(punteroPolaca-2),this.polaca.get(punteroPolaca-1));
                    break;
            }
        }
        // Errores
        this.assembler.add("invoke ExitProcess, 0");
        this.assembler.add("LABEL_ERROR_PERDIDA_PRECISION:");
        this.assembler.add("invoke MessageBox, NULL, addr ERROR_PERDIDA_PRECISION, addr ERROR_PERDIDA_PRECISION, MB_OK");
        this.assembler.add("invoke ExitProcess, 0");
        this.assembler.add("LABEL_ERROR_UNDERFLOW_RESTA:");
        this.assembler.add("invoke MessageBox, NULL, addr ERROR_UNDERFLOW_RESTA, addr ERROR_UNDERFLOW_RESTA, MB_OK");
        this.assembler.add("invoke ExitProcess, 0");
        this.assembler.add("end start");
    }
    
    private void generarAssembler() {
        if (this.analSint.isCompilable()) { // si el código no tiene error
            // Obtengo la polaca inversa y creo el banco de registros
            this.polaca = this.analSint.getPolacaInversa();
            this.bancoRegistros = new BancoRegistros();
            // Prepara el código assembler
            this.assembler = new Vector<String>();
            // Lo genera
            this.generarHeaderAssembler();
            this.generarData();
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

    private void getAssemblerPrint(ElementoPolaca e) {
        this.assembler.add("invoke MessageBox, NULL, addr " + e.getNombre() + ", addr " + e.getNombre() + ", MB_OK");
    }

    private void getAssemblerAsignacion(ElementoPolaca destino, ElementoPolaca origen) {
        this.getAssemblerConversionImplicita(destino,origen);
        if (origen.getTipo()!=ElementoPolaca.REGISTRO) { // si es una variable
            // Conversión implícita
            Registro r = this.bancoRegistros.ocuparRegistroLibre();
            this.assembler.add("MOV " + r.getNombre() + " , " + getNombreVariable(origen));
            this.assembler.add("MOV " + getNombreVariable(destino) + " , " + r.getNombre());
            r.desocupar();
        } else {
            this.assembler.add("MOV " + getNombreVariable(destino) + " , " + getNombreVariable(origen));
        }
    }
    
    private String getNombreVariable(ElementoPolaca e) {
        if (e.getTipo()==ElementoPolaca.VARIABLE) {
            return "_" + e.getNombre();
        }
        return e.getNombre();
    }

    private void getAssemblerConversionImplicita(ElementoPolaca destino, ElementoPolaca origen) {
        if (origen.getTipo_dato()!=destino.getTipo_dato()) {
            if (origen.getTipo_dato()==Tipo.INTEGER) {
                generarCodigoConversionImplicita(origen);
            } else {
                generarCodigoConversionImplicita(destino);
            }
        }
    }
    
    private void generarCodigoConversionImplicita(ElementoPolaca elementoConflicto) {
        this.assembler.add("CMP " + this.getNombreVariable(elementoConflicto) + ", 0");
        this.assembler.add("JL LABEL_ERROR_PERDIDA_PRECISION");
        elementoConflicto.setTipo_dato(Tipo.ULONGINT);
    }

    private void getAssemblerMultiplicacion(ElementoPolaca op1, ElementoPolaca op2) {
        // Convierto si es necesario
        this.getAssemblerConversionImplicita(op1, op2);
        // Preparo el operando 1 en EAX y libero su registro anterior en caso de que haya sido un registro
        this.assembler.add("MOV EAX , " + this.getNombreVariable(op1));
        this.bancoRegistros.ocuparRegistro("EAX");
        if (op1.getTipo()==ElementoPolaca.REGISTRO) {
            this.bancoRegistros.desocuparRegistro(op1.getNombre());
        }
        // Preparo el operando 2 en un nuevo registro, siempre y cuando no haya sido un registro
        String nombreRegistroAuxiliar;
        if (op2.getTipo()!=ElementoPolaca.REGISTRO) {
            Registro r = this.bancoRegistros.ocuparRegistroLibre();
            this.assembler.add("MOV " + r.getNombre() + " , " + this.getNombreVariable(op2));
            nombreRegistroAuxiliar = r.getNombre();
        } else {
            nombreRegistroAuxiliar = op2.getNombre();
        }
        // Decido qué instrucción de la ALU ejecutar
        if (op1.getTipo_dato()==Tipo.INTEGER) {
            this.assembler.add("IMUL " + nombreRegistroAuxiliar);
        } else {
            this.assembler.add("MUL " + nombreRegistroAuxiliar);
        }
        this.assembler.add("MOV " + nombreRegistroAuxiliar + ", EAX");
        this.bancoRegistros.desocuparRegistro("EAX");
        this.desapilarPolaca(new ElementoPolaca(ElementoPolaca.REGISTRO,op1.getTipo_dato(),nombreRegistroAuxiliar));
    }

    private void desapilarPolaca(ElementoPolaca elementoPolaca) {
        punteroPolaca = punteroPolaca - 2;
        this.polaca.remove(punteroPolaca);
        this.polaca.remove(punteroPolaca);
        this.polaca.set(punteroPolaca,elementoPolaca);
    }

    private void getAssemblerResta(ElementoPolaca op1, ElementoPolaca op2) {
        // Convierto si es necesario
        this.getAssemblerConversionImplicita(op1, op2);
        // Preparo el operando 1 en EAX y libero su registro anterior en caso de que haya sido un registro
        this.assembler.add("MOV EAX , " + this.getNombreVariable(op1));
        this.bancoRegistros.ocuparRegistro("EAX");
        if (op1.getTipo()==ElementoPolaca.REGISTRO) {
            this.bancoRegistros.desocuparRegistro(op1.getNombre());
        }
        // Hago la operación
        this.assembler.add("SUB EAX , " + this.getNombreVariable(op2));
        // Estoy en condiciones de liberar el registro que usé para operar
        if (op2.getTipo()==ElementoPolaca.REGISTRO) {
            this.bancoRegistros.desocuparRegistro(op2.getNombre());
        }
        // CONTROL DE UNDERFLOW
        this.assembler.add("JS LABEL_ERROR_UNDERFLOW_RESTA");
        // Muevo el resultado a un registro libre
        Registro r = this.bancoRegistros.ocuparRegistroLibre();
        this.assembler.add("MOV " + r.getNombre() + " , EAX");
        // Libero EAX
        this.bancoRegistros.desocuparRegistro("EAX");
        // Desapilo en la polaca
        this.desapilarPolaca(new ElementoPolaca(ElementoPolaca.REGISTRO,op1.getTipo_dato(),r.getNombre()));
    }

    private void getAssemblerSuma(ElementoPolaca op1, ElementoPolaca op2) {
        // Convierto si es necesario
        this.getAssemblerConversionImplicita(op1, op2);
        // Preparo el operando 1 en EAX y libero su registro anterior en caso de que haya sido un registro
        this.assembler.add("MOV EAX , " + this.getNombreVariable(op1));
        this.bancoRegistros.ocuparRegistro("EAX");
        if (op1.getTipo()==ElementoPolaca.REGISTRO) {
            this.bancoRegistros.desocuparRegistro(op1.getNombre());
        }
        // Hago la operación
        this.assembler.add("ADD EAX , " + this.getNombreVariable(op2));
        // Estoy en condiciones de liberar el registro que usé para operar
        if (op2.getTipo()==ElementoPolaca.REGISTRO) {
            this.bancoRegistros.desocuparRegistro(op2.getNombre());
        }
        // Muevo el resultado a un registro libre
        Registro r = this.bancoRegistros.ocuparRegistroLibre();
        this.assembler.add("MOV " + r.getNombre() + " , EAX");
        // Libero EAX
        this.bancoRegistros.desocuparRegistro("EAX");
        this.desapilarPolaca(new ElementoPolaca(ElementoPolaca.REGISTRO,op1.getTipo_dato(),r.getNombre()));
    }

    private void getAssemblerDivision(ElementoPolaca op1, ElementoPolaca op2) {
        // Convierto si es necesario
        this.getAssemblerConversionImplicita(op1, op2);
        // Verificamos que EDX esté libre
        Registro registroSalvavidas = null;
        boolean estabaOcupado = this.bancoRegistros.getRegistro("EDX").isOcupado();
        if (estabaOcupado) {
            // Agrego código para salvar el valor que estaba ahí
            registroSalvavidas = this.bancoRegistros.ocuparRegistroLibre();
            this.assembler.add("MOV " + registroSalvavidas.getNombre() + " , EDX");
            this.bancoRegistros.desocuparRegistro("EDX");
        }
        // Ponemos en 0 EDX (a donde va a parar el resto de la division) por si no hay resto
        this.assembler.add("MOV EDX , 0");
        // Preparo el operando 1 en EAX y libero su registro anterior en caso de que haya sido un registro
        this.assembler.add("MOV EAX , " + this.getNombreVariable(op1));
        this.bancoRegistros.ocuparRegistro("EAX");
        if (op1.getTipo()==ElementoPolaca.REGISTRO) {
            this.bancoRegistros.desocuparRegistro(op1.getNombre());
        }
        // Extendemos el signo
        this.assembler.add("CDQ");
        // Preparo el operando 2 en un nuevo registro, siempre y cuando no haya sido un registro
        String nombreRegistroAuxiliar;
        if (op2.getTipo()!=ElementoPolaca.REGISTRO) {
            Registro r = this.bancoRegistros.ocuparRegistroLibre();
            this.assembler.add("MOV " + r.getNombre() + " , " + this.getNombreVariable(op2));
            nombreRegistroAuxiliar = r.getNombre();
        } else {
            nombreRegistroAuxiliar = op2.getNombre();
        }
        // Hago la operación
        if(op1.getTipo_dato()==Tipo.INTEGER) {
            this.assembler.add("IDIV " + nombreRegistroAuxiliar);
        } else {
            this.assembler.add("DIV " + nombreRegistroAuxiliar);
        }
        // Libero EAX
        this.assembler.add("MOV " + nombreRegistroAuxiliar + " , EAX");
        this.bancoRegistros.desocuparRegistro("EAX");
        // Restauro EDX
        if (estabaOcupado) {
            // Restauro
            this.assembler.add("MOV EDX , " + registroSalvavidas.getNombre());
            registroSalvavidas.desocupar();
        }
        // Desapilo de la polaca
        this.desapilarPolaca(new ElementoPolaca(ElementoPolaca.REGISTRO,op1.getTipo_dato(),nombreRegistroAuxiliar));
    }

    private void getAssemblerSalto(String condicionFalsa, ElementoPolaca el) {
        // Agregamos el rótulo
        this.assembler.add(condicionFalsa + " " + el.getNombre());
    }

    private void getAssemblerCondiciones(String instruccion, ElementoPolaca e1, ElementoPolaca e2) {
        this.assembler.add("MOV EAX , " + this.getNombreVariable(e1));
        this.assembler.add("CMP EAX , " + this.getNombreVariable(e2));
        this.condicionFalsa = instruccion;
    }
    
    
    
    
    
}

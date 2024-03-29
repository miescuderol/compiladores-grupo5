/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;

/**
 *
 * @author JosÃ© Ignacio Orlando
 */
public class Compilador {
    
    private TablaSimbolos tablaSimbolos;
    private AnalizadorLexico analLex;
    private Parser analSint;
    private ArrayList<String> assembler;
    private ArrayList<ElementoPolaca> polaca;
    private ArrayList<ElementoPolaca> polaca_original;
    
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
        this.analSint.run();
        this.generarAssembler();
        if(this.isCompilable())
            return this.getAssembler();
        else
            return null;
    }

    
    public String getContenidoTablaSimbolos() {
        return this.tablaSimbolos.toString();
    }

    public String getErrores() {
        ArrayList<String> salida = new ArrayList<String>();
        ArrayList<String> erroresLexicos = this.analLex.getErrores();
        if (!erroresLexicos.isEmpty()) {
            salida.add("ERRORES LÉXICOS:");
            salida.addAll(erroresLexicos);
        }
        salida.add("---------------------------------");
        ArrayList<String> erroresSintacticos = this.analSint.getErrores(); // harcodeo
        if (!erroresSintacticos.isEmpty()) {
            salida.add("ERRORES SINTÁCTICOS:");
            salida.addAll(erroresSintacticos);
        }
        ArrayList<String> erroresSemanticos = this.analSint.getErroresSemanticos();
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
        String polacaInversa = "";
        for(int i = 0; i < this.polaca_original.size();i++){
            polacaInversa = polacaInversa + this.polaca_original.get(i) + "\n";
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
        this.assembler.add("ERROR_UNDERFLOW_RESTA DB 'Error: No puede asignarse un resultado negativo a una variable ULONGINT.'");
        this.assembler.add("ERROR_PERDIDA_PRECISION DB 'Error: Conversion implicita genero perdida de precision'");
        // Generación de elementos de la tabla de símbolos
        IteradorTablaSimbolos it = this.analLex.getTablaSimbolos().iterator();
        while (it.hasNext()) {
            Entrada e = it.nextEntrada();
            // De acuerdo al tipo, reservamos el espacio que corresponda
            if (e.getTipo()==Tipo.ID) {
                Tipo t = e.getTipo_dato();
                 if(e.isElementoEstructura()){
                    String renombre = getNombreVariableEstructura((EntradaEstructura)e);
                    this.assembler.add(renombre + " dd 0");
                } else
                      this.assembler.add("_"+ e.getNombre() + " dd 0");
            } else if (e.getTipo()==Tipo.CADENA) {
                String nombre = getNombreVariableCadena(e);
                String cadena = e.getNombre().replace("\'", "\"");
                this.assembler.add(nombre + " db " + cadena + ", 0");
            }
        }
    }
    
    private void generarCuerpo() {
        // Generación del encabezado
        this.assembler.add(".CODE");
        this.assembler.add("start:");
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
                case ElementoPolaca.ROTULO :
                    this.assembler.add(this.polaca.get(punteroPolaca).getNombre() + ':');
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
        // Obtengo la polaca inversa y creo el banco de registros
        this.polaca = this.analSint.getPolacaInversa();
        this.polaca_original = (ArrayList<ElementoPolaca>) (this.polaca.clone());
        if (isCompilable()) { // si el código no tiene error
            this.bancoRegistros = new BancoRegistros();
            // Prepara el código assembler
            this.assembler = new ArrayList<String>();
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

    private void getAssemblerPrint(ElementoPolaca ep) {
        Entrada e = tablaSimbolos.get('\'' + ep.getNombre() + '\'');
        this.assembler.add("invoke MessageBox, NULL, addr " + getNombreVariableCadena(e) + ", addr " + getNombreVariableCadena(e) + ", MB_OK");
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
            Entrada entrada = tablaSimbolos.get(e.getNombre());
            if(!entrada.isElementoEstructura())
                return "_" + e.getNombre();
            else{
                return getNombreVariableEstructura((EntradaEstructura)entrada);
            }
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
        String reg = "";
        if (elementoConflicto.getTipo()==ElementoPolaca.CONSTANTE) {
            this.assembler.add("MOV EAX , " + elementoConflicto.getNombre());
            reg = "EAX";
        } else {
            reg = this.getNombreVariable(elementoConflicto);
        }
        this.assembler.add("CMP " + reg + ", 0");
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
        // CONTROL DE UNDERFLOW EN CASO DE OPERACION ENTRE ULONGINT
        if(op1.getTipo_dato() == Tipo.ULONGINT)
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
        // Libero EDX y obtengo los operandos
        String nom_op2 = "";
        boolean flag = false, flag2 =false;
        Registro libre= null, libre2 = null;
        if (op1.getTipo()==ElementoPolaca.REGISTRO) {
            this.bancoRegistros.desocuparRegistro(op1.getNombre());
        }
        this.assembler.add("MOV EAX , " + this.getNombreVariable(op1));
        if (op2.getTipo()==ElementoPolaca.REGISTRO) {
            if (op2.getNombre().equals("EDX")) {
                libre = this.bancoRegistros.ocuparRegistroLibre();
                this.assembler.add("MOV " + libre.getNombre() + " , EDX");
                nom_op2 = libre.getNombre();
                this.assembler.add("MOV EDX , 0");
                this.assembler.add("CDQ");
                //estaba ya ocupado y no se livero
            } else{
                nom_op2 = this.getNombreVariable(op2);
                //ya es un registro opero con el
            }
        } else {
            if (this.bancoRegistros.getRegistro("EDX").isOcupado()){
                //EDX se encuentra ocupado.
                libre = this.bancoRegistros.ocuparRegistroLibre();
                this.assembler.add("MOV " + libre.getNombre() + " , EDX");
                this.bancoRegistros.desocuparRegistro("EDX");
                flag = true;
            }
            this.assembler.add("MOV EDX , 0");
            this.assembler.add("CDQ");
            this.bancoRegistros.ocuparRegistro("EDX");
            libre2 = this.bancoRegistros.ocuparRegistroLibre();
            this.assembler.add("MOV " + libre2.getNombre() + " , " + this.getNombreVariable(op2));
            nom_op2 = libre2.getNombre();
        }
        //--cris
        if ((op1.getTipo()!=ElementoPolaca.REGISTRO)) {
            if (op1.getTipo_dato() != Tipo.INTEGER)
                this.assembler.add("DIV " + nom_op2);
            else
                this.assembler.add("IDIV " + nom_op2);
        } 
        else  
            if ((op2.getTipo()!=ElementoPolaca.REGISTRO)){
                if (op2.getTipo_dato() != Tipo.INTEGER)
                    this.assembler.add("DIV " + nom_op2);
                else {
                    this.assembler.add("IDIV " + nom_op2);
            }
        };
        //xx cris
        this.bancoRegistros.desocuparRegistro(nom_op2);
        this.bancoRegistros.desocuparRegistro("EDX");
        if (flag){
            this.assembler.add("MOV EDX , " + libre.getNombre());
            this.bancoRegistros.desocuparRegistro(libre.getNombre());
            this.bancoRegistros.ocuparRegistro("EDX");
        }
        libre = this.bancoRegistros.ocuparRegistroLibre();
        this.assembler.add("MOV " + libre.getNombre()+" , EAX");

        this.desapilarPolaca(new ElementoPolaca(ElementoPolaca.REGISTRO,op1.getTipo_dato(),libre.getNombre()));
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

    public boolean isCompilable() {
        return (this.analSint.isCompilable() && this.analLex.getErrores().isEmpty());
    }

    private String getNombreVariableEstructura(EntradaEstructura ee) {
        String padre = ee.getEstructura().getNombre();
        return "_" + padre + "_" + ee.getNombre();
    }

    private String getNombreVariableCadena(Entrada e) {
        return "_" + e.getNombre().replace(" ", "").replace("\'", "");
    }
    
}

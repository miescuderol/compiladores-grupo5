/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author José Ignacio Orlando
 */
public class AnalizadorLexico {
    
    // Atributos
    private Archivo archivoTexto;
    private int[][] matrizTransicionEstados;
    private AccionSemantica[][] matrizAccionesSemanticas;
    private ArrayList<String> erroresLexicos;
    private static int numeroLinea;
    private TablaSimbolos tb;
    
    // Constantes
    public static final int TRANSICION_NO_VALIDA = -1;
    public static final int ESTADO_INICIAL = 0;
    public static final int ESTADO_FINAL = 13;
    
    public AnalizadorLexico(Archivo fuente, TablaSimbolos t) {
        numeroLinea = 1;
        // seteamos el archivo con el código fuente del programa que estamos parseando
        this.archivoTexto = fuente;
        // seteamos la matriz de transición de estados
        this.matrizTransicionEstados = new int[][] {{ 1, 2, 0, 1, 1, 4,13,13, 7,13,13,13,13,13,13, 8,11, 9,10, 12, 0, 0, 13,1,-1},
                                                    { 1, 1,13, 1, 1,13,13,13,13,13,13,13,13,13,13,13,13,13,13, 13, 13, 13, 13, 1,13},
                                                    {-1, 2,-1, 3,13,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, -1,-1},
                                                    {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 13,-1},
                                                    {13,13,13,13,13,13,13, 5,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13, 13,-1},
                                                    { 5, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,5,5,5,-1,5,5},
                                                    { 5, 5, 5, 5, 5, 5, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,5,5,5,-1,5,5},
                                                    { 7, 7, 7, 7, 7, 7, 7, 7,13, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,-1,-1,7,7},
                                                    {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,13,-1,-1,-1,-1,-1,-1,-1,-1},
                                                    {13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,-1},
                                                    {13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,-1},
                                                    {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,13,-1,-1,-1,-1,-1,-1,-1,-1},
                                                    {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,13,-1,-1,-1,-1,-1,-1,-1,-1}};

        // seteamos la matriz de acciones semánticas
        this.matrizAccionesSemanticas = new AccionSemantica[][] {{AccionSemantica.inicializarYConcatenar, AccionSemantica.inicializarYConcatenar, AccionSemantica.nada, AccionSemantica.inicializarYConcatenar, AccionSemantica.inicializarYConcatenar, AccionSemantica.nada, AccionSemantica.reconocerSimbolo, AccionSemantica.reconocerSimbolo, AccionSemantica.inicializar, AccionSemantica.reconocerSimbolo, AccionSemantica.reconocerSimbolo, AccionSemantica.reconocerSimbolo, AccionSemantica.reconocerSimbolo, AccionSemantica.reconocerSimbolo, AccionSemantica.reconocerSimbolo, AccionSemantica.inicializarYConcatenar, AccionSemantica.inicializarYConcatenar, AccionSemantica.inicializarYConcatenar, AccionSemantica.inicializarYConcatenar, AccionSemantica.inicializarYConcatenar, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.finDeArchivo, AccionSemantica.inicializarYConcatenar,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.chequeoIdentificadores, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.chequeoIdentificadores, AccionSemantica.concatenar,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.concatenar, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.nada, AccionSemantica.chequeoRangoEntero, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.errorTipoConstanteIndefinido, AccionSemantica.concatenar,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.errorSeEsperabaL, AccionSemantica.chequeoRangoEnteroLargoSinSigno,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.nada, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto, AccionSemantica.tokenParentesisAbierto,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.errorComentarioNoFinalizado, AccionSemantica.nada,AccionSemantica.nada},
                                                                {AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.nada, AccionSemantica.errorComentarioNoFinalizado, AccionSemantica.nada,AccionSemantica.nada},
                                                                {AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.reconocerString, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.concatenar, AccionSemantica.errorSaltoDeLineaEnCadena, AccionSemantica.concatenar, AccionSemantica.concatenar,AccionSemantica.concatenar},
                                                                {AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.reconocerSimboloDoble, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloDoble, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloDoble, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple, AccionSemantica.reconocerSimboloSimple,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.reconocerSimboloDoble, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual,AccionSemantica.errorCaracterInvalido},
                                                                {AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.reconocerSimboloDoble, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual, AccionSemantica.errorSeEsperabaIgual,AccionSemantica.errorCaracterInvalido}};

        // declaramos la lista de errores        
        this.erroresLexicos = new ArrayList<String>();
        // Le doy la tabla de símbolos a las acciones semánticas
        this.tb = t;
        AccionSemantica.setTablaSimbolos(t);
    }
    
    public Token getToken() {
        // Seteo las condiciones iniciales respecto al estado, el caracter leido y su indice correspondiente
        int estadoActual = AnalizadorLexico.ESTADO_INICIAL;
        AccionSemantica accionActual = null;
        char caracterLeido;
        int indiceCaracterLeido;
        // Mientras no alcance el estado final
        while ((estadoActual!=AnalizadorLexico.ESTADO_FINAL) && (estadoActual!=AnalizadorLexico.TRANSICION_NO_VALIDA)) {
            archivoTexto.mark(); // Marco donde voy a leer
            caracterLeido = ((char)(archivoTexto.read())); // Leo el caracter del archivo fuente
            if ((caracterLeido=='\n')){// || (caracterLeido=='\n')) { // si se terminó la linea
                numeroLinea++;  // la incremento
            }
            indiceCaracterLeido = CorrespondenciaColumnas.getColumna(caracterLeido); // Obtengo el indice que le corresponde al caracter en la matriz
            accionActual = this.matrizAccionesSemanticas[estadoActual][indiceCaracterLeido]; // Recupero la acción semántica correspondiente
            if (accionActual.ejecutar(caracterLeido))  // La ejecuto y retrocedo si necesita retroceder
                archivoTexto.reset();
            estadoActual = this.matrizTransicionEstados[estadoActual][indiceCaracterLeido]; // Cambio de estado
        }
        // chequeo si hubo algún error léxico o algún warning
        String error = AccionSemantica.getError();
        if (error!=null) {
            this.erroresLexicos.add("Linea " + numeroLinea + ": " + error);
        }
        
        if(estadoActual==AnalizadorLexico.TRANSICION_NO_VALIDA)   //doduso
                return new Token(AccionSemantica.elementoParcial, TablaSimbolos.YYERRCODE); //claro queno esta bien
        
        return accionActual.getToken(); // Retornamos el token armado
    }

    public ArrayList<String> getErrores() {
        return new ArrayList<String>(this.erroresLexicos);
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }
    
    public TablaSimbolos getTablaSimbolos(){
        return this.tb;
    }
}

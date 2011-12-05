/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;


/**
 *
 * @author José Ignacio Orlando
 */
public abstract class AccionSemantica {
    
    protected static String elementoParcial;
    protected static Token token;
    protected static String error;

    protected static TablaSimbolos tablaSimbolos;
    
    public static final int MAX_INTEGER = 32768;
    public static final double MAX_ULONGINTEGER = 4294967295d;
    
    
    /*
     * Retorna un boolean indicando si hay que retroceder (true) o no (false)
     */
    public abstract boolean ejecutar(char recienLeido);
    
    public static String getError() {
        String e = AccionSemantica.error;
        AccionSemantica.error = null;
        return e;
    }
    
    public static void setTablaSimbolos(TablaSimbolos t) {
        AccionSemantica.tablaSimbolos = t;
    }
    
    public Token getToken() {
        return AccionSemantica.token;
    }
    
    /* Acciones semánticas */
    
    // Acción - (no hace nada)
    public static AccionSemantica nada = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            return false;
        }
    };
    
    // Acción A1 (inicializa el string y concatena, sin retrocede)
    public static AccionSemantica inicializarYConcatenar = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.inicializar.ejecutar(recienLeido);
            return AccionSemantica.concatenar.ejecutar(recienLeido);
        }
    };
    
    // Acción A2 (concatena y no retrocede)
    public static AccionSemantica concatenar = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.elementoParcial = AccionSemantica.elementoParcial + recienLeido;
            return false;
        }
    };
            
    // Acción A3 (chequea el tamaño del identificador, verifica si es o no una palabra reservada o
    //            agrega el identificador a la tabla de símbolos, y retrocede)
    public static AccionSemantica chequeoIdentificadores = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            // si es una palabra reservada
            if (AccionSemantica.tablaSimbolos.isPalabraReservada(AccionSemantica.elementoParcial)) {
                // creo el token asociado a la palabra reservada
                AccionSemantica.token = new Token(AccionSemantica.elementoParcial.toUpperCase(),AccionSemantica.tablaSimbolos.getCodigoYacc(AccionSemantica.elementoParcial));
            } else { // si es un identificador
                // si tiene nombre con longitud mayor a 12
                if (AccionSemantica.elementoParcial.length()>12) {
                    // aviso que lo trunco
                    AccionSemantica.error = "WARNING: Identificador " + AccionSemantica.elementoParcial + "supera los 12 caracteres. Será truncado en " + AccionSemantica.elementoParcial.substring(0,12);
                    // la trunco
                    AccionSemantica.elementoParcial = AccionSemantica.elementoParcial.substring(0, 12);
                }
                // lo buscamos en la tabla de símbolos
                Entrada e = AccionSemantica.tablaSimbolos.get(AccionSemantica.elementoParcial);
                if (e==null) { // si no estaba
                    e = new Entrada(AccionSemantica.elementoParcial, Tipo.ID, null);
                    AccionSemantica.tablaSimbolos.addNuevaEntrada(e);
                }
                AccionSemantica.token = new TokenConAtributo(AccionSemantica.elementoParcial,AccionSemantica.tablaSimbolos.getCodigoYacc(AccionSemantica.elementoParcial),e);
            }
            return true;
        }
    };        
    
    // Acción A4 (chequea el rango del entero)
    public static AccionSemantica chequeoRangoEntero = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            if(AccionSemantica.elementoParcial.length() < 6){
                Integer valor = Integer.valueOf(AccionSemantica.elementoParcial);
                if ((0 <= valor) && (valor <= AccionSemantica.MAX_INTEGER)) { // si está dentro del rango
                    String nombreToken = valor + "i";
                    Entrada e = AccionSemantica.tablaSimbolos.get(nombreToken);
                    if (e==null) {
                        e = new Entrada(nombreToken, Tipo.CONSTANTE_INTEGER, valor);
                        e.setTipo_dato(Tipo.INTEGER);
                        AccionSemantica.tablaSimbolos.addNuevaEntrada(e);
                    }
                    AccionSemantica.token = new TokenConAtributo(nombreToken,AccionSemantica.tablaSimbolos.getCodigoYacc(nombreToken), e);
                } else { // si no está dentro del rango
                    AccionSemantica.error = "Entero fuera del rango [-32768;32767]";
                }
            } else {
                AccionSemantica.error = "Entero fuera del rango [-32768;32767]";
            }
            return false;
        }
    };       
            
    // Acción A5 (chequea el rango del entero largo sin signo)
    public static AccionSemantica chequeoRangoEnteroLargoSinSigno = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            double valor = Double.valueOf(AccionSemantica.elementoParcial);
            if ((0 <= valor) && (valor <= AccionSemantica.MAX_ULONGINTEGER)) { // si está dentro del rango
                String nombreToken = AccionSemantica.elementoParcial + "ul";
                Entrada e = AccionSemantica.tablaSimbolos.get(nombreToken);
                if (e==null) {
                    e = new Entrada(nombreToken, Tipo.CONSTANTE_ULONGINT, AccionSemantica.elementoParcial);
                    e.setTipo_dato(Tipo.ULONGINT);
                    AccionSemantica.tablaSimbolos.addNuevaEntrada(e);
                }
                AccionSemantica.token = new TokenConAtributo(nombreToken,AccionSemantica.tablaSimbolos.getCodigoYacc(nombreToken), e);
            } else { // si no está dentro del rango
                AccionSemantica.error = "Entero largo sin signo fuera del rango [0;4294967295]";
            }
            return false;   
        }
    };          
    
    // Acción A6 (prepara el token '(' y retrocede)
    public static AccionSemantica tokenParentesisAbierto = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.token = new Token("(",40);
            return true;
        }
    };    
    
    // Acción A7 (inicializa el string sin concatenar lo que recibe por parámetro)
    public static AccionSemantica inicializar = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.elementoParcial = "";
            AccionSemantica.token = null;
            AccionSemantica.error = null;
            return false;
        }
    };

    // Acción A8 (se agrega el string a la tabla de simbolos y se prepara el token)
    public static AccionSemantica reconocerString = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            String nom = "\'" + AccionSemantica.elementoParcial + "\'";
            Entrada e = AccionSemantica.tablaSimbolos.get(nom);
            if (e==null) {
                e = new Entrada(nom, Tipo.CADENA, AccionSemantica.elementoParcial);
                AccionSemantica.tablaSimbolos.addNuevaEntrada(e);
            }
            AccionSemantica.token = new TokenConAtributo(nom,AccionSemantica.tablaSimbolos.getCodigoYacc(nom),e);
            return false;
        }
    };    
        
    // Acción A9 (inicializa y prepara el token del simbolo)
    public static AccionSemantica reconocerSimbolo = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.inicializar.ejecutar(recienLeido);
            AccionSemantica.token = new Token("" + recienLeido,(int) (recienLeido));
            return false;
        }
    };
    
    // Acción A10 (prepara el token del simbolo)
    public static AccionSemantica reconocerSimboloDoble = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.elementoParcial = AccionSemantica.elementoParcial + recienLeido;
            AccionSemantica.token = new Token(AccionSemantica.elementoParcial,AccionSemantica.tablaSimbolos.getCodigoYacc(AccionSemantica.elementoParcial));
            return false;
        }
    };
    
    // Acción A11 (prepara el token del simbolo)
    public static AccionSemantica reconocerSimboloSimple = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.token = new Token(AccionSemantica.elementoParcial,(int)(AccionSemantica.elementoParcial.charAt(0)));
            return true;
        }
    };
    
    // Acción A12 (se encuentra el final del archivo)
    public static final AccionSemantica finDeArchivo = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.token = new Token("EOF",-1);
            return false;
        }
    };
    
    // Acción de error ER1
    public static AccionSemantica errorSeEsperabaIgual = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.error = "Error léxico. Se esperaba un = pero se encontró " + recienLeido;
            return true;
        }
    };
    
    // Acción de error ER2
    public static AccionSemantica errorSaltoDeLineaEnCadena = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.error = "Error léxico. Se encontró un salto de línea dentro de una cadena.";
            return false;
        }
    };
    
    // Acción de error ER3
    public static AccionSemantica errorSeEsperabaL = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.error = "Error léxico. Se esperaba l pero se encontró " + recienLeido;
            return true;
        }
    };
    
    // Acción de error ER4
    public static AccionSemantica errorComentarioNoFinalizado = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.error = "Error léxico. Se esperaba *) pero se encontró " + recienLeido;
            return true;
        }
    };
    
    // Acción de error ER5
    public static AccionSemantica errorTipoConstanteIndefinido = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.error = "Error léxico. Constante de tipo indefinido. Se esperaba i o ul pero se encontró " + recienLeido;
            return true;
        }
    };
    
    // Acción de error ER6
    public static AccionSemantica errorCaracterInvalido = new AccionSemantica() {
        @Override
        public boolean ejecutar(char recienLeido) {
            AccionSemantica.error = "Error léxico. Se encontró el carácter inválido " + recienLeido;
            return false;
        }
    };
}

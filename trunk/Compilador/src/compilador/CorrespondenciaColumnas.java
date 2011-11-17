/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;



/**
 *
 * @author José Ignacio Orlando
 */
public class CorrespondenciaColumnas {
    
    public static int getColumna(char c) {
        // si es una letra
        if (((c>=65) && (c<=90)) || ((c>=97) && (c<=122))) {
            // si es una u
            if (c=='u') {
                return 3;
            } else if (c=='i') { // si es una i
                return 4;
            } else if (c=='l') { // si es una l
                return 23;
            } else {
                return 0;
            }
        } else if ((c>=48) && (c<=57)) { // si es un digito
            return 1;
        } else if (c==' ') { // si es un blanco
            return 2;
        } else if (c=='(') {
            return 5;
        } else if (c==')') {
            return 6;
        } else if (c=='*') {
            return 7;
        } else if (c=='\'') {
            return 8;
        } else if ((c=='+')  || (c=='{') || (c=='}')) {
            return 9;
        } else if (c=='-') {
            return 10;
        }else if (c=='/') {
            return 11;
        }else if (c=='.') {
            return 12;
        }else if (c==',') {
            return 13;
        }else if (c==';') {
            return 14;
        }else if (c==':') {
            return 15;
        }else if (c=='=') {
            return 16;
        }else if (c=='>') {
            return 17;
        }else if (c=='<') {
            return 18;
        }else if (c=='!') {
            return 19;
        }else if (c=='\t') {
            return 20;
        }else if ((c=='\r') || (c=='\n')) {
            return 21;
        }else if (c==((char)(0))) { // ¿fin de archivo?
            return 22;
        }
        return 24;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author José Ignacio Orlando
 */
public class Archivo {
    
    private String contenido;
    private int marca;
    private int posicionLectura;
    
    public static final int EOF = -1;
    
    public Archivo(File archivo) throws FileNotFoundException {
        FileInputStream fff = new FileInputStream(archivo);
        DataInputStream ff = new DataInputStream(fff);
        this.contenido = "";
        this.marca = 0;
        this.posicionLectura = 0;
        try {
            int leido = ff.read();
            while (leido!=Archivo.EOF) {
                contenido = contenido + ((char)(leido));
                leido = ff.read();
            }
            contenido = contenido + ((char)(0));
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error leyendo el archivo");
        }
    }
    
    public void mark() {
        this.marca = posicionLectura;
    }
    
    public char read() {
        char ret = this.contenido.charAt(this.posicionLectura);
        posicionLectura++;
        return ret;
    }
    
    public void reset() {
        this.posicionLectura = this.marca;
    }
    
    @Override
    public String toString() {
        return this.contenido;
    }
    
}

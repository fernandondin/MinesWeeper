/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.Serializable;
/**
 *
 * @author brandondin1
 */
public class Datos {
    public void escribe(Object o,String archivo) throws ClassNotFoundException,IOException{
        {
            File file = new File(archivo);
            System.out.println(file.exists());
                FileOutputStream f = new FileOutputStream(file);
                ObjectOutputStream s = new ObjectOutputStream(f);
                s.writeObject(o);
                s.close();   
        }
    }
    public Object lee(String archivo) throws ClassNotFoundException,IOException{
        File file = new File(archivo);
	FileInputStream f = new FileInputStream(file);
	ObjectInputStream s = new ObjectInputStream(f);
        Object object  = s.readObject();
	s.close();
        return object;
    }
}

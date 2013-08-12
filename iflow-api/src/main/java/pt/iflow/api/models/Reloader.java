package pt.iflow.api.models;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Reloader extends ClassLoader {
    
    @Override
    public Class<?> loadClass(String className) {
        return findClass(className);
    }

    @Override
    public Class<?> findClass(String className) {
        try {
            String str = "modelsClasses";
            String[] aux = className.split(str);
            String aux2 = null; 
            if(aux.length>1)
                aux2=str+aux[aux.length-1];
            else
                aux2 = className;
            byte[] bytes = loadClassData(className);
            return defineClass(aux2, bytes, 0, bytes.length);
        } catch (IOException ioe) {
            try {
                return super.loadClass(className);
            } catch (ClassNotFoundException ignore) { }
            ioe.printStackTrace(System.out);
            return null;
        }
    }

    private byte[] loadClassData(String className) throws IOException {
        File f = new File(className.replaceAll("\\.", "/") + ".class");
        int size = (int) f.length();
        byte buff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        dis.readFully(buff);
        dis.close();
        return buff;
    }
}
package pt.iflow.api.models;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import modelsClasses.AbstractModel;

import pt.iflow.api.utils.Const;

public class Reloader extends ClassLoader {
    
    @Override
    public Class<?> loadClass(String className) {
        return findClass(className);
    }

    @Override
    public Class<?> findClass(String className) {
        String aux2 = null;   
        String className2 = null;
        try {
            URL location = Reloader.class.getProtectionDomain().getCodeSource().getLocation();
            System.out.println("JM0: "+location.getFile());
            URL location2 = AbstractModel.class.getProtectionDomain().getCodeSource().getLocation();
            System.out.println("JM1: "+location2.getFile());
            //System.out.println("JM1: "+className);
            String str = "modelsClasses";
            String[] aux = className.split(str);
            
            if(aux.length>1){
              aux2=str+aux[aux.length-1];
              //System.out.println("JM2: "+aux2);
            }
            else{
              //System.out.println("JM3: "+className);
              aux2 = className;
            }
            byte[] bytes = loadClassData(className);
            return defineClass(aux2, bytes, 0, bytes.length);
        } catch (IOException ioe) {
            try {
              aux2 = className;
              className2 = Const.MODELS_PATH + className;
              byte[] bytes = loadClassData(className2);
              return defineClass(aux2, bytes, 0, bytes.length);
            } catch (IOException ioe2) {
              try {
                return super.loadClass(className);
              } catch (ClassNotFoundException ignore) { }
            }
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
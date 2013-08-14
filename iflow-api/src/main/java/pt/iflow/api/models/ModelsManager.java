package pt.iflow.api.models;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.iflow.api.utils.Const;
import model.Models;
import model.Objects;

import integration.IntegrationFactory;
import integration.ModelsAccess;
import integration.ObjectsAccess;

public class ModelsManager {
  
  
  static{  
    IntegrationFactory.setBaseURL("http://localhost:8081/DocMetaTag/obj");
  }
  private static ModelsAccess modelsAcc = new ModelsAccess();
  private static ObjectsAccess objAcc = new ObjectsAccess();
  
  public static Object[] listModels() {
    int i = 0;
    String[] modelsClassName =null;
    try {
      List<Models> modelsList = modelsAcc.GETDistinctModels();
      Integer arraySize = (modelsList.size()*2);
      modelsClassName = new String[arraySize]; 
      Iterator<Models> it = modelsList.iterator();
      while (it.hasNext())
      {
        Models m = it.next();
        modelsClassName[i++]= m.getModelName();
        modelsClassName[i++]= m.getModelName()+"Array";
      }
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return modelsClassName;
  }

  public static Object getObjInstance(Integer objId) {
    Object obj = null;
    try {
      obj= modelsAcc.getObjectInstance(Const.MODELS_PATH, objId);
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return obj;
  }

  public static Map<String, Class<?>> getModelProperties(Integer objId) {
    Map<String, Class<?>> props = null;
    try {
      Objects m = (Objects) objAcc.GETObjectsID(objId);
      props = modelsAcc.getModelProperties(m.getDocModel());
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return props;
  }
  
  
  

}

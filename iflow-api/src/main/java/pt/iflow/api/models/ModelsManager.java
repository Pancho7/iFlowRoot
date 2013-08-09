package pt.iflow.api.models;

import java.util.Iterator;
import java.util.List;
import model.Models;

import integration.IntegrationFactory;
import integration.ModelsAccess;

public class ModelsManager {
  
  
  static{  
    IntegrationFactory.setBaseURL("http://localhost:8081/DocMetaTag/obj");
  }
  private static ModelsAccess modelsAcc = new ModelsAccess();
  
  public static Object[] listModels() {
    //IntegrationFactory.setBaseURL("http://localhost:8081/DocMetaTag/obj");
    //ModelsAccess modelsAcc = new ModelsAccess();
    int i = 0;
    String[] modelsClassName =null;
    try {
      List<Models> modelsList = modelsAcc.GETDistinctModels();
      Integer arraySize = (modelsList.size()*2)+2;
      modelsClassName = new String[arraySize]; 
      Iterator<Models> it = modelsList.iterator();
      while (it.hasNext())
      {
        Models m = it.next();
        modelsClassName[i++]= m.getClassName();
        modelsClassName[i++]= m.getClassName()+"Array";
      }
      modelsClassName[i++]= "AbstractModel";
      modelsClassName[i++]= "AbstractModelArray";
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return modelsClassName;
  }

}

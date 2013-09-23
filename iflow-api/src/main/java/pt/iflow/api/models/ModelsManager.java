package pt.iflow.api.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.iflow.api.utils.Const;
import model.AbstractModelClass;
import model.Metatags;
import model.Models;

import integration.IntegrationFactory;
import integration.MetatagsAccess;
import integration.ModelsAccess;
import integration.ObjectsAccess;

public class ModelsManager {
  
  
  static{  
    IntegrationFactory.setBaseURL("http://LXWIN7019.infosistema.com:8081/DocMetaTag/obj");
  }
  private static ModelsAccess modelsAcc = new ModelsAccess();
  private static MetatagsAccess metatagsAcc = new MetatagsAccess();
  private static ObjectsAccess objAcc = new ObjectsAccess();
  //private static ObjectMetatagAccess objMetatagAcc = new ObjectMetatagAccess();
  
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
/*
  public static Map<String, Class<?>> getModelProperties(Integer objId) {
    Map<String, Class<?>> props = null;
    try {
      Objects m = (Objects) objAcc.GETObjectsID(objId);
      props = modelsAcc.getModelProperties(m.getDocModel());
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return props;
  }*/
  public static Map<String, Class<?>> getModelProperties(Integer objModel) {
    Map<String, Class<?>> props = null;
    try {
      //Objects m = (Objects) objAcc.GETObjectsID(objModel);
      props = modelsAcc.getModelProperties(objModel);
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return props;
  }

  public static Integer saveObj(Object value) {
    Integer id=null;
    try {
      
     //TODO JM preencher docModel e path
      
     ((AbstractModelClass)value).setPath("");
     //((AbstractModelClass)value).setId(6);
      
     id = objAcc.saveObjects((AbstractModelClass)value);
     ((AbstractModelClass)value).setId(id);
    } catch (Exception e) {
      System.err.println(e.toString());
    } 
    return id;
  }
  
  
  public static HashMap<Integer,String> getAllModels() {
    HashMap<Integer,String> mapRes = new HashMap<Integer,String>();
    try {
      List<Models> list = modelsAcc.GETDistinctModels();
      Iterator<Models> it = list.iterator();
      while(it.hasNext()) {
        Models m = it.next();
        mapRes.put(m.getModelspk().getId(), m.getModelName());
      }
    } catch (Exception e) {
      System.err.println(e.toString());
    } 
    return mapRes;
   }
  
  public static List<String>  getAllTagsFromModel(Integer modelId) {
    List<String> listRes = new ArrayList<String>(); 
    try {
      List<Object> list = modelsAcc.GETObjectsID(modelId);
      Iterator<Object> it = list.iterator();
      while(it.hasNext()) {
        Models m = (Models)it.next();
        String aux = ((Metatags)metatagsAcc.GETObjectsID(m.getModelspk().getMetaTagId())).getLabel();
        listRes.add(aux);
        System.out.println("lalalb"+aux); 
      }
    } catch (Exception e) {
      System.err.println(e.toString());
    } 
    return listRes;
   }
  
  public static List<String>  getAllModelsFromTag(Integer tagId) {
    List<String> listRes = new ArrayList<String>(); 
    try {
      List<Object> list = modelsAcc.GETAllModelsByMetatagId(tagId);
      Iterator<Object> it = list.iterator();
      while(it.hasNext()) {
        Models m = (Models)it.next();
        listRes.add(m.getModelName());
        System.out.println("lalalc"+m.getModelName()); 
      }
    } catch (Exception e) {
      System.err.println(e.toString());
    } 
    return listRes;
   }

  public static Object[] listTags(String modelName) {
    int i = 0;
    String[] tagsArray =null;
    List<String> tagNameList=null;
    try {
      if(modelName.equals("*")){
        tagNameList = new ArrayList<String>();
        tagNameList.add(modelName);
      }
      else{
        tagNameList = modelsAcc.GETAllTagNameByModelName(modelName);
        tagsArray = new String[tagNameList.size()];
        Iterator<String> it = tagNameList.iterator();
        while (it.hasNext())
        {
          tagsArray[i++] = it.next();
        }
      }
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return tagsArray;
  }
  
}

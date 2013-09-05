package pt.iflow.blocks.form;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import pt.iflow.api.blocks.FormProps;
import pt.iflow.api.models.ModelsManager;
import pt.iflow.api.processdata.ProcessData;
import pt.iflow.api.processdata.ProcessSimpleVariable;
import pt.iflow.api.processdata.ProcessVariable;
import pt.iflow.api.utils.ServletUtils;
import pt.iflow.api.utils.UserInfoInterface;
import pt.iknow.floweditor.blocks.JSPFieldData;

public class ObjectsModels implements FieldInterface {

  private ProcessSimpleVariable pv = null;

  public String getDescription() {
    return "�rea de Objectos";
  }

  public void generateHTML(PrintStream out, Properties prop) {
    // TODO
  }

  public void generateXSL(PrintStream out) {
    // TODO
  }

  public void generateXML(PrintStream out, Properties prop) {
    // TODO
  }

  public String getXML(Properties prop) {
    try {
      StringBuffer sb = new StringBuffer();

      HashMap<String, String> modelsRules = new HashMap<String, String>();
     
      
      
      String modelLabel = prop.getProperty("modelLabel");
      if (modelLabel == null){
        modelLabel = "";
      }

      String modelVarName = prop.getProperty("modelVarName");
      if (modelVarName == null){
        modelVarName = "";
      }
      
      
      String modelRulesArea = prop.getProperty("modelRulesArea");
      if (modelRulesArea == null){
        modelRulesArea = "";
      }
      
      String modelRulesTagArea = prop.getProperty("modelRulesTagArea"); 
      if (modelRulesTagArea == null){
        modelRulesTagArea = "";
      }
      
      String modelo="";
      String props="";
      String campo="";
      String valor="";
      
      String[] modelsLine = modelRulesArea.split("\n"); 
      for(int i= 0; i<modelsLine.length; i++){
        if(!modelsLine[i].startsWith("//")){
           String[] lineSplit = modelsLine[i].split("\\.");
           if(lineSplit.length==2){
             List<String> list = new ArrayList<String>();
             modelo = lineSplit[0].trim();
             props = lineSplit[1].trim();
             modelsRules.put(modelo, props);   
           }
        }
      }
      
      //HashMap<String, Boolean> tagsValorRules = new HashMap<String, Boolean>();
     
      HashMap<String, HashMap<String, HashMap<String, Boolean>>> tagsModelRules = new HashMap<String, HashMap<String,HashMap<String,Boolean>>>();
      
      String[] tagsLine = modelRulesTagArea.split("\n"); 
      for(int i= 0; i<tagsLine.length; i++){
        if(!tagsLine[i].startsWith("//")){
           String[] lineSplit = tagsLine[i].split("\\.");
           if(lineSplit.length==4){ 
             HashMap<String, Boolean> tagsPropsRules = new HashMap<String, Boolean>();
             HashMap<String, HashMap<String, Boolean>> tagsCampoRules = new HashMap<String, HashMap<String,Boolean>>();
             modelo = lineSplit[0].trim();
             campo = lineSplit[1].trim();
             props = lineSplit[2].trim();
             valor = lineSplit[3].trim();
             tagsPropsRules.put(props, Boolean.parseBoolean(valor));
             tagsCampoRules.put(campo, tagsPropsRules);
             tagsModelRules.put(modelo, tagsCampoRules);
           }
        }
      }
      
      Object objModel = null;
      //Object result=null;
      Object objId=null;
      
      if(pv!=null)
        if(pv.getValue()!=null){
          objModel  = pv.getValue();
          if(pv.getRawValue()!=null)
            objId = pv.getRawValue();
        }
          
      
      sb.append("<field><type>object_model</type>");
      sb.append("<modellabel>").append(modelLabel).append("</modellabel>");
      sb.append("<models>");
      String modelDataType = prop.getProperty("modelDataType");
      
      /*
      if(!objId.equals("")){
        Object obj = ModelsManager.getObjInstance(Integer.parseInt(objId));
        try {
          result = (Object) obj.getClass().getMethod("get"+modelName.getValue()).invoke(obj);
        } catch (Exception e) {
        } 
      } 
      */
      //TODO JM verifica se alem de ser abstract o id vem preenchido (se ja escolheu o fatura para o abstract)
      // verificar se no value já vem algum modelo selecionado e ele agora esta a selecionar um outro diferente.
      
      
      List<String> modelsResList = new ArrayList<String>();
      if(modelDataType!=null){
        if(modelDataType.equals("AbstractModel")){
          HashMap<Integer, String> modelsList = ModelsManager.getAllModels();
          modelsResList = checkModelsRules(modelsList, modelsRules);
          Iterator<String> it1 = modelsResList.iterator();      
          while (it1.hasNext()) {
              String m = it1.next();
              sb.append("<modelname>"+m+"</modelname>");
          }
        }
        else{
          sb.append("<modelname>"+modelDataType+"</modelname>");
          modelsResList.add(modelDataType);
        }
      }
      sb.append("</models>");    
      
      
      
      sb.append("<modelslist>");
      
      if(modelsResList.size()>0){
        sb.append(ModelXML(modelsResList,objId,objModel,tagsModelRules));
      }
      sb.append("</modelslist>");
      sb.append("</field>");
      System.out.println(sb.toString());
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private List<String> checkModelsRules(HashMap<Integer, String> modelList, HashMap<String, String> modelsRules) {

    List<String> listRes = new ArrayList<String>();

    Iterator<Entry<Integer, String>> it = modelList.entrySet().iterator();
    String aste = modelsRules.get("*");
    if(aste!=null){
      if(aste.equals("allow")){
        while (it.hasNext()) {
          Map.Entry<Integer, String> modelPair = (Map.Entry<Integer, String>)it.next();
          listRes.add(modelPair.getValue());         
          }
      }else
        listRes = new ArrayList<String>();
    }
    String rule=null;
    it = modelList.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<Integer, String> modelPair = (Map.Entry<Integer, String>)it.next();
        String modelName = modelPair.getValue();
        rule = modelsRules.get(modelName);
        if(rule!=null){
          if(rule.equals("allow"))
            listRes.add(modelName);
        }
    }
    /*
     *  *.deny
     *  fatura.allow
     * 
     */
    return listRes;
  }

  private Object ModelXML(List<String> modelsList, Object objId, Object objModel, HashMap<String, HashMap<String, HashMap<String, Boolean>>> tagsModelRules) {
    StringBuffer sb = new StringBuffer();
    Iterator<String> it = modelsList.iterator();
    String valor = "";
    Object result=null;
    //AbstractModelClass absModel = (AbstractModelClass)objId;
    while (it.hasNext()) {
        String model = it.next();
        Object[] fieldsList = ModelsManager.listTags(model);
        
        if(objModel!=null){
          //Object obj = ModelsManager.getObjInstance(absModel.getId());
          try {
            result = (Object) objModel.getClass().getMethod("get"+model).invoke(objModel);
          } catch (Exception e) {
          } 
          if(result!=null)
            valor = result.toString();
        }
        sb.append("<model>");
        sb.append("<modelname>");
        sb.append(model);
        sb.append("</modelname>");
        sb.append("<objid>");
        if(objId!=null)
          sb.append(objId);
        sb.append("</objid>");
        sb.append("<modelfieldlist>");
        for(int i = 0;i<fieldsList.length;i++)
        {
          //if(checkVisible(model,(String)fieldsList[i],tagsModelRules)){
            sb.append("<modelfield>");
            sb.append("<name>");
            sb.append((String)fieldsList[i]);
            sb.append("</name>");
            sb.append("<valor>");
            try {
              valor = ((Object) objModel.getClass().getMethod("get"+fieldsList[i]).invoke(objModel)).toString();
            } catch (Exception e) {
            } 
            sb.append(valor);
            sb.append("</valor>");
            sb.append("<editavel>");
            //if(checkEditavel(model,(String)fieldsList[i],tagsModelRules))
              sb.append("true");
            //else
             // sb.append("false");
            sb.append("</editavel>");
            sb.append("<obrigatorio>");
            //if(checkObrigatorio(model,(String)fieldsList[i],tagsModelRules))
              sb.append("true");
           // else
            //  sb.append("false");
            sb.append("</obrigatorio>");
            sb.append("</modelfield>");
          //}
        }
        sb.append("</modelfieldlist>");
        sb.append("</model>");
    }
    return sb.toString();
  }

  

  private boolean checkObrigatorio(String model, String string,
      HashMap<String, HashMap<String, HashMap<String, Boolean>>> tagsModelRules) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean checkEditavel(String model, String string,
      HashMap<String, HashMap<String, HashMap<String, Boolean>>> tagsModelRules) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean checkVisible(String model, String string,
      HashMap<String, HashMap<String, HashMap<String, Boolean>>> tagsModelRules) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isOutputField() {
    return false;
  }

  public boolean isArrayTable() {
    return false;
  }

  public void setup(UserInfoInterface userInfo, ProcessData procData, Properties props, ServletUtils response) {
  }

  public void initVariable(UserInfoInterface userInfo, ProcessData procData, String name, Properties props) {
    if(name!=null){
      pv =  procData.get(name);
      props.setProperty("modelDataType", pv.getType().toString());
    }
  }

  private String replaceLeftBar(String textAreaValue) {
    StringBuffer result = new StringBuffer();
    if(textAreaValue != null && textAreaValue.length() > 0){
      result.append(textAreaValue.replaceAll("\\r\\n", ""));
    } else {
      return "";
    }
    return result.toString();
  }
}

package pt.iflow.api.datatypes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import pt.iflow.api.blocks.FormService;
import pt.iflow.api.blocks.FormUtils;
import pt.iflow.api.datatypes.msg.Messages;
import pt.iflow.api.processdata.ProcessData;
import pt.iflow.api.processdata.ProcessListVariable;
import pt.iflow.api.processdata.ProcessVariableValue;
import pt.iflow.api.processtype.ProcessDataType;
import pt.iflow.api.utils.Logger;
import pt.iflow.api.utils.ServletUtils;
import pt.iflow.api.utils.UserInfoInterface;
import pt.iknow.utils.html.FormData;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ObjectModel implements DataTypeInterface {

  public ObjectModel() {
  }

  public int getID() {
    return 4;
  }

  public String getDescription() {
    return Messages.getString("ObjectModel.description"); //$NON-NLS-1$
  }

  public String getShortDescription() {
    return Messages.getString("ObjectModel.short_description"); //$NON-NLS-1$
  }

  public String getPrimitiveType() {
    return getPrimitiveTypeMethod();
  }

  public String getPrimitiveTypeMethod() {
    return "String"; //$NON-NLS-1$
  }

  public int getFormSize() {
    return 50;
  }

  public String getFormPrefix() {
    return getFormPrefix(null);
  }
  public String getFormPrefix(Object[] aoaArgs) {
    return ""; //$NON-NLS-1$
  }

  public String getFormSuffix() {
    return getFormSuffix(null);
  }
  public String getFormSuffix(Object[] aoaArgs) {
    return ""; //$NON-NLS-1$
  }

  public String validateFormData (Object input) {
    return validateFormData(input,null);
  }

  public String validateFormData (Object input, Object[] aoaArgs) {
    return null;
  }

  public String formatToHtml (Object input) {
    return formatToHtml(input,null);
  }

  public String formatToHtml (Object input, Object[] aoaArgs) {
    String retObj = ""; //$NON-NLS-1$

    if (input != null) {
      retObj = (String)input;
    }

    return retObj;
  }

  public String formatToForm (Object input) {
    return formatToForm(input,null);
  }

  public String formatToForm (Object input, Object[] aoaArgs) {
    String retObj = ""; //$NON-NLS-1$

    if (input != null) {
      retObj = (String)input;
    }

    return retObj;
  }

  public String getText(Object input) {
    return value(input);
  }

  public double getValue(Object input) {
    return java.lang.Double.NaN;
  }

  public static String value (Object s) {
    return (String)s;
  }

  public void setLocale(Locale locale) {
  }

  public String format(UserInfoInterface userInfo, ProcessData procData, FormService service, 
      int fieldNumber, String name,
      ProcessVariableValue value, Properties props, ServletUtils response) {
    return formatRow(userInfo, procData, service, fieldNumber, -1, name, -1, value, props, response);
  }

  public String formatRow(UserInfoInterface userInfo, ProcessData procData, FormService service, 
      int fieldNumber, int varIndex, String name,
      int row, ProcessVariableValue value, Properties props, ServletUtils response) {
    String ret = "";
    if (value != null)
      ret = value.format();
    
    return StringUtils.isEmpty(ret) ? "" : ret; 
  }

  public void init(UserInfoInterface userInfo, ProcessData procData, Map<String, String> extraProps, Map<String, Object> deps) {
  }

  public String parseAndSet(UserInfoInterface userInfo, 
      ProcessData procData, String name, FormData formData, Properties props, boolean ignoreValidation, StringBuilder logBuffer) {
    String value = parseValue(name, formData);
    
    HashMap<java.lang.Integer, List<String>> tagsRules = new HashMap<java.lang.Integer, List<String>>();
    
    String modelRulesTagArea = props.getProperty("modelRulesTagArea"); 
    if (modelRulesTagArea == null){
      modelRulesTagArea = "";
    }
    
    String modelo="";
    String prop="";
    String campo="";
    String valor="";
    
    String[] tagsLine = modelRulesTagArea.split("\n"); 
    for(int i= 0; i<tagsLine.length; i++){
      if(!tagsLine[i].startsWith("//")){
         String[] lineSplit = tagsLine[i].split("\\.");
         if(lineSplit.length==4){ 
           List<String> listAux = new ArrayList<String>();
           modelo = lineSplit[0].trim();
           campo = lineSplit[1].trim();
           prop = lineSplit[2].trim();
           valor = lineSplit[3].trim();
           listAux.add(modelo);
           listAux.add(campo);
           listAux.add(prop);
           listAux.add(valor);
           tagsRules.put(i, listAux);
         }
      }
    }
    
    ProcessDataType varType = procData.getVariableDataType(name);
    Class<?> varClass = varType.getSupportingClass();
    
    Object obj=null;
    
    try {
      obj = varClass.newInstance();
    } catch (Exception e) {
      
    } 
    HashMap<String, String> modelValues = new HashMap<String, String>();
    
    Map<String, String[]> aux = formData.getParameters();
    Iterator<Entry<String, String[]>> it = aux.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<String, String[]> pairs = (Map.Entry<String, String[]>)it.next();
        if(pairs.getKey().startsWith(name+".")){
          String auxStr= pairs.getValue()[0];
          String auxStr2 = pairs.getKey().substring(pairs.getKey().lastIndexOf(name+"."));
          String metodoSet = auxStr2.replaceAll(name+".", "set");
          String metodoGet = metodoSet.replaceAll("set", "get");
          modelValues.put(auxStr2, auxStr);
          try {
            Class<?> clz = obj.getClass().getMethod(metodoGet).getReturnType();
            if(clz==java.lang.Integer.class){
              java.lang.Integer intVal = java.lang.Integer.parseInt(auxStr);
              varClass.getMethod(metodoSet, clz).invoke(obj, intVal);
            }else if(clz==java.util.Date.class){
              //TODO JM Falta tratar os casos das datas
              java.util.Date d = new java.util.Date();
              varClass.getMethod(metodoSet, clz).invoke(obj, d);
            }else{
              varClass.getMethod(metodoSet, clz).invoke(obj, auxStr);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          if (!ignoreValidation && checkObrigatorio(value,metodoSet.substring(3),tagsRules) && auxStr.isEmpty()) {
            return userInfo.getMessages().getString("Datatype.required_field");
          }
        }        
    }

    DataTypeUtils.setObject(userInfo, procData, name, obj, null);
    logBuffer.append("Set '" + name + "' with '" + value + "';");
    debug(userInfo, "parseAndSet", "Set '" + name + "' with '" + value + "'");

    return null;
  }
  
  private boolean checkObrigatorio(String model, String field, HashMap<java.lang.Integer, List<String>> tagsRules) {
    Boolean res = false;
    Iterator<Entry<java.lang.Integer, List<String>>> it = tagsRules.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<java.lang.Integer, List<String>> occur = (Map.Entry<java.lang.Integer, List<String>>)it.next();
        List<String> listAux = occur.getValue();
        if((listAux.get(0).equals(model)||listAux.get(0).equals("*"))&&(listAux.get(1).equals(field)||listAux.get(1).equals("*"))
            &&listAux.get(2).equals("mandatory")&&listAux.get(3).equals("false"))
          res = false;
        if((listAux.get(0).equals(model)||listAux.get(0).equals("*"))&&(listAux.get(1).equals(field)||listAux.get(1).equals("*"))
            &&listAux.get(2).equals("mandatory")&&listAux.get(3).equals("true"))
          res = true;
    }        
    return res;  
  }

  public String parseAndSetList(UserInfoInterface userInfo, 
      ProcessData procData, int varIndex, String name, int count,
      FormData formData, Properties props, boolean ignoreValidation, StringBuilder logBuffer) {

    ProcessListVariable list = procData.getList(name);
    for (int i = 0; i < count; i++) {
      if (FormUtils.checkOutputField(props, varIndex, i)) {
        debug(userInfo, "parseAndSetList", "Row " + i + " of list var '" + name + " is output only... continuing to next row.");
        continue;
      }
      String value = parseValue(FormUtils.getListKey(name, i), formData);
      DataTypeUtils.setListObject(userInfo, procData, name, i, value, null);
      logBuffer.append("Set list var '" + name + "[" + i + "]' with '" + value + "';");
      debug(userInfo, "parseAndSetList", "Set list var '" + name + "[" + i + "]' with '" + value + "'");
    }

    if (!ignoreValidation && 
        FormUtils.checkRequiredField(userInfo, procData, props) &&
        list.size() == 0) {
      return userInfo.getMessages().getString("Datatype.required_field");
    }

    return null;
  }

  private String parseValue(String name, FormData formData) {
    String value = null;
    
    if (formData.hasParameter(name)) {
      String formValue = formData.getParameter(name);
      if (StringUtils.isNotEmpty(formValue)) {
        value =  formValue;
      }
    }
    return value;
  }

  public void formPreProcess(UserInfoInterface userInfo, ProcessData procData, String name, Properties props, StringBuilder logBuffer) {
  }

  private void debug(UserInfoInterface userInfo, String method, String message) {
    if (Logger.isDebugEnabled()) {
      Logger.debug(userInfo.getUtilizador(), this, method, message);
    }
  }
}

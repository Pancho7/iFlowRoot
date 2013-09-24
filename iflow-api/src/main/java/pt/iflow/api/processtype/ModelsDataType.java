package pt.iflow.api.processtype;

import java.text.ParseException;
import java.util.HashMap;

import modelsClasses.AbstractModel;

import pt.iflow.api.models.ModelsManager;
import pt.iflow.api.models.Reloader;
import pt.iflow.api.utils.Const;

public class ModelsDataType implements ProcessDataType {

  private String modelName = DataTypeEnum.AbstractModel.toString();
  private static HashMap<String, Class<?>> loadedClasses = new HashMap<String, Class<?>>();
  
  public void setClass(String modelName) {
    this.modelName = modelName;
  }
  
  @Override
	public String toString() {
		return modelName;
	}
	
	public Class<?> getSupportingClass() {
	  if (!loadedClasses.containsKey(modelName)) {
        Class<?> c = new Reloader().loadClass(Const.MODELS_PATH+"modelsClasses."+modelName);
        loadedClasses.put(modelName, c);
      }
      return loadedClasses.get(modelName);
	}
	
	public static Class<?> getSupportingClassForModel(String modelName) {
	    Class<?> c = new Reloader().loadClass(Const.MODELS_PATH+"modelsClasses."+modelName);
	    loadedClasses.put(modelName, c);
	    try {
          Class<?> sc = c.getSuperclass();
          String scName = sc.getSimpleName();
          if (!loadedClasses.containsKey(scName))
            loadedClasses.put(scName, sc);
	    } catch (Exception e) { }
	    return loadedClasses.get(modelName);
	}

  public Object convertFrom(String rawvalue) throws ParseException {
    //Vai buscar a instancia do objecto. 
    if(rawvalue!=null)
      return ModelsManager.getObjInstance(Integer.parseInt(rawvalue));
    else 
      return null;
  }

  public String convertTo(Object value) {
    if(value!=null)
      return ((AbstractModel)value).getId() == null ? null : ((AbstractModel)value).getId().toString();
    return null;
  }

  public Object parse(String source) throws ParseException {
    return null;
  }

  public String format(Object obj) {
    return null;
  }

  public boolean isBindable() {
    return false;
  }

  public String saveExternal(Object value) {
    //TODO Guardar nos metas e se o id for null actualizar
    if(value == null) return null;
    Integer id = ModelsManager.saveObj(value);
    if (((AbstractModel)value).getId() == null) ((AbstractModel)value).setId(id);
    return id.toString();
  }

}

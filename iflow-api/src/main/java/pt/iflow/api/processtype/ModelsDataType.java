package pt.iflow.api.processtype;

import java.text.ParseException;

import model.AbstractModelClass;

import pt.iflow.api.models.ModelsManager;
import pt.iflow.api.models.Reloader;
import pt.iflow.api.utils.Const;

public class ModelsDataType implements ProcessDataType {

  private String modelName = DataTypeEnum.AbstractModel.toString();
  
  public void setClass(String modelName) {
    this.modelName = modelName;
  }
  
  @Override
	public String toString() {
		return modelName;
	}
	
	public Class<?> getSupportingClass() {
      return new Reloader().loadClass(Const.MODELS_PATH+"modelsClasses."+modelName);
	}

  public Object convertFrom(String rawvalue) throws ParseException {
    //Vai buscar a instancia do objecto. 
    if(rawvalue!=null)
      return ModelsManager.getObjInstance(Integer.parseInt(rawvalue));
    else 
      return null;
  }

  public String convertTo(Object value) {
    return ((AbstractModelClass)value).getId() == null ? null : ((AbstractModelClass)value).getId().toString();
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

  public void saveExternal(Object value) {
    //TODO Guardar nos metas e se o id for null actualizar
    if(value == null) return;
    Integer id = ModelsManager.saveObj(value);
    if (((AbstractModelClass)value).getId() == null) ((AbstractModelClass)value).setId(id);
  }

}

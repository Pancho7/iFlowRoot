package pt.iflow.api.processtype;

import java.text.ParseException;

import pt.iflow.api.models.Reloader;
import pt.iflow.api.utils.Const;

public class ModelsDataType implements ProcessDataType {

  private String modelName;
  
  public ModelsDataType(String modelName) {
    this.modelName = modelName;
  }
  
  @Override
	public String toString() {
		return modelName;
	}
	
	public Class<?> getSupportingClass() {
//      return pt.iflow.api.documents.DocumentData.class;  
      //return Document.class;
      return new Reloader().loadClass(Const.MODELS_PATH+"modelsClasses."+modelName);
	}

  public Object convertFrom(String rawvalue) throws ParseException {
    return null;
  }

  public String convertTo(Object value) {
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
}

package pt.iknow.floweditor.blocks;

import pt.iflow.api.datatypes.DataTypeInterface;
import pt.iknow.floweditor.FlowEditorAdapter;
import pt.iknow.floweditor.blocks.JSPFieldData.PropDependency;
import pt.iknow.floweditor.blocks.JSPFieldData.PropDependencyItem;

public class JSPObjectsModels extends JSPFieldData {

//id constructor
 public JSPObjectsModels(FlowEditorAdapter adapter, int anID) {
   super(adapter, anID);
 }

 // simple constructor
 public JSPObjectsModels(FlowEditorAdapter adapter) {
   this(adapter, -1);
 }


 // full constructor
 public JSPObjectsModels(FlowEditorAdapter adapter, int anID, int anPosition, String asText, String asVarName, int anCols, int anRows) {
   this(adapter, anID);
   this._nPosition = anPosition;

   // now set all field properties
   this.setProperty(JSPFieldData.nPROP_TEXT,asText);
   this.setProperty(JSPFieldData.nPROP_VAR_NAME,asVarName);

 }

 public JSPObjectsModels(JSPFieldData afdData) {
   super(afdData);
 }

 protected void init() {
   this._nFieldType = JSPFieldTypeEnum.FIELD_TYPE_OBJECTSMODELS;

   // add text single properties
   this._alEditSingleProps.add(new Integer(JSPFieldData.nPROP_MODELS_LABEL));
   this._alEditSingleProps.add(new Integer(JSPFieldData.nPROP_MODELS_VAR_NAME));
   this._alEditSingleProps.add(new Integer(JSPFieldData.nPROP_MODELS_RULES_AREA));
   this._alEditSingleProps.add(new Integer(JSPFieldData.nPROP_TAGS_RULES_AREA));

   // add static/constant properties
   
   DataTypeInterface dti = loadDataType(adapter, "pt.iflow.api.datatypes.ObjectModel");
   if(dti != null) {
     this.setStaticProperty(JSPFieldData.nPROP_DATA_TYPE, dti.getDescription());
   } else {
     this.setStaticProperty(JSPFieldData.nPROP_DATA_TYPE, "ObjectModel");
   }

   // add required properties
   
   this._alRequiredProps.add(new Integer(JSPFieldData.nPROP_MODELS_VAR_NAME));
   
   // add models props
   this._alModelProps.add(new Integer(JSPFieldData.nPROP_MODELS_RULES_AREA));
   this._alModelProps.add(new Integer(JSPFieldData.nPROP_TAGS_RULES_AREA));
   
   

   
   PropDependency pd = new PropDependency(JSPFieldData.nPROP_MODELS_VAR_NAME, PropDependency.nDISABLE, PropDependency.nABSTRACT_MODEL);
   PropDependencyItem pdi = new PropDependencyItem(JSPFieldData.nPROP_MODELS_RULES_AREA, PropDependency.nENABLE);
   pd.addDependency(pdi);
   this._hmPropDependencies.put(new Integer(JSPFieldData.nPROP_MODELS_VAR_NAME), pd);
   

 }
}

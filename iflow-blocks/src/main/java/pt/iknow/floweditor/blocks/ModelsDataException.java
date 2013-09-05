package pt.iknow.floweditor.blocks;

import pt.iknow.floweditor.FlowEditorAdapter;

public class ModelsDataException extends Exception{

  private static final long serialVersionUID = -7612896638221609570L;

  private StringBuffer _sbErrMsg;

  public ModelsDataException(FlowEditorAdapter adapter, Integer aiPropID) {
    this(adapter, null, aiPropID);
  }

  public ModelsDataException(FlowEditorAdapter adapter, Integer aiRow, Integer aiPropID) {
    final String _sLinePrefix = adapter.getString("ModelsDataException.line"); //$NON-NLS-1$
    final String _sLinePrefixJunction = " - "; //$NON-NLS-1$
    final String _sPrefix = adapter.getString("ModelsDataException.the.field"); //$NON-NLS-1$
    final String _sReqSuffix = adapter.getString("ModelsDataException.obligatory.field"); //$NON-NLS-1$
    this._sbErrMsg = new StringBuffer();
    if (aiRow != null) {
      int nRow = aiRow.intValue() + 1;
      this._sbErrMsg.append(_sLinePrefix);
      this._sbErrMsg.append(nRow);
      this._sbErrMsg.append(_sLinePrefixJunction);
    }

    this._sbErrMsg.append(_sPrefix);

    this._sbErrMsg.append(JSPFieldData.getPropLabel(aiPropID));
    this._sbErrMsg.append(_sReqSuffix);
    
  }

  public ModelsDataException(FlowEditorAdapter adapter, int anPropID) {
    this(adapter, new Integer(anPropID));
  }

  public ModelsDataException(FlowEditorAdapter adapter, int anRow, int anPropID) {
    this(adapter, new Integer(anRow), new Integer(anPropID));
  }

  public String getMessage() {
    return _sbErrMsg.toString();
  }

  public void append(ModelsDataException amde) {
    this._sbErrMsg.append(amde.getMessage());
  }
}

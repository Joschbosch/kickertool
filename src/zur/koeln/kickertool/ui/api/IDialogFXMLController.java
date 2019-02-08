package zur.koeln.kickertool.ui.api;

public interface IDialogFXMLController<T> extends IFXMLController{
	
	void applyDialogOKClicked();
	
	default T sendDialogResult() {
		return null;
	};
}

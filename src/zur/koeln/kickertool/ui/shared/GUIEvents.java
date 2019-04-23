package zur.koeln.kickertool.ui.shared;

public enum GUIEvents {

	MATCH_RESULT_ENTERED(0);
	
	private int id;

	private GUIEvents(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}

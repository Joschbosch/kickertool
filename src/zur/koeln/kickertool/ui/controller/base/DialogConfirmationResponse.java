package zur.koeln.kickertool.ui.controller.base;

public class DialogConfirmationResponse {
	
	private final boolean accepted;

	public DialogConfirmationResponse(boolean accepted) {
		super();
		this.accepted = accepted;
	}
	
	public boolean isAccepted() {
		return accepted;
	}
	
	public boolean isDeclined() {
		return !accepted;
	}
	
}

package bikeGarageDatabase;

public class UnavailableOperationException extends Exception {
	private String message;
	
	public UnavailableOperationException() {
		
	}
	
	public UnavailableOperationException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}

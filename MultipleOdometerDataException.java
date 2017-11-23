
@SuppressWarnings("serial")
public class MultipleOdometerDataException extends Exception{

	public MultipleOdometerDataException() {
		super("Only one intance of OdometerData can be created.");
	}

}


public class TestOdoData {
	
	public static void main(String[] args) throws MultipleOdometerDataException, InterruptedException {
		
		
		// Set up time out 
		long timeout = 8000;
		
		// create a thread pool
		Thread[] tpool = new Thread[3]; 
		
		// create odoData
		OdometerData odoData = OdometerData.getOdometerData();
		
		MockOdometer odometer = new MockOdometer(odoData,timeout); // Create a mock odometer
		MockCorrector odoCorrector = new MockCorrector(odoData, timeout); // Create a mock odometer
		Display odoDisplay = new Display(odoData,timeout); // Create a display
		
		// Populate the thread pool
		tpool[0] = new Thread((Runnable) odometer);  
		tpool[1] = new Thread((Runnable) odoCorrector);
		tpool[2] = new Thread((Runnable) odoDisplay);
		
		// Start Threads
		for (int i = 0; i < tpool.length; i++) {
			tpool[i].start();
		}
		
		// Join threads
		for (int i = 0; i < tpool.length; i++) {
			tpool[i].join();
		}
			
	}
}

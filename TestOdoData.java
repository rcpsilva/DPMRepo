
public class TestOdoData {
	
	public static void main(String[] args) throws MultipleOdometerDataException, InterruptedException {
		
		long timeout = 8000;
		
		Thread[] tpool = new Thread[3]; 
		
		OdometerData odoData = OdometerData.getOdometerData();
		MockOdometer odometer = new MockOdometer(odoData,timeout);
		MockCorrector odoCorrector = new MockCorrector(odoData, timeout);
		Display odoDisplay = new Display(odoData,timeout);
		
		tpool[0] = new Thread((Runnable) odometer);
		tpool[1] = new Thread((Runnable) odoCorrector);
		tpool[2] = new Thread((Runnable) odoDisplay);
		
		
		for (int i = 0; i < tpool.length; i++) {
			tpool[i].start();
		}
		
		for (int i = 0; i < tpool.length; i++) {
			tpool[i].join();
		}
			
	}
}

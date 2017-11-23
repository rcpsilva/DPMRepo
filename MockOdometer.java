
/**
 * This class simulates an odometer
 * 
 * @author Rodrigo Silva
 *
 */
public class MockOdometer implements Runnable{

	private OdometerData odoData;
	private static final long ODOMETER_PERIOD = 25;
	private long timeout = Long.MAX_VALUE;


	public MockOdometer(OdometerData odoData) {
		this.odoData = odoData;
	}
	
	public MockOdometer(OdometerData odoData,long timeout) {
		this.odoData = odoData;
		this.timeout = timeout;
	}

	@Override
	public void run() {
		long updateStart, updateEnd;
		long tStart = System.currentTimeMillis();
		
		do {
			updateStart = System.currentTimeMillis();

			// Compute the change in the odometer data
			float dx = (float) Math.random();
			float dy = (float) Math.random();
			float dtheta = (float) Math.random();

			// Update odometer data
			odoData.update(dx, dy, dtheta);

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}while((updateEnd - tStart) <= timeout);	

	}



}

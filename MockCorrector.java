/**
 * This class implements a mock odometry corrector
 * 
 * @author Rodrigo Silva
 *
 */
public class MockCorrector implements Runnable{

	private OdometerData odoData;
	private long timeout; 
	private static final long ODOMETER_PERIOD = 100;


	public MockCorrector(OdometerData odoData) {
		this.odoData = odoData;
		this.timeout  = Long.MAX_VALUE; 
	}
	
	public MockCorrector(OdometerData odoData, long timeout) {
		this.odoData = odoData;
		this.timeout  = timeout; 
	}

	
	@Override
	public void run() {
		long updateStart, updateEnd;

		long tStart = System.currentTimeMillis();
		
		int count = 0;
		do {
			updateStart = System.currentTimeMillis();

			// Compute the new values for the odometer data
			float x = 10;
			float y = 5;
			float theta = 360;
			
			
			// Update odometer data
			switch (count%4) {
			case 0:
				odoData.reset(x, y, theta);
				break;
			case 1:
				odoData.resetX(x);
				break;
			case 2:
				odoData.resetY(y);
				break;
			case 3:
				odoData.resetTheta(theta);
				break;

			default:
				break;
			}

			count++;
			
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

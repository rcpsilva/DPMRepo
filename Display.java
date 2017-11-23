
public class Display implements Runnable{

	private OdometerData odoData;
	private float[] position = new float[3];
	private final long DISPLAY_PERIOD = 10;
	private long timeout = Long.MAX_VALUE;


	public Display(OdometerData odoData) {
		this.odoData = odoData;
	}
	
	public Display(OdometerData odoData, long timeout) {
		this.odoData = odoData;
		this.timeout = timeout;
	}
	
	
	public void run() {
		long updateStart, updateEnd;

		long tStart = System.currentTimeMillis();
		do {
			updateStart = System.currentTimeMillis();

			position = odoData.getXYT(position);
			
			System.out.printf("x = %.2f y = %.2f theta = %.2f\n", position[0],position[1],position[2]);
			
			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < DISPLAY_PERIOD) {
				try {
					Thread.sleep(DISPLAY_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}while((updateEnd - tStart) <= timeout);

	}

}

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class stores and provides thread safe access to the odometer data.
 * 
 * @author Rodrigo Silva 
 * @author Dirk Dubois
 * @author Derek Yu
 *
 */

public class OdometerData {

	// Position parameters
	private volatile float x; // x-axis position
	private volatile float y; // y-axis position
	private volatile float theta; // Head angle

	// Class control variables
	private volatile static int numberOfIntances = 0; // Number of OdometerData objects instantiated so far
	private static final int MAX_INSTANCES = 1; // Maximum number of OdometerData instances

	// Thread control tools
	private static Lock lock = new ReentrantLock(true); // Fair lock for concurrent writing
	private volatile boolean isReseting = false; // Indicates if a thread is trying to reset any position parameters
	private Condition doneReseting = lock.newCondition(); // Let other threads know that a reset operation is over.

	/**
	 * Default constructor. The constructor is private. A factory is used instead
	 * such that only one instance of this class is ever created.
	 */
	private OdometerData() {
		this.x = 0;
		this.y = 0;
		this.theta = 0;
	}

	/**
	 * OdometerData factory. Returns an OdometerData instance and makes sure that
	 * only one instance is ever created. If the user tries to instantiate multiple
	 * objects, the method throws a MultipleOdometerDataException.
	 * 
	 * @return An OdometerData object
	 * @throws MultipleOdometerDataException
	 */
	public synchronized static OdometerData getOdometerData() throws MultipleOdometerDataException {
		if (numberOfIntances < MAX_INSTANCES) {
			numberOfIntances += 1;
			return new OdometerData();
		} else { // I could not figure out how to return the previously created object
			throw new MultipleOdometerDataException();
		}
	}

	/**
	 * Return the Odomometer data.
	 * <p>
	 * Writes the current position and orientation of the robot onto the odoData
	 * array. odoData[0] = x, odoData[1] = y; odoData[2] = theta;
	 * 
	 * @param position the array to store the odometer data
	 * @return the odometer data.
	 */
	public float[] getXYT(float[] position) {
		while (isReseting) { // If a reset operation is being executed, wait until it is over.
			try {
				doneReseting.await(); // Using await() is lighter on the CPU than simple busy wait.
			} catch (InterruptedException e) {
				// At this point I am not sure about how to treat this exception
				e.printStackTrace();
			}
		}

		position[0] = x;
		position[1] = y;
		position[2] = theta;

		return position;
	}

	/**
	 * Adds dx, dy and dtheta to the current values of x, y and theta, respectively.
	 * Useful for odometry.
	 * 
	 * @param dx
	 * @param dy
	 * @param dtheta
	 */
	public void update(float dx, float dy, float dtheta) {
		lock.lock();
		try {
			x += dx;
			y += dy;
			theta = (theta + (360 + dtheta) % 360) % 360; // keeps the updates within 360 degrees
		} finally {
			lock.unlock();
		}

	}

	/**
	 * Overrides the values of x, y and theta. Use for odometry correction.
	 * 
	 * @param x the value of x
	 * @param y the value of y
	 * @param theta the value of theta
	 */
	public void reset(float x, float y, float theta) {
		lock.lock();
		isReseting = true;
		try {
			this.x = x;
			this.y = y;
			this.theta = theta;
			isReseting = false; // Done reseting
			doneReseting.signalAll(); // Let the other threads know that you are done reseting
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Overrides x. Use for odometry correction.
	 * 
	 * @param x the value of x
	 */
	public void resetX(float x) {
		lock.lock();
		isReseting = true;
		try {
			this.x = x;
			isReseting = false; // Done reseting
			doneReseting.signalAll(); // Let the other threads know that you are done reseting
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Overrides y. Use for odometry correction.
	 * 
	 * @param y the value of y
	 */
	public void resetY(float y) {
		lock.lock();
		isReseting = true;
		try {
			this.y = y;
			isReseting = false; // Done reseting
			doneReseting.signalAll(); // Let the other threads know that you are done reseting
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Overrides theta. Use for odometry correction.
	 * 
	 * @param theta the value of theta
	 */
	public void resetTheta(float theta) {
		lock.lock();
		isReseting = true;
		try {
			this.theta = theta;
			isReseting = false; // Done reseting
			doneReseting.signalAll(); // Let the other threads know that you are done reseting
		} finally {
			lock.unlock();
		}
	}

}

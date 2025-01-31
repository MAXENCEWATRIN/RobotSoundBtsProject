package i2c.samples.motorHAT;

import com.pi4j.io.i2c.I2CFactory;
import i2c.servo.adafruitmotorhat.AdafruitMotorHAT;

import java.io.IOException;

/*
 * See https://learn.adafruit.com/adafruit-dc-and-stepper-motor-hat-for-raspberry-pi/using-stepper-motors
 */
public class StepperDemo {
	private AdafruitMotorHAT mh;
	private AdafruitMotorHAT.AdafruitStepperMotor stepper;

	private boolean keepGoing = true;
	private final static String DEFAULT_RPM = "30";
	int nbSteps = 100;

	private static int nbStepsPerRev = AdafruitMotorHAT.AdafruitStepperMotor.DEFAULT_NB_STEPS; // 200 steps per rev

	public StepperDemo() throws I2CFactory.UnsupportedBusNumberException {

		System.out.println("Starting Stepper Demo");
		int rpm = Integer.parseInt(System.getProperty("rpm", DEFAULT_RPM));
		System.out.println(String.format("RPM set to %d.", rpm));

		nbSteps = Integer.parseInt(System.getProperty("steps", "100"));

		this.mh = new AdafruitMotorHAT(nbStepsPerRev); // Default addr 0x60
		this.stepper = mh.getStepper(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M1_M2);
		this.stepper.setSpeed(rpm); // Default 30 RPM
	}

	public void go() {
		keepGoing = true;
		while (keepGoing) {
			try {
				System.out.println(String.format(
						"-----------------------------------------------------------------------------------\n" +
						"Motor # %d, RPM set to %f, %d Steps per Rev, %f sec per step, %d steps per move.\n" +
						"-----------------------------------------------------------------------------------",
						this.stepper.getMotorNum(),
						this.stepper.getRPM(),
						this.stepper.getStepPerRev(),
						this.stepper.getSecPerStep(),
						nbSteps));

				if (keepGoing) {
					System.out.println("-- 2. Double coil steps --");
					System.out.println("  Forward");
					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.DOUBLE);
				}

     
				if (keepGoing) {
					System.out.println("  Backward");
					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.DOUBLE);
				}
        /*

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			System.out.println("==== Again! ====");
		}
		System.out.println("... Done with the demo ...");
//	try { Thread.sleep(1_000); } catch (Exception ex) {} // Wait for the motors to be released.
	}

	public void stop() {
		this.keepGoing = false;
		if (mh != null) {
			try { // Release all
				mh.getMotor(AdafruitMotorHAT.Motor.M1).run(AdafruitMotorHAT.ServoCommand.RELEASE);
				mh.getMotor(AdafruitMotorHAT.Motor.M3).run(AdafruitMotorHAT.ServoCommand.RELEASE);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * System properties:
	 * rpm, default 30
	 * hat.debug, default false
	 *
	 * @param args Not used
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
		StepperDemo demo = new StepperDemo();
		System.out.println("Hit Ctrl-C to stop the demo");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			demo.stop();
			try { Thread.sleep(5000); } catch (Exception absorbed) {}
		}));

		demo.go();

		System.out.println("Bye.");
	}
}

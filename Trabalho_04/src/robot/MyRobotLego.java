package robot;

import RobotLego.RobotLego;
import robot.states.BackScanner;
import robot.states.FrontScanner;
import robot.states.Roam;
import robot.states.StateMachine;

public class MyRobotLego {
	public final static boolean LIVE_MODE = false;
	public final static int FRONT_SCANNER_PORT = RobotLego.S_2;
	public final static int BACK_SCANNER_PORT = RobotLego.S_1;
	public final static int DEFAULT_AVERAGE_SPEED = 25;										// cm/s
	
	private int realSpeed = DEFAULT_AVERAGE_SPEED;
	
	private RobotLego robot;
	
	protected final StateMachine stateMachine = new StateMachine(this);
	
	public int getSpeed() { return this.realSpeed; }

	public MyRobotLego() {
		if(LIVE_MODE) robot = new RobotLego();
	}

	public boolean OpenNXT(String name) {
		return LIVE_MODE ? robot.OpenNXT(name) : true;
	}

	public boolean CloseNXT() {
		System.out.println("Connection is closed");

		if (LIVE_MODE) {
			robot.Parar(true);
			robot.CloseNXT();
		}
		
		return true;
	}

	public void Reta(int units) {
		System.out.println("Moving forward " + units + " units");

		if (LIVE_MODE) robot.Reta(units);
	}

	public void CurvarDireita(int radius, int angle) {
		System.out.println("Turning left " + radius + " radius " + angle + " angle");

		if (LIVE_MODE) robot.CurvarDireita(radius, angle);
	}

	public void CurvarEsquerda(int radius, int angle) {
		System.out.println("Turning right " + radius + " radius " + angle + " angle");

		if (LIVE_MODE) robot.CurvarEsquerda(radius, angle);
	}

	public void AjustarVMD(int offset) {
		if (LIVE_MODE) robot.AjustarVMD(offset);
	}

	public void AjustarVME(int offset) {
		if (LIVE_MODE) robot.AjustarVME(offset);
	}

	public void Parar(boolean trueStop) {
		System.out.println("Robot stop");

		if (LIVE_MODE) robot.Parar(trueStop);
	}
	
	public void SetSpeed(int speed) {
		if (LIVE_MODE) robot.SetSpeed(speed);
	}
	
	public void SetSensorLowspeed(int port) {
		if (LIVE_MODE) robot.SetSensorLowspeed(port);
	}
	
	public int SensorUS(int port) {
		if (LIVE_MODE) return robot.SensorUS(port);
		
		return 10;
	}
	
	public void SetSensorTouch(int port) {
		if (LIVE_MODE) robot.SetSensorTouch(port);
	}
	
	public int Sensor(int port) {
		if (LIVE_MODE) return robot.Sensor(port);
		
		return 0;
	}
	

	public void roam() {
		if(stateMachine.getPassiveState() != Roam.ID) { stateMachine.setPassiveState(new Roam(this)); }
		else { stateMachine.deactivatePassiveState(); }
	}
	
	public void escape(int minDistance, int maxDistance) { 
		stateMachine.addScanner(new BackScanner(stateMachine, BACK_SCANNER_PORT, new int[]{ minDistance, maxDistance }));
		
		// TODO:
		// toggle
		// if escape deactivate escape
		stateMachine.deactivateActiveState();
	}
	
	public void avoid() {
		// TODO:
		// toggle
		// if avoid deactivate avoid
		
		stateMachine.addScanner(new FrontScanner(stateMachine, FRONT_SCANNER_PORT));	
	}

	public static void sleep(int ms) { 
		try { Thread.sleep(ms); }
		catch (InterruptedException e) { }
	}

	public static int calculateMovementDelay(int speed, int distance) {
		return (int)(distance / speed) * 1000;
	}

	public static int calculateMovementDelay(int speed, int radius, int angle) {
		double radians = (angle * 2 * Math.PI) / 360;
		return (int)((radius * radians) / speed) * 1000;
	}
}

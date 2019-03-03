package OOP;

// Test the static import which let the user to use constants(static final variables) without fully qualified names
import static LearnImport.ConstantsOfOOP.*;
import java.util.Timer;
import java.util.TimerTask;

//import LearnImport.ConstantsOfOOP;

import java.util.EnumMap;
import java.time.LocalDateTime;
import java.time.LocalTime;

// https://www.slf4j.org/manual.html#swapping
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object: anything which has states and behaviors
 * 	  eg.: Lamp: 
 * 			States: 
 * 					LightState[On,Off], 
 * 					PlugState[Plugged-in,Plugged-out]
 * 			Behaviors:
 * 					GetStateOfLight(returns with the current state of the light regarding this lamp)
 * 					GetStateOfPlug(returns with the current state of the plug regarding this lamp)
 * 					ToggleLamp(Does nothing when it is plugged out, Toggles Light from On to Off or Off to On)
 * 					PlugIn(Plugs in the lamp's wire to the electrical network, Does nothing when it has been plugged in)	
 * 					PlugOut(Plugs in the lamp's wire from the electrical network, Does nothing when it has been plugged out)	
 * 
 * */



// Can't have more than one public class/enum/interface JVM cannot understand it
/*public*/ enum LightState{
	On,
	Off
}

enum PlugState{
	PluggedIn,
	PluggedOut
}

enum BatteryVoltage{
	MaxVoltage,
	MidVoltage,
	MinVoltage,
	LowVoltage,
	EmptyBattery
}

/**
 * Basic OOP parent class:
 * 			each and every child class will inherit these 
 * 			states(variables eg. light)	and behaviors(methods eg. toggleLamp())
 * */
abstract class Lamp{
	/** State of the light */
	private LightState light;
	protected Logger logger;
	
	
	/** Toggle the light  */
	abstract void toggleLamp();
	
	/** Get the current state of the light */
	public LightState getStateOfLight() {
		return this.light;
	}
	
	/** Set the current state of the light */
	protected void setStateOfLight( LightState aNewLightState ) {
		this.light = aNewLightState;
	}
	
	/** Getter for the logger */
	public Logger getLogger() {
		return this.logger;
	}
	
	/** Constructor for initialize the plug and light states */
	public Lamp() {
		this.light = LightState.Off;			/* Without it the value of the light is null */
		this.logger = LoggerFactory.getLogger(Lamp.class);
		
	}
}

/**
 * Basic OOP child class:
 * 			Here are more states(eg. currentLux) and behaviors(eg. getCurrentLux()).
 * 
 * */
class FlashLight extends Lamp{

	private double currentLux;
	private BatteryVoltage currentVoltage;
	private Timer voltageTimer;
	private TimerTask voltageTask;
	private EnumMap<BatteryVoltage, Double> voltageLuxMap; /* Calculating with Double is slower than basic double */
	

	
	public double getCurrentLux() {
		return this.currentLux;
	}
	
	public BatteryVoltage getCurrentBatteryVoltage() {
		return this.currentVoltage;
	}
	
	public void setBatteryVoltage( BatteryVoltage aVoltage ) {
		this.currentVoltage = aVoltage;
		this.currentLux = this.voltageLuxMap.get(this.currentVoltage);
	}
	
	/*
	 * Constructor
	 * */
	public FlashLight(){
		this.logger.info(this.getClass().getName() + " is created");
		this.voltageLuxMap = new EnumMap<BatteryVoltage, Double>(BatteryVoltage.class);
		this.voltageLuxMap.put(BatteryVoltage.EmptyBattery, LUX_OF_EMPTY);
		this.voltageLuxMap.put(BatteryVoltage.LowVoltage,	LUX_OF_LOW_VOLTAGE);
		this.voltageLuxMap.put(BatteryVoltage.MinVoltage, 	LUX_OF_MIN_VOLTAGE);
		this.voltageLuxMap.put(BatteryVoltage.MidVoltage, 	LUX_OF_MID_VOLTAGE);
		this.voltageLuxMap.put(BatteryVoltage.MaxVoltage, 	LUX_OF_MAX_VOLTAGE);
		
		this.currentLux = this.voltageLuxMap.get(BatteryVoltage.EmptyBattery);
		this.currentVoltage = BatteryVoltage.EmptyBattery;
		
		this.voltageTimer = new Timer("VoltageTimer");
		
		/** AnonymusClass 
		 * 	Documentation: https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html */
		this.voltageTask = new TimerTask() {
			public void run() {
				try {
					logger.trace("The current lux is " + currentLux);
					logger.trace("Thread's name is " + Thread.currentThread().getName());
					logger.trace("Thread's ID is " + Thread.currentThread().getId());
					/** Floating point equality test to finish the Thread */
					if ( Math.abs( LUX_OF_EMPTY - currentLux ) < EPSILON ) {
						cancel();
						logger.info("Flat battery. Thread is canceled! Application is exiting.");	
						System.exit(0);
					}
					/** Decrementing battery */
					else{
						if ( LightState.On == getStateOfLight() ) {
							currentLux -= 1; /* switched on reduction */
						}
						else {
							currentLux -= 0.01; /* idle reduction */
						}
	/** BEGIN This is only for generating an exception */					
						/** Generate an exception when the seconds of current local time divisable by 5 */
						//logger.trace(LocalTime.now().getSecond());
						if ( 0L == (LocalTime.now().getSecond() % 5L) ){
							//logger.trace("Division by zero");
							long divisionByZero = 10L / 0L;
						}
	/** END This is only for generating an exception */
					}
				}
				catch (Exception e) {
					logger.error(e.getMessage());
					cancel();
					System.exit(0);
				}
			}
		};
		
		long delay = 1000L; /* 1s */
		long period = 1L; /* 1ms */
		
		this.voltageTimer.schedule(this.voltageTask, delay, period);
		
		this.setBatteryVoltage(BatteryVoltage.MaxVoltage);
	}
	
	public void toggleLamp() {
		if ( LightState.On == this.getStateOfLight() ){
			this.setStateOfLight(LightState.Off);
		}
		else{
			this.setStateOfLight(LightState.On);
		}
		this.logger.info("The Lamp is toggled. The light is " + this.getStateOfLight().toString());
	}
}

class TableLamp extends Lamp{
	/** State of the plug */
	private PlugState plug;
	
	/** Get the current state of the plug */
	public PlugState getStateOfPlug() {
		return this.plug;
	}
	
	/** Plug in or out the Lamp to or from the electrical network */
	public void changePlugConnection( PlugState aNewPlugState ) {
		if ( this.plug != aNewPlugState ) {
			this.plug = aNewPlugState;
			this.logger.info("The lamp is " + this.plug.toString());
		}	
	}
	
	/** Toggle the light when it is plugged in */
	public void toggleLamp() {
		if (PlugState.PluggedIn == plug) {
			if ( LightState.On == this.getStateOfLight() ){
				this.setStateOfLight(LightState.Off);
			}
			else{
				this.setStateOfLight(LightState.On);
			}
			this.logger.trace("The Lamp is toggled. The light is " + this.getStateOfLight().toString());
		}
	}
	
	public TableLamp(){
		this.logger.trace(this.getClass().getName() + " is created");
		plug = PlugState.PluggedOut; 	/* Without it the value of the plug is null */
	}
}

public class Classes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Lamp tableLamp = new Lamp(); /* Cannot instantiate Lamp because it has an abstract method */
		TableLamp tableLamp = new TableLamp();

		/** Checking basic functions of tableLamp */
		tableLamp.getLogger().trace(tableLamp.getStateOfLight() + " \t:Light State of a new lamp");
		tableLamp.getLogger().trace(tableLamp.getStateOfPlug() + " \t:Plug State of a new lamp");
		tableLamp.toggleLamp();
		tableLamp.getLogger().trace(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after toggled without connecting the plug");
		tableLamp.changePlugConnection(PlugState.PluggedOut);
		tableLamp.getLogger().trace(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after plugged out" );
		tableLamp.changePlugConnection(PlugState.PluggedIn);
		tableLamp.getLogger().trace(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after plugged in");
		
		/** Playing with the lamp  */
//		int count = 0;
//		while( count < 10 ){
//			tableLamp.toggleLamp();
//			tableLamp.getLogger().info(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after plugged in" );
//			count++;
//		}
		/** Creating a new thread for FlashLight  */

		FlashLight flashLight0 = new FlashLight();
		FlashLight flashLight1 = new FlashLight();
		FlashLight flashLight2 = new FlashLight();
		FlashLight flashLight3 = new FlashLight();
		FlashLight flashLight4 = new FlashLight();


		
	}

}

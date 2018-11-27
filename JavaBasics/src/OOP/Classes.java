package OOP;

// Test the static import which let the user to use constants(static final variables) without fully qualified names
import static LearnImport.ConstantsOfOOP.*;
import java.util.Timer;
import java.util.TimerTask;

import LearnImport.ConstantsOfOOP;

import java.util.EnumMap;

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

abstract class Lamp{
	/** State of the light */
	private LightState light;
	
	
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
	
	/** Constructor for initialize the plug and light states */
	public Lamp() {
		this.light = LightState.Off;			/* Without it the value of the light is null */
	}
}

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
	
	public FlashLight(){
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
				System.out.println("The current lux is " + currentLux);
				System.out.println("Thread's name is " + Thread.currentThread().getName());
				/** Floating point equality test to finish the Thread */
				if ( Math.abs( LUX_OF_EMPTY - currentLux ) < EPSILON ) {
					cancel();
					System.out.println("Thread is canceled! Application is exiting.");	
					System.exit(0);
				}
				/** Decrementing battery */
				else
				{
					if ( LightState.On == getStateOfLight() ) {
						currentLux -= 1;
					}
					else
					{
						currentLux -= 0.01;
					}
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
		System.out.println("The Lamp is toggled. The light is " + this.getStateOfLight().toString());
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
			System.out.println("The lamp is " + this.plug.toString());
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
			System.out.println("The Lamp is toggled. The light is " + this.getStateOfLight().toString());
		}
	}
	
	public TableLamp(){
		plug = PlugState.PluggedOut; 	/* Without it the value of the plug is null */
	}
}

public class Classes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Lamp tableLamp = new Lamp(); /* Cannot instantiate Lamp because it has an abstract method */
		TableLamp tableLamp = new TableLamp();
		
		/** Checking basic functions of tableLamp */
		System.out.println(tableLamp.getStateOfLight() + " \t:Light State of a new lamp");
		System.out.println(tableLamp.getStateOfPlug() + " \t:Plug State of a new lamp");
		tableLamp.toggleLamp();
		System.out.println(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after toggled without connecting the plug");
		tableLamp.changePlugConnection(PlugState.PluggedOut);
		System.out.println(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after plugged out" );
		tableLamp.changePlugConnection(PlugState.PluggedIn);
		System.out.println(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after plugged in");
		
		/** Playing with the lamp  */
		int count = 0;
		while( count < 10 ){
			tableLamp.toggleLamp();
			System.out.println(tableLamp.getStateOfLight() + " \t:Light State of a new lamp after plugged in" );
			count++;
		}
		
		/** Creating a new thread for FlashLight  */
		FlashLight flashLight = new FlashLight();
		
	}

}

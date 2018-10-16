package OOP;

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

class Lamp{
	/** State of the light */
	private LightState light;
	/** State of the plug */
	private PlugState plug;
	
	/** Toggle the light when it is plugged in */
	public void toggleLamp() {
		if (PlugState.PluggedIn == plug) {
			if ( LightState.On == light ){
				light = LightState.Off;
			}
			else{
				light = LightState.On;
			}
			System.out.println("The Lamp is toggled. The light is " + light.toString());
		}
	}
	
	/** Plug in or out the Lamp to or from the electrical network */
	public void changePlugConnection( PlugState aNewPlugState ) {
		if ( plug != aNewPlugState ) {
			plug = aNewPlugState;
			System.out.println("The lamp is " + plug.toString());
		}
		
	}
	
	/** Get the current state of the light */
	public LightState getStateOfLight() {
		return light;
	}
	
	/** Get the current state of the plug */
	public PlugState getStateOfPlug() {
		return plug;
	}
	
	/** Constructor for initialize the plug and light states */
	public Lamp() {
		light = LightState.Off;			/* Without it the value of the light is null */
		plug = PlugState.PluggedOut; 	/* Without it the value of the plug is null */
	}
}


public class Classes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lamp tableLamp = new Lamp();
		
		System.out.println("Light State of a new lamp: " + tableLamp.getStateOfLight());
		System.out.println("Plug State of a new lamp: " + tableLamp.getStateOfPlug());
		tableLamp.toggleLamp();
		System.out.println("Light State of a new lamp after toggled without connecting the plug: " + tableLamp.getStateOfLight());
		tableLamp.changePlugConnection(PlugState.PluggedOut);
		System.out.println("Light State of a new lamp after plugged out: " + tableLamp.getStateOfLight());
		tableLamp.changePlugConnection(PlugState.PluggedIn);
		System.out.println("Light State of a new lamp after plugged in: " + tableLamp.getStateOfLight());
		
		
		tableLamp.toggleLamp();
		System.out.println("Light State of a new lamp after plugged in: " + tableLamp.getStateOfLight());
		
		
	}

}

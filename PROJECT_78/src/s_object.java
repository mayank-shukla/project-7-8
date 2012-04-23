/**
 * deze classe wordt gebruikt om meerdere ledjes tegelijk aan te sturen
 * @author Jimmy
 * 
 */
public class s_object {

	private s_led[] leds;

	public s_object() {
		leds = new s_led[0];
	}

	public void addLed(s_led led) throws Exception {
		if(leds.length==255)
			throw new Exception("het maximum aantal leds in dit object is bereikt");
		s_led[] templeds = new s_led[leds.length+1];
		int i;
		for(i=0;i<=leds.length;i++) {
			templeds[i] = leds[i];
		}
		i++;
		templeds[i] = led;
		leds = templeds;
	}

	public s_led[] getLeds() {
		return leds;
	}

	public void reset() {
		leds = new s_led[0];
	}
}
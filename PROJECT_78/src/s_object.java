public class s_object {

	s_led[] leds;

	public s_object() {
		leds = new s_led[0];
	}
	
	public void addLed(s_led led) {
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
	
	public void setLeds(s_led[] leds) {
		this.leds = leds;
	}
}
package window;

import javax.swing.JFrame;
import simulator.Simulator;

public class Window extends JFrame {
	private static final long serialVersionUID = -7150710923108249953L;

	public Window() {
		super("3D LED Cube Simulator and Editor " + Version.getVersion());
		Simulator sim = new Simulator();
		this.setContentPane(sim.createWindow(this));
	}
}
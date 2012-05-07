import javax.swing.JFrame;

public class g_window extends JFrame{

	private static final long serialVersionUID = 1L;

	g_window() {
		super("3D LED Cube Simulator and Editor " + s_version.getVersion());
		g_simulator sim = new g_simulator();
		this.setContentPane(sim.createWindow(this));
	}

}
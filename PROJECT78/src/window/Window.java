package window;

import game.Game;
import game.GameMenu;
import java.awt.Container;
import javax.swing.JFrame;
import simulator.Simulator;

public class Window extends JFrame {
	private static final long serialVersionUID = -7150710923108249953L;

	public Window(boolean simu) {
		super("3D LED Cube Simulator and Editor " + Version.getVersion());
		if (simu == true) {
			Simulator sim = new Simulator();
			this.setContentPane(sim.createWindow(this));
		}
		else {
			GameMenu game = new GameMenu();
			this.setContentPane(game.createWindow(this));
		}
	}

	public void setWindow(Container c) {
		this.setContentPane(c);
	}

	public void run(Game game) {
		// TODO Auto-generated method stub
	}
}
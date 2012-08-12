package window;

import game.Game;
import game.GameCube;
import game.GameMenu;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import simulator.Simulator;

public class Window extends JFrame {
	private static final long serialVersionUID = -7150710923108249953L;

	public Window(int simu,Game game) {
		super("3D LED Cube Simulator and Editor " + Version.getVersion());
		if (simu == 0) {
			Simulator sim = new Simulator();
			this.setContentPane(sim.createWindow(this));
		}
		else if (simu == 1) {
			GameMenu games = new GameMenu();
			this.setContentPane(games.createWindow(this));
		}
		else if (simu == 2) {
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			int width = 800;
			int height = 660;
			int x = (screen.width - width) / 2;
			int y = (screen.height - height) / 2 - 20;
			this.setBounds(x,y,width,height);
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			this.setResizable(false);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Container pane = this.getContentPane();
			GLCapabilities capabilities = new GLCapabilities(null);
			capabilities.setRedBits(8);
			capabilities.setBlueBits(8);
			capabilities.setGreenBits(8);
			capabilities.setAlphaBits(8);
			GameCube cube = new GameCube(370,370,capabilities);
			cube.addGLEventListener(cube);
			cube.setFocusable(true);
			JPanel gamePanel = game.run(cube);
			if (gamePanel == null)
				gamePanel = new JPanel();
			gamePanel.addKeyListener(game);
			gamePanel.setFocusable(true);
			pane.add(cube);
			pane.add(gamePanel);
			Dimension size = cube.getPreferredSize();
			cube.setBounds(0,0,size.width,size.height);
			size = gamePanel.getPreferredSize();
			gamePanel.setBounds(380,0,size.width,size.height);
		}
	}

	public void setWindow(Container c) {
		this.setContentPane(c);
	}
}
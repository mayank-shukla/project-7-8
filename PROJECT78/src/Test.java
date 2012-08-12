import game.Game;
import game.GameCube;
import game.GamePanel;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import simulator.Simulator;

public class Test implements KeyListener {
	private static GamePanel panel;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Test z = new Test();
		Container pane = frame.getContentPane();
		/////////////////////////////////////////////////////////////////////////
		Game[] game = new Game[20];
		for (int i = 0;i < 20;i++) {
			TestGame test = z.new TestGame();
			test.setName("game " + i);
			test.setDescription("the description of game " + i);
			game[i] = test;
		}
		/////////////////////////////////////////////////////////////////////////
		int width = 800;
		int height = 660;
		panel = new GamePanel(null,game,width,height);
		pane.add(panel);
		frame.setContentPane(pane);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2 - 20;
		frame.setBounds(x,y,width,height);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(z);
		frame.setFocusable(true);
	}

	private class TestGame extends Game {
		private String name;
		private String description;

		@Override
		public void setLoader(ClassLoader loader) {}

		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public Image getImage() {
			java.net.URL imgURL = Simulator.class.getResource("graphics/icons/play.png");
			if (imgURL == null)
				return null;
			return new ImageIcon(imgURL).getImage();
		}

		@Override
		public JPanel run(GameCube game) {
			return null;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			case KeyEvent.VK_UP:
				panel.selectUp();
				break;
			case KeyEvent.VK_DOWN:
				panel.selectDown();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}
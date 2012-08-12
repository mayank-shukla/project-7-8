package game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean animating;
	private int selected;
	private int width;
	private int height;
	private Game[] game;
	private TextArea description;
	private GameMenu menu;
	private JButton start;
	private Image img;
	private int direction;
	private double animloop;

	public GamePanel(GameMenu menu,Game[] game,int width,int height) {
		img = createImageIcon("graphics/icons/option.png").getImage();
		this.menu = menu;
		this.game = game;
		animating = false;
		selected = 0;
		this.width = width;
		this.height = height;
		this.setSize(width,height);
		this.setLayout(null);
		if (game != null)
			description = new TextArea(game[selected].getDescription(),0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		else
			description = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		description.setSize(width - 480,(height / 2) - 74);
		description.setFocusable(false);
		description.setEditable(false);
		description.setEnabled(true);
		this.add(description);
		Dimension size = description.getPreferredSize();
		description.setBounds(width - 416 - 32 - size.width,(height / 2) + 32,size.width,size.height);
		start = new JButton();
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		start.setSize(50,50);
		start.setVisible(true);
		start.setText("START");
		this.add(start);
		size = start.getPreferredSize();
		start.setBounds((width - 516) / 2,(height / 2) - 39,100,48);
	}

	protected void start() {
		menu.run(selected);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font font = new Font("Arial",Font.PLAIN,18);
		g.setFont(font);
		int[] horposi = {422,390,358,294,230,134,6};
		if (game == null)
			return;
		if (!animating) {
			for (int i = 0;i < game.length;i++) {
				try {
					switch(Math.abs(i - selected)) {
						case 0:
							g.drawImage(img,width - horposi[0],height / 2 - 47 + ((i - selected) * 65),416,65,null);
							g.drawImage(game[i].getImage(),width - horposi[0] + 16,height / 2 - 47 + ((i - selected) * 65) + 16,32,32,null);
							g.drawString(game[i].getName(),width - horposi[0] + 69,height / 2 - 47 + ((i - selected) * 65) + 35);
							break;
						case 1:
							g.drawImage(img,width - horposi[1],height / 2 - 47 + ((i - selected) * 65),416,65,null);
							g.drawImage(game[i].getImage(),width - horposi[1] + 16,height / 2 - 47 + ((i - selected) * 65) + 16,32,32,null);
							g.drawString(game[i].getName(),width - horposi[1] + 69,height / 2 - 47 + ((i - selected) * 65) + 35);
							break;
						case 2:
							g.drawImage(img,width - horposi[2],height / 2 - 47 + ((i - selected) * 65),416,65,null);
							g.drawImage(game[i].getImage(),width - horposi[2] + 16,height / 2 - 47 + ((i - selected) * 65) + 16,32,32,null);
							g.drawString(game[i].getName(),width - horposi[2] + 69,height / 2 - 47 + ((i - selected) * 65) + 35);
							break;
						case 3:
							g.drawImage(img,width - horposi[3],height / 2 - 47 + ((i - selected) * 65),416,65,null);
							g.drawImage(game[i].getImage(),width - horposi[3] + 16,height / 2 - 47 + ((i - selected) * 65) + 16,32,32,null);
							g.drawString(game[i].getName(),width - horposi[3] + 69,height / 2 - 47 + ((i - selected) * 65) + 35);
							break;
						case 4:
							g.drawImage(img,width - horposi[4],height / 2 - 47 + ((i - selected) * 65),416,65,null);
							g.drawImage(game[i].getImage(),width - horposi[4] + 16,height / 2 - 47 + ((i - selected) * 65) + 16,32,32,null);
							g.drawString(game[i].getName(),width - horposi[4] + 69,height / 2 - 47 + ((i - selected) * 65) + 35);
							break;
						case 5:
							g.drawImage(img,width - horposi[5],height / 2 - 47 + ((i - selected) * 65),416,65,null);
							g.drawImage(game[i].getImage(),width - horposi[5] + 16,height / 2 - 47 + ((i - selected) * 65) + 16,32,32,null);
							g.drawString(game[i].getName(),width - horposi[5] + 69,height / 2 - 47 + ((i - selected) * 65) + 35);
							break;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		else {
			if (direction == 0)
				return;
			if (direction > 0)
				direction = 1;
			if (direction < 0)
				direction = -1;
			for (int i = 0;i < game.length;i++) {
				int locwidth,locheight;
				if (direction > 0)
					locheight = height / 2 - 47 + ((i - selected + 1) * 65) - (int)(animloop);
				else
					locheight = height / 2 - 47 + ((i - selected - 1) * 65) + (int)(animloop);
				try {
					switch(i - selected + 5) {
						case -1:
							if (direction > 0) {
								locwidth = width - horposi[5] + (int)((horposi[5] - horposi[6]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 0:
							if (direction < 0) {
								locwidth = width - horposi[6] - (int)((horposi[5] - horposi[6]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[4] + (int)((horposi[4] - horposi[5]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 1:
							if (direction < 0) {
								locwidth = width - horposi[5] - (int)((horposi[4] - horposi[5]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[3] + (int)((horposi[3] - horposi[4]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 2:
							if (direction < 0) {
								locwidth = width - horposi[4] - (int)((horposi[3] - horposi[4]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[2] + (int)((horposi[2] - horposi[3]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 3:
							if (direction < 0) {
								locwidth = width - horposi[3] - (int)((horposi[2] - horposi[3]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[1] + (int)((horposi[1] - horposi[2]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 4:
							if (direction < 0) {
								locwidth = width - horposi[2] - (int)((horposi[1] - horposi[2]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[0] + (int)((horposi[0] - horposi[1]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 5:
							if (direction < 0) {
								locwidth = width - horposi[1] - (int)((horposi[0] - horposi[1]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[1] - (int)((horposi[0] - horposi[1]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 6:
							if (direction < 0) {
								locwidth = width - horposi[0] + (int)((horposi[0] - horposi[1]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[2] - (int)((horposi[1] - horposi[2]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 7:
							if (direction < 0) {
								locwidth = width - horposi[1] + (int)((horposi[1] - horposi[2]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[3] - (int)((horposi[2] - horposi[3]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 8:
							if (direction < 0) {
								locwidth = width - horposi[2] + (int)((horposi[2] - horposi[3]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[4] - (int)((horposi[3] - horposi[4]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 9:
							if (direction < 0) {
								locwidth = width - horposi[3] + (int)((horposi[3] - horposi[4]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[5] - (int)((horposi[4] - horposi[5]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 10:
							if (direction < 0) {
								locwidth = width - horposi[4] + (int)((horposi[4] - horposi[5]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							else {
								locwidth = width - horposi[6] - (int)((horposi[5] - horposi[6]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
						case 11:
							if (direction < 0) {
								locwidth = width - horposi[5] + (int)((horposi[5] - horposi[6]) * (animloop / 65.0));
								g.drawImage(img,locwidth,locheight,416,65,null);
								g.drawImage(game[i].getImage(),locwidth + 16,locheight + 16,32,32,null);
								g.drawString(game[i].getName(),locwidth + 69,locheight + 35);
							}
							break;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {}
			}
			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e) {}
		}
	}

	private ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = GameMenu.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		}
		else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public void selectDown() {
		if (animating)
			return;
		if (selected < game.length - 1) {
			selected++;
			new Thread() {
				public void run() {
					animating = true;
					direction = 1;
					for (animloop = 0;animloop < 65;animloop++) {
						repaint();
						try {
							Thread.sleep(8);
						}
						catch (InterruptedException e) {}
					};
					direction = 0;
					animating = false;
					repaint();
				}
			}.start();
			description.setText(game[selected].getDescription());
		}
	}

	public void selectUp() {
		if (animating)
			return;
		if (selected > 0) {
			selected--;
			new Thread() {
				public void run() {
					animating = true;
					direction = -1;
					for (animloop = 0;animloop < 65;animloop++) {
						repaint();
						try {
							Thread.sleep(8);
						}
						catch (InterruptedException e) {}
					};
					direction = 0;
					animating = false;
					repaint();
				}
			}.start();
			description.setText(game[selected].getDescription());
		}
	}
}
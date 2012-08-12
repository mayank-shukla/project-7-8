package game;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.swing.JFrame;
import window.Window;

public class GameMenu implements KeyListener {
	private Window frame;
	private GamePanel panel;
	private Game[] game;

	public Container createWindow(Window frame) {
		this.frame = frame;
		//dit is het path naar de locatie van de jar file voeg \\games aan toe voor de game map en maak er een file van
		String path = System.getProperty("user.dir");
		path += "\\games";
		File games = new File(path);
		//als het niet bestaat maak het aan en ga verder met de weergaven
		if (!games.exists()) {
			System.out.println("dir made");
			games.mkdir();
		}
		//als het bestaat en geen directory is ga verder met weergaven
		else if (games.isDirectory()) {
			System.out.println("dir read");
			//haal alle files in de gam map op
			File[] jars = games.listFiles();
			System.out.println("files in dir read with " + jars.length + " files");
			for (int i = 0;i < jars.length;i++) {
				//als de file een jar is  lee de namen van alle nuttige classes in en kijk of alle hulpclasses aanwezig zijn
				if (jars[i].getName().endsWith(".jar")) {
					System.out.println("filenumber " + i + "is a jar file and will now be read");
					try {
						ZipFile jar = new ZipFile(jars[i]);
						Enumeration<? extends ZipEntry> entries = jar.entries();
						int condition = 0;
						String[] classes = new String[0];
						while (entries.hasMoreElements()) {
							ZipEntry entry = entries.nextElement();
							if (entry.getName().equals("game/Game.class"))
								condition++;
							else if (entry.getName().equals("display/CubeObject.class"))
								condition++;
							else if (entry.getName().equals("display/Led.class"))
								condition++;
							else if (entry.getName().equals("display/Display.class"))
								condition++;
							else if (entry.getName().lastIndexOf("/") != -1 && entry.getName().endsWith(".class")) {
								String[] temp = new String[classes.length + 1];
								int j;
								for (j = 0;j < classes.length;j++) {
									temp[j] = classes[j];
								}
								j++;
								temp[j] = entry.getName();
								classes = temp;
							}
						}
						//als de conditie 4 is zijn alle 4 hulpclasses gevonden
						if (condition == 4) {
							System.out.println("this file has the correct classes with " + classes.length + "games");
							//maak een class loader voor deze jar
							ClassLoader loader = URLClassLoader.newInstance(new URL[] {jars[i].toURI().toURL()},getClass().getClassLoader());
							//loop alle namen door om deze in te laden
							for (int j = 0;j < classes.length;j++) {
								//kijk of de naam van de class aan de eisen voldoet
								if (classes[j].substring(classes[j].lastIndexOf("/") + 1,classes[j].lastIndexOf(".")).startsWith("Game_")) {
									//verander / in . om het als packages te lezen
									String clas = classes[j].replaceAll("/",".");
									//verweider de .class extensie
									int k = clas.lastIndexOf(".");
									clas = clas.substring(0,k + 1);
									//maak een geinstantieerd object van de gevonden class
									Class<?> clazz = Class.forName(clas,true,loader);
									Class<? extends Game> runClass = clazz.asSubclass(Game.class);
									Constructor<? extends Game> ctor = runClass.getConstructor();
									Game game = ctor.newInstance();
									//geef de Classloader aan het object mee zodat eventueel bestanden uit de jar kunnen worden geladen
									game.setLoader(loader);
									//voeg dit object toe aan de array met alle games
									int l;
									Game[] temp = new Game[this.game.length + 1];
									for (l = 0;l < this.game.length;l++) {
										temp[l] = this.game[l];
									}
									l++;
									temp[l] = game;
									this.game = temp;
								}
							}
						}
					}
					catch (ZipException e) {
						e.printStackTrace();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					catch (SecurityException e) {
						e.printStackTrace();
					}
					catch (InstantiationException e) {
						e.printStackTrace();
					}
					catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 800;
		int height = 660;
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
		Container pane = frame.getContentPane();
		panel = new GamePanel(this,game,width,height);
		pane.add(panel);
		return pane;
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

	public void run(int selected) {
		if (game != null)
			frame.run(game[selected]);
	}
}
package game;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import display.Display;

public abstract class Game {
	/**
	 * this is the classloader used for the jar where this class would be exported to to load other classes or images
	 * 
	 * @param loader the classloader for this jar
	 */
	public abstract void setLoader(ClassLoader loader);

	/**
	 * this method should return the name of this game
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * this method should return th description of this game
	 * 
	 * @return
	 */
	public abstract String getDescription();

	/**
	 * this method should return the icon of this game or null
	 * 
	 * @return
	 */
	public abstract ImageIcon getImage();

	/**
	 * this methode starts the game
	 * 
	 * @param dis the display to be used througout the whole game
	 * @return a costum JPanel that could be displayed at the screen
	 */
	public abstract JPanel run(Display dis);
}
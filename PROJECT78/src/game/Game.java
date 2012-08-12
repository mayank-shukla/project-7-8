package game;

import java.awt.Image;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public abstract class Game implements KeyListener {
	/**
	 * 
	 * this is the classloader used for the jar where this class would be exported to to load other classes or images.
	 * <p>
	 * 
	 * here is an example to load a class from your jar:
	 * 
	 * <pre>
	 * Class&lt;?&gt; clazz = Class.forName(&quot;YourClass&quot;,true,loader);
	 * Class&lt;? extends Game&gt; runClass = clazz.asSubclass(Game.class);
	 * Constructor&lt;? extends Game&gt; ctor = runClass.getConstructor();
	 * Game game = ctor.newInstance();
	 * </pre>
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
	public abstract Image getImage();

	/**
	 * this methode starts the game. here you should return the JPanel that you want to display on the screen and start a thread that manages the game
	 * 
	 * @param cube the GameCube that manages and holds the display to be used througout the whole game and for the sceen
	 * @return a costum JPanel that could be displayed at the screen
	 */
	public abstract JPanel run(GameCube cube);
}
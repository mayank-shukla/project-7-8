//TODO play en stop button die de frames afspeelt cube ook van voor en zijkant bewerken
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.media.opengl.GLCapabilities;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/* g_window.java
 *
 * Hier wordt het venster geïnitialiseerd dat voor heel het programma
 * gebruikt zal worden.
 * 
 */
public class g_window implements MouseListener
{
	private JFrame frame = new JFrame("3D LED Cube Simulator and Editor " + s_version.getVersion());
	private g_jogl_cube jogl;
	private g_jogl_cube_layer layer;
	private JButton prev,next,save,saveas,load,copy,paste,insert,remove,red,green,yellow,first,play,stop,last,loop;
	private JLabel framenumber;
	private File curFile;
	private JFileChooser fc;
	private JTextArea console, border, space;
	
	boolean anim_loop;

	/**
	 * een nieuw window initialiseren
	 */
	public void createWindow()
	{
		fc = new JFileChooser();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 800;
		int height = 660;
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2-20;
		anim_loop = false;
		
		frame.setBounds(x,y,width,height);
		frame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GLCapabilities capabilities = new GLCapabilities(null);
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		jogl = new g_jogl_cube(370, 370, capabilities,this);
		jogl.addGLEventListener(jogl);
		jogl.setFocusable(true);
		jogl.addMouseMotionListener(jogl);
		jogl.addMouseWheelListener(jogl);
		
		layer = new g_jogl_cube_layer(415, 370, capabilities,jogl);
		layer.addGLEventListener(layer);
		layer.setFocusable(true);
		layer.addMouseMotionListener(layer);
		layer.addMouseListener(layer);
		layer.addKeyListener(layer);

		ImageIcon prevIcon = createImageIcon("graphics/icons/prev.png");
		ImageIcon nextIcon = createImageIcon("graphics/icons/next.png");
		ImageIcon copyIcon = createImageIcon("graphics/icons/copy.png");
		ImageIcon pasteIcon = createImageIcon("graphics/icons/paste.png");
		ImageIcon insertIcon = createImageIcon("graphics/icons/insert.png");
		ImageIcon removeIcon = createImageIcon("graphics/icons/delete.png");
		ImageIcon loadIcon = createImageIcon("graphics/icons/load.png");
		ImageIcon saveIcon = createImageIcon("graphics/icons/save.png");
		ImageIcon saveAsIcon = createImageIcon("graphics/icons/save_ass.png");
		
		ImageIcon redIcon = createImageIcon("graphics/icons/red.png");
		ImageIcon greenIcon = createImageIcon("graphics/icons/green.png");
		ImageIcon yellowIcon = createImageIcon("graphics/icons/yellow.png");
		
		ImageIcon firstFrameIcon = createImageIcon("graphics/icons/first.png");
		ImageIcon playIcon = createImageIcon("graphics/icons/play.png");
		ImageIcon pauseIcon = createImageIcon("graphics/icons/stop.png");
		ImageIcon lastFrameIcon = createImageIcon("graphics/icons/last.png");
		ImageIcon loopIcon = createImageIcon("graphics/icons/loop.png");

		prev = new JButton(prevIcon);
		prev.setToolTipText("Select previous frame.");
		prev.setFocusable(false);
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionPrev();
			}
		});
		next = new JButton(nextIcon);
		next.setToolTipText("Select next frame.");
		next.setFocusable(false);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionNext();
			}
		});
		copy = new JButton(copyIcon);
		copy.setToolTipText("Copy selected frame.");
		copy.setFocusable(false);
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionCopy();
			}
		});
		paste = new JButton(pasteIcon);
		paste.setToolTipText("Paste copied frame.");
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionPaste();
			}
		});
		save = new JButton(saveIcon);
		save.setToolTipText("Save file.");
		save.setFocusable(false);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionSave();
			}
		});
		saveas = new JButton(saveAsIcon);
		saveas.setToolTipText("Save file as...");
		saveas.setFocusable(false);
		saveas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionSaveAs();
			}
		});
		load = new JButton(loadIcon);
		load.setToolTipText("Load file.");
		load.setFocusable(false);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionLoad();
			}
		});
		insert = new JButton(insertIcon);
		insert.setToolTipText("Insert a new frame.");
		insert.setFocusable(false);
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionInsert();
			}
		});
		remove = new JButton(removeIcon);
		remove.setToolTipText("Remove selected frame.");
		remove.setFocusable(false);
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionRemove();
			}
		});

		//TODO: Het eerste getal kunnen editten zodat je meteen naar die frame skipt
		framenumber = new JLabel("1/1");
		frame.addMouseListener(this);
		
		console = new JTextArea(8, 56);
		console.setEditable(false);

		console.append("3D LED Cube Simulator and Editor " + s_version.getVersion() + " has been loaded successfully.\n");
		
		red = new JButton(redIcon);
		red.setToolTipText("Set your mouse to red LED paint mode.");
		red.setFocusable(false);
		red.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionLED(0, console);
			}
		});
		
		green = new JButton(greenIcon);
		green.setToolTipText("Set your mouse to green LED paint mode.");
		green.setFocusable(false);
		green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionLED(1, console);
			}
		});
		
		yellow = new JButton(yellowIcon);
		yellow.setToolTipText("Set your mouse to yellow LED paint mode.");
		yellow.setFocusable(false);
		yellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionLED(2, console);
			}
		});
		
		first = new JButton(firstFrameIcon);
		first.setToolTipText("Skip to the first frame of the cube.");
		first.setFocusable(false);
		
		play = new JButton(playIcon);
		play.setToolTipText("Play the cube animation.");
		play.setFocusable(false);
		
		stop = new JButton(pauseIcon);
		stop.setToolTipText("Stop the current animation.");
		stop.setFocusable(false);
		
		last = new JButton(lastFrameIcon);
		last.setToolTipText("Skip to the last frame of the cube.");
		last.setFocusable(false);
		
		loop = new JButton(loopIcon);
		loop.setToolTipText("Toggle animation loop.");
		loop.setFocusable(false);
		loop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionLoop(console);
			}
		});
		
		space = new JTextArea(9, 64);
		space.setEditable(false);
		space.setBackground(Color.LIGHT_GRAY);
		
		border = new JTextArea(9, 64);
		border.setEditable(false);

		Container pane = frame.getContentPane();
		pane.setLayout(null);

		pane.add(jogl);
		pane.add(layer);
		pane.add(prev);
		pane.add(next);
		pane.add(copy);
		pane.add(paste);
		pane.add(save);
		pane.add(saveas);
		pane.add(load);
		pane.add(framenumber);
		pane.add(insert);
		pane.add(remove);
		
		pane.add(red);
		pane.add(green);
		pane.add(yellow);
		
		pane.add(first);
		pane.add(play);
		pane.add(stop);
		pane.add(last);
		pane.add(loop);
		
		pane.add(console);
		pane.add(border);
		pane.add(space);
		
		Dimension size = jogl.getPreferredSize();
		jogl.setBounds(0, 0, size.width, size.height);
		
		size = layer.getPreferredSize();
		layer.setBounds(380, 0, size.width, size.height);
		
		size = prev.getPreferredSize();
		prev.setBounds(10, 378, size.width, size.height);
		
		size = next.getPreferredSize();
		next.setBounds(84, 378, size.width, size.height);
		
		size = copy.getPreferredSize();
		copy.setBounds(10, 428, size.width, size.height);
		
		size = paste.getPreferredSize();
		paste.setBounds(84, 428, size.width, size.height);
		
		size = save.getPreferredSize();
		save.setBounds(10, 478, size.width, size.height);
		
		size = saveas.getPreferredSize();
		saveas.setBounds(84, 478, size.width, size.height);
		
		size = load.getPreferredSize();
		load.setBounds(10, 578, size.width, size.height);
		
		size = framenumber.getPreferredSize();
		framenumber.setBounds(102, 588, size.width, size.height);
		
		size = insert.getPreferredSize();
		insert.setBounds(10, 528, size.width, size.height);
		
		size = remove.getPreferredSize();
		remove.setBounds(84, 528, size.width, size.height);
		
		size = red.getPreferredSize();
		red.setBounds(232, 428, size.width, size.height);
		
		size = green.getPreferredSize();
		green.setBounds(306, 428, size.width, size.height);
		
		size = yellow.getPreferredSize();
		yellow.setBounds(380, 428, size.width, size.height);
		
		size = first.getPreferredSize();
		first.setBounds(232, 378, size.width, size.height);
		
		size = play.getPreferredSize();
		play.setBounds(306, 378, size.width, size.height);
		
		size = stop.getPreferredSize();
		stop.setBounds(380, 378, size.width, size.height);
		
		size = last.getPreferredSize();
		last.setBounds(454, 378, size.width, size.height);
		
		size = loop.getPreferredSize();
		loop.setBounds(718, 378, size.width, size.height);
		
		size = space.getPreferredSize();
		space.setBounds(159, 478, 625, 146);
		
		size = border.getPreferredSize();
		border.setBounds(160, 479, 623, size.height);
		
		size = console.getPreferredSize();
		console.setBounds(170, 486, 613, size.height);
	}

	/**
	 * print een message op de console
	 * @param message
	 * @param console
	 * @param type
	 */
	public void consoleMessage(String message, JTextArea console, int type)
	{
		Calendar rightNow = Calendar.getInstance();
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minute = rightNow.get(Calendar.MINUTE);
		int second = rightNow.get(Calendar.SECOND);
		String h = "";
		String m = "";
		String s = "";
		
		if (hour < 10)
			h = "0";
		if (minute < 10)
			m = "0";
		if (second < 10)
			s = "0";
		
		console.append("(" + h + hour + ":" + m + minute + ":" + s + second + ") " + message + "\n");
	}

	/**
	 * zet de ledmode op een kleur en print een message op de console
	 * @param value
	 * @param console
	 */
	protected void actionLED(int value, JTextArea console) 
	{
		layer.setMode(value);
		
		String colour = "yellow";
		if (value == 0)
			colour = "red";
		else if (value == 1)
			colour = "green";
		consoleMessage("LED paint colour set to " + colour + ".", console, 0);
	}

	protected void actionLoop(JTextArea console) 
	{
		if (anim_loop)
		{
			consoleMessage("Animation loop has been disabled.", console, 0);
			anim_loop = false;
		}
		else if (!anim_loop)
		{
			consoleMessage("Animation loop has been enabled.", console, 0);
			anim_loop = true;
		}
	}

	/**
	 * haalt een frame weg uit de cube
	 */
	protected void actionRemove() {
		jogl.remove();
	}

	/**
	 * plaatst een frame tussen twee bestaande frames
	 */
	protected void actionInsert() {
		jogl.insert();
	}

	/**
	 * slaat de gegevens op in een nieuw bestand
	 */
	protected void actionSaveAs() {
		fc.setApproveButtonText("Save");
		while(fc.getFileFilter() != null)
			fc.removeChoosableFileFilter(fc.getFileFilter());

		fc.setFileFilter(new s_extensionfilefilter(new String[] {"3DC"},"Select 3D cube."));

		int returnVal = fc.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File tempFile = fc.getSelectedFile();

			String name =tempFile.getAbsolutePath();

			if(!name.contains(".3DC")) {
				if(name.contains(".")) {
					int index = name.indexOf(".");
					char[] temp = new char[index];
					name.getChars(0, index, temp, 0);
					name = temp.toString();
					JOptionPane.showMessageDialog(frame, "Your filename may not contain a \".\".");
				}
				name += ".3DC";
			}
			curFile = new File(name);

			actionSave();
		}
	}

	/**
	 * laad gegevens uit een bestaand bestand
	 */
	protected void actionLoad() {
		fc.setApproveButtonText("Open");
		while(fc.getFileFilter() != null)
			fc.removeChoosableFileFilter(fc.getFileFilter());

		fc.setFileFilter(new s_extensionfilefilter(new String[] {"3DC"},"Select 3D cube."));

		int returnVal = fc.showOpenDialog(frame);

		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		curFile = fc.getSelectedFile();

		String name = curFile.getName();

		if(!name.contains(".3DC")) {
			if(name.contains(".")) {
				int index = name.indexOf(".");
				char[] temp = new char[index];
				name.getChars(0, index, temp, 0);
				name = temp.toString();
				JOptionPane.showMessageDialog(frame, "You can not open a file which doesn't end with \".3DC\".");
				return;
			}
			name += ".3DC";
		}

		FileInputStream fin;
		try {
			fin = new FileInputStream(curFile);
			byte data[] = new byte[(int)curFile.length()];
			fin.read(data);
			jogl.load(data);
		}
		catch (FileNotFoundException e) {}
		catch (IOException e) {}
		catch (Exception e) {}
	}

	/**
	 * slaat de gegevens op in een bestaand bestand
	 */
	protected void actionSave() {
		jogl.generate5Cube();
/*
		try {
			PrintWriter out = new PrintWriter(new FileWriter("C:\\Program Files\\simulatoroutput.txt"));
			out.println(code);
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}
//*/
		if(curFile==null) {
			actionSaveAs();
		}
		else {
			
			if(!curFile.exists()){
				try {curFile.createNewFile();}
				catch (IOException e) {}
			}
			else {
				curFile.delete();
				try {curFile.createNewFile();}
				catch (IOException e) {}
			}
			
			FileOutputStream fos;
			
			byte[] save = jogl.save();
			
			try {
				fos = new FileOutputStream(curFile.getPath());
				fos.write(save);
				fos.close();
			}
			catch (FileNotFoundException e) {}
			catch (IOException e) {}
		}
	}

	/**
	 * plakt de inhoud van een eerder gekopieerde cube
	 */
	protected void actionPaste() {
		jogl.paste();
	}

	/**
	 * kopieert de inhoud van een cube
	 */
	protected void actionCopy() {
		jogl.copy();
	}

	/**
	 * ga naar de volgende frame
	 */
	protected void actionNext() {
		jogl.next();
	}

	/**
	 * ga naar de vorige frame
	 */
	protected void actionPrev() {
		jogl.prev();
	}

	/**
	 * zet een nieuw frame nummer op de label en past zo nodig de groote van de label aan
	 * @param frame
	 */
	public void setFrameNumber(String frame) {
		framenumber.setText(frame);
		
		Dimension size = framenumber.getPreferredSize();
		framenumber.setBounds(102, 588, size.width, size.height);
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}
	
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) 
    {
        java.net.URL imgURL = g_window.class.getResource(path);
        if (imgURL != null) { return new ImageIcon(imgURL);} 
        else { System.err.println("Couldn't find file: " + path); return null; }
    }
}
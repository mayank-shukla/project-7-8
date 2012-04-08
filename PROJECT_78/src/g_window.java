//TODO play en stop button die de frames afspeelt
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

import javax.media.opengl.GLCapabilities;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
	private JButton prev,next,save,saveas,load,copy,paste,insert,remove;
	private JLabel framenumber;
	private File curFile;
	private JFileChooser fc;

	public void createWindow()
	{
		fc = new JFileChooser();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 800;
		int height = 660;
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2-20;
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

		prev = new JButton("Prev");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionPrev();
			}
		});
		next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionNext();
			}
		});
		copy = new JButton("Copy");
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionCopy();
			}
		});
		paste = new JButton("Paste");
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionPaste();
			}
		});
		save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionSave();
			}
		});
		saveas = new JButton("Save As");
		saveas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionSaveAs();
			}
		});
		load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionLoad();
			}
		});
		insert = new JButton("Insert");
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionInsert();
			}
		});
		remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionRemove();
			}
		});

		framenumber = new JLabel("1/1");

		frame.addMouseListener(this);

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

		Dimension size = jogl.getPreferredSize();
		jogl.setBounds(0, 0, size.width, size.height);
		
		size = layer.getPreferredSize();
		layer.setBounds(380, 0, size.width, size.height);
		
		size = prev.getPreferredSize();
		prev.setBounds(50, 380, size.width, size.height);
		
		size = next.getPreferredSize();
		next.setBounds(250, 380, size.width, size.height);
		
		size = copy.getPreferredSize();
		copy.setBounds(50, 420, size.width, size.height);
		
		size = paste.getPreferredSize();
		paste.setBounds(250, 420, size.width, size.height);
		
		size = save.getPreferredSize();
		save.setBounds(50, 500, size.width, size.height);
		
		size = saveas.getPreferredSize();
		saveas.setBounds(50, 540, size.width, size.height);
		
		size = load.getPreferredSize();
		load.setBounds(250, 500, size.width, size.height);
		
		size = framenumber.getPreferredSize();
		framenumber.setBounds(250, 540, size.width, size.height);
		
		size = insert.getPreferredSize();
		insert.setBounds(50,460, size.width, size.height);
		
		size = remove.getPreferredSize();
		remove.setBounds(250, 460, size.width, size.height);
	}
	
	protected void actionRemove() {
		jogl.remove();
	}

	protected void actionInsert() {
		jogl.insert();
	}

	protected void actionSaveAs() {
		while(fc.getFileFilter() != null)
			fc.removeChoosableFileFilter(fc.getFileFilter());

		fc.setFileFilter(new s_extensionfilefilter(new String[] {"3dmod"},"select 3d model"));

		int returnVal = fc.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File tempFile = fc.getSelectedFile();

			String name =tempFile.getAbsolutePath();

			if(!name.contains(".3dmod")) {
				if(name.contains(".")) {
					int index = name.indexOf(".");
					char[] temp = new char[index];
					name.getChars(0, index, temp, 0);
					name = temp.toString();
					JOptionPane.showMessageDialog(frame, "you can not save a file with a \".\" in it");
				}
				name += ".3dmod";
			}
			curFile = new File(name);

			actionSave();
		}
	}

	protected void actionLoad() {
		while(fc.getFileFilter() != null)
			fc.removeChoosableFileFilter(fc.getFileFilter());

		fc.setFileFilter(new s_extensionfilefilter(new String[] {"3dmod"},"select 3d model"));

		int returnVal = fc.showOpenDialog(frame);

		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		curFile = fc.getSelectedFile();

		String name = curFile.getName();

		if(!name.contains(".3dmod")) {
			if(name.contains(".")) {
				int index = name.indexOf(".");
				char[] temp = new char[index];
				name.getChars(0, index, temp, 0);
				name = temp.toString();
				JOptionPane.showMessageDialog(frame, "you can not open a file that does not end with \".3dmod\"");
				return;
			}
			name += ".3dmod";
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

	protected void actionPaste() {
		jogl.paste();
	}

	protected void actionCopy() {
		jogl.copy();
	}

	protected void actionNext() {
		jogl.next();
	}

	protected void actionPrev() {
		jogl.prev();
	}

	public void setFrameNumber(String frame) {
		framenumber.setText(frame);
		
		Dimension size = framenumber.getPreferredSize();
		framenumber.setBounds(250, 540, size.width, size.height);
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("X:" + e.getX() + " Y:" + e.getY());
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}
}
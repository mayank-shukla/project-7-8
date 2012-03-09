import java.awt.*;
import javax.swing.*; 

public class s_loadingbar extends JWindow
{
	private static final long serialVersionUID = 1L;
	JProgressBar current;
    Thread runner;

    public s_loadingbar() 
    {
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        current = new JProgressBar(0, 2000);
        current.setValue(0);
        current.setStringPainted(true);
        pane.add(current);
        showSplash();
    }
    
    public void showSplash() 
    {
        JPanel content = (JPanel)getContentPane();
        content.setBackground(Color.white);
        
        // Set the window's bounds, centering the window
        int width = 725;
        int height = 550;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);
        
        // Build the splash screen
        JLabel label = new JLabel(new ImageIcon("test.png"));
        JLabel copyrt = new JLabel("Software by HRO-STUDENTS™ © 2012 TI-PROJECT-78", JLabel.CENTER);
        JLabel loading = new JLabel("Loading - please wait...", JLabel.CENTER);
        copyrt.setFont(new Font("Verdana", Font.BOLD, 20));
        loading.setFont(new Font("Verdana", Font.BOLD, 40));
        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.NORTH);
        content.add(loading, BorderLayout.SOUTH);
        Color oraRed = new Color(156, 20, 20,  255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 20));
        
        // Display it
        setVisible(true);
        
        // Wait a little while, maybe while loading resources
        try { Thread.sleep(3000); } catch (Exception e) {}
        Color oraWhite = new Color(255, 255, 255, 255);
        content.setBorder(BorderFactory.createLineBorder(oraWhite, 1000));
        try { Thread.sleep(75); } catch (Exception e) {}
        setVisible(false);
    }
}
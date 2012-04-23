import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * deze class wordt gebruikt om alleen een bepaald type bestand te kunnen lezen bij de fileselectie
 * @author Jimmy
 *
 */
public class s_extensionfilefilter extends FileFilter{

	String[] valid;
	String description;

	public s_extensionfilefilter(String[] valid, String description) {
		this.valid = valid;
		this.description = description;
	}
	
	public boolean accept(File f) {

		if(valid[0]=="")
			return true;
		
		if(f.isDirectory())
			return true;
		
		for(int i=0;i<valid.length;i++)
			if (f.getName().toLowerCase().endsWith(valid[i]))
				return true;
		
		return false;
	}

	public String getDescription() {		
		return description;
	}
}

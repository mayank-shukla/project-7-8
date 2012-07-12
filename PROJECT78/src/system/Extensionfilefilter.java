package system;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class Extensionfilefilter extends FileFilter {
	String[] valid;
	String description;

	public Extensionfilefilter(String[] valid,String description) {
		this.valid = valid;
		this.description = description;
	}

	@Override
	public boolean accept(File f) {
		if (valid[0] == "")
			return true;
		if (f.isDirectory())
			return true;
		for (int i = 0;i < valid.length;i++)
			if (f.getName().toLowerCase().endsWith(valid[i]))
				return true;
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}
}

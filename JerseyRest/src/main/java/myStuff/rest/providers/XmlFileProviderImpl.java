package myStuff.rest.providers;

import java.io.File;

public class XmlFileProviderImpl implements XmlFileProvider {
	
	private final File folder;

	public XmlFileProviderImpl(File folder) {
		this.folder = folder;
	}

	@Override
	public File getFolder() {
		return folder;
	}

}

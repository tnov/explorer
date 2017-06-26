package net.tnk_toys.JerseyClient.gui.propertyeditor;

import java.nio.file.Path;

public class FolderTreeItemData {

	private String name = null;
	private Path path = null;
	
	public FolderTreeItemData(Path path) {
		this(path.getFileName()==null?"root": path.getFileName().toString(),path);
	}
	
	public FolderTreeItemData(String name, Path path) {
		this.setName(name);
		this.setPath(path);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return this.name;
	}
}

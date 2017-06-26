package net.tnk_toys.JerseyClient.gui.propertyeditor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FileItem {
	private StringProperty nameProperty;
	private StringProperty extProperty;
	private StringProperty sizeProperty;
	private StringProperty lastModifiedProperty;
	
	public FileItem(String name,String ext,String size,String lastModified) {
		nameProperty = new SimpleStringProperty(name);
		extProperty = new SimpleStringProperty(ext);
		sizeProperty = new SimpleStringProperty(size);
		lastModifiedProperty = new SimpleStringProperty(lastModified);
	}
	
	public StringProperty getNameProperty() {
		return nameProperty;
	}
	public void setNameProperty(StringProperty nameProperty) {
		this.nameProperty = nameProperty;
	}
	public StringProperty getSizeProperty() {
		return sizeProperty;
	}
	public void setSizeProperty(StringProperty sizeProperty) {
		this.sizeProperty = sizeProperty;
	}
	public StringProperty getLastModifiedProperty() {
		return lastModifiedProperty;
	}
	public void setLastModifiedProperty(StringProperty lastModifiedProperty) {
		this.lastModifiedProperty = lastModifiedProperty;
	}
	public StringProperty getExtProperty() {
		return extProperty;
	}
	public void setExtProperty(StringProperty extProperty) {
		this.extProperty = extProperty;
	}
}

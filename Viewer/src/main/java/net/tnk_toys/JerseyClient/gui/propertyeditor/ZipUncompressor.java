package net.tnk_toys.JerseyClient.gui.propertyeditor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUncompressor {
	Path path;
	
	public ZipUncompressor(Path path) {
		this.path = path;
	}
	
	public void getInfo() {
		try (ZipInputStream stream = new ZipInputStream(new FileInputStream(this.path.toFile()))) {
			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null ) 
				entry.getName();
			stream.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void uncompress() {
		try (ZipInputStream stream = new ZipInputStream(new FileInputStream(this.path.toFile()))) {
			stream.getNextEntry();
			
			stream.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

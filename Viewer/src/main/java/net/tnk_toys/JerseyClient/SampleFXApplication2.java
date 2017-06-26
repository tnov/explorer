package net.tnk_toys.JerseyClient;

import java.io.File;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import net.tnk_toys.JerseyClient.gui.propertyeditor.EditorController;

public class SampleFXApplication2 extends Application {

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.out.println("close");
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("start");
		primaryStage.setTitle(this.getClass().getSimpleName());
		EditorController editor = new EditorController(primaryStage,Paths.get("c:"+File.separator));
		editor.show();
		System.out.println("end");
	}
}

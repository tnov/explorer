package net.tnk_toys.JerseyClient;

import javafx.application.Application;
import javafx.stage.Stage;
import net.tnk_toys.JerseyClient.gui.LoginController;

public class SampleFXApplication extends Application {

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
		LoginController login = new LoginController(primaryStage);
		login.show();
		System.out.println("end");
	}
}

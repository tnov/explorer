package net.tnk_toys.JerseyClient.gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.tnk_toys.JerseyClient.TreeWatcher;
import net.tnk_toys.JerseyClient.service.FxService;

public class LoginController extends AnchorPane implements Initializable {
	
	Stage stage = null;
	FxService service = null;
	@FXML public Button startButton;
	@FXML public Button endButton;
	@FXML public Button serviceStart;
	@FXML public Button serviceEnd;
	@FXML public Button actionBtn;
	private Thread thread;
	
	public LoginController(Stage stage) {
		this.stage = stage;
		this.service = new FxService();
		loadFxml();
	}
	
	private void loadFxml() {
		System.out.println(LoginController.class.getSimpleName());
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LoginController.class.getSimpleName() + ".fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onStart() {
		System.out.println("onStart");
		TreeWatcher watcher = new TreeWatcher(Paths.get("C:\\tmp\\"));
		thread = new Thread(watcher);
		thread.start();
	}
	
	public void onEnd() {
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("onEnd");
	}
	

	
	public void onServiceStart() {
		System.out.println("onStart");
		service.reset();
		service.start();
	}
	
	public void onServiceEnd() {
		service.cancel();
		System.out.println("onEnd");
	}

	public void onAction() {
		System.out.println("onAction");
	}
	
	public void show() {
		Scene scene = stage.getScene();
		if (scene == null) {
        	scene = new Scene(this);
            stage.setScene(scene);
        } else {
        	stage.getScene().setRoot(this);
        }
		this.stage.getScene().getStylesheets().add(getClass().getResource(LoginController.class.getSimpleName() + ".css").toExternalForm());
		this.stage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}

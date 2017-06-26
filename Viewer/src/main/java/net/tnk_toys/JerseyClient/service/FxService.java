package net.tnk_toys.JerseyClient.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FxService extends Service<Void> {

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				System.out.println("FxService start");
				System.out.println("FxService end");
				return null;
			}
		};
	}
}

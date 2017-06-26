package net.tnk_toys.JerseyClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class TreeWatcher implements Runnable {
	
	private static final Logger logger = Logger.getLogger(TreeWatcher.class.getName());
	private static final String DEFAULT_ROOT = System.getProperty("user.dir");

	private static final String DEFAULT_EXTENTION = ".properties";
	private String extention = DEFAULT_EXTENTION;
	
	private Map<Path,Properties> map = null;
	private Set<WatchKey> set = null;
	boolean isRunning = false;
	Path root = null;
	
	public TreeWatcher() {
		this.map = new HashMap<>();
		this.set = new HashSet<>();
		this.root = Paths.get(DEFAULT_ROOT);
	}
	
	public TreeWatcher(Path root) {
		this.map = new HashMap<>();
		this.set = new HashSet<>();
		this.root = root;
	}
	
	public static void main(String[] args) throws InterruptedException {
		logger.info("start main");
		TreeWatcher watcher = new TreeWatcher(Paths.get("C:\\tmp\\"));
		Thread thread = new Thread(watcher);
		thread.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.interrupt();
		thread.join();
		logger.info("end main");
	}
	
	public void watch() {
		logger.info("start watch");
		try (WatchService service = FileSystems.getDefault().newWatchService()) {
			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					logger.info("visit file = " + file.toFile().getCanonicalPath());
					if (file.getFileName().toString().endsWith(extention)) {
						Properties prop = new Properties();
						prop.load(Files.newBufferedReader(file));
						map.put(file, prop);
						logger.info("add properties = " + file.toFile().getCanonicalPath());
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					try {
						set.add(dir.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_MODIFY));
						logger.info("add watcher = " + dir.toFile().getCanonicalPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return FileVisitResult.CONTINUE;
				}
			});
			isRunning = true;
			while (isRunning) {
				try {
					WatchKey key = service.take();
					if(key.isValid()) {
						for (WatchEvent<?> event : key.pollEvents()) {
							Path targetPath = FileSystems.getDefault().getPath(key.watchable().toString() + File.pathSeparator + event.context());
							if(StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
								if (targetPath.toFile().isFile()) {
									if (targetPath.endsWith(extention)) {
										Properties prop = new Properties();
										prop.load(Files.newBufferedReader(targetPath));
										map.put(targetPath, prop);
										logger.info("add properties = " + targetPath.toFile().getCanonicalPath());
									}
								} else if (targetPath.toFile().isDirectory()) {
									set.add(targetPath.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_MODIFY));
									logger.info("add watcher = " + targetPath.toFile().getCanonicalPath());
								}
							} else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
								if (targetPath.toFile().isFile()) {
									if (targetPath.endsWith(extention)) {
										Properties prop = map.get(targetPath);
										prop.clear();
										prop.load(Files.newBufferedReader(targetPath));
										map.put(targetPath, prop);
										logger.info("modify properties = " + targetPath.toFile().getCanonicalPath());
									}
								} else if (targetPath.toFile().isDirectory()) {
									logger.info("modify watcher = " + targetPath.toFile().getCanonicalPath());
								}
							} else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
								if (targetPath.toFile().isFile()) {
									Properties prop = map.get(targetPath);
									prop.clear();
									prop = null;
									map.remove(targetPath);
									logger.info("delete properties = " + targetPath.toFile().getCanonicalPath());
								} else if (FileSystems.getDefault().getPath(key.watchable().toString()).toFile().isDirectory()) {
									set.remove(key);
									logger.info("delete watcher = " + FileSystems.getDefault().getPath(key.watchable().toString()).toFile().getCanonicalPath());
									key.cancel();
									continue;
								}
							}
						}
					}
					key.reset();
				} catch (InterruptedException e) {
					logger.info("interrupt");
					stop();
				}
			}
			stop();
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
		logger.info("end watch");
	}

	private void stop() {
		Set<Path> keyset = map.keySet();
		for (Path key : keyset) {
			map.get(key).clear();
		}
		map.clear();
		for (WatchKey key : set) {
			key.cancel();
		}
		set.clear();
		isRunning = false;
	}
	
	public void interrupt() {
		Thread.currentThread().interrupt();
	}
	
	@Override
	public void run() {
		logger.info("start run");
		watch();
		logger.info("end run");
	}
}

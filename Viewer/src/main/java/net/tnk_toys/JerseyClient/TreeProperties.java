package net.tnk_toys.JerseyClient;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TreeProperties {
	
	private static final Logger logger = Logger.getLogger(TreeProperties.class.getName());
	private static final String DEFAULT_ROOT = System.getProperty("user.dir");
	private static final String FILE_EXTENSION = ".properties";
	
	private static Map<Path, Properties> map = new HashMap<>();
	private boolean isAutoReload = false;

	private Path base = null;
	
	public TreeProperties() {
		base = Paths.get(DEFAULT_ROOT);
		loadProperties(base);
	}
	
	public TreeProperties(URI root) {
		base = Paths.get(root);
		loadProperties(base);
	}
	
	private void loadProperties(Path root) {
		if (root == null) {
			root = base;
		}
		try {
			Files.walkFileTree(root, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().endsWith(FILE_EXTENSION)) {
						logger.info(file.toString());
						Properties prop = new Properties();
						prop.load(new FileReader(file.toFile()));
						map.put(file, prop);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			logger.warning(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Properties getProperty(Path key) {
		return map.get(key);
	}
	
	public void clear() {
		map.clear();
	}
	
	public void reload() {
		map.clear();
		loadProperties(null);
	}
	
	public void setAutoReload(boolean isAutoReload) {
		this.isAutoReload = isAutoReload;
		// 監視処理はスレッド化(ポーリング時間を指定)
		// 停止処理はプロパティ変更
		if (this.isAutoReload) {
			try {
				WatchKey watchers = base.register(new WatchService() {
					@Override
					public WatchKey take() throws InterruptedException {
						return null;
					}
					
					@Override
					public WatchKey poll(long timeout, TimeUnit unit) throws InterruptedException {
						return null;
					}
					
					@Override
					public WatchKey poll() {
						return null;
					}
					
					@Override
					public void close() throws IOException {
					}
				}, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_DELETE);
				for (WatchEvent<?> pollEvent : watchers.pollEvents()) {
					pollEvent.kind().equals(StandardWatchEventKinds.ENTRY_CREATE);
					pollEvent.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY);
					pollEvent.kind().equals(StandardWatchEventKinds.ENTRY_DELETE);
				}
			} catch (IOException e) {
				logger.warning(e.getMessage());
				e.printStackTrace();
			}
		} else {
			
		}
	}
	
	public boolean isAutoReload() {
		return isAutoReload;
	}
}

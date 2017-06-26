package net.tnk_toys.JerseyClient.gui.propertyeditor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;

public class FolderTreeItemCreator {

	private TreeItem<FolderTreeItemData> treeItem;
	
	public TreeItem<FolderTreeItemData> getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem<FolderTreeItemData> treeItem) {
		this.treeItem = treeItem;
	}
	
	public FolderTreeItemCreator(Path dir) {
		treeItem = new TreeItem<FolderTreeItemData>(new FolderTreeItemData(dir));
		if (Files.isDirectory(dir) && Files.isReadable(dir)) {
			treeItem.addEventHandler(TreeItem.<FolderTreeItemData>branchExpandedEvent(), new EventHandler<TreeItem.TreeModificationEvent<FolderTreeItemData>>() {
				@Override
				public void handle(TreeModificationEvent<FolderTreeItemData> event) {
					event.getTreeItem().getChildren().clear();
					createFileTree(event.getTreeItem(),2);
				}
			});
		}
		createFileTree(treeItem, 2);
	}
	
	private void createFileTree(TreeItem<FolderTreeItemData> treeItem,int depth) {
		int d = depth;
		if (d > 0) {
			if (Files.isReadable(treeItem.getValue().getPath())) {
				try (Stream<Path> list = Files.list(treeItem.getValue().getPath())) {
					list.filter(file->Files.isDirectory(file))
					.filter(file->Files.isReadable(file))
					.sorted()
					.forEach(file->{
						TreeItem<FolderTreeItemData> item = new TreeItem<FolderTreeItemData>(new FolderTreeItemData(file));
						createFileTree(item,d-1);
						treeItem.getChildren().add(item);
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

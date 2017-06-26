package net.tnk_toys.JerseyClient.gui.propertyeditor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EditorController extends AnchorPane {
	
	Stage stage = null;
        @FXML
        public ComboBox<String> driveSelector;
	@FXML
	public TreeView<FolderTreeItemData> folderTree;
	@FXML
	public TableView<FileItem> tableView;
	@FXML
	public TableColumn<FileItem,String> nameColumn;
	@FXML
	public TableColumn<FileItem,String> extColumn;
	@FXML
	public TableColumn<FileItem,String> sizeColumn;
	@FXML
	public TableColumn<FileItem,String> lastModifiedColumn;
	@FXML
	public ListView<ImageView> listView;
	
	private Path base = null;
	
	public EditorController(Stage stage,Path path) {
		this.stage = stage;
		this.base = path;
		loadFxml();
                this.driveSelector.setItems(FXCollections.observableList(Arrays.asList(File.listRoots()).stream().map(file -> file.getPath()).collect(Collectors.toList())));
                this.driveSelector.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    // TODO UPDATE CHANGE ACTION
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                });
		FolderTreeItemCreator creator = new FolderTreeItemCreator(this.base);
		this.folderTree.setRoot(creator.getTreeItem());
		this.folderTree.getFocusModel().focusedItemProperty().addListener((ObservableValue<? extends TreeItem<FolderTreeItemData>> observable, TreeItem<FolderTreeItemData> oldValue, TreeItem<FolderTreeItemData> newValue) -> {
                    try {
                        tableView.getItems().clear();
                        Files.list(newValue.getValue().getPath()).filter((java.nio.file.Path path1) -> !Files.isDirectory(path1)).sorted().forEach((java.nio.file.Path path2) -> {
                            try {
                                tableView.getItems().add(new FileItem(path2.getFileName().toString(), path2.getFileName().toString(), Long.valueOf(Files.size(path2)).toString(), Files.getLastModifiedTime(path2).toString()));
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                });
		// カラム設定
		Callback<TableColumn<FileItem, String>, TableCell<FileItem, String>> nameFactory = TextFieldTableCell.forTableColumn();
		nameColumn.setCellFactory(nameFactory);
		nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());
		Callback<TableColumn<FileItem, String>, TableCell<FileItem, String>> extFactory = TextFieldTableCell.forTableColumn();
		extColumn.setCellFactory(extFactory);
		extColumn.setCellValueFactory(param -> param.getValue().getExtProperty());
		Callback<TableColumn<FileItem, String>, TableCell<FileItem, String>> sizeFactory = TextFieldTableCell.forTableColumn();
		sizeColumn.setCellFactory(sizeFactory);
		sizeColumn.setCellValueFactory(param -> param.getValue().getSizeProperty());
		Callback<TableColumn<FileItem, String>, TableCell<FileItem, String>> lastModifiedFactory = TextFieldTableCell.forTableColumn();
		lastModifiedColumn.setCellFactory(lastModifiedFactory);
		lastModifiedColumn.setCellValueFactory(param -> param.getValue().getLastModifiedProperty());
	}
	
	private void loadFxml() {
		System.out.println(EditorController.class.getSimpleName());
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(EditorController.class.getSimpleName() + ".fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void show() {
		Scene scene = stage.getScene();
		if (scene == null) {
        	scene = new Scene(this);
            stage.setScene(scene);
        } else {
        	stage.getScene().setRoot(this);
        }
		this.stage.getScene().getStylesheets().add(getClass().getResource(EditorController.class.getSimpleName() + ".css").toExternalForm());
		this.stage.show();
	}
}

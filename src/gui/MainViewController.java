package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSeller() {
		Alerts.showAlert(null, null, "Seller", AlertType.INFORMATION);
	}
	
	@FXML
	public void onMenuItemDepartment() {
		//Alerts.showAlert(null, null, "Department", AlertType.INFORMATION);
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAbout() {
		//Alerts.showAlert(null, null, "About", AlertType.INFORMATION);
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		
	}
	
	private synchronized void loadView (String absolutName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2 (String absolutName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//Preenche o tableview do departamento
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}
	}

}

package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	public void onMenuItemSeller() {
		Alerts.showAlert(null, null, "Seller", AlertType.INFORMATION);
	}
	
	public void onMenuItemDepartment() {
		Alerts.showAlert(null, null, "Department", AlertType.INFORMATION);		
	}
	
	public void onMenuItemAbout() {
		Alerts.showAlert(null, null, "About", AlertType.INFORMATION);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}

}

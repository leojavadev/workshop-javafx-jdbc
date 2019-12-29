package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	private DepartmentService service;
	
	private ObservableList<Department> obsList;
	
	@FXML
	private Button btNovo;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	public void onBtNovoAction() {
		Alerts.showAlert(null, null, "Novo Departamento", AlertType.INFORMATION);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		//Faz a tableview preencher toda a tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		//Fim
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("O atributo service está nulo");
		}
		List<Department> lista = service.findAll();
		obsList = FXCollections.observableArrayList(lista);
		tableViewDepartment.setItems(obsList);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
}

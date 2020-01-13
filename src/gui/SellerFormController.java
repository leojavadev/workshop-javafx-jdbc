package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity;
	
	private SellerService sellerService;
	
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField tfId;
	
	@FXML
	private TextField tfNome;
	
	@FXML
	private Label lbErro;
	
	@FXML
	private TextField tfEmail;
	
	@FXML
	private Label lbErroEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private Label lbErroBirthDate;
		
	@FXML
	private TextField tfSalary;
	
	@FXML
	private Label lbErroSalary;
	
	@FXML
	private ComboBox<Department> cbDepartment;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("O atributo entiy estava null");
		}
		if(sellerService == null) {
			throw new IllegalStateException("O atributo service estava null");
		}
		try {
			entity = getFormData();
			sellerService.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Erro de validação!");
		
		obj.setId(Utils.tryParseToInt(tfId.getText()));
				
		if(tfNome.getText() == null || tfNome.getText().trim().equals("")) {
			exception.addErrors("nome", "   Esse campo deve ser preenchido!");
		}
		obj.setName(tfNome.getText());
		
		if(tfEmail.getText() == null || tfEmail.getText().trim().equals("")) {
			exception.addErrors("email", "   Esse campo deve ser preenchido!");
		}
		obj.setEmail(tfEmail.getText());
		
		if(dpBirthDate.getValue() == null) {
			exception.addErrors("birthDate", "   Esse campo deve ser preenchido!");
		} else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		
		if(tfSalary.getText() == null || tfSalary.getText().trim().equals("")) {
			exception.addErrors("baseSalary", "   Esse campo deve ser preenchido!");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(tfSalary.getText()));
		
		obj.setDepartment(cbDepartment.getValue());
				
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
//		if(fields.contains("nome")) {
//			lbErro.setText(errors.get("nome"));
//		} else {
//			lbErro.setText("");
//		} 
//		
//		OU
		
		lbErro.setText((fields.contains("nome") ? errors.get("nome") : ""));
		
		lbErroEmail.setText(fields.contains("email") ? errors.get("email") : "");
		
		lbErroBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");
		
		lbErroSalary.setText(fields.contains("baseSalary") ? errors.get("baseSalary") : "");
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setServices(SellerService sellerService, DepartmentService departmentService) {
		this.sellerService = sellerService;
		this.departmentService = departmentService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(tfId);
		Constraints.setTextFieldMaxLength(tfNome, 70);
		Constraints.setTextFieldDouble(tfSalary);
		Constraints.setTextFieldMaxLength(tfEmail, 50);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("O atributo entity é null");
		}
		tfId.setText(String.valueOf(entity.getId()));
		tfNome.setText(entity.getName());
		tfEmail.setText(entity.getEmail());
		if(entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		tfSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if(entity.getDepartment() == null) {
			cbDepartment.getSelectionModel().selectFirst();
		} else {
			cbDepartment.setValue(entity.getDepartment());
		}
	}
	
	public void loadAssociatedObjects() {
		if(departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
//		for(Department dp : list) {
//			obsList.add(dp);
//		}
		obsList = FXCollections.observableArrayList(list);
		cbDepartment.setItems(obsList);
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cbDepartment.setCellFactory(factory);
		cbDepartment.setButtonCell(factory.call(null));
	}

}

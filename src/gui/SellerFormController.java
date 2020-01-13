package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity;
	
	private SellerService service;
	
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
	private DatePicker tfBirthDate;
	
	@FXML
	private Label lbErroBirthDate;
	
	@FXML
	private TextField tfSalary;
	
	@FXML
	private Label lbErroSalary;
	
	@FXML
	private ComboBox<Department> cbDepartmentId;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("O atributo entiy estava null");
		}
		if(service == null) {
			throw new IllegalStateException("O atributo service estava null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
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
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("nome")) {
			lbErro.setText(errors.get("nome"));
		}
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
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
		Utils.formatDatePicker(tfBirthDate, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("O atributo entity é null");
		}
		tfId.setText(String.valueOf(entity.getId()));
		tfNome.setText(entity.getName());
		tfEmail.setText(entity.getEmail());
		if(entity.getBirthDate() != null) {
			tfBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		tfSalary.setText(String.format("%.2f", entity.getBaseSalary()));
	}

}

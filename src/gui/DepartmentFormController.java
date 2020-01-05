package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	@FXML
	private TextField tfNome;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	private Label lbErro;
	
	private DepartmentDao dpDao = DaoFactory.createDepartmentDao();
	
	private DepartmentListController dpLC;
	
	@FXML
	public void onBtSalvarAction() {
		Department dp = new Department(null, tfNome.getText());
		dpDao.insert(dp);
		tfNome.setText("");
		dpLC.updateTableView();
		dpLC.initialize(null, null);
	}
	
	@FXML
	public void onBtCancelarAction() {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(tfNome, 30);
	}

}

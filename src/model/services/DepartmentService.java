package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	public List<Department> findAll(){
		List<Department> lista = new ArrayList<>();
		lista.add(new Department(1, "Brinquedos"));
		lista.add(new Department(2, "Auto"));
		lista.add(new Department(3, "Limpeza"));
		return lista;
	}

}

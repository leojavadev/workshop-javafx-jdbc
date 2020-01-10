package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dpDao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return dpDao.findAll();
	}
	
	public void saveOrUpdate(Department obj) {
		if(obj.getId() == null) {
			dpDao.insert(obj);
		} else {
			dpDao.update(obj);
		}
	}
	
	public void remove(Department obj) {
		dpDao.deleteById(obj.getId());
	}

}

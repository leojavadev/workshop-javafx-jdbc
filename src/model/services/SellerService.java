package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao slDao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		return slDao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) {
		if(obj.getId() == null) {
			slDao.insert(obj);
		} else {
			slDao.update(obj);
		}
	}
	
	public void remove(Seller obj) {
		slDao.deleteById(obj.getId());
	}

}

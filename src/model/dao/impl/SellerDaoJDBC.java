package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	private static Statement st = null;
	private static PreparedStatement pst = null;
	private static ResultSet rs = null;

	private Seller instantiateSeller(ResultSet rs, Department dept) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(dept);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dept = new Department();
		dept.setId(rs.getInt("DepartmentId"));
		dept.setName(rs.getString("DepName"));
		return dept;
	}

	@Override
	public void insert(Seller obj) {
		try {
			pst = conn.prepareStatement("INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			int rowsAffected = pst.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = pst.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				} else {
					throw new DbException("Falha ao inserir um novo vendedor no banco de dados!");
				}
				DB.closeResultSet(rs);
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closePreparedStatement(pst);
		}		
	}

	@Override
	public void update(Seller obj) {
		try {			
			pst = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, "
					+ "BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			pst.setInt(6, obj.getId());			
			pst.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public void deleteById(Integer id) {
		try {
			pst = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			pst.setInt(1, id);
			pst.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closePreparedStatement(pst);
		}
	}

	@Override
	public Seller findById(Integer id) {
		try {
			pst = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department "
					+ "ON department.Id = seller.DepartmentId "
					+ "WHERE seller.Id = ?");
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if(rs.next()) {
				Department dept = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, dept);
				return seller;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closePreparedStatement(pst);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT seller.*,department.Name AS DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			List<Seller> lista = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while(rs.next()) {
				Department dept = map.get(rs.getInt("DepartmentId"));
				if(dept == null) {
					dept = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dept);
				}
				Seller seller = instantiateSeller(rs, dept);
				lista.add(seller);
			}
			return lista;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		try {
			pst = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			pst.setInt(1, department.getId());
			rs = pst.executeQuery();
			List<Seller> lista = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); 
			while(rs.next()) {
				Department dept = map.get(rs.getInt("DepartmentId"));
				if(dept == null) {
					dept = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dept);
				}
				Seller seller = instantiateSeller(rs, dept);
				lista.add(seller);
			}
			return lista;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closePreparedStatement(pst);
			DB.closeResultSet(rs);
		}
	}

}

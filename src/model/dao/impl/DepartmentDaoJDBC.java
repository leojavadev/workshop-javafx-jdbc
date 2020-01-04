package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn = null;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	private static PreparedStatement pst = null;
	private static Statement st = null;
	private static ResultSet rs = null;

	@Override
	public void insert(Department obj) {
		try {
			pst = conn.prepareStatement("INSERT INTO department (Name) "
				+ "VALUES "
				+ "(?)",Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getName());
			int rowsAffected = pst.executeUpdate();
			if(rowsAffected > 0) {
				rs = pst.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Falha ao inserir um novo departamento no banco de dados!");
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
	public void update(Department obj) {
		try {
			pst = conn.prepareStatement("UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
			pst.setString(1, obj.getName());
			pst.setInt(2, obj.getId());
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
	public void deleteById(Integer id) {
		try {
			pst = conn.prepareStatement("DELETE FROM department "
					+ "WHERE Id = ?");
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
	public Department findById(Integer id) {
		try {
			pst = conn.prepareStatement("SELECT * FROM department "
					+ "WHERE Id = ?");
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if(rs.next()) {
				Department dp = new Department(rs.getInt("Id"), rs.getString("Name"));
				return dp;
			}
			return null;
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
	public List<Department> findAll() {
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM department ORDER BY Name");
			List<Department> lista = new ArrayList<>();
			while(rs.next()) {
				Department dp = new Department(rs.getInt("Id"), rs.getString("Name"));
				lista.add(dp);
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

}

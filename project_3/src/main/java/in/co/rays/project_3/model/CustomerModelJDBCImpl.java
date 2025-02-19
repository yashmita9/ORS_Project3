package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.CustomerDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

public class CustomerModelJDBCImpl implements CustomerModelInt{
	
	private static Logger log = Logger.getLogger(CustomerModelJDBCImpl.class);
	Connection con = null;
	PreparedStatement ps = null;

	/**
	 * create id
	 * 
	 * @return pk
	 * @throws DatabaseException
	 */
	public long nextPK() throws DatabaseException {
		long pk = 0;
		log.debug("model nextPk start");

		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("select max(ID) from customer");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1);
			}
			ps.close();
			con.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new DatabaseException("Exception getting in pk");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		log.debug("model nextpk end");
		return pk + 1;

	}

	/**
	 * add new course
	 * 
	 * @param b
	 * @return pk
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(CustomerDTO b) throws ApplicationException, DuplicateRecordException {
		long pk = 0;
		log.debug("model add start");
		
		String query = "insert into customer values(?,?,?,?,?)";
		try {
			pk = nextPK();
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, pk);
			ps.setString(2, b.getClienName());
			ps.setString(3, b.getLocation());
			ps.setString(4, b.getContactNo());

			ps.setLong(5, b.getImportance());
			
			int a = ps.executeUpdate();
			System.out.println("ok:");
			ps.close();
			con.commit();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				con.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add Customer");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		log.debug("Model add End");
		return 0;
	}

	/**
	 * update course information
	 * 
	 * @param b
	 * @throws ApplicationException
	 */
	public void update(CustomerDTO dto) throws ApplicationException {
		System.out.println("hello" + dto.getId());
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("UPDATE customer SET client_name=?,location=?,contact_no=?"
					+ ",importance=?" + " WHERE ID=?");
			con.setAutoCommit(false);

			ps.setString(1, dto.getClienName());
			ps.setString(2, dto.getLocation());
			ps.setString(3, dto.getContactNo());
			ps.setLong(4, dto.getImportance());
			ps.setLong(5, dto.getId());
			ps.executeUpdate();
			System.out.println("DATA UPDATE ");
			ps.close();
			con.commit();
		} catch (Exception e) {
			throw new ApplicationException("exception in customer model update...." + e.getMessage());
		}
		JDBCDataSource.closeConnection(con);

	}

	/**
	 * delete course information in table
	 * 
	 * @param b
	 * @throws ApplicationException
	 */
	public void delete(CustomerDTO b) throws ApplicationException {
		log.debug("model delete start");
		String query = " delete  from customer where ID=?";
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setLong(1, b.getId());
			int a = ps.executeUpdate();

			System.out.print("ok " + a);
			ps.close();
			con.commit();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				con.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Customer");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		log.debug("Model delete Started");
	}

	public List list() throws ApplicationException {

		return list(0, 0);
	}

	/**
	 * to show course list
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return list
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from customer");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;
		CustomerDTO dto = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				dto = new CustomerDTO();
				dto.setId(rs.getLong(1));
				dto.setClienName(rs.getString(2));
				dto.setLocation(rs.getString(3));
				dto.setContactNo(rs.getString(4));
				dto.setImportance(rs.getLong(5));
				
				list.add(dto);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of Role");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;

	}

	public List search(CustomerDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	/**
	 * search list of course detail
	 * 
	 * @param cdto1
	 * @param pageNo
	 * @param pageSize
	 * @return list
	 * @throws ApplicationException
	 */
	public List search(CustomerDTO cdto1, int pageNo, int pageSize) throws ApplicationException {
		log.debug("model search start");
		StringBuffer sql = new StringBuffer("select * from customer where 1=1");
		if (cdto1 != null) {

			if (cdto1.getId() > 0) {
				sql.append(" AND ID = " + cdto1.getId());
			}
			if ((cdto1.getClienName() != null) && (cdto1.getClienName().length() > 0)) {
				sql.append(" AND client_name like '" + cdto1.getClienName() + "%'");
			}
			if ((cdto1.getLocation() != null) && (cdto1.getLocation().length() > 0)) {
				sql.append(" AND location like '" + cdto1.getLocation() + "%'");

			}
			if ((cdto1.getContactNo() != null) && (cdto1.getContactNo().length() > 0)) {
				sql.append(" AND contact_no like '" + cdto1.getContactNo() + "%'");

			}
		}
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + "," + pageSize);

			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList<CustomerDTO> ar = new ArrayList<CustomerDTO>();
		Connection con = null;
		try {

			con = JDBCDataSource.getConnection();

			PreparedStatement ps = con.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDTO dto = new CustomerDTO();
				dto.setId(rs.getLong(1));
				dto.setClienName(rs.getString(2));
				dto.setLocation(rs.getString(3));
				dto.setContactNo(rs.getString(4));
				dto.setImportance(rs.getLong(5));
				ar.add(dto);
			}

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search customer");
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		log.debug("Model search End");

		return ar;

	}

	/**
	 * find information by pk
	 * 
	 * @param pk
	 * @return dto
	 * @throws ApplicationException
	 */
	public CustomerDTO findByPK(long pk) throws ApplicationException {
		log.debug("model findby pk start");
		CustomerDTO dto = null;
		try {
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement("select * from customer where ID=?");
			ps.setLong(1, pk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dto = new CustomerDTO();
				dto.setId(rs.getLong(1));
				dto.setClienName(rs.getString(2));
				dto.setLocation(rs.getString(3));
				dto.setContactNo(rs.getString(4));
				dto.setImportance(rs.getLong(5));
				}
			ps.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting customerBYPk");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		log.debug("model findBy pk end");

		return dto;

	}

	/**
	 * find course by name
	 * 
	 * @param Name
	 * @return dto
	 * @throws ApplicationException
	 */
	
}

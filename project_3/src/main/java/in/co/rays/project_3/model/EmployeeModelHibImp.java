package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import in.co.rays.project_3.dto.CollegeDTO;
import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of Employee model
 * 
 * @author Yashmita Rathore
 *
 */
public class EmployeeModelHibImp implements EmployeeModelInt {

	@Override
	public void delete(EmployeeDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Employee Delete" + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		EmployeeDTO dtoExist = findByLogin(dto.getUserName());

		if (dtoExist != null && dtoExist.getId() != dto.getId()) {
			throw new DuplicateRecordException("Login Id already exist....");
		}

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();

			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Employee update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0, 0);
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize) + 1;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();
		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in  Employee list");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List search(EmployeeDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto, 0, 0);
	}

	@Override
	public List search(EmployeeDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			if (dto.getId() > 0) {
				criteria.add(Restrictions.eq("id", dto.getId()));
			}
			if (dto.getName() != null && dto.getName().length() > 0) {
				criteria.add(Restrictions.like("name", dto.getName() + "%"));
			}
			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in Employee search");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public EmployeeDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		EmployeeDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (EmployeeDTO) session.get(EmployeeDTO.class, pk);
		} catch (HibernateException e) {
			// TODO: handle exception
			throw new ApplicationException("Exception : Exception in getting Employee by pk");
		} finally {
			session.close();
		}
		System.out.println("++++" + dto);
		return dto;
	}

	@Override
	public EmployeeDTO findByLogin(String login) throws ApplicationException {
		Session session = null;
		EmployeeDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			criteria.add(Restrictions.eq("userName", login));
			List list=criteria.list();
			if(list.size()==1){
				dto=(EmployeeDTO) list.get(0);
			}
		}catch (HibernateException e) {
	            
	            throw new ApplicationException("Exception in getting User by Login " + e.getMessage());

	        } finally {
	            session.close();
	        }
			return dto;
		}

	@Override
	public long add(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		EmployeeDTO duplicateEmp = findByLogin(dto.getUserName());
		if (duplicateEmp != null) {
			throw new DuplicateRecordException("UserName already exist");
		}
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Employee Add " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();
	}

}

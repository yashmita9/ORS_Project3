package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.CourseDTO;
import in.co.rays.project_3.dto.CustomerDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class CustomerModelHibImp implements CustomerModelInt {

	@Override
	public long add(CustomerDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		long pk = 0;
		
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.save(dto);
			pk = dto.getId();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in customer Add " + e.getMessage());
		} finally {
			session.close();
		}
		return pk;
	}

	@Override
	public void delete(CustomerDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		}catch (HibernateException e) {
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in customer delete " + e.getMessage());
		} finally {
			session.close();
		}

		
	}

	@Override
	public void update(CustomerDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.update(dto);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in customer update " + e.getMessage());
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
			Criteria criteria = session.createCriteria(CustomerDTO.class);
			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize) + 1;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();
		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in  Customer list");
		} finally {
			session.close();
		}
		return list;

	}

	@Override
	public List search(CustomerDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(CustomerDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
        List list = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(CustomerDTO.class);

            if (dto.getId() > 0) {
                criteria.add(Restrictions.eq("id", dto.getId()));
            }
            if (dto.getClienName() != null && dto.getClienName().length() > 0) {
                criteria.add(Restrictions.like("clientName", dto.getClienName() + "%"));
            }
            if (dto.getLocation() != null && dto.getLocation().length() > 0) {
                criteria.add(Restrictions.like("location", dto.getLocation()
                        + "%"));
            }
            if (dto.getContactNo() != null && dto.getContactNo().length() > 0) {
                criteria.add(Restrictions.like("contactNo", dto.getContactNo() + "%"));
            }
            

            // if page size is greater than zero the apply pagination
            if (pageSize > 0) {
                criteria.setFirstResult(((pageNo - 1) * pageSize));
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        } catch (HibernateException e) {
            
            throw new ApplicationException("Exception in Customer search");
        } finally {
            session.close();
        }

       
        return list;
	}

	@Override
	public CustomerDTO findByPK(long pk) throws ApplicationException {
		System.out.println("======"+pk);
		Session session = null;
		CustomerDTO dto = null;
		try {
			session = HibDataSource.getSession();

			dto = (CustomerDTO) session.get(CustomerDTO.class, pk);
		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in getting customer by pk");
		} finally {
			session.close();
		}
		System.out.println("-------"+dto);
		return dto;
	}

}

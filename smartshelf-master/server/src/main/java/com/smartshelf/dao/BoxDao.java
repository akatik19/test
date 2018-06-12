package com.smartshelf.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.smartshelf.model.Box;

@Repository
@Transactional
public class BoxDao extends AbstractGenericDao<Box> {
	
   public void add(Box box) {
	   entityManager.persist(box);
   }

   public List<Box> listPersons() {
      CriteriaQuery<Box> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Box.class);
      @SuppressWarnings("unused")
      Root<Box> root = criteriaQuery.from(Box.class);
      return entityManager.createQuery(criteriaQuery).getResultList();
   }
   
   /***
    * Finds the box by its box id (not primary key)
    * @param boxid
    * @return
    */
   public Box findByBoxId(long boxid) {
	   
	   TypedQuery<Box> query = entityManager.createQuery("SELECT e FROM Box e WHERE e.boxid = :boxid", Box.class);
	   query.setParameter("boxid", boxid);
	   
	   return query.getSingleResult();
   }
}

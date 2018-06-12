package com.smartshelf.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class AbstractGenericDao<Entity extends Serializable> {

	@PersistenceContext
	protected EntityManager entityManager; 
	
	protected Class<Entity> entityType; 
	protected String tableName;
	
	AbstractGenericDao() {
	}
	
	public void setEntityClass(Class<Entity> entityType) {
        this.entityType = entityType;
        this.tableName = entityType.getName();
    }
		
	public void create(Entity entity) {
		entityManager.persist( entity );
		entityManager.flush(); 
	}
	
	
	public Entity findById(long id) {
		return entityManager.find(entityType, id);
	}
	
	public List<Entity> findAll() {
		String query = String.format("SELECT s FROM %s s", this.tableName); 
		return entityManager.createQuery(query, entityType).getResultList();
	}
	
	
	public Entity save(Entity entity) {
		return entityManager.merge(entity);
	}

	public void delete(Entity entity) {
		entityManager.remove(entity);
	}
}

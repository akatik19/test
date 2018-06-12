package com.smartshelf.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.smartshelf.model.Item;

@Repository
@Transactional
public class ItemDao extends AbstractGenericDao<Item> {
	
	private final static String GET_BY_KEYWORD_QUERY = "SELECT e FROM Item e WHERE e.name LIKE :keyword";
	
	public List<Item> findWithKeyword(String keyword) {
		
		TypedQuery<Item> query = entityManager.createQuery(GET_BY_KEYWORD_QUERY, Item.class);
		String wildcardKeyword = "%" + keyword + "%";
		query.setParameter("keyword", wildcardKeyword);
		
		return query.getResultList(); 
	}
}

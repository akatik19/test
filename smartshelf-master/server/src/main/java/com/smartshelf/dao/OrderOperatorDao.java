package com.smartshelf.dao;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.smartshelf.model.OrderOperator;

@Repository
@Transactional
public class OrderOperatorDao extends AbstractGenericDao<OrderOperator> {

}

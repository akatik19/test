package com.smartshelf.dao;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.smartshelf.model.User;

@Repository
@Transactional
public class UserDao extends AbstractGenericDao<User> {

}

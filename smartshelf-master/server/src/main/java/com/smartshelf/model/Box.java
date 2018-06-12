package com.smartshelf.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "box")
public class Box implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id; 
	
	@Column(unique=true)
	public long boxid;
	
	@OneToOne(cascade=CascadeType.ALL)
	public Item item; 
	
	@Column
	public Integer amount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public long getBoxid() {
		return boxid;
	}

	public void setBoxid(long boxid) {
		this.boxid = boxid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	} 
}

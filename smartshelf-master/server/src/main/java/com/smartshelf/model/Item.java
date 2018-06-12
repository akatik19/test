package com.smartshelf.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "item")
@SequenceGenerator(name="item_seq", initialValue=1, allocationSize=100)
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "item_seq")
	public long id; 
	
	@Column(nullable = false)
	public String name; 
	
	@Column
	public String description; 
	
	@Column 
	public ItemStatus status;
	
	@OneToOne(mappedBy="item")
	public Box box;
	
	@OneToMany
	public List<ItemDatasheet> datasheet;
	
	@Column
	public Float weight; 

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ItemDatasheet> getDatasheet() {
		return datasheet;
	}

	public void setDatasheet(List<ItemDatasheet> datasheet) {
		this.datasheet = datasheet;
	}

	public ItemStatus getStatus() {
		return status;
	}

	public void setStatus(ItemStatus status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
}

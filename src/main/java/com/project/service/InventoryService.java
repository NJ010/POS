package com.project.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dao.InventoryDao;
import com.project.pojo.InventoryPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao inventory_dao;
	
	// Adding Inventory 
	@Transactional
	public void add(InventoryPojo pojo) throws ApiException {
		check(pojo);
		List<InventoryPojo> pojo_list= checkIfBarcodePresent(pojo);
		if(pojo_list.size()>0) {
			inventory_dao.updatequantity(pojo_list.get(0), pojo);
		}
		else {
			inventory_dao.insert(pojo);
		}
	}

	// Deletion of inventory by id 
	@Transactional
	public void delete(int id) {
		inventory_dao.delete(id);
	}

	// Fetch inventory by id 
	@Transactional
	public InventoryPojo get(int id) throws ApiException {
		InventoryPojo pojo = checkIfExists(id);
		return pojo;
	}

	//Fetch inventory by product id 
	@Transactional
	public InventoryPojo getByProductId(int product_id) throws ApiException {
		List<InventoryPojo> lis = getAll();
		for (InventoryPojo ip : lis) {
			if (ip.getProduct().getId() == product_id) {
				return ip;
			}
		}
		throw new ApiException("No product with this id exist");
	}

	// Fetch all inventory pojos
	@Transactional
	public List<InventoryPojo> getAll() {
		return inventory_dao.selectAll();
	}

	// Updation of inventory 
	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, InventoryPojo p) throws ApiException {
		check(p);
		InventoryPojo pojo = checkIfExists(id);
		pojo.setQuantity(p.getQuantity());
		inventory_dao.update(id,pojo);
	}
	
	///// HELPER FUNCTIONS/////////

	// Checking if particular inventory pojo exists 
	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo checkIfExists(int id) throws ApiException {
		InventoryPojo p = inventory_dao.select(id);
		if (p == null) {
			throw new ApiException("Inventory with given ID does not exist, id: " + id);
		}
		return p;
	}

	/* Validate */
	protected void check(InventoryPojo pojo) throws ApiException {
		if (pojo.getQuantity() < 0) {
			throw new ApiException("Inventory quantity should be positive");
		}

	}

	// Check if inventory exists or not by barcode 
	protected List<InventoryPojo> checkIfBarcodePresent(InventoryPojo p) throws ApiException {
		List<InventoryPojo> lis = inventory_dao.selectByProduct(p.getProduct());
		return lis;

	}
	
}

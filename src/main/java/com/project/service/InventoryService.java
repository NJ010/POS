package com.project.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.project.model.InventoryForm;
import com.project.model.TsvError;
import com.project.pojo.ProductPojo;
import com.project.utilities.DataConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dao.InventoryDao;
import com.project.pojo.InventoryPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao inventory_dao;

	@Autowired
	private ProductService product_service;
	
	// Adding Inventory 
	@Transactional
	public void add(InventoryPojo pojo) throws ApiException {
		check(pojo);
		List<InventoryPojo> pojo_list= checkIfBarcodePresent(pojo);
		if(pojo_list.size()>0) {
			inventory_dao.updatequantity(pojo_list.get(0).getId(), pojo);
		}
		else {
			inventory_dao.insert(pojo);
		}
	}

	@Transactional
	public List<TsvError> add(List<InventoryForm> list) throws ApiException {
		List<TsvError> errors= new ArrayList<TsvError>();

		for (int i = 0; i < list.size(); i++) {
			try{
				ProductPojo product = product_service.get(list.get(i).getBarcode());
				InventoryPojo inventory_pojo = DataConversionUtil.convert(list.get(i),product);
				check(inventory_pojo);
				List<InventoryPojo> pojo_list= checkIfBarcodePresent(inventory_pojo);
				if(pojo_list.size()>0) {
					inventory_dao.updatequantity(pojo_list.get(0).getId(), inventory_pojo);
				}
				else {
					inventory_dao.insert(inventory_pojo);
				}
				TsvError error = new TsvError();
				error.setLine(i+1);
				error.setErrorMessage("Success");
				errors.add(error);
			}
			catch (ApiException e){

				TsvError error = new TsvError();
				error.setLine(i+1);
				error.setErrorMessage(e.getMessage());
				errors.add(error);
			}
		}
		return errors;
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

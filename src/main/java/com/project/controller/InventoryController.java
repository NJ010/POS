package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.InventoryData;
import com.project.model.InventoryForm;
import com.project.pojo.InventoryPojo;
import com.project.pojo.ProductPojo;
import com.project.service.ApiException;
import com.project.service.InventoryService;
import com.project.service.ProductService;
import com.project.utilities.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {

	@Autowired
	private InventoryService inventory_service;

	@Autowired
	private ProductService product_service;

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm userform) throws ApiException {
		ProductPojo product = product_service.get(userform.getBarcode());
		InventoryPojo inventory_pojo = DataConversionUtil.convert(userform,product);
		inventory_service.add(inventory_pojo);
	}

	@ApiOperation(value = "Deletes an Inventory record")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		inventory_service.delete(id);
	}

	@ApiOperation(value = "Gets an Inventory record by id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo inventory_pojo = inventory_service.get(id);
		return DataConversionUtil.convert(inventory_pojo);
	}

	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		List<InventoryPojo> inventory_pojo_list = inventory_service.getAll();
		return DataConversionUtil.convertInventoryList(inventory_pojo_list);
	}

	@ApiOperation(value = "Updates an Inventory record")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		ProductPojo product = product_service.get(f.getBarcode());
		InventoryPojo inventory_pojo = DataConversionUtil.convert(f,product);
		inventory_service.update(id, inventory_pojo);
	}

	
}

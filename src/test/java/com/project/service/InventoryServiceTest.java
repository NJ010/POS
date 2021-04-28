package com.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.project.pojo.InventoryPojo;
import com.project.pojo.ProductPojo;
import com.project.spring.AbstractUnit;

public class InventoryServiceTest extends AbstractUnit{
	
	
	@Before
	public void Initialize() throws ApiException {
		insertObjects();
	}
	
	@Test
	public void testAdd() throws ApiException {
		
		InventoryPojo i = getInventoryPojo(products.get(2));
		List<InventoryPojo> inv_list_before = inventory_service.getAll();
		inventory_service.add(i);
		List<InventoryPojo> inv_list_after = inventory_service.getAll();
		// Number of brand pojos should increase by one
		assertEquals(inv_list_before.size() + 1, inv_list_after.size());
		assertEquals(i.getProduct(), inventory_service.get(i.getId()).getProduct());
		assertEquals(i.getQuantity(), inventory_service.get(i.getId()).getQuantity());

	}

	

	/* Testing adding of an invalid pojo. Should throw exception */
	@Test()
	public void testAddWrong() throws ApiException {

		InventoryPojo i = getWrongInventoryPojo(products.get(0));

		try {
			inventory_service.add(i);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}

	}

	/* Testing deletion of pojo */
	@Test()
	public void testDelete() throws ApiException {

		InventoryPojo i = getInventoryPojo(products.get(2));
		inventory_service.add(i);
		List<InventoryPojo> inv_list_before = inventory_service.getAll();
		inventory_service.delete(i.getId());
		List<InventoryPojo> inv_list_after = inventory_service.getAll();
		// Number of pojos should get decreased by one
		assertEquals(inv_list_before.size() - 1, inv_list_after.size());
		try {
			inventory_service.get(i.getId());
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + i.getId());
		}

	}
	
	@Test()
	public void testCheckIfExistsId() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.checkIfExists(inventories.get(0).getId());
		assertEquals(inventories.get(0).getProduct(), db_inventory_pojo.getProduct());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());
	}

	/* Testing checkifexists with an non-existent id. Should throw exception */
	@Test()
	public void testCheckIfExistsIdWrong() throws ApiException {
		int id = 5;
		try {
			inventory_service.checkIfExists(5);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + id);
		}
	}

	/* Testing validate */
	@Test
	public void testValidate() throws ApiException {
		InventoryPojo ip = getInventoryPojo(products.get(2));
		inventory_service.check(ip);
		assertTrue(ip.getQuantity() > 0);
	}

	/* Testing get by id */
	@Test()
	public void testGetById() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.get(inventories.get(0).getId());
		assertEquals(inventories.get(0).getProduct(), db_inventory_pojo.getProduct());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());

	}

	/* Testing get by id of a non-existent pojo. Should throw exception */
	@Test()
	public void testGetByIdNotExisting() throws ApiException {

		int id = 5;
		try {
			inventory_service.get(5);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + id);
		}

	}

	/* Testing get by product id */
	@Test()
	public void testGetByProductId() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.getByProductId(inventories.get(0).getProduct().getId());
		assertEquals(inventories.get(0).getProduct(), db_inventory_pojo.getProduct());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());

	}

	

	private InventoryPojo getInventoryPojo(ProductPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProduct(p);
		i.setQuantity(20);
		return i;
	}

	private InventoryPojo getNewInventoryPojo(ProductPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProduct(p);
		i.setQuantity(30);
		return i;
	}

	private InventoryPojo getWrongInventoryPojo(ProductPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProduct(p);
		i.setQuantity(-5);
		return i;
	}
}

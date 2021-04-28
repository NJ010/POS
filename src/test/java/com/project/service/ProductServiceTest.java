package com.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.ProductPojo;
import com.project.spring.AbstractUnit;

public class ProductServiceTest extends AbstractUnit{

	@Before
	public void Initialize() throws ApiException {
		insertObjects();
	}
	
	/* Testing adding of product details pojo */
	@Test
	public void testAdd() throws ApiException {

		BrandCategoryPojo b = brands.get(0);
		ProductPojo p = getProductDetailsPojo(b);
		List<ProductPojo> product_list_before = product_service.getAll();
		product_service.add(p);
		List<ProductPojo> product_list_after = product_service.getAll();
		assertEquals(product_list_before.size() + 1, product_list_after.size());
		assertEquals(p.getBarcode(), product_service.get(p.getId()).getBarcode());
		assertEquals(p.getName(), product_service.get(p.getId()).getName());
		assertEquals(p.getMrp(), product_service.get(p.getId()).getMrp(), 0.001);
		assertEquals(p.getBrandCategory(), product_service.get(p.getId()).getBrandCategory());

	}

	/* Testing adding of an invalid pojo. Should throw an exception */
	@Test()
	public void testAddWrong() throws ApiException {

		BrandCategoryPojo b = brands.get(0);
		ProductPojo p = getWrongProductDetailsPojo(b);
		try {
			product_service.add(p);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "The name of product must not be empty");
		}

	}

	

	/* Testing deletion of product details pojo */
	@Test()
	public void testDelete() throws ApiException {

		BrandCategoryPojo b = brands.get(0);
		ProductPojo p = getProductDetailsPojo(b);
		product_service.add(p);

		List<ProductPojo> product_list_before = product_service.getAll();
		product_service.delete(p.getId());
		List<ProductPojo> product_list_after = product_service.getAll();
		assertEquals(product_list_before.size() - 1, product_list_after.size());
		try {
			product_service.get(p.getId());
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "ProductDetails with given ID does not exist, id: " + p.getId());
		}

	}

	/* Testing get by id */
	@Test()
	public void testGetById() throws ApiException {

		ProductPojo db_product_pojo = product_service.get(products.get(0).getId());
		assertEquals(products.get(0).getBarcode(), db_product_pojo.getBarcode());
		assertEquals(products.get(0).getBrandCategory(), db_product_pojo.getBrandCategory());
		assertEquals(products.get(0).getMrp(), db_product_pojo.getMrp(), 0.001);
		assertEquals(products.get(0).getName(), db_product_pojo.getName());

	}

	/* Testing get by id for a non-existent pojo. Should throw an exception */
	@Test()
	public void testGetByIdNotExisting() throws ApiException {
		try {
			product_service.get(100);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "ProductDetails with given ID does not exist, id: " + 100);
		}

	}

	/* Testing get by barcode for a productdetails pojo */
	@Test()
	public void testGetByBarcode() throws ApiException {

		ProductPojo db_product_pojo = product_service.get(products.get(0).getBarcode());
		assertEquals(products.get(0).getId(), db_product_pojo.getId());
		assertEquals(products.get(0).getBrandCategory(), db_product_pojo.getBrandCategory());
		assertEquals(products.get(0).getMrp(), db_product_pojo.getMrp(), 0.001);
		assertEquals(products.get(0).getName(), db_product_pojo.getName());

	}

	/* Testing get by barcode for a non-existent productdetails pojo */
	@Test()
	public void testGetByBarcodeNotExisting() throws ApiException {

		try {
			product_service.get("abcdefgh");
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "ProductDetails with given barcode does not exist, barcode: " + "abcdefgh");
		}

	}

	private ProductPojo getProductDetailsPojo(BrandCategoryPojo b) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBrandCategory(b);
		p.setName("Milk");
		p.setMrp(50);
		return p;
	}

	private ProductPojo getNewProductDetailsPojo(BrandCategoryPojo b) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBrandCategory(b);
		p.setName("Milk2");
		p.setMrp(70);
		return p;
	}

	private ProductPojo getWrongProductDetailsPojo(BrandCategoryPojo b) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBrandCategory(b);
		p.setName("");
		p.setMrp(-5);
		return p;
	}
}

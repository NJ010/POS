package com.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.project.pojo.BrandCategoryPojo;
import com.project.spring.AbstractUnit;

public class BrandServiceTest extends AbstractUnit{
	
	@Before
	public void Initialize() throws ApiException {
		insertObjects();
	}

	
	//Test for add(), get by id and getAll() in service layer
	@Test
	public void testAdd() throws ApiException {

		BrandCategoryPojo p = getBrandPojo();
		List<BrandCategoryPojo> brand_list_before = brand_service.getAll();
		brand_service.add(p);
		List<BrandCategoryPojo> brand_list_after = brand_service.getAll();
		BrandCategoryPojo pojo=brand_service.get(p.getId());
		// Number of brands should increase
		assertEquals(brand_list_before.size() + 1, brand_list_after.size());
		assertEquals(p.getBrand(), pojo.getBrand());
		assertEquals(p.getCategory(), pojo.getCategory());
	}
	
	@Test
	public void testQueries() throws ApiException{
		BrandCategoryPojo p = getBrandPojo();
		brand_service.add(p);
		BrandCategoryPojo pojo=brand_service.getBrandPojo(p.getBrand(), p.getCategory());
		
		assertEquals(p.getBrand(), pojo.getBrand());
		assertEquals(p.getCategory(), pojo.getCategory());
	}
	
	
	@Test
	public void testNormalize() {
		BrandCategoryPojo pojo= getNewBrandPojo();
		String s1="parle2";
		String s2="biscuits2";
		brand_service.normalize(pojo);
		
		assertEquals(s1,pojo.getBrand());
		assertEquals(s2,pojo.getCategory());
	}
	
	@Test()
	public void testAddDuplicate() throws ApiException {

		BrandCategoryPojo p = getBrandPojo();
		brand_service.add(p);

		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand and Category already exist!!");
		}

	}

	/* Testing adding of invalid brand pojo. Exception should be thrown */
	@Test()
	public void testAddWrong() throws ApiException {

		BrandCategoryPojo p = getBrandPojo();
		p.setBrand("");
		p.setCategory("");
		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand field Should be filled");
		}

	}
	
	
	@Test
	public void testCheck() throws ApiException {
		BrandCategoryPojo p = getBrandPojo();

		brand_service.check(p);
		assertTrue(!p.getBrand().isEmpty());
		assertTrue(!p.getCategory().isEmpty());
	}

	private BrandCategoryPojo getBrandPojo() {
		BrandCategoryPojo p = new BrandCategoryPojo();
		p.setBrand("Parle");
		p.setCategory("Biscuits");
		return p;
	}

	private BrandCategoryPojo getNewBrandPojo() {
		BrandCategoryPojo p = new BrandCategoryPojo();
		p.setBrand("Par  le  2");
		p.setCategory("BiS c U it s2");
		return p;
	}

	
	
	
}

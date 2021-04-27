package com.project.utilities;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.project.dao.ProductDao;
import com.project.model.BrandData;
import com.project.model.BrandForm;
import com.project.model.InventoryForm;
import com.project.model.InventoryXmlList;
import com.project.model.OrderItemForm;
import com.project.model.ProductData;
import com.project.model.ProductForm;
import com.project.model.SaleXmlList;
import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.InventoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.ProductPojo;
import com.project.service.ApiException;

public class DataConversionTest extends TestObjects{
	
	

	/* Testing conversion of brand form to pojo */
	@Test
	public void testConvertBrandFormToPojo() {

		BrandForm form = new BrandForm();
		form.setBrand("brand1");
		form.setCategory("category1");
		BrandCategoryPojo brand_pojo = DataConversionUtil.convert(form);
		assertEquals(form.getBrand(), brand_pojo.getBrand());
		assertEquals(form.getCategory(), brand_pojo.getCategory());
	}

	/* Testing conversion of brand pojo to data */
	@Test
	public void testConvertBrandPojoToData() {

		BrandCategoryPojo pojo = new BrandCategoryPojo();
		pojo.setId(1);
		pojo.setBrand("brand1");
		pojo.setCategory("category1");
		BrandData brand_data = DataConversionUtil.convert(pojo);
		assertEquals(pojo.getBrand(), brand_data.getBrand());
		assertEquals(pojo.getCategory(), brand_data.getCategory());
	}

	/* Testing conversion of product form to pojo */
	@Test
	public void testConvertProductFormToPojo() throws ApiException {

		ProductForm form = new ProductForm();
		form.setBrand("brand");
		form.setCategory("category0");
		form.setMrp(50);
		form.setName("milk");
		ProductPojo product_pojo = DataConversionUtil.convert(brand_obj, form);
		assertEquals(form.getBrand(), product_pojo.getBrandCategory().getBrand());
		assertEquals(form.getCategory(), product_pojo.getBrandCategory().getCategory());
		assertEquals(form.getName(), product_pojo.getName());
		assertEquals(form.getMrp(), product_pojo.getMrp(), 0.001);
	}

	/* Testing conversion of product pojo to data */
	@Test
	public void testConvertProductPojoToData() throws ApiException {

		ProductPojo product_pojo = new ProductPojo();
		product_pojo.setBarcode("12345678");
		product_pojo.setBrandCategory(brand_obj);
		product_pojo.setMrp(50);
		product_pojo.setName("product1");
		ProductData product_data = DataConversionUtil.convert(product_pojo);
		assertEquals(product_data.getBarcode(), product_pojo.getBarcode());
		assertEquals(product_data.getBrand(), product_pojo.getBrandCategory().getBrand());
		assertEquals(product_data.getCategory(), product_pojo.getBrandCategory().getCategory());
		assertEquals(product_data.getName(), product_pojo.getName());
		assertEquals(product_data.getMrp(), product_pojo.getMrp(), 0.001);
	}

	/* Testing conversion of inventory form to pojo */
	@Test
	public void testConvertInventoryFormToPojo() throws ApiException {

		InventoryForm form = new InventoryForm();
		form.setBarcode(product_obj.getBarcode());
		form.setQuantity(20);
		InventoryPojo inventory_pojo = DataConversionUtil.convert(form, product_obj);
		assertEquals(form.getQuantity(), inventory_pojo.getQuantity());
	}

	

	/* Testing conversion of orderitem form to pojo */
	@Test
	public void testConvertOrderItemFormToPojo() throws ApiException {

		OrderItemForm form = new OrderItemForm();
		form.setBarcode(product_obj.getBarcode());
		form.setQuantity(2);
		OrderItemPojo pojo = DataConversionUtil.convert(product_obj, form);
		assertEquals(form.getBarcode(), pojo.getProduct().getBarcode());
		assertEquals(form.getQuantity(), pojo.getQuantity());
	}

	

	

	/* Test conversion to inventory report list */
	@Test
	public void testConvertInventoryReportList() throws ApiException {
		Map<BrandCategoryPojo, Integer> quantityPerBrandPojo = new HashMap<BrandCategoryPojo, Integer>();
		BrandCategoryPojo b1 = new BrandCategoryPojo();
		b1.setBrand("brand1");
		b1.setCategory("category1");
		BrandCategoryPojo b2 = new BrandCategoryPojo();
		b2.setBrand("brand2");
		b2.setCategory("category2");
		quantityPerBrandPojo.put(b1, 1);
		quantityPerBrandPojo.put(b2, 2);
		InventoryXmlList inv_list = DataConversionUtil.convertInventoryReportList(quantityPerBrandPojo);
		assertEquals(2, inv_list.getInventory_list().size());
	}

	/* Test conversion to sales list */
	@Test
	public void testConvertSalesList() {
		Map<BrandCategoryPojo, Integer> quantityPerBrandPojo = new HashMap<BrandCategoryPojo, Integer>();
		Map<BrandCategoryPojo, Double> revenuePerBrandCategory = new HashMap<BrandCategoryPojo, Double>();
		BrandCategoryPojo b1 = new BrandCategoryPojo();
		b1.setBrand("brand1");
		b1.setCategory("category1");
		BrandCategoryPojo b2 = new BrandCategoryPojo();
		b2.setBrand("brand2");
		b2.setCategory("category2");
		quantityPerBrandPojo.put(b1, 1);
		quantityPerBrandPojo.put(b2, 2);
		revenuePerBrandCategory.put(b1, 100.00);
		revenuePerBrandCategory.put(b2, 200.00);
		SaleXmlList sales_list = DataConversionUtil.convertSalesList(quantityPerBrandPojo, revenuePerBrandCategory);
		assertEquals(2, sales_list.getSales_list().size());
	}
}

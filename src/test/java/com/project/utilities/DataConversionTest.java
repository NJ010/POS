package com.project.utilities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.project.model.BrandData;
import com.project.model.BrandForm;
import com.project.model.InventoryData;
import com.project.model.InventoryForm;
import com.project.model.InventoryXmlList;
import com.project.model.OrderData;
import com.project.model.OrderInvoiceXmlList;
import com.project.model.OrderItemData;
import com.project.model.OrderItemForm;
import com.project.model.ProductData;
import com.project.model.ProductForm;
import com.project.model.SaleXmlList;
import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.InventoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;
import com.project.pojo.ProductPojo;
import com.project.service.ApiException;
import com.project.spring.AbstractUnit;
import com.project.spring.TestObjects;


public class DataConversionTest extends AbstractUnit {
	
	@Before
	public void Initialize() throws ApiException {
		insertObjects();
	}

	/* Testing conversion of brand form to pojo */
	@Test
	public void testConvertBrandFormToPojo() {

		BrandForm form = new BrandForm();
		form.setBrand("amul");
		form.setCategory("milk");
		BrandCategoryPojo brand_pojo = DataConversionUtil.convert(form);
		assertEquals(form.getBrand(), brand_pojo.getBrand());
		assertEquals(form.getCategory(), brand_pojo.getCategory());
	}

	/* Testing conversion of brand pojo to data */
	@Test
	public void testConvertBrandPojoToData() {

		BrandCategoryPojo pojo = new BrandCategoryPojo();
		pojo.setId(1);
		pojo.setBrand("amul");
		pojo.setCategory("dairy");
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
		ProductPojo product_pojo = DataConversionUtil.convert(brands.get(0), form);
		assertEquals(form.getBrand(), product_pojo.getBrandCategory().getBrand());
		assertEquals(form.getCategory(), product_pojo.getBrandCategory().getCategory());
		assertEquals(form.getName(), product_pojo.getName());
		assertEquals(form.getMrp(), product_pojo.getMrp(), 0.001);
	}

	/* Testing conversion of product pojo to data */
	@Test
	public void testConvertProductPojoToData() throws ApiException {

		ProductPojo product_pojo = new ProductPojo();
		product_pojo.setBarcode("abcdefgh");
		product_pojo.setBrandCategory(brands.get(0));
		product_pojo.setMrp(50);
		product_pojo.setName("milk");
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
		form.setBarcode(products.get(0).getBarcode());
		form.setQuantity(20);
		InventoryPojo inventory_pojo = DataConversionUtil.convert(form, products.get(0));
		assertEquals(form.getQuantity(), inventory_pojo.getQuantity());
	}

	/* Testing conversion of inventory pojo to data */
	@Test
	public void testConvertInventoryPojoToData() throws ApiException {

		InventoryPojo pojo = new InventoryPojo();
		pojo.setProduct(product_service.get(products.get(0).getBarcode()));
		pojo.setQuantity(20);
		InventoryData inventory_data = DataConversionUtil.convert(pojo);
		assertEquals(inventory_data.getBarcode(), pojo.getProduct().getBarcode());
		assertEquals(inventory_data.getQuantity(), pojo.getQuantity());
	}

	/* Testing conversion of orderitem form to pojo */
	@Test
	public void testConvertOrderItemFormToPojo() throws ApiException {

		OrderItemForm form = new OrderItemForm();
		form.setBarcode(products.get(0).getBarcode());
		form.setQuantity(2);
		OrderItemPojo pojo = DataConversionUtil.convert(products.get(0), form);
		assertEquals(form.getBarcode(), pojo.getProduct().getBarcode());
		assertEquals(form.getQuantity(), pojo.getQuantity());
	}

	/* Testing conversion of orderitem pojo to data */
	@Test
	public void testConvertOrderItemPojoToData() throws ApiException {

		OrderItemPojo pojo = new OrderItemPojo();
		pojo.setProduct(products.get(0));
		pojo.setOrder(order_service.getOrder(order_id));
		pojo.setQuantity(2);
		pojo.setSellingPrice(products.get(0).getMrp());
		OrderItemData data = DataConversionUtil.convert(pojo);
		assertEquals(data.getBarcode(), pojo.getProduct().getBarcode());
		assertEquals(data.getOrderId(), pojo.getOrder().getId());
		assertEquals(data.getQuantity(), pojo.getQuantity());
	}

	/* Testing conversion of list of brand pojos to data */
	@Test
	public void testListBrandPojoToData() {
		List<BrandCategoryPojo> brand_list = brand_service.getAll();
		List<BrandData> brand_data_list = DataConversionUtil.convert(brand_list);
		assertEquals(brand_list.size(), brand_data_list.size());
		assertEquals(brand_list.get(0).getBrand(), brand_data_list.get(0).getBrand());
		assertEquals(brand_list.get(0).getCategory(), brand_data_list.get(0).getCategory());
	}

	/* Testing conversion of list of product pojos to data */
	@Test
	public void testListProductPojoToData() {
		List<ProductPojo> product_list = product_service.getAll();
		List<ProductData> product_data_list = DataConversionUtil.convertProductList(product_list);
		assertEquals(product_list.size(), product_data_list.size());
		assertEquals(product_data_list.get(0).getBarcode(), product_list.get(0).getBarcode());
		assertEquals(product_data_list.get(0).getBrand(), product_list.get(0).getBrandCategory().getBrand());
		assertEquals(product_data_list.get(0).getCategory(), product_list.get(0).getBrandCategory().getCategory());
		assertEquals(product_data_list.get(0).getName(), product_list.get(0).getName());
		assertEquals(product_data_list.get(0).getMrp(), product_list.get(0).getMrp(), 0.001);
	}

	/* Testing conversion of list of inventory pojos to data */
	@Test
	public void testListInventoryPojoToData() {
		List<InventoryPojo> inventory_list = inventory_service.getAll();
		List<InventoryData> inventory_data_list = DataConversionUtil.convertInventoryList(inventory_list);
		assertEquals(inventory_data_list.size(), inventory_list.size());
		assertEquals(inventory_data_list.get(0).getBarcode(), inventory_list.get(0).getProduct().getBarcode());
		assertEquals(inventory_data_list.get(0).getQuantity(), inventory_list.get(0).getQuantity());
	}

	/* Testing conversion of list of order items to invoice */
	@Test
	public void testOrderItemstoInvoice() {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		OrderInvoiceXmlList invoice_list = DataConversionUtil.convertToInvoiceDataList(order_item_list);
		assertEquals(order_item_list.size(), invoice_list.getInvoiceLis().size());
		assertEquals(invoice_list.getInvoiceLis().get(0).getName(), order_item_list.get(0).getProduct().getName());
		assertEquals(invoice_list.getInvoiceLis().get(0).getQuantity(), order_item_list.get(0).getQuantity());
		assertEquals(invoice_list.getInvoiceLis().get(0).getMrp(), order_item_list.get(0).getSellingPrice(), 0.001);
	}

	/* Testing conversion of list of order item pojos to data */
	@Test
	public void testOrderItemsPojotoData() {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<OrderItemData> data_list = DataConversionUtil.convertOrderItemList(order_item_list);
		assertEquals(order_item_list.size(), data_list.size());
		assertEquals(data_list.get(0).getBarcode(), order_item_list.get(0).getProduct().getBarcode());
		assertEquals(data_list.get(0).getOrderId(), order_item_list.get(0).getOrder().getId());
		assertEquals(data_list.get(0).getQuantity(), order_item_list.get(0).getQuantity());
	}

	/* Testing conversion of list of orderitem forms to pojos */
	@Test
	public void testOrderItemsFormtoPojo() throws ApiException {
		OrderItemForm[] order_item_forms = new OrderItemForm[1];
		OrderItemForm form1 = new OrderItemForm();
		form1.setBarcode(products.get(0).getBarcode());
		form1.setQuantity(2);
		order_item_forms[0] = form1;
		Map<String, ProductPojo> barcode_product = new HashMap<String, ProductPojo>();
		barcode_product.put(products.get(0).getBarcode(), products.get(0));
		barcode_product.put(products.get(1).getBarcode(), products.get(1));

		List<OrderItemPojo> pojo_list = DataConversionUtil.convertOrderItemForms(barcode_product, order_item_forms);
		assertEquals(1, pojo_list.size());
		assertEquals(order_item_forms[0].getBarcode(), pojo_list.get(0).getProduct().getBarcode());
		assertEquals(order_item_forms[0].getQuantity(), pojo_list.get(0).getQuantity());
	}

	/* Testing conversion of order pojo to data */
	@Test
	public void testConvertOrderPojoToData() throws ApiException {
		OrderPojo pojo = order_service.getOrder(order_id);
		OrderData order_data = DataConversionUtil.convertOrderPojo(pojo);
		assertEquals(pojo.getId(), order_data.getId());
	}

	/* Testing conversion of list of order pojos to data */
	@Test
	public void testListOrderPojoToData() throws ApiException {
		OrderPojo pojo = order_service.getOrder(order_id);
		List<OrderPojo> order_list = new ArrayList<OrderPojo>();
		order_list.add(pojo);
		List<OrderData> order_data_list = DataConversionUtil.convertOrderList(order_list);
		assertEquals(order_list.size(), order_data_list.size());
		assertEquals(order_list.get(0).getId(), order_data_list.get(0).getId());
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

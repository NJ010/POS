package com.project.spring;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.InventoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;
import com.project.pojo.ProductPojo;
import com.project.service.ApiException;
import com.project.service.BrandService;
import com.project.service.InventoryService;
import com.project.service.OrderService;
import com.project.service.ProductService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnit {
	

	@Autowired
	protected BrandService brand_service;

	@Autowired
	protected ProductService product_service;

	@Autowired
	protected InventoryService inventory_service;
	
	@Autowired
	protected OrderService order_service;
	
	protected int order_id;
	
	protected List<String> barcodes;
	
	protected List<BrandCategoryPojo> brands;
	protected List<ProductPojo> products;
	protected List<InventoryPojo> inventories;
	protected List<OrderPojo> orders;
	protected List<OrderItemPojo> orderitems;
	
	protected void insertObjects() throws ApiException {
		barcodes = new ArrayList<String>();
		brands = new ArrayList<BrandCategoryPojo>();
		products = new ArrayList<ProductPojo>();
		inventories = new ArrayList<InventoryPojo>();
		orders = new ArrayList<OrderPojo>();
		orderitems = new ArrayList<OrderItemPojo>();
		
		for(int i=0; i<2; i++) {
			BrandCategoryPojo brand = new BrandCategoryPojo();
			brand.setBrand("brand");
			brand.setCategory("category"+i);
			brand_service.add(brand);
			brands.add(brand);
			
			ProductPojo product = new ProductPojo();
			product.setBrandCategory(brand);
			product.setName("product"+i);
			product.setMrp(50);
			product_service.add(product);
			products.add(product);
			barcodes.add(product.getBarcode());
			
			InventoryPojo inventory = new InventoryPojo();
			inventory.setProduct(product);
			inventory.setQuantity(20);
			inventory_service.add(inventory);
			inventories.add(inventory);
		}
		
		//Product without inventory
		ProductPojo product = new ProductPojo();
		product.setBrandCategory(brands.get(0));
		product.setName("product3");
		product.setMrp(50);
		product_service.add(product);
		products.add(product);
		
		List<OrderItemPojo> order_item_list = new ArrayList<OrderItemPojo>();
		for(int i=0; i<2; i++) {
			OrderItemPojo order_item = new OrderItemPojo();
			order_item.setProduct(product_service.get(barcodes.get(i)));
			order_item.setQuantity(2);
			order_item.setSellingPrice(product_service.get(barcodes.get(i)).getMrp());
			order_item_list.add(order_item);
		}
		order_id = order_service.add(order_item_list);
		orders.add(order_service.getOrder(order_id));
		orderitems.addAll(order_item_list);
	}
	
}

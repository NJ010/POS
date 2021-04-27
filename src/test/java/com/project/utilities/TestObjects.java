package com.project.utilities;

import java.time.LocalDateTime;

import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.InventoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;
import com.project.pojo.ProductPojo;



public class TestObjects {

	public BrandCategoryPojo brand_obj;
	public InventoryPojo inventory_obj;
	public ProductPojo product_obj;
	public OrderPojo order_obj;
	public OrderItemPojo order_item_obj;
	 
	 public void CreateObjects() {
		 
		 InventoryPojo inventory= new InventoryPojo();
		 ProductPojo product= new ProductPojo();
		 OrderPojo order= new OrderPojo();
		 OrderItemPojo order_item= new OrderItemPojo();
		 BrandCategoryPojo brand= new BrandCategoryPojo();
		
		
		 brand.setBrand("brand1");
		 brand.setCategory("category1");
		 brand.setId(1);
		 brand_obj=brand;
		 
		 product.setBarcode("12345678");
		 product.setBrandCategory(brand);
		 product.setId(1);
		 product.setMrp(1);
		 product.setName("product1");
		 product_obj=product;
	
		 inventory.setProduct(product);
		 inventory.setId(1);
		 inventory.setQuantity(100000);
		 inventory_obj=inventory;
		 
		 order.setDate_time(LocalDateTime.now());
		 order.setId(1);
		 order_obj=order;
		 
		 order_item.setId(1);
		 order_item.setOrder(order);
		 order_item.setProduct(product);
		 order_item.setQuantity(10);
		 order_item.setSellingPrice(product.getMrp());
		 order_item_obj=order_item;
		 
		 
	 }
	 
	
	
	
}

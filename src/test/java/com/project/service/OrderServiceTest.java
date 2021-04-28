package com.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;
import com.project.pojo.ProductPojo;
import com.project.spring.AbstractUnit;

public class OrderServiceTest extends AbstractUnit{

	@Before
	public void Initialize() throws ApiException {
		insertObjects();
	}
	
	@Test
	public void testAdd() throws ApiException {

		OrderItemPojo order_item = getOrderItemPojo(products.get(0), 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		List<OrderPojo> order_list_before = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_before = order_service.getAll();
		order_service.add(lis);
		List<OrderPojo> order_list_after = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_after = order_service.getAll();

		assertEquals(order_list_before.size() + 1, order_list_after.size());
		assertEquals(orderitem_list_before.size() + 1, orderitem_list_after.size());
		List<OrderItemPojo> db_orderitem_list = order_service.getOrderItems(order_item.getOrder().getId());
		assertEquals(lis.size(), db_orderitem_list.size());
		assertEquals(order_item.getOrder(), db_orderitem_list.get(0).getOrder());
		assertEquals(order_item.getProduct(), db_orderitem_list.get(0).getProduct());
		assertEquals(order_item.getQuantity(), db_orderitem_list.get(0).getQuantity());
		assertEquals(order_item.getSellingPrice(), db_orderitem_list.get(0).getSellingPrice(), 0.001);

	}

	/* Testing adding of invalid order. Exception should be thrown */
	@Test
	public void testAddWrong() throws ApiException {
		
		


		OrderItemPojo order_item = getWrongOrderItemPojo(products.get(0));
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);

		try {
			order_service.add(lis);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Quantity must be positive");
		}

	}
	/* Testing updation of order items */
	@Test
	public void testUpdate() throws ApiException {

		OrderItemPojo new_order_item = getOrderItemPojo(products.get(0), 7);
		order_service.update(orderitems.get(0).getId(), new_order_item);
		assertEquals(orderitems.get(0).getProduct(), new_order_item.getProduct());
		assertEquals(orderitems.get(0).getQuantity(), new_order_item.getQuantity());
		assertEquals(orderitems.get(0).getSellingPrice(), new_order_item.getSellingPrice(), 0.001);
	}
	
	/* Testing adding of invalid order (with invalid product). Exception should be thrown */
	@Test
	public void testAddNullProduct() throws ApiException {

		OrderItemPojo order_item = getOrderItemPojo(null,5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);

		try {
			order_service.add(lis);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Product with this id does not exist");
		}

	}

	/* Testing Get for order items */
	@Test
	public void testGet() throws ApiException {

		OrderItemPojo db_orderitem_pojo = order_service.get(orderitems.get(0).getId());
		assertEquals(orderitems.get(0).getOrder(), db_orderitem_pojo.getOrder());
		assertEquals(orderitems.get(0).getProduct(), db_orderitem_pojo.getProduct());
		assertEquals(orderitems.get(0).getQuantity(), db_orderitem_pojo.getQuantity());
		assertEquals(orderitems.get(0).getSellingPrice(), db_orderitem_pojo.getSellingPrice(), 0.001);
	}

	
	private OrderItemPojo getOrderItemPojo(ProductPojo p, int quantity) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProduct(p);
		order_item.setQuantity(quantity);
		if(p!=null) {
			order_item.setSellingPrice(p.getMrp());
		}
		return order_item;
	}
	
	

	private OrderItemPojo getWrongOrderItemPojo(ProductPojo p) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProduct(p);
		order_item.setQuantity(-5);
		order_item.setSellingPrice(p.getMrp());
		return order_item;
	}
}

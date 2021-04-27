package com.project.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.OrderData;
import com.project.model.OrderItemData;
import com.project.model.OrderItemForm;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;
import com.project.pojo.ProductPojo;
import com.project.service.ApiException;
import com.project.service.OrderService;
import com.project.service.ProductService;
import com.project.utilities.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {

	@Autowired
	private OrderService order_service;

	@Autowired
	private ProductService product_service;

	@ApiOperation(value = "Adds Order Details")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public OrderData add(@RequestBody OrderItemForm[] forms, HttpServletResponse response)
			throws ApiException, Exception {
		Map<String, ProductPojo> barcode_product = product_service.getAllProductPojosByBarcode();
		List<OrderItemPojo> orderitem_list = DataConversionUtil.convertOrderItemForms(barcode_product, forms);
		int order_id = order_service.add(orderitem_list);
		return DataConversionUtil.convertOrderPojo(order_service.getOrder(order_id));

	}

	@ApiOperation(value = "Adds an OrderItem to an existing order")
	@RequestMapping(path = "/api/order_item/{order_id}", method = RequestMethod.POST)
	public void addOrderItem(@PathVariable int order_id, @RequestBody OrderItemForm f) throws ApiException {
		ProductPojo product_pojo = product_service.get(f.getBarcode());
		OrderItemPojo orderitem_pojo = DataConversionUtil.convert(product_pojo, f);
		order_service.addOrderItem(order_id, orderitem_pojo);
	}

	@ApiOperation(value = "Gets a OrderItem details record by id")
	@RequestMapping(path = "/api/order_item/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo orderitem_pojo = order_service.get(id);
		return DataConversionUtil.convert(orderitem_pojo);
	}

	@ApiOperation(value = "Deletes an Order by id")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void deleteOrder(@PathVariable int id) throws ApiException {
		order_service.delete(id);
	}

	@ApiOperation(value = "Gets list of Order Items")
	@RequestMapping(path = "/api/order_item", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> orderitem_pojo_list = order_service.getAll();
		return DataConversionUtil.convertOrderItemList(orderitem_pojo_list);
	}

	@ApiOperation(value = "Gets list of Orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAllOrders() {
		List<OrderPojo> orders_list = order_service.getAllOrders();
		return DataConversionUtil.convertOrderList(orders_list);
	}

	@ApiOperation(value = "Gets list of Order Items of a particular order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public List<OrderItemData> getOrderItemsbyOrderId(@PathVariable int id) throws ApiException {
		List<OrderItemPojo> orderitem_pojo_list = order_service.getOrderItems(id);
		return DataConversionUtil.convertOrderItemList(orderitem_pojo_list);
	}

	@ApiOperation(value = "Deletes Order Item record")
	@RequestMapping(path = "/api/order_item/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		order_service.delete(id);
	}

	@ApiOperation(value = "Updates a OrderItem record")
	@RequestMapping(path = "/api/order_item/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		ProductPojo product_pojo = product_service.get(f.getBarcode());
		OrderItemPojo orderitem_pojo = DataConversionUtil.convert(product_pojo, f);
		order_service.update(id, orderitem_pojo);
	}

}

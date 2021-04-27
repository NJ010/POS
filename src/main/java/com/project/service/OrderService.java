package com.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dao.OrderDao;
import com.project.dao.OrderItemDao;
import com.project.pojo.InventoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;
import com.project.pojo.ProductPojo;

@Service
public class OrderService {

	@Autowired
	private OrderItemDao order_item_dao;
	
	@Autowired
	private OrderDao order_dao;
	
	@Autowired
	private InventoryService inventory_service;
	
	//add Order Item
	@Transactional(rollbackFor = ApiException.class)
	public int add(List<OrderItemPojo> pojos) throws ApiException{
		OrderPojo new_order= new OrderPojo();
		new_order.setDate_time(LocalDateTime.now());
		int order_id=order_dao.insert(new_order);
		for (OrderItemPojo pojo : pojos) {
			pojo.setOrder(order_dao.select(order_id));
			check(pojo);
			order_item_dao.insert(pojo);
			updateInventory(pojo,0);
		}
		return order_id;
		
	}
	
	@Transactional
	public OrderItemPojo get(int id) throws ApiException {
		OrderItemPojo p = checkIfExists(id);
		return p;
	}

	

	// Fetching an Order by id 
	@Transactional
	public OrderPojo getOrder(int id) throws ApiException {
		OrderPojo p = checkIfExistsOrder(id);
		return p;
	}

	// Fetch all order items of a particular order 
	@Transactional
	public List<OrderItemPojo> getOrderItems(int order_id) throws ApiException {
		OrderPojo pojo=order_dao.select(order_id);
		List<OrderItemPojo> lis = order_item_dao.selectOrder(order_id,pojo);
		return lis;
	}

	// Fetching all order items 
	@Transactional
	public List<OrderItemPojo> getAll() {
		return order_item_dao.selectAll();
	}

	// Fetching all orders 
	@Transactional
	public List<OrderPojo> getAllOrders() {
		return order_dao.selectAll();
	}

	// Deletion of order item 
	@Transactional
	public void delete(int id) {
		int order_id = order_item_dao.select(id).getOrder().getId();
		order_item_dao.delete(id);
		OrderPojo pojo=order_dao.select(order_id);
		List<OrderItemPojo> lis = order_item_dao.selectOrder(order_id,pojo);
		if (lis.isEmpty()) {
			order_dao.delete(order_id);
		}
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, OrderItemPojo p) throws ApiException {

		check(p);
		checkIfExists(id);
		OrderItemPojo pojo=order_item_dao.select(id);
		if(!p.getProduct().getBarcode().equals(pojo.getProduct().getBarcode())) {
			throw new ApiException("You are trying to change order of different product");
		}
		int order_id=pojo.getOrder().getId();
		updateInventory(p,pojo.getQuantity());
		pojo.setQuantity(p.getQuantity());
		pojo.setOrder(pojo.getOrder());
		
		order_item_dao.delete(id);
		order_item_dao.insert(pojo);
		
		
	}
	
	/* Adding order item to an existing order */
	@Transactional(rollbackFor = ApiException.class)
	public void addOrderItem(int order_id, OrderItemPojo p) throws ApiException {
		check(p);
		OrderPojo pojo=order_dao.select(order_id);
		p.setOrder(pojo);
		List<OrderItemPojo> list=order_item_dao.selectOrder(order_id,pojo);
		for(OrderItemPojo item: list) {
			if(item.getProduct().getBarcode().equals(p.getProduct().getBarcode())){
				update(item.getId(),p);
				return;
			}
		}
		updateInventory(p,0);
		order_item_dao.insert(p);
		
	}
	
	
	//after adding or updating orders makes changes to the inventory
	@Transactional(rollbackFor = ApiException.class)
	protected void updateInventory(OrderItemPojo pojo, int old_qty) throws ApiException {
		int quantity = pojo.getQuantity();
		int quantityInInventory;
		try {
			quantityInInventory = inventory_service.getByProductId(pojo.getProduct().getId()).getQuantity() + old_qty;
		} catch (Exception e) {
			throw new ApiException("Inventory for this item does not exist " + pojo.getProduct().getBarcode());
		}

		if (quantity > quantityInInventory) {
			throw new ApiException(
					"Inventory does not contain this much quantity of product. Existing quantity in inventory: "
							+ quantityInInventory);
		}
		InventoryPojo new_ip = new InventoryPojo();
		new_ip.setQuantity(quantityInInventory - quantity);
		inventory_service.update(inventory_service.getByProductId(pojo.getProduct().getId()).getId(), new_ip);

	}
	///////HELPER FUNCTIONS //////////
	
	public void check(OrderItemPojo pojo)throws ApiException {
		if(pojo.getProduct()==null) {
			throw new ApiException("Product with this id does not exist");
		}
		if(pojo.getQuantity()<=0) {
			throw new ApiException("Item Quantity must be greater than 10000");
		}
		
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public OrderItemPojo checkIfExists(int id) throws ApiException {
		OrderItemPojo p = order_item_dao.select(id);
		if (p == null) {
			throw new ApiException("OrderItem with given ID does not exist, id: " + id);
		}
		return p;
	}

	// Checking if a particular order exists or not 
	@Transactional(rollbackFor = ApiException.class)
	public OrderPojo checkIfExistsOrder(int id) throws ApiException {
		OrderPojo p = order_dao.select(id);
		if (p == null) {
			throw new ApiException("Order with given ID does not exist, id: " + id);
		}
		return p;
	}
}

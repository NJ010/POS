package com.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;

@Repository
public class OrderItemDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from OrderItemPojo p";
	private static String select_order = "select p from OrderItemPojo p where order=:order";
	
	//Insert OrderItem
	public void insert(OrderItemPojo p) {
		em.persist(p);
	}
	
	//Delete OrderItem
	public void delete(int id) {
		OrderItemPojo p = em.find(OrderItemPojo.class, id);
		em.remove(p);
	}
	
	//Select OrderItem 
	public OrderItemPojo select(int id) {
		return em.find(OrderItemPojo.class, id);
	}
	
	//Select all OrderItems
	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(select_all);
		return query.getResultList();	
	}
	
	//Select all OrderItems 
	public List<OrderItemPojo> selectOrder(int orderId,OrderPojo pojo) {
		TypedQuery<OrderItemPojo> query = getQuery(select_order);
		query.setParameter("order", pojo);
		return query.getResultList();
	}
	
	
	
	private TypedQuery<OrderItemPojo> getQuery(String jpql) {
		return em.createQuery(jpql,OrderItemPojo.class);
	}

	

}
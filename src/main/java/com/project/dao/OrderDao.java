package com.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.project.pojo.OrderPojo;

@Repository
public class OrderDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from OrderPojo p";
	
	//Insert Order 
	public int insert(OrderPojo p) {
		em.persist(p);
		em.flush();
		return p.getId();
	}
	
	//Delete Order 
	public void delete(int id) {
		OrderPojo p = em.find(OrderPojo.class, id);
		em.remove(p);
	}
	
	//Select Order
	public OrderPojo select(int id) {
		return em.find(OrderPojo.class, id);
	}
	
	//Select All Orders 
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	
	
	private TypedQuery<OrderPojo> getQuery(String jpql) {
		return em.createQuery(jpql,OrderPojo.class);
	}


}
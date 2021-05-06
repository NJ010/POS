package com.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.project.service.ApiException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.project.pojo.InventoryPojo;
import com.project.pojo.ProductPojo;


@Repository
public class ProductDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from ProductPojo p";
	private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
	private static String select_duplicate = "select p from ProductPojo p where name =:name and mrp =:mrp and brandcategory=:pojo";


	//Insert product 
	public void insert(ProductPojo pojo) throws ConstraintViolationException,ApiException{
		List<ProductPojo> p= em.createQuery(select_duplicate)
				.setParameter("pojo",pojo.getBrandCategory())
				.setParameter("mrp", pojo.getMrp())
				.setParameter("name", pojo.getName()).getResultList();
		if(p.size()>0){
			throw new ApiException("Duplicates");

		}
		else {
			em.persist(pojo);
		}

	}
	
	//Delete Product 
	public void delete(int id) {
		ProductPojo p = em.find(ProductPojo.class, id);
		em.remove(p);
	}
	
	//Select product 
	public ProductPojo select(int id) {
		return em.find(ProductPojo.class, id);
	}
	
	//Select product by barcode
	public ProductPojo select(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(select_barcode);
		query.setParameter("barcode", barcode);
		List<ProductPojo> lis = query.getResultList();
		if(lis.size()>0) {
			ProductPojo p = lis.get(0);
			return p;
		}
		else {
			return null;
		}
	}
	
	//Select all products 
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all);
		return query.getResultList();	
	}
	
	
	public void update(int id,ProductPojo pojo) {
		String update_query ="update ProductPojo set brandcategory=:brandid , mrp=:mrp,barcode =:barcode,name=:name  where id = :id";
		em.createQuery(update_query)
		.setParameter("id", id)
		.setParameter("brandid",pojo.getBrandCategory())
		.setParameter("mrp", pojo.getMrp())
		.setParameter("barcode", pojo.getBarcode())
		.setParameter("name", pojo.getName())
		.executeUpdate();
		return;
	}
	
	
	private TypedQuery<ProductPojo> getQuery(String jpql) {
		return em.createQuery(jpql,ProductPojo.class);
	}

}
package com.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.project.pojo.BrandCategoryPojo;

@Repository
public class BrandDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from BrandCategoryPojo p";
	private static String select_brand_category = "select p from BrandCategoryPojo p where p.brand=:brand and p.category=:category";
	
	//Insert brand 
	public void insert(BrandCategoryPojo p) {
		em.persist(p);
	}
	
	//Delete brand 
	public void delete(int id) {
		BrandCategoryPojo p = em.find(BrandCategoryPojo.class, id);
		em.remove(p);
	}
	
	//Select brand 
	public BrandCategoryPojo select(int id) {
		return em.find(BrandCategoryPojo.class, id);
	}
	
	//Select All brands 
	public List<BrandCategoryPojo> selectAll() {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	//Select Brand using category and brand
	public List<BrandCategoryPojo> selectAllBrandCategory(String brand, String category) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brand_category);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		List<BrandCategoryPojo> results = query.getResultList();
		return results;
	}
	
	public void update(int id,BrandCategoryPojo pojo) {
		BrandCategoryPojo orignal_pojo=em.find(BrandCategoryPojo.class, id);
		orignal_pojo.setBrand(pojo.getBrand());
		orignal_pojo.setCategory(pojo.getCategory());
		em.merge(orignal_pojo);
		
	}
	
	
	private TypedQuery<BrandCategoryPojo> getQuery(String jpql) {
		return em.createQuery(jpql,BrandCategoryPojo.class);
	}
	
	
}
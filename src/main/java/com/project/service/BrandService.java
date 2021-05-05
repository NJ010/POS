package com.project.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.project.model.TsvError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dao.BrandDao;
import com.project.pojo.BrandCategoryPojo;

@Service
public class BrandService {

	
	@Autowired
	private BrandDao brand_dao;
	
	@Transactional
	public void add(BrandCategoryPojo pojo) throws ApiException {
		normalize(pojo);
		check(pojo);
		brand_dao.insert(pojo);
	}

	@Transactional
	public List<TsvError> addList(List<BrandCategoryPojo> list) throws ApiException {
		List<TsvError> errors= new ArrayList<TsvError>();
		for (int i = 0; i < list.size(); i++) {
			try{
			normalize(list.get(i));
			check(list.get(i));
			brand_dao.insert(list.get(i));
				TsvError error = new TsvError();
				error.setLine(i+1);
				error.setErrorMessage("Success");
			}
			catch (ApiException e){
				TsvError error = new TsvError();
				error.setLine(i+1);
				error.setErrorMessage(e.getMessage());
			}
		}
		return errors;
	}
	
	
	
	@Transactional(rollbackOn = ApiException.class)
	public BrandCategoryPojo get(int id) throws ApiException {
		BrandCategoryPojo p = checkIfExist(id);
		return p;
	}
	
	//Fetching all brands 
	@Transactional(rollbackOn = ApiException.class)
	public List<BrandCategoryPojo> getAll() {
		return brand_dao.selectAll();
	}
	
	//update brand and category
	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, BrandCategoryPojo pojo) throws ApiException {
		check(pojo);
		normalize(pojo);
		BrandCategoryPojo ex = checkIfExist(id);
		ex.setBrand(pojo.getBrand());
		ex.setCategory(pojo.getCategory());
		brand_dao.update(id,pojo);
	}
	
	/* Getting a BrandPojo with particular brand and category */
	@Transactional(rollbackOn = ApiException.class)
	public BrandCategoryPojo getBrandPojo(String brand, String category) throws ApiException {
		
		List<BrandCategoryPojo> brand_list = brand_dao.selectAllBrandCategory(brand, category);

		if (brand_list.isEmpty()) {
			throw new ApiException("The brand name and category given does not exist " + brand + " " + category);
		}
		return brand_list.get(0);
	}

	
	//// HELPER FUNCTIONS////////
	
	
	// to normalize according to requirements
	public void normalize(BrandCategoryPojo pojo) {
		
		pojo.setBrand(pojo.getBrand().toLowerCase().trim());
		pojo.setCategory(pojo.getCategory().toLowerCase().trim());
		
	}
	
	// to check if brand and category fields are not empty and also to keep them unique i.e. no duplicates.
	public void check(BrandCategoryPojo pojo) throws ApiException{
		if(pojo.getBrand().isEmpty()) {
			throw new ApiException("Brand field Should be filled");
		}
		if(pojo.getCategory().isEmpty()) {
			throw new ApiException("Category field Should be filled");
		}
		List<BrandCategoryPojo> brand_category_list= brand_dao.selectAllBrandCategory(pojo.getBrand(),pojo.getCategory());
		if(!brand_category_list.isEmpty()) {
			throw new ApiException("Brand and Category already exist!!");
		}
		
		
	}
	
	// check if the id exist in the database or not
	public BrandCategoryPojo checkIfExist(int id) throws ApiException {
		BrandCategoryPojo p=brand_dao.select(id);
		if(p==null) {
			throw new ApiException("No brand with this id exist");
		}
		
		return p;
	}
	
}

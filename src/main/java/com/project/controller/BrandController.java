package com.project.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.ApiError;
import com.project.model.BrandData;
import com.project.model.BrandForm;
import com.project.pojo.BrandCategoryPojo;
import com.project.service.ApiException;
import com.project.service.BrandService;
import com.project.utilities.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController

public class BrandController extends ExceptionalHandler {
	
	@Autowired
	private BrandService brand_service;
	
	//insert brand and category to our database
	@ApiOperation(value = "Adds Brand Details")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm userform) throws ApiException {
		BrandCategoryPojo brand_pojo = DataConversionUtil.convert(userform);
		brand_service.add(brand_pojo);
	}

	

	@ApiOperation(value = "Gets  Brand details record by id")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET) 
	public BrandData get(@PathVariable int id) throws ApiException {
		BrandCategoryPojo brand_pojo = brand_service.get(id);
		return DataConversionUtil.convert(brand_pojo);
	}

	@ApiOperation(value = "Gets list of all Brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		List<BrandCategoryPojo> brand_pojo_list = brand_service.getAll();
		return DataConversionUtil.convert(brand_pojo_list);
	}

	@ApiOperation(value = "Updates a Brand details record")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
		BrandCategoryPojo brand_pojo = DataConversionUtil.convert(f);
		brand_service.update(id, brand_pojo);
	}
	
	
	
	

}

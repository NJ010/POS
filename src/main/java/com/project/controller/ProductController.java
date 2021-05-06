package com.project.controller;

import java.util.ArrayList;
import java.util.List;

import com.project.model.TsvError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.ProductData;
import com.project.model.ProductForm;
import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.ProductPojo;
import com.project.service.ApiException;
import com.project.service.BrandService;
import com.project.service.ProductService;
import com.project.utilities.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController extends ExceptionalHandler{


	@Autowired
	private ProductService product_service;

	@Autowired
	private BrandService brand_service;

	@ApiOperation(value = "Adds ProductDetails")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm userform) throws ApiException {
		userform.setBrand(userform.getBrand().toLowerCase().trim());
		userform.setCategory(userform.getCategory().toLowerCase().trim());
		BrandCategoryPojo brand_pojo = brand_service.getBrandPojo(userform.getBrand(), userform.getCategory());
		
		ProductPojo p = DataConversionUtil.convert(brand_pojo,userform);
		product_service.add(p);
	}
	@ApiOperation(value = "Adds ProductDetails")
	@RequestMapping(path = "/api/product/list", method = RequestMethod.POST)
	public List<TsvError> add(@RequestBody List<ProductForm> list) throws ApiException {
		List<TsvError> list_errors= new ArrayList<TsvError>();
		list_errors=product_service.add(list);
		return list_errors;
	}

	@ApiOperation(value = "Deletes a ProductDetails record")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		product_service.delete(id);
	}

	@ApiOperation(value = "Gets a ProductDetails record by id")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductPojo p = product_service.get(id);
		return DataConversionUtil.convert(p);
	}

	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		List<ProductPojo> product_pojo_list = product_service.getAll();
		return DataConversionUtil.convertProductList(product_pojo_list);
	}

	@ApiOperation(value = "Updates a ProductDetails record")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm userform) throws ApiException {
		BrandCategoryPojo brand_pojo = brand_service.getBrandPojo(userform.getBrand(), userform.getCategory());
		ProductPojo p = DataConversionUtil.convert(brand_pojo,userform);
		product_service.update(id, p);
	}
}

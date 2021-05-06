package com.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.model.ProductForm;
import com.project.model.TsvError;
import com.project.pojo.BrandCategoryPojo;
import com.project.utilities.DataConversionUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dao.ProductDao;
import com.project.pojo.ProductPojo;
import com.project.utilities.BarcodeUtil;



@Service
public class ProductService {
	@Autowired
	private ProductDao product_dao;

	@Autowired
	private BrandService brand_service;

	/* Adding Product Details */
	@Transactional(rollbackFor = ApiException.class)
	public void add(ProductPojo p) throws ApiException {
		check(p);
		normalize(p);
		p.setBarcode(BarcodeUtil.randomString(8));
		product_dao.insert(p);
	}

	@Transactional()
	public List<TsvError> add(List<ProductForm> p) throws ApiException, ConstraintViolationException {
		List<TsvError> errors= new ArrayList<TsvError>();
		for(int i=0;i<p.size();i++){
		try{
			p.get(i).setBrand(p.get(i).getBrand().toLowerCase().trim());
			p.get(i).setCategory(p.get(i).getCategory().toLowerCase().trim());
			BrandCategoryPojo brand_pojo = brand_service.getBrandPojo(p.get(i).getBrand(), p.get(i).getCategory());
			ProductPojo pojo = DataConversionUtil.convert(brand_pojo,p.get(i));
			check(pojo);
			normalize(pojo);
			pojo.setBarcode(BarcodeUtil.randomString(8));
			product_dao.insert(pojo);
			TsvError error = new TsvError();
			error.setLine(i+1);
			error.setErrorMessage("Was a Success");
			errors.add(error);
		}
		catch (ApiException e){

			TsvError error = new TsvError();
			error.setLine(i+1);
			error.setErrorMessage(e.getMessage());
			errors.add(error);
			}
		}
		return errors;
	}

	/* Deletion of Product Details by id */
	@Transactional(rollbackFor = ApiException.class)
	public void delete(int id) {
		product_dao.delete(id);
	}

	/* Fetching of product details by id */
	@Transactional(rollbackFor = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		ProductPojo p = checkIfExists(id);
		return p;
	}

	/* Fetching of product details by barcode */
	@Transactional(rollbackFor = ApiException.class)
	public ProductPojo get(String barcode) throws ApiException {
		ProductPojo p = checkIfExists(barcode);
		return p;
	}

	/* Fetching all product details */
	@Transactional(rollbackFor = ApiException.class)
	public List<ProductPojo> getAll() {
		return product_dao.selectAll();
	}

	/* Getting a map of product details pojos with barcode as key */
	@Transactional(rollbackFor = ApiException.class)
	public Map<String, ProductPojo> getAllProductPojosByBarcode() {
		List<ProductPojo> product_list = getAll();
		Map<String, ProductPojo> barcode_product = new HashMap<String, ProductPojo>();
		for (ProductPojo product : product_list) {
			barcode_product.put(product.getBarcode(), product);
		}
		return barcode_product;
	}

	/* Update of product details */
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {
		check(p);
		normalize(p);
		ProductPojo ex = checkIfExists(id);
		ex.setBrandCategory(p.getBrandCategory());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		product_dao.update(id,ex);
	}

	/* Check if product exists with given id */
	@Transactional(rollbackFor = ApiException.class)
	public ProductPojo checkIfExists(int id) throws ApiException {
		ProductPojo p = product_dao.select(id);
		if (p == null) {
			throw new ApiException("ProductDetails with given ID does not exist, id: " + id);
		}
		return p;
	}

	/* Check if product exists with given barcode */
	@Transactional(rollbackFor = ApiException.class)
	public ProductPojo checkIfExists(String barcode) throws ApiException {
		ProductPojo p = product_dao.select(barcode);
		if (p == null) {
			throw new ApiException("ProductDetails with given barcode does not exist, barcode: " + barcode);
		}
		return p;
	}

	/* Normalize */
	protected void normalize(ProductPojo p) {
		p.setName(p.getName().toLowerCase().replaceAll("\\s", ""));
		p.getBrandCategory().setBrand(p.getBrandCategory().getBrand().toLowerCase().replaceAll("\\s", ""));
		p.getBrandCategory().setCategory(p.getBrandCategory().getCategory().toLowerCase().replaceAll("\\s", ""));
	}

	/* Validate */
	protected void check(ProductPojo p) throws ApiException {
		if (p.getName().isEmpty()) {
			throw new ApiException("The name of product must not be empty");
		}
		if (p.getMrp() <= 0) {
			throw new ApiException("Mrp value should be positive");
		}
	}

}
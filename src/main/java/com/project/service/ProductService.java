package com.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/* Adding Product Details */
	@Transactional
	public void add(ProductPojo p) throws ApiException {
		check(p);
		normalize(p);
		p.setBarcode(BarcodeUtil.randomString(8));
		product_dao.insert(p);
	}

	/* Deletion of Product Details by id */
	@Transactional
	public void delete(int id) {
		product_dao.delete(id);
	}

	/* Fetching of product details by id */
	@Transactional
	public ProductPojo get(int id) throws ApiException {
		ProductPojo p = checkIfExists(id);
		return p;
	}

	/* Fetching of product details by barcode */
	@Transactional
	public ProductPojo get(String barcode) throws ApiException {
		ProductPojo p = checkIfExists(barcode);
		return p;
	}

	/* Fetching all product details */
	@Transactional
	public List<ProductPojo> getAll() {
		return product_dao.selectAll();
	}

	/* Getting a map of product details pojos with barcode as key */
	@Transactional
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
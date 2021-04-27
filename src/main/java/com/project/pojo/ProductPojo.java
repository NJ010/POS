package com.project.pojo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy=false) 
@Table(indexes= {
		@Index (name ="id",columnList ="id"),
		@Index (name ="barocde",columnList = "barcode", unique=true),
		@Index (name ="Brand_Category_name_mrp",columnList = "brand_category,name,mrp", unique=true)
})


public class ProductPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String barcode;
	
	//mapped to primary key of BrandCategory pojo
	@ManyToOne(fetch = FetchType.LAZY)
	
    @JoinColumns({
        @JoinColumn(name="brand_category", referencedColumnName="id",nullable = false),
    })
    private BrandCategoryPojo brandcategory;
	private String name;
	private double mrp;
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public BrandCategoryPojo getBrandCategory() {
		return brandcategory;
	}
	public void setBrandCategory(BrandCategoryPojo brandCategory) {
		brandcategory = brandCategory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	} 
	
	

	
}

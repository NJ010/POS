package com.project.pojo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy=false) 
public class OrderItemPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="order_id", referencedColumnName="id"),
    })
    private OrderPojo order;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="product_id", referencedColumnName="id"),
    })
    private ProductPojo product;
	
	private int quantity;
	private double sellingPrice;
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public OrderPojo getOrder() {
		return order;
	}
	public void setOrder(OrderPojo order) {
		this.order = order;
	}
	public ProductPojo getProduct() {
		return product;
	}
	public void setProduct(ProductPojo product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	
	public BrandCategoryPojo getBrand() {
		return product.getBrandCategory();
	}
	
	public double getRevenue() {
		return quantity*sellingPrice;
	}
	
	
	
}

package com.project.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
public class OrderInvoiceXmlList {


	private int order_id;
	private String datetime;
	private double total;
	private List<OrderInvoiceData> invoicelist;
	
	@XmlElement(name="total")
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@XmlElement(name="order_id")
	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	
	@XmlElement(name="datetime")
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}


	@XmlElement(name="item")
	public List<OrderInvoiceData> getInvoiceLis() {
		return invoicelist;
	}

	public void setInvoiceLis(List<OrderInvoiceData> invoicelist) {
		this.invoicelist = invoicelist;
	}
	
	
}

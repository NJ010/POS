package com.project.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="sales_items")
public class SaleXmlList {

	private List<SaleReportData> sales_list;

	@XmlElement(name="sales_item")
	public List<SaleReportData> getSales_list() {
		return sales_list;
	}

	public void setSales_list(List<SaleReportData> sales_list) {
		this.sales_list = sales_list;
	}
	
}

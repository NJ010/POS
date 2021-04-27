package com.project.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.BrandData;
import com.project.model.BrandXmlList;
import com.project.model.InventoryXmlList;
import com.project.model.OrderInvoiceData;
import com.project.model.OrderInvoiceXmlList;
import com.project.model.ReportFilter;
import com.project.model.SaleXmlList;
import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.InventoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.utilities.DataConversionUtil;
import com.project.utilities.PdfConversionUtil;

@Service
public class ReportService {
	

	@Autowired
	private OrderService order_service;

	@Autowired
	private BrandService brand_service;

	@Autowired
	private InventoryService inventory_service;

	/* General class for generating Pdf Response */
	public byte[] generatePdfResponse(String type, Object... obj) throws Exception {
		if (type.contentEquals("brand")) {
			BrandXmlList brand_list = generateBrandList();
			PdfConversionUtil.generateXml(new File("brand.xml"), brand_list, BrandXmlList.class);
			return PdfConversionUtil.generatePDF(new File("brand.xml"), new StreamSource("brand.xsl"));
		} else if (type.contentEquals("inventory")) {
			InventoryXmlList inventory_list = generateInventoryList();
			PdfConversionUtil.generateXml(new File("inventory.xml"), inventory_list, InventoryXmlList.class);
			return PdfConversionUtil.generatePDF(new File("inventory.xml"), new StreamSource("inventory.xsl"));
		}
		else if (type.contentEquals("sales")) {
			
			SaleXmlList sales_data_list = generateSalesList((ReportFilter) obj[0]);
			if(sales_data_list.getSales_list().isEmpty()) {
				throw new ApiException("No sales was done in this date range for this particular brand and category pair");
			}
			PdfConversionUtil.generateXml(new File("sales.xml"), sales_data_list, SaleXmlList.class);
			return PdfConversionUtil.generatePDF(new File("sales.xml"), new StreamSource("sales.xsl"));
		}
		 else {
			 OrderInvoiceXmlList idl = generateInvoiceList((Integer) obj[0]);
			 PdfConversionUtil.generateXml(new File("invoice.xml"), idl, OrderInvoiceXmlList.class);
				return PdfConversionUtil.generatePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
			}
	}

	/* Generating brand list for brand report */
	public BrandXmlList generateBrandList() throws Exception {
		List<BrandCategoryPojo> brand_pojo_list = brand_service.getAll();
		List<BrandData> brand_data_list = DataConversionUtil.convert(brand_pojo_list);
		BrandXmlList brand_list = new BrandXmlList();
		brand_list.setBrand_list(brand_data_list);
		return brand_list;
	}

	/* Generate inventory list for inventory report */
	public InventoryXmlList generateInventoryList() throws Exception {
		List<InventoryPojo> inventory_pojo_list = inventory_service.getAll();
		Map<BrandCategoryPojo, Integer> quantityPerBrandPojo = GroupByBrandCategory(inventory_pojo_list);
		return DataConversionUtil.convertInventoryReportList(quantityPerBrandPojo);

	}

	/* Getting inventory per Brand Category */
	private static Map<BrandCategoryPojo, Integer> GroupByBrandCategory(List<InventoryPojo> inventory_pojo_list) {
		Map<BrandCategoryPojo, Integer> quantityPerBrandPojo = inventory_pojo_list.stream().collect(
				Collectors.groupingBy(InventoryPojo::getBrandPojo, Collectors.summingInt(InventoryPojo::getQuantity)));
		return quantityPerBrandPojo;
	}

	/*Generate sales list for sales report */
	public SaleXmlList generateSalesList(ReportFilter sales_filter) throws Exception {

		List<OrderItemPojo> order_list = order_service.getAll();
		List<OrderItemPojo> filtered_orderitem_list = FilterByDate(sales_filter, order_list);
		Map<BrandCategoryPojo, Integer> quantityPerBrandCategory = getMapQuantity(sales_filter, filtered_orderitem_list);
		Map<BrandCategoryPojo, Double> revenuePerBrandCategory = getMapRevenue(sales_filter, filtered_orderitem_list);
		return DataConversionUtil.convertSalesList(quantityPerBrandCategory, revenuePerBrandCategory);
	}

	/*Getting order items based on date */
	private static List<OrderItemPojo> FilterByDate(ReportFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startDate = LocalDate.parse(sales_filter.getStartDate(), formatter).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(sales_filter.getEndDate(), formatter).atStartOfDay().plusDays(1);
		List<OrderItemPojo> filtered_date_list = orderitem_list.stream()
				.filter(orderitem -> (orderitem.getOrder().getDate_time().isAfter(startDate)
						&& orderitem.getOrder().getDate_time().isBefore(endDate)) || orderitem.getOrder().getDate_time().isEqual(startDate)
						&& orderitem.getOrder().getDate_time().isEqual(endDate) )
				.collect(Collectors.toList());
		return filtered_date_list;
	}

	/* Getting quantity sold based on brand category */
	private static Map<BrandCategoryPojo, Integer> getMapQuantity(ReportFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandCategoryPojo, Integer> quantityPerBrandCategory = orderitem_list.stream()
				.filter(order_item -> Equals(order_item.getBrand().getBrand(), sales_filter.getBrand())
						&& Equals(order_item.getBrand().getCategory(), sales_filter.getCategory()))
				.collect(Collectors.groupingBy(OrderItemPojo::getBrand,
						Collectors.summingInt(OrderItemPojo::getQuantity)));
		return quantityPerBrandCategory;
	}

	/*Getting revenue generated based on brand category */
	private static Map<BrandCategoryPojo, Double> getMapRevenue(ReportFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandCategoryPojo, Double> revenuePerBrandCategory = orderitem_list.stream()
				.filter(order_item -> Equals(order_item.getBrand().getBrand(), sales_filter.getBrand())
						&& Equals(order_item.getBrand().getCategory(), sales_filter.getCategory()))
				.collect(Collectors.groupingBy(OrderItemPojo::getBrand,
						Collectors.summingDouble(OrderItemPojo::getRevenue)));;
		return revenuePerBrandCategory;
	}

	/*String equals or empty (used for filtering) */
	private static Boolean Equals(String a, String b) {
		return (a.contentEquals(b) || b.isEmpty());
	}
	public OrderInvoiceXmlList generateInvoiceList(int order_id) throws Exception {
		List<OrderItemPojo> lis = order_service.getOrderItems(order_id);
		OrderInvoiceXmlList idl = DataConversionUtil.convertToInvoiceDataList(lis);
		idl.setOrder_id(lis.get(0).getOrder().getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(lis.get(0).getOrder().getDate_time().format(formatter));
		double total = calculateTotal(idl);
		idl.setTotal(total);
		return idl;
	}

	/*Calculating total cost of order */
	private static double calculateTotal(OrderInvoiceXmlList idl) {
		double total = 0;
		for (OrderInvoiceData i : idl.getInvoiceLis()) {
			total += (i.getMrp() * i.getQuantity());
		}
		return total;
	}


}

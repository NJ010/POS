package com.project.utilities;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.project.model.BrandData;
import com.project.model.BrandForm;
import com.project.model.InventoryData;
import com.project.model.InventoryForm;
import com.project.model.InventoryReportData;
import com.project.model.InventoryXmlList;
import com.project.model.OrderData;
import com.project.model.OrderInvoiceData;
import com.project.model.OrderInvoiceXmlList;
import com.project.model.OrderItemData;
import com.project.model.OrderItemForm;
import com.project.model.ProductData;
import com.project.model.ProductForm;
import com.project.model.SaleReportData;
import com.project.model.SaleXmlList;
import com.project.pojo.BrandCategoryPojo;
import com.project.pojo.InventoryPojo;
import com.project.pojo.OrderItemPojo;
import com.project.pojo.OrderPojo;
import com.project.pojo.ProductPojo;
import com.project.service.ApiException;

public class DataConversionUtil {

	//Convert to BrandPojo
		public static BrandCategoryPojo convert(BrandForm d) {
			BrandCategoryPojo p = new BrandCategoryPojo();
			p.setBrand(d.getBrand().toLowerCase().replaceAll("\\s", ""));
			p.setCategory(d.getCategory().toLowerCase().replaceAll("\\s", ""));
			return p;
		}

		//Convert to Brand Data
		public static BrandData convert(BrandCategoryPojo p) {
			BrandData d = new BrandData();
			d.setBrand(p.getBrand().toLowerCase().replaceAll("\\s", ""));
			d.setCategory(p.getCategory().toLowerCase().replaceAll("\\s", ""));
			d.setId(p.getId());
			return d;
		}

		//Convert to Product Details Pojo
		public static ProductPojo convert(BrandCategoryPojo brand_pojo, ProductForm f) throws ApiException {
			brand_pojo.setBrand(brand_pojo.getBrand().toLowerCase().replaceAll("\\s", ""));
			brand_pojo.setCategory(brand_pojo.getCategory().toLowerCase().replaceAll("\\s", ""));
			ProductPojo p = new ProductPojo();
			p.setName(f.getName());
			p.setMrp(f.getMrp());
			p.setBrandCategory(brand_pojo);
			return p;
		}

		//Convert to Product Details Data
		public static ProductData convert(ProductPojo p) {
			ProductData d = new ProductData();
			d.setId(p.getId());
			d.setBrand(p.getBrandCategory().getBrand().toLowerCase().replaceAll("\\s", ""));
			d.setCategory(p.getBrandCategory().getCategory().toLowerCase().replaceAll("\\s", ""));
			d.setMrp(p.getMrp());
			d.setName(p.getName().toLowerCase().replaceAll("\\s", ""));
			d.setBarcode(p.getBarcode().toLowerCase().replaceAll("\\s", ""));
			return d;
		}

		//Convert to Inventory Pojo
		public static InventoryPojo convert(InventoryForm f, ProductPojo product_pojo) throws ApiException {
			InventoryPojo p = new InventoryPojo();
			p.setProduct(product_pojo);
			p.setQuantity(f.getQuantity());
			return p;
		}

		//Convert to Inventory Data
		public static InventoryData convert(InventoryPojo p) {
			InventoryData d = new InventoryData();
			d.setId(p.getId());
			d.setBarcode(p.getProduct().getBarcode());
			d.setName(p.getProduct().getName());
			d.setQuantity(p.getQuantity());
			return d;
		}

		//Convert to OrderItem Pojo
		public static OrderItemPojo convert(ProductPojo product_pojo, OrderItemForm f) throws ApiException {
			OrderItemPojo p = new OrderItemPojo();
			p.setProduct(product_pojo);
			p.setQuantity(f.getQuantity());
			if(product_pojo != null) {
				p.setSellingPrice(product_pojo.getMrp());
			}
			
			return p;
		}

		//Convert to OrderItem data
		public static OrderItemData convert(OrderItemPojo p) {
			OrderItemData d = new OrderItemData();
			d.setId(p.getId());
			d.setBarcode(p.getProduct().getBarcode());
			d.setQuantity(p.getQuantity());
			d.setOrderId(p.getOrder().getId());
			d.setName(p.getProduct().getName());
			d.setMrp(p.getSellingPrice());
			return d;
		}

		//Convert list of orderitems to invoice list
		public static OrderInvoiceXmlList convertToInvoiceDataList(List<OrderItemPojo> lis) {
			List<OrderInvoiceData> invoiceLis = new ArrayList<OrderInvoiceData>();
			for (OrderItemPojo p : lis) {
				OrderInvoiceData i = new OrderInvoiceData();
				i.setId(p.getId());
				i.setMrp(p.getProduct().getMrp());
				i.setName(p.getProduct().getName());
				i.setQuantity(p.getQuantity());
				invoiceLis.add(i);
			}
			OrderInvoiceXmlList idl = new OrderInvoiceXmlList();
			idl.setInvoiceLis(invoiceLis);
			return idl;
		}

		//Convert list of brand pojos to list of brand data
		public static List<BrandData> convert(List<BrandCategoryPojo> list) {
			List<BrandData> list2 = new ArrayList<BrandData>();
			for (BrandCategoryPojo p : list) {
				list2.add(convert(p));
			}
			return list2;
		}

		//Convert list of product details pojos to list of product details data
		public static List<ProductData> convertProductList(List<ProductPojo> list) {
			List<ProductData> list2 = new ArrayList<ProductData>();
			for (ProductPojo p : list) {
				list2.add(convert(p));
			}
			return list2;
		}

		//Convert list of inventory pojos to list of inventory data
		public static List<InventoryData> convertInventoryList(List<InventoryPojo> list) {
			List<InventoryData> list2 = new ArrayList<InventoryData>();
			for (InventoryPojo p : list) {
				list2.add(convert(p));
			}
			return list2;
		}

		//Convert list of order item pojos to list of order item data
		public static List<OrderItemData> convertOrderItemList(List<OrderItemPojo> list) {
			List<OrderItemData> list2 = new ArrayList<OrderItemData>();
			for (OrderItemPojo p : list) {
				list2.add(convert(p));
			}
			return list2;
		}

		//Convert Order Item Forms to Pojo
		public static List<OrderItemPojo> convertOrderItemForms(Map<String,ProductPojo> barcode_product,
				OrderItemForm[] forms) throws ApiException {
			List<OrderItemPojo> list2 = new ArrayList<OrderItemPojo>();
			for (OrderItemForm f : forms) {
				list2.add(convert(barcode_product.get(f.getBarcode()), f));
			}
			return list2;
		}

		//Convert Order Pojo to Order Data
		public static OrderData convertOrderPojo(OrderPojo pojo) {
			OrderData d = new OrderData();
			d.setId(pojo.getId());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			String datetime = pojo.getDate_time().format(formatter);
			d.setDatetime(datetime);
			return d;
		}
		
		//Convert List of Order Pojos to Data
		public static List<OrderData> convertOrderList(List<OrderPojo> list) {
			List<OrderData> list2 = new ArrayList<OrderData>();
			for (OrderPojo p : list) {
				list2.add(convertOrderPojo(p));
			}
			return list2;
		}

		//Convert Map of quantity per BrandPojo to inventory list
		public static InventoryXmlList convertInventoryReportList(Map<BrandCategoryPojo, Integer> quantityPerBrandPojo) {
			List<InventoryReportData> inventory_report_list = new ArrayList<InventoryReportData>();
			for (BrandCategoryPojo brand_pojo : quantityPerBrandPojo.keySet()) {
				InventoryReportData d = new InventoryReportData();
				d.setBrand(brand_pojo.getBrand());
				d.setCategory(brand_pojo.getCategory());
				d.setQuantity(quantityPerBrandPojo.get(brand_pojo));
				inventory_report_list.add(d);
			}
			InventoryXmlList inventory_list = new InventoryXmlList();
			inventory_list.setInventory_list(inventory_report_list);
			return inventory_list;
		}
		
		//Convert Maps of quantity sold and revenue per BrandPojo to sales list
		public static SaleXmlList convertSalesList(Map<BrandCategoryPojo, Integer> quantityPerBrandCategory,
				Map<BrandCategoryPojo, Double> revenuePerBrandCategory) {
			
			List<SaleReportData> sales_list = new ArrayList<SaleReportData>();
			for(BrandCategoryPojo brand: quantityPerBrandCategory.keySet()) {
				SaleReportData sales = new SaleReportData();
				sales.setBrand(brand.getBrand());
				sales.setCategory(brand.getCategory());
				sales.setQuantity(quantityPerBrandCategory.get(brand));
				sales.setRevenue(revenuePerBrandCategory.get(brand));
				sales_list.add(sales);
			}
			SaleXmlList sales_data_list = new SaleXmlList();
			sales_data_list.setSales_list(sales_list);
			return sales_data_list;

		}
}

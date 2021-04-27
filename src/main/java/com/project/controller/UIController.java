package com.project.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController extends AbstractUIController {

	// WEBSITE PAGES
	@RequestMapping(value = "/brand")
	public ModelAndView index() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/product")
	public ModelAndView login() {
		return mav("product_details.html");
	}

	@RequestMapping(value = "/inventory")
	public ModelAndView logout() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/report")
	public ModelAndView pricing() {
		return mav("reports.html");
	}

	@RequestMapping(value = "/order")
	public ModelAndView features() {
		return mav("previousorders.html");
	}
	@RequestMapping(value = "/")
	public ModelAndView home() {
		return mav("index.html");
	}

}

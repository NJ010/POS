var barcodeList = [];
var brandList = [];
var categoryList = [];
var inventoryMap = {};

var errorData = [];

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}


function getProductDetailsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/product";
}

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/inventory";
}

function createBarcodeList() {
	var url = getProductDetailsUrl();
	ajaxQuery(url,'GET','',formBarcodeList);
}

function createBrandCategoryList() {
	var url = getBrandUrl();
	ajaxQuery(url,'GET','',formBrandCategoryList);
}

function createInventoryMap() {
	var url = getInventoryUrl();
	ajaxQuery(url,'GET','',formInventoryMap);
}

function formBarcodeList(data) {
	for(var i in data){
		var e = data[i];
		barcodeList.push(e.barcode);
	}
}

function formBrandCategoryList(data) {
	for(var i in data){
		var e = data[i];
		brandList.push(e.brand);
		categoryList.push(e.category);
	}
}

function formInventoryMap(data) {
	for(var i in data){
		var e = data[i];
		inventoryMap[e.barcode] = e.quantity;
	}
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		preview: 5000,
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}

function checkHeader(file,header_list,callback) {
	var allHeadersPresent = true;
	Papa.parse(file,{
		delimiter: "\t",
		step: function(results, parser) {

        for(var i=0; i<header_list.length; i++){
					if(!results.data.includes(header_list[i])){
						allHeadersPresent = false;
						break;
					}
				}

        parser.abort();
        results=null;
        delete results;

    }, complete: function(results){

        results=null;
        delete results;
				if(allHeadersPresent) {
					readFileData(file,callback);
				}
				else{
					toastr.error("Improper File Format Try again with different file");
				}

    }
	});
}

function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click();
}

function handleAjaxError(response) {
	console.log(response);
	var response = JSON.parse(response);
	toastr.error(response.message);
}


//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    console.log(json);
    return json;
}

function ajaxQuery(url, type, data, successFunction,errorFunction) {
	$.ajax({
	   url: url,
	   type: type,
	   data: data,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		successFunction(response);
	   },
	   error: function(response){
	   		errorFunction(response.responseText);
	   }
	});
}

function ajaxQueryRecur(url, type, data, successFunction,recurFunction) {
    console.log(url);
	$.ajax({
	   url: url,
	   type: type,
	   data: data,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   console.log(response);
           for(var i=0;i<response.length;i++){
           console.log(response[i]);
               var error_obj = response[i];
               errorData.push(error_obj);
           }
           console.log(errorData);
	   },
	   error: function(response){
	          console.log(response);
			  var error_obj = JSON.parse(response);
			  var error = "For " + data;
				console.log(error_obj.message);
				error_obj.message = error + " " + error_obj.message;
				toastr.error(error_obj.message);
	   		errorData.push(error_obj);

	   }
	});
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function isInt(n) {
   return n % 1 === 0;
}

function init() {
	createBarcodeList();
	createBrandCategoryList();
	createInventoryMap();
	console.log(barcodeList);
	
}
$(document).ready(init);

function getProductDetailsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/product";
}

function getProductDetailsListUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/product/list";
}

//BUTTON ACTIONS
function addProductDetails(event){

	//Set the values to update
	var $form = $("#productdetails-form");
	var json = toJson($form);
	console.log(json);
	var check = validateProduct(json);
	if(check) {
		var url = getProductDetailsUrl();
		ajaxQuery(url,'POST',json,function(response) {
			getProductDetailsList(response);
			$('#add-productdetails-modal').modal('toggle');
		},handleAjaxError);
	}


	return false;
}

function updateProductDetails(event){

	//Get the ID
	var id = $("#productdetails-edit-form input[name=id]").val();
	var url = getProductDetailsUrl() + "/" + id;


	var $form = $("#productdetails-edit-form");
	var json = toJson($form);

	var check = validateProduct(json);
	if(check) {
		ajaxQuery(url,'PUT',json,function(response) {
			getProductDetailsList(response);
			$('#edit-productdetails-modal').modal('toggle');
		},handleAjaxError);
	}

	return false;

}


function getProductDetailsList(){
	var url = getProductDetailsUrl();
	ajaxQuery(url,'GET','',displayProductDetailsList,handleAjaxError);
}

function deleteProductDetails(id){
	var url = getProductDetailsUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getProductDetailsList,handleAjaxError);
}

//UI DISPLAY METHODS

function displayProductDetailsList(data){
	console.log('Printing ProductDetails data');
	var $tbody = $('#productdetails-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		
		var buttonHtml = ' <button style="padding: 0;border: none;background: none;" onclick="displayEditProductDetails(' + e.id + ')"><span class="material-icons" style="color:#CCCC00">edit</span></button>';
		console.log('brand');
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + parseFloat(e.mrp).toFixed(2) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
    $tbody.append(row);
	}
}


function displayEditProductDetails(id){
	var url = getProductDetailsUrl() + "/" + id;
	ajaxQuery(url,'GET','',displayProductDetails,handleAjaxError);
}

function displayProductDetails(data){
	$("#productdetails-edit-form input[name=brand]").val(data.brand);
	$("#productdetails-edit-form input[name=category]").val(data.category);
	$("#productdetails-edit-form input[name=name]").val(data.name);
	$("#productdetails-edit-form input[name=mrp]").val(data.mrp);
	$("#productdetails-edit-form input[name=id]").val(data.id);
	$('#edit-productdetails-modal').modal('toggle');
}

function displayUploadDataProductDetails(){
	resetUploadDialogProduct();
	$("#download-errors-productdetails").prop("disabled",true);
	$('#upload-productdetails-modal').modal('toggle');
}

function displayAddProductDetailsModal() {
	$('#add-productdetails-modal').modal('toggle');
}

//FILE METHODS

var fileData = [];
var rowsProcessed = 0;

function processDataProductDetails(){
	var file = $('#productdetailsFile')[0].files[0];
	checkHeader(file,["brand","category","name","mrp"],readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRowsProductDetails();
}

function uploadRowsProductDetails(){
	updateUploadDialog();
	$("#download-errors-productdetails").prop("disabled",false);

	var row = fileData;
	console.log(row);

	var json = JSON.stringify(row);

	var url = getProductDetailsListUrl();

	ajaxQueryRecur(url,'POST',json,uploadRowsProductDetails,uploadRowsProductDetails);

}

function downloadErrors(){
	writeFileData(errorData);
}



function updateFileNameProductDetails(){
	var $file = $('#productdetailsFile');
	var fileName = $file.val();
	$('#productdetailsFileName').html(fileName);
}

function resetUploadDialogProduct(){

	var $file = $('#productdetailsFile');
	$file.val('');
	$('#productdetailsFileName').html("Choose File");

	rowsProcessed = 0;
	fileData = [];
	errorData = [];
	updateUploadDialog();
}

function validateProduct(json) {
	json = JSON.parse(json);
	if(isBlank(json.brand)) {
		toastr.error("Brand field must not be empty");
		return false;
	}
	if(isBlank(json.category)) {
		toastr.error("Category field must not be empty");
		return false;
	}
	if(isBlank(json.brand)) {
		toastr.error("Brand field must not be empty");
		return false;
	}
	if(isBlank(json.name)) {
		toastr.error("Name field must not be empty");
		return false;
	}
	if(isBlank(json.mrp) || isNaN(parseFloat(json.mrp))) {
		toastr.error("Mrp field must not be empty and must be a float value");
		return false;
	}
	return true;
}

function updateUploadDialog(){
	var correct_rows = parseInt(fileData.length) - parseInt(errorData.length);
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + correct_rows);
	$('#errorCount').html("" + errorData.length);
}

function productDetailsFilter() {
	$("#productdetails-filter").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#productdetails-table-body tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
}

//INITIALIZATION CODE
function init(){
	$('#open-add-productdetails').click(displayAddProductDetailsModal);
	$('#add-productdetails').click(addProductDetails);
	$('#update-productdetails').click(updateProductDetails);
	$('#refresh-data-productdetails').click(getProductDetailsList);
	$('#upload-data-productdetails').click(displayUploadDataProductDetails);
	$('#download-errors-productdetails').click(downloadErrors);
	$('#process-data-productdetails').click(processDataProductDetails);
	$('#productdetailsFile').on('change', updateFileNameProductDetails);
}

$(document).ready(init);
$(document).ready(getProductDetailsList);
$(document).ready(productDetailsFilter);

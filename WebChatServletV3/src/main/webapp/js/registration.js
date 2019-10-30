/**
 * 
 * Implements user's registration. Supports client-server validation. Does not
 * send data until all the parts are validated by regex methods. Uses dynamic
 * css for highlighting input errors.
 * 
 * @author Sergey Vorobyev
 * @since 09/27/2019
 * @version 1.0
 * 
 */

$(document).ready(function() {
	$("#registrationForm").on("submit", function(e) {
		let error = false;

		$('.inputFields').css("border", "1px solid #e5e5e5");
		$('.error').empty();
		error = checkNick(error);
		error = checkPassword(error);
		error = checkPasswordDuplucates(error);
		error = checkFullName(error);
		error = checkPhone(error);
		error = checkEmail(error);

		if (error == false) {
			sendFormAjax();
		} else {
			e.preventDefault();
		}
		
		let csrf = Cookies.get('csrfPrevention');
		$('#registrationForm').append($('<input type="hidden" id="csrfPrev"'  
				+ 'name="csrfPrevention" value="' + csrf + '"/>'));
	});
});

function checkNick(error){
	let nick = $('#inputNick').val();	
	let patternSymbolsDigits = /^[a-zA-Zа-яА-Я][a-zA-Zа-яА-Я0-9-_\.]{1,20}$/i;
	
	if (patternSymbolsDigits.test(nick) == false || nick == '') {
		error = true;
		$('#displayErrorNick').html(
				'Invalid Nick! It should contain from 1 to 20 symbols(digits)');
		$('#displayErrorNick').slideDown('slow', 'linear');
		$('#inputNick').css("border", "1px solid red");
	}	
	return error;
}

function checkPassword(error){
	let password = $('#inputPass').val();	
	let patternPassword = /[a-zA-Zа-яА-Я1-9]/i;
	
	if (patternPassword.test(password) == false || password == '') {
		error = true;
		$('#displayErrorPass').html(
			'Invalid Password! It should contain from 1 to 20 symbols(digits)');
		$('#displayErrorPass').slideDown('slow', 'linear');
		$('#inputPass').css("border", "1px solid red");
	}
	return error;
}

function checkPasswordDuplucates(error){
	let password = $('#inputPass').val();
	let passwordCheck = $('#inputPassCheck').val();
	
	if (password != passwordCheck || passwordCheck == '') {
		error = true;
		$('#displayErrorPassCheck').html('The passwords do not match!');
		$('#displayErrorPassCheck').slideDown('slow', 'linear');
		$('#inputPassCheck').css("border", "1px solid red");
	}
	return error;
}

function checkFullName(error){
	let fullName = $('#inputFullName').val();
	let patternNoEmpty = /^\s*$/i;

	if (patternNoEmpty.test(fullName) == true) {
		error = true;
		$('#displayErrorFullName').html(
				'The full name is incorrect');
		$('#displayErrorFullName').slideDown('slow', 'linear');
		$('#inputFullName').css("border", "1px solid red");
	}	
	return error;
}

function checkPhone(error){
	let phoneNumber = $('#inputPhoneNumber').val();
	let patternPhNumber = /((8|\+7)[\- ]?)?(\(?\d{3}\)?[\- ]?)?[\d\- ]{7,10}$/i;
	
	if (patternPhNumber.test(phoneNumber) == false || phoneNumber == '') {
		error = true;
		$('#displayErrorPhoneNumber').html('The Phone is incorrect');
		$('#displayErrorPhoneNumber').slideDown('slow', 'linear');
		$('#inputPhoneNumber').css("border", "1px solid red");
	}
	return error;
}

function checkEmail(error){
	let email = $('#inputEmail').val();
	let patternEmail = /^[-\w.]+@([A-z0-9][-A-z0-9]+\.)+[A-z]{2,4}$/i;

	if (patternEmail.test(email) == false || email == '') {
		error = true;
		$('#displayErrorEmail').html('The Email is incorrect');
		$('#displayErrorEmail').slideDown('slow', 'linear');
		$('#inputEmail').css("border", "1px solid red");
	}	
	return error;
}

function sendFormAjax() {
    let csrfReg = Cookies.get('csrfPrevention');
	$('#registrationForm').append($('<input type="hidden" id="csrfPrev"' 
			+ 'name="csrfPrevention" value="' + csrfReg + '"/>'));
    var form = $('#registrationForm')[0];	
    var data = new FormData(form);
    event.preventDefault();
    
	$.ajax({
		url : 'update',
		type : 'POST',
		enctype: 'multipart/form-data',
		dataType : 'json',
		data : data,
        processData: false, 
        contentType : false,
        cache: false,
		success : function(data) {
			if (data.isNickInUse) {
				error = true;
				$('#displayErrorNick').html('The nick is already in use.'
						+ 'Please, choose another one.');
				$('#displayErrorNick').slideDown('slow', 'linear');
			} else {
				window.location.hash = '#/' + data.URLLogin;
			}
		}
	});
}
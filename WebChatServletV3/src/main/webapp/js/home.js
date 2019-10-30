/**
 * The starting point of the app. Sends the userNick and the password to the 
 * server side and handles the result. Performs the controller role such as 
 * shooting scripts according to the user's actions and shows and hides the 
 * pages. 
 * The main function is "hashTrigger" which starts functions when the hash 
 * change occurs. 
 * 
 * @author Sergey Vorobyev
 * @since 10/30/2019
 * @version 1.1
 * 
 */

let animationTime = 400;

$(document).ready(function() {
	homePage();
	hashTrigger();
	login();
});

function homePage() {
	if (window.location.hash == '') {
		window.location.hash = '#/login';
	} else {
		$(window).trigger("hashchange");
	}
}

function hashTrigger() {
	$(window).on('hashchange', function(e) {
		var url = window.location.hash;

		if (/registration/.test(url)) {
			clearInterval(messageInterval);
			clearInterval(onlineUsersInterval);			
			$('#loginWindow').fadeOut(animationTime);
			$('#chatMainPage').fadeOut(animationTime);
			$("#registrationFormBlock").delay(animationTime)
				.fadeIn(animationTime); 
		} else if (/login/.test(url)) {
			clearInterval(messageInterval);
			clearInterval(onlineUsersInterval);		
			$('#chatMainPage').fadeOut(animationTime);
			$("#registrationFormBlock").fadeOut(animationTime);
			$('#loginWindow').delay(animationTime).fadeIn(animationTime); 
		} else if (/chat/.test(url)) {
			let sessionExists = checkSession();		
			if (sessionExists == false) {
				window.location.hash = '#/login';
			} else {			
				$("#registrationFormBlock").fadeOut(animationTime);
				$('#loginWindow').fadeOut(animationTime);
				$('#chatMainPage').delay(animationTime).fadeIn(animationTime);
				$('#currentUserNick').empty();			
				userDataHoverMenu(); //hover menu with user data
				ajaxLogout();// performs logout action
				ajaxSendMessage();//performs message sending				
				getCurrentUserNick();				
				ajaxOnlineUsers();//receives all the online users and shows them
				ajaxLastMessages();//receives the last messages from the page 				
				messageInterval = setInterval(function() {
					ajaxLastMessages();
				}, intervalMessagesCall);
				onlineUsersInterval = setInterval(function() {
					ajaxOnlineUsers();
				}, intervalOnlineUsersCall);				   
				setTimeout(function(){
					scrollBottom();
						},500);
			}			
		}
	}).trigger('hashchange');
}

function login() {
	$('#userLoginForm').submit(function() {		
	let csrf = Cookies.get('csrfPrevention');
	$('#userLoginForm').append($('<input type="hidden" id="csrfPrev"' 
			+ ' name="csrfPrevention" value="' + csrf + '"/>'));
		$.ajax({
			url : 'update',
			type : 'POST',
			dataType : 'json',
			data : $('#userLoginForm').serialize(),
			success : function(data) {
				if (data.isValid == false) {
					$('#displayError').html('Invalid password or login');
					$('#displayError').slideDown('slow', 'linear');
					$('#displayError').delay(5000).slideUp();
				} else {
					window.location.hash = '#/' + data.URLLogin;
				}
			}
		});
		return false;
	});
}
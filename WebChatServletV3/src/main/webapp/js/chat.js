/**
 * Implements chat page methods. Using AJAX and timers. Refreshes online users
 * and messages list.
 * 
 * @author Sergey Vorobyev
 * @since 09/27/2019
 * @version 1.0
 * 
 */

let intervalMessagesCall = 15000;
let intervalOnlineUsersCall = 15000;
let messageInterval;
let onlineUsersInterval;

function checkSession() {
	let serverResponse = null;
	$.ajax({
		type : 'POST',
		url : 'update',
		dataType : 'json',
		async : false,
		data : {
			csrfPrevention: Cookies.get('csrfPrevention'),
			command : "sessionCheck",
		},
		success : function(data) {
			serverResponse = data.sessionExists;
		}
	});
	return serverResponse;
}

function scrollBottom() {
	$("#bottom").get(0).scrollIntoView();
}

function getCurrentUserNick() {
	$.ajax({
		type : 'POST',
		url : 'update',
		dataType : 'json',
		data : {
			csrfPrevention: Cookies.get('csrfPrevention'),
			command : "currentNick",		
		},
		success : function(data) {
			$("#currentUserNick").html("");
			$('#greetTitle').append('<strong id=currentUserNick>' 
					+ data.loginUser + '</strong>');
		}
	});
}

function ajaxSendMessage() {
	$(document).on("click", "#sendMessageButton", function() {
		let messageContent = $('#messageToSend').val();
		$.ajax({
			type : 'POST',
			url : 'update',
			dataType : 'json',
			data : {
				csrfPrevention : Cookies.get('csrfPrevention'),
				command : "message",
				messageToSend : messageContent,
			},
			success : function(data) {
				
				if (data.messageSent == true) {
					$('#messageToSend').val('');
					$("#messages").empty();
					ajaxLastMessages();

					$('#messageSentSign').html('Message sent');
					$('#messageSentSign').fadeIn();
					$('#messageSentSign').delay(2000).fadeOut();
				} else {
					$('#messageSentSign').html('Message has not been sent');
					$('#messageSentSign').fadeIn();
					$('#messageSentSign').delay(5000).fadeOut();
				}
				
			}
		});
	});
}

function ajaxLogout() {
	$(document).on("click", "#logout", function() {
		$.ajax({
			type : 'POST',
			url : 'update',
			dataType : 'json',
			data : {
				csrfPrevention: Cookies.get('csrfPrevention'),
				command : "logout",
			},
			success : function(data) {
				window.location = data.URLLogout;
			}
		});
	});
}

function ajaxOnlineUsers() {
	let sessionExists = checkSession();

	if (sessionExists == false) {
		window.location.hash = '#/login';
	} else {
		$("#onlineUsers").empty();
		$.ajax({
			type : 'POST',
			url : 'update',
			dataType : 'json',
			data : {
				csrfPrevention : Cookies.get('csrfPrevention'),
				command : "getOnlineUsers"
			},
			success : function(data) {
				$.each(data, function(key, value) {
					$('#onlineUsers')
							.append(
									$('<li class="onlineUsersList">' + value
											+ '</li>'));
				});
			}
		});
	}
}

function ajaxLastMessages() {
	let sessionExists = checkSession();

	if (sessionExists == false) {
		window.location.hash = '#/login';
	} else {
		$.ajax({
			type : 'POST',
			url : 'update',
			dataType : 'json',
			data : {
				csrfPrevention : Cookies.get('csrfPrevention'),
				command : "getLastMessages"
			},
			success : function(data) {
				$("#messages").empty();
				let counter = 0;

				$.each(data, function(key, value) {
					if (counter == 0) {
						counter++;
						$('#messages').append(
								'<p class="dateMessage">' + value + " "
										+ "</p>");
					} else if (counter == 1) {
						counter++;
						$('#messages').append(
								'<p class="userMessage">' + value + " "
										+ "</p>");
					} else {
						counter = 0;
						$('#messages').append(
								'<p class="messageContent">' + value + " "
										+ "</p><br>");
					}
				});
				scrollBottom();
			}
			
		});
		scrollBottom();
	}
	scrollBottom();
}

function userDataHoverMenu() {
	let timer;

	$('body').on('mouseenter','.onlineUsersList', function(e) {	
		var currentValue = $( this ).text();
		timer = setTimeout(function(){
			$('#userDataHoverMenu').html('');
				$.ajax({
					type : 'POST',
					url : 'update',
					dataType : 'json',
					data : {
						csrfPrevention: Cookies.get('csrfPrevention'),
						command : "getUserData",
						user: currentValue, 	
					},
						success : function(data) {
							$('#userDataHoverMenu').append($(
									'<li class="hoverUserData">' + 
										'<strong>Full name: </strong>' 
											+ data.fullName +'</li>'));
							$('#userDataHoverMenu').append($(
									'<li class="hoverUserData">' 
										+ '<strong>Telephone: </strong>' 
											+ data.telephone +'</li>'));
							$('#userDataHoverMenu').append($(
									'<li class="hoverUserData">' 
										+ '<strong>Email: </strong>' 
											+ data.email +'</li>'));
							$('#userDataHoverMenu').append($(
									'<img id="profileImage"' + 'src="images/' 
										+ data.userImage + '" height="160"' 
											+ 'width="160"/>'));
						}
				});
		$("#userDataHoverMenu").css({
			left: e.pageX + 1,
			top: e.pageY + 1,
		}).stop().show(100);
	},300);
});

$('body').on('mouseleave','.onlineUsersList', function(e) {
	clearTimeout(timer);
	 $("#userDataHoverMenu").hide();
	});
}
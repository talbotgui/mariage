var login = function(e) {
	e.preventDefault();
	
	valideForm("form", function(data) {
		var req = $.post( REST_PREFIX + "/dologin", data);
		req.success(function(dataString) { window.location.href = REST_PREFIX + "/" });
		req.fail(function(jqXHR, textStatus, errorThrown) { $("#loginError").html(jqXHR.responseText).show(); });
	});
};

$(document).ready(function() {
	$("#button_login").on("click", login);
});

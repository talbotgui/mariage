var afficheUtilisateur = function() {
	var req = $.get( REST_PREFIX + "/utilisateur/moi");
	req.success(function(data) {
		$("input").val("");
		$("#login").val(data.login);
	});
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement de l'utilisateur");});
};

var modifierMotDePasse = function(e) {	
	e.preventDefault();
	
	valideForm("form", function(data) {
		delete data.mdp2;
		var req = $.post( REST_PREFIX + "/utilisateur", data);
		req.success(function(dataString) { 
			afficheUtilisateur();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "la modification du mot de passe");});
	});
};

$(document).ready(function() {
	$("#button_modifier_mdp").on("click", modifierMotDePasse);
	afficheUtilisateur();
});
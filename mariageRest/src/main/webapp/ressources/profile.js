var afficheUtilisateur = function() {
	var req = $.get(REST_PREFIX + "/utilisateur/moi");
	req.success(function(data) {
		$("input").val("");
		$("#login").val(data.login);
	});
	req.fail(function(jqXHR) {
		ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement de l'utilisateur");
	});
};

var modifierMotDePasse = function(e) {
	e.preventDefault();

	valideForm("form", function(data) {
		delete data.mdp2;
		var login = $("input[name='login']").val();
		var req = $.post(REST_PREFIX + "/utilisateur/" + login + "/changeMdp", data);
		req.success(function() {
			afficheUtilisateur();
		});
		req.fail(function(jqXHR) {
			ajaxFailFunctionToDisplayWarn(jqXHR, "la modification du mot de passe");
		});
	});
};

$(document).ready(function() {
	$("#button_modifier_mdp").on("click", modifierMotDePasse);
	afficheUtilisateur();
});
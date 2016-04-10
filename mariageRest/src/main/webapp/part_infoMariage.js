idMariage = "";

if (typeof onReadyFunctions == "undefined") {
	onReadyFunctions = [];
}

onReadyFunctions.push(function() {

	// Redirection si pas de mariage sélectionné
	idMariage = getIdMariage();
	if (idMariage === "") {window.location.href="/";}
	
	// Chargement des données du mariage
	var req = $.get( REST_PREFIX + "/mariage/" + idMariage);
	req.success(function(dataString) {
		var data = eval(dataString);
		$("#maries span:first").html(data.marie1);
		$("#maries span:last").html(data.marie2);
		$("#date span").html(data.dateCelebration);
	});
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("readMariage");});
});
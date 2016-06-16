
var afficheInfoMariage = function(data) {
	// Affichage / masquage de divs
	$("#accueil").hide();
	$("#infoMariage").show();
	
	// Affichage des infos
	$("#maries span:first").html(data.marie1);
	$("#maries span:last").html(data.marie2);
	$("#date span").html(data.dateCelebration);
	$("#maries input:first").val(data.marie1);
	$("#maries input:last").val(data.marie2);
	$("#date input").val(data.dateCelebration);
};

var afficheInfoMariageDepuisId = function() {
	var req = $.get( REST_PREFIX + "/mariage/" + getIdMariage());
	req.success(function(dataString) { afficheInfoMariage(dataString); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("afficheInfoMariageDepuisId");});
};

var afficheInfoMariageDepuisSelect = function(e) {
	afficheInfoMariage(JSON.parse($(e.target).find("option:selected").attr("data-object"))[0]);

	idMariage = e.target.value;
	setIdMariage(idMariage);
};

var afficheInfoMariagePourNouveau = function(e) {
	afficheInfoMariage({marie1:"", marie2:"", dateCelebration:""});
	clicBoutonModifier();
};

var clicBoutonModifier = function(e) {
	var divParente = $("#infoMariage");
	divParente.find("span").hide();
	divParente.find(".form-control").show();
	divParente.find("a").toggle();
}

var sauvegardeInfoMariage = function(e) {
	valideForm("#infoMariage form", function(data) {
		var req = $.post( REST_PREFIX + "/mariage/", data);
		req.success(function(dataString) { 
			idMariage = dataString; 
			setIdMariage(idMariage); 
			afficheInfoMariageDepuisId();
			
			var divParente = $("#infoMariage");
			divParente.find("span").show();
			divParente.find(".form-control").hide();
			divParente.find("a").toggle();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("afficheInfoMariageDepuisId");});
	});
};

$(document).ready(function() {
	// Events
	$(".btn-nouveau").on("click", afficheInfoMariagePourNouveau);
	$(".btn-sauvegarder").on("click", sauvegardeInfoMariage);
	$(".btn-modifier").on("click", clicBoutonModifier);
	
	// Affichage / masquage des divs
	idMariage = getIdMariage();
	if (idMariage === "") {
		$("#selectionMariage").on("change", afficheInfoMariageDepuisSelect);
	} else {
		afficheInfoMariageDepuisId();
	}
});
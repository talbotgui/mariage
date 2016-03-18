// Flag du debug
var debug = false;

// Constantes
var REST_PREFIX = "http://localhost:8080";

// Modification pour le debug 
if (debug) {
  REST_PREFIX = "/test"
}

/**
 * Sauvegarde de l'identifiant du mariage.
 * Cookie de 8 heures
 */
var setIdMariage = function(idMariage) {
    var d = new Date();
    d.setTime(d.getTime() + (8*60*60*1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = "idMariage=" + idMariage + "; " + expires;
};

/**
 * Lecture de l'identifiant du mariage dans les cookies
 */
var getIdMariage = function() {
    var name = "idMariage=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
};

/**
 * En cas d'erreur AJAX.
 */
var ajaxFailFunctionToDisplayWarn = function(appelant) {
	alert("Une erreur est arrivée dans la methode '" + appelant + "'");
};

/**
 * Alimentation des selectsBox
 */
var alimentationSelectBox = function() {
	$("select[source!='']").each(function(i, v) {
		var sel = $(v);
		var source = sel.attr("data-source");
		var empty = sel.attr("data-empty") === "true";
		
		var req = $.get( REST_PREFIX + source);
		req.success(function(dataString) {
			if (empty) {
				sel.append($('<option>', {value:-1, text:""}));
			}
			
			var data = eval(dataString);
			
			data.forEach(function(e, i, array) {
				var txt = e.marie1 + " & " + e.marie2;
				sel.append($('<option>', {value:e.id, text:txt, "data-object":JSON.stringify(data)}));
			});
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("alimentationSelectBox");});
	});
};

/**
 * Au clic sur un bouton modifier.
 */
var clicBoutonModifier = function(e) {
	var divParente = $(e.target).parent();
	divParente.find("span").hide();
	divParente.find(".form-control").show();
	divParente.find("a").toggle();
}

/**
 * Au clic sur un bouton sauvegarder.
 */
var clicBoutonSauvegarder = function(e) {
	var divParente = $(e.target).parent();
	divParente.find("span").show();
	divParente.find(".form-control").hide();
	divParente.find("a").toggle();
}

/** 
 * On ready.
 */
 $(document).ready(function() {
	
	 alimentationSelectBox();
	 
	 $(".dateTimePicker").datepicker({ format: "dd/mm/yyyy", weekStart: 1, language: "fr", daysOfWeekHighlighted: "0,6", autoclose: true });
	 
	 $(".btn-modifier").on("click", clicBoutonModifier);
	 $(".btn-sauvegarder").on("click", clicBoutonSauvegarder);
});
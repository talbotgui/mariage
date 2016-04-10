// Flag du debug
var debug = false;

// Constantes
var REST_PREFIX = "http://" + window.location.hostname + ":" + window.location.port + "/";
var DHTMLX_IMAGE_PATH = "/ressources/dhtmlxGrid_v46_std/imgs/";

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
 * Affiche le menu et sélectionne le bon élément
 */
var afficheMenu = function() {
	$(".nav a[href$='" + window.location.pathname.substring(1) + "']").parent().addClass("active");
	$("div[role=navigation]").show();
};

/**
 * Lecture de l'identifiant du mariage dans les cookies
 * et affichage du menu
 */
var getIdMariage = function() {
    var name = "idMariage=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) {
        	afficheMenu();
        	return c.substring(name.length,c.length);
        }
    }
    return "";
};

/**
 * Pour serialiser un formulaire en objet JS.
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * Validation de formulaire
 */
var valideForm = function(formSelector, callback) {
	var inputs = $(formSelector + " :input[required]");
	inputs.removeClass("error");
	var nbError = inputs.filter(function() {return !this.value;}).addClass("error").length;
	if (nbError == 0) {
		var data = $(formSelector).serialize();
		callback(data);
	}
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
		var labelAtt = sel.attr("data-labelAtt");
		
		var req = $.get( REST_PREFIX + source);
		req.success(function(dataString) {
			if (empty) {
				sel.append($('<option>', {value:-1, text:""}));
			}
			
			var data = eval(dataString);
			
			data.forEach(function(e, i, array) {
				var txt = e;
				if (labelAtt) {
					txt = e[labelAtt];
				}
				var id = e;
				if (e.id) {
					id = e.id;
				}
				sel.append($('<option>', {value:id, text:txt, "data-object":JSON.stringify(data)}));
			});
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("alimentationSelectBox");});
	});
};

/**
 * Fonction de sauvegarde d'une données venant d'une table JqGrid
 * @see http://www.jqwidgets.com/jquery-widgets-documentation/
 */
var majAttribute = function (url, event, success) {
	if (event.args.value === event.args.oldvalue) { return; }

	var data = event.args.row;
	data.id = data.uid;
	delete data.uid;
	delete data.boundindex;
	delete data.uniqueid;
	delete data.visibleindex;
		data[event.args.datafield] = event.args.value;

	var req = $.ajax({ type: "POST", url: url, data: data});
		req.success(function(dataString) { success(); });
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("majAttribute");});
}

/** 
 * On ready.
 */
 $(document).ready(function() {

	 // Execution de toutes les fonctions onReady presentes
	 if (typeof onReadyFunctions !== "undefined") {
		 onReadyFunctions.forEach(function(e, i, array) { e(); });
	 }
	 
	 // Alimentation des <select> avec les info en parametres du tag
	 alimentationSelectBox();
	 
	 // Initialisation des timepicker
	 $(".dateTimePicker").datepicker({ format: "dd/mm/yyyy", weekStart: 1, language: "fr", daysOfWeekHighlighted: "0,6", autoclose: true });
	 
	 // Préparation des popup
	 var popups = $(".popup");
	 if (popups.length > 0) { popups.jqxWindow({ width: 450, autoOpen: false }); }
});
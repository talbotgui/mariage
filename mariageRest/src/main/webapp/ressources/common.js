// Constantes
var REST_PREFIX = window.location.protocol + "//" + window.location.hostname + ((window.location.port !== "") ? ":" + window.location.port : "") + "/monMariage";
var DHTMLX_IMAGE_PATH = "/ressources/dhtmlxGrid_v46_std/imgs/";
var JQX_GRID_PAGE_OPTIONS = ['20', '50', '100', '500'];

/**
 * Affiche la div class="content"
 */
var afficheContent = function() {
	document.getElementsByClassName("invisible")[0].style.visibility = "visible";
};

/**
 * Sauvegarde de l'identifiant du mariage.
 * Cookie de 8 heures
 */
var setIdMariage = function(idMariage) {
    var d = new Date();
    d.setTime(d.getTime() + (8*60*60*1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = "idMariage=" + idMariage + "; " + expires;
    afficheMenu();
};

/**
 * Affiche le menu et sélectionne le bon élément
 */
var afficheMenu = function() {
	// Mise en place du timeout pour attendre le chargement de la div par le script de google
	setTimeout(function(){ 
			var nomPage = window.location.pathname.substring(1);
			if (nomPage === "") { nomPage = "index.html"; }
			$(".nav a[href$='" + nomPage + "']").parent().addClass("active");
			$("div[role=navigation]").show();
 	}, 500);
};

var attributesToArray = function(obj) {
	var arr = [];
	for (var key in obj) {
		if (obj.hasOwnProperty(key)) {
			arr.push({clef:key, valeur:obj[key]});
		}
	};
	return arr;
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

	var inputsRequired = $(formSelector + " :input[required]");
	var inputsMinLength = $(formSelector + " :input[data-minLength]");
	var inputsPattern = $(formSelector + " :input[data-validationPattern]");

	inputsRequired.removeClass("error");
	inputsMinLength.removeClass("error");
	inputsPattern.removeClass("error");

	var errors = inputsRequired.filter(function() { return !$(this).val(); });
	errors = errors.add(inputsMinLength.filter(function() { return this.value.length < $(this).attr("data-minLength"); }));
	errors = errors.add(inputsPattern.filter(function() { return !(new RegExp($(this).attr("data-validationPattern"))).test(this.value); }));
	errors.addClass("error");

	// Do it
	if (errors.length == 0) {
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
				sel.append($('<option>', {value:"", text:""}));
			}
			
			var data = dataString;
			if (typeof dataString === "string") {
				data = JSON.parse(dataString);
			}
			
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
	delete data.boundindex;
	delete data.uniqueid;
	delete data.visibleindex;
	data[event.args.datafield] = event.args.value;

	var req = $.ajax({ type: "POST", url: url, data: data});
		req.success(function(dataString) { success(); });
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("majAttribute");});
}

/**
 * Déconnexion
 */
var logout = function() {
	var req = $.get( REST_PREFIX + "/dologout");
	req.success(function(dataString) { 
		document.cookie = 'idMariage=; expires=Thu, 01-Jan-1970 00:00:01 GMT;';
		window.location.reload();
	});
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("logout");});
}

/** Chargement des données du mariage */
var chargementDonneesDivMaries = function() {
	
	var maj = function() { 
		if (document.getElementById("maries") != null) {
			var req = $.get(REST_PREFIX + "/mariage/" + idMariage);
			req.success(function(dataString) {
				var data = dataString;
				if (typeof dataString === "string") {
					data = JSON.parse(dataString);
				}
				$("#maries span:first").html(data.marie1);
				$("#maries span:last").html(data.marie2);
				$("#date span").html(data.dateCelebration);
			});
			req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("chargementDonneesDivMaries");});
		}
	};
	
	// Mise en place du timeout pour attendre le chargement de la div par le script de google
	setTimeout(maj, 500);
	
	// Pour le cas où
	setTimeout(function() {
		if ($("#maries span:first").html() == "") {
			maj();
		}
	}, 2000);
}

/** 
 * On ready.
 */
$(document).ready(function() {

	// Redirection si pas de mariage sélectionné
	idMariage = getIdMariage();
	if (idMariage === "" && window.location.href !== REST_PREFIX + "/" && window.location.href !== REST_PREFIX + "/login.html") {
		window.location.href = REST_PREFIX + "/";
	}

	// Chargement des données du mariage
	chargementDonneesDivMaries();
	
	// Alimentation des <select> avec les info en parametres du tag
	alimentationSelectBox();
	 
	// Initialisation des timepicker
	$(".datePicker").datetimepicker({ format: 'dd/mm/yyyy', autoclose: true, weekStart: 1, language: 'fr', minView: 2 });
	$(".dateTimePicker").datetimepicker({ format: 'dd/mm/yyyy hh:ii', autoclose: true, weekStart: 1, language: 'fr' });
	 
	// Préparation des popup
	var popups = $(".popup");
	if (popups.length > 0) { popups.jqxWindow({ width: 450, autoOpen: false }); }
});
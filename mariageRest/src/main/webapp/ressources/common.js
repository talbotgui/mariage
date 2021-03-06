// Constantes
var REST_PREFIX = window.location.protocol + "//" + window.location.hostname
		+ ((window.location.port !== "") ? ":" + window.location.port : "") + "/monMariage";
var DHTMLX_IMAGE_PATH = "/ressources/dhtmlxGrid_v46_std/imgs/";
var JQX_GRID_PAGE_OPTIONS = [ '20', '50', '100', '500' ];

/**
 * Affiche la div class="content"
 */
var afficheContent = function() {
	document.getElementsByClassName("invisible")[0].style.visibility = "visible";
};

/**
 * Lecture de l'identifiant du mariage dans les cookies et affichage du menu
 */
var getIdMariage = function() {
	var name = "idMariage=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1);
		if (c.indexOf(name) == 0) {
			afficheMenu();
			return c.substring(name.length, c.length);
		}
	}
	return "";
};

/**
 * En cas d'erreur AJAX.
 */
var ajaxFailFunctionToDisplayWarn = function(jqXHR, descriptionFonctionEnErreur) {
	var message = "Erreur durant " + descriptionFonctionEnErreur;
	if (jqXHR.status === 404) {
		message += ", la ressource demandée n'existe pas/plus";
	} else if (jqXHR.status > 500) {
		message += ", le serveur a retourné une erreur non prévue";
	}
	alert(message);
};

/**
 * Chargement des erreurs et affichage dans une div
 * 
 * @param divSelector
 *            selecteur JQuery pour la div d'erreur
 */
var chargeErreurs = function(divSelector) {
	if (getIdMariage() === "") {
		return;
	}

	var req = $.get(REST_PREFIX + "/mariage/" + getIdMariage() + "/erreurs");
	req.fail(function(jqXHR) {
		ajaxFailFunctionToDisplayWarn(jqXHR, "la recherche des incohérences dans les données du mariage");
	});
	req.success(function(dataString) {

		// Avec les donnees
		var data = dataString;
		if (typeof dataString === "string") {
			data = JSON.parse(dataString);
		}

		var erreursUl = "";
		if (data.length > 0) {
			erreursUl += "<ul>";
			data.forEach(function(e) {
				erreursUl += "<li>" + e + "</li>";
			});
			erreursUl += "</ul>";
		}

		var erreursDiv = $(divSelector);
		erreursDiv.html(erreursUl);
		if (data.length > 0) {
			erreursDiv.show();
		} else {
			erreursDiv.hide();
		}
	});
}

/**
 * Sauvegarde de l'identifiant du mariage. Cookie de 8 heures
 */
var setIdMariage = function(idMariage) {
	var cookieName = "idMariage";
	if (idMariage === "") {
		document.cookie = cookieName + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	} else {
		var d = new Date();
		d.setTime(d.getTime() + (8 * 60 * 60 * 1000));
		var expires = "expires=" + d.toUTCString();
		document.cookie = cookieName + "=" + idMariage + "; " + expires;
	}
};

/**
 * Sélectionne le bon élément
 */
var afficheMenu = function() {
	// Mise en place du timeout pour attendre le chargement de la div par le
	// script de google
	setTimeout(function() {
		var nomPage = window.location.pathname.substring(1);
		if (nomPage === "") {
			nomPage = "index.html";
		}
		$(".nav a[href$='" + nomPage + "']").parent().addClass("active");
	}, 500);
};

/**
 * Affiche une div d'attente devant un tableau jqxGrid
 */
var requestCount = 0;
var afficheDivAttente = function(idTableau) {
	if (requestCount === 0) {
		$(idTableau).jqxGrid('showloadelement');
	}
	requestCount = requestCount + 1;
};
var masqueDivAttente = function(idTableau) {
	requestCount = requestCount - 1;
	if (requestCount === 0) {
		$(idTableau).jqxGrid('hideloadelement');
	}
};

/**
 * Transforme un objet en tableau de clef/valeur
 */
var attributesToArray = function(obj) {
	var arr = [];
	for ( var key in obj) {
		if (obj.hasOwnProperty(key)) {
			arr.push({
				clef : key,
				valeur : obj[key]
			});
		}
	}
	return arr;
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
				o[this.name] = [ o[this.name] ];
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
	var inputsValueEqualsField = $(formSelector + " :input[data-valueEqualsField]");

	inputsRequired.removeClass("error");
	inputsMinLength.removeClass("error");
	inputsPattern.removeClass("error");
	inputsValueEqualsField.removeClass("error");

	var errors = inputsRequired.filter(function() {
		return !$(this).val();
	});
	errors = errors.add(inputsMinLength.filter(function() {
		return this.value.length < $(this).attr("data-minLength");
	}));
	errors = errors.add(inputsPattern.filter(function() {
		return !(new RegExp($(this).attr("data-validationPattern"))).test(this.value);
	}));
	errors = errors.add(inputsValueEqualsField.filter(function() {
		return this.value !== $($(this).attr("data-valueEqualsField")).val();
	}));
	errors.addClass("error");

	// Do it
	if (errors.length == 0) {
		var data = $(formSelector).serialize();
		callback(data);
	}
};

/**
 * Alimentation des selectsBox
 */
var alimentationSelectBox = function() {
	$("select[data-source!=''][data-source]").each(function(i, v) {
		var sel = $(v);
		var source = sel.attr("data-source");
		var empty = sel.attr("data-empty") === "true";
		var labelAtt = sel.attr("data-labelAtt");
		var valueAtt = sel.attr("data-valueAtt");

		var req = $.get(REST_PREFIX + source);
		req.success(function(dataString) {

			sel.empty();

			if (empty) {
				sel.append($('<option>', {
					value : "",
					text : ""
				}));
			}

			var data = dataString;
			if (typeof dataString === "string") {
				data = JSON.parse(dataString);
			}

			data.forEach(function(e) {
				var txt = e;
				if (labelAtt) {
					txt = e[labelAtt];
				}
				var id = e;
				if (e.id) {
					id = e.id;
				}
				if (valueAtt) {
					id = e[valueAtt];
				}
				sel.append($('<option>', {
					value : id,
					text : txt,
					"data-object" : JSON.stringify(data)
				}));
			});
		});
		req.fail(function(jqXHR) {
			ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement de la liste");
		});
	});
};

/**
 * Fonction de sauvegarde d'une données venant d'une table JqGrid
 * 
 * @see http://www.jqwidgets.com/jquery-widgets-documentation/
 */
var majAttribute = function(url, event, success) {
	if (typeof event == "undefined" || typeof event.args == "undefined" || event.args.value === event.args.oldvalue) {
		return;
	}

	var data = event.args.row;
	delete data.boundindex;
	delete data.uniqueid;
	delete data.visibleindex;
	data[event.args.datafield] = event.args.value;

	var req = $.ajax({
		type : "POST",
		url : url,
		data : data
	});
	req.success(function() {
		success();
	});
	req.fail(function(jqXHR) {
		ajaxFailFunctionToDisplayWarn(jqXHR, "la mise à jour des données");
	});
}

/**
 * Déconnexion
 */
var logout = function() {
	var req = $.get(REST_PREFIX + "/dologout");
	req.success(function() {
		document.cookie = 'idMariage=; expires=Thu, 01-Jan-1970 00:00:01 GMT;';
		window.location.reload();
	});
	req.fail(function(jqXHR) {
		ajaxFailFunctionToDisplayWarn(jqXHR, "la déconnexion");
	});
}

/** Chargement des données du mariage */
var chargementDonneesDivMaries = function() {

	var maj = function() {
		var idMariage = getIdMariage();
		if (document.getElementById("maries") != null && idMariage !== "") {
			var req = $.get(REST_PREFIX + "/mariage/" + idMariage);
			req.success(function(dataString) {
				var data = dataString;
				if (typeof dataString === "string") {
					data = JSON.parse(dataString);
				}
				$("#maries span:first").html(data.marie1);
				$("#maries span:last").html(data.marie2);
				$("#date span").html(data.dateCelebration);
				$("#infoMariage").removeClass("invisible");
			});
			req.fail(function(jqXHR) {
				ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement des données du mariage");
			});
		}
	};

	// Mise en place du timeout pour attendre le chargement de la div par le
	// script de google
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

	// Chargement et affichage du menu
	afficheMenu();

	// Chargement des données du mariage
	chargementDonneesDivMaries();

	// Alimentation des <select> avec les info en parametres du tag
	alimentationSelectBox();

	// Initialisation des timepicker
	$(".datePicker").datetimepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
		weekStart : 1,
		language : 'fr',
		minView : 2
	});
	$(".dateTimePicker").datetimepicker({
		format : 'dd/mm/yyyy hh:ii',
		autoclose : true,
		weekStart : 1,
		language : 'fr'
	});

	// Préparation des popup
	var popups = $(".popup");
	if (popups.length > 0) {
		popups.jqxWindow({
			width : 450,
			autoOpen : false
		});
	}

	// Ajout de la deconnexion
	var activeLogout = function() {
		return $("#logout").off("click").on("click", logout).length;
	};

	// Si le menu n'a pas chargé assez vite, la ligne précédente n'a rien fait
	if (activeLogout() == 0) {
		setTimeout(function() {
			activeLogout();
		}, 2000);
	}
});
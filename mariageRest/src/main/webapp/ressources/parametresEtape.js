var donneesDejaChargees = false;

// Affiche la popup des invités
var affichePopupEtapeRepas = function() {
	$("#popupAjoutEtapeRepas").jqxWindow('open');
};
var affichePopupEtapeCeremonie = function() {
	$("#popupAjoutEtapeCeremonie").jqxWindow('open');
};

// Sauvegarde etape par un POST
var ajouteEtape = function(e, idPopup) {
	e.preventDefault();

	valideForm("#" + idPopup + " form", function(data) {
		var req = $.ajax({
			type : "POST",
			url : REST_PREFIX + "/mariage/" + getIdMariage() + "/etape",
			data : data
		});
		req.success(function() {
			$("#" + idPopup).jqxWindow("close");
			chargeEtapes();
		});
		req.fail(function(jqXHR) {
			ajaxFailFunctionToDisplayWarn(jqXHR, "l'ajoute d'une étape");
		});
	});
};
var ajouteEtapeCeremonie = function(e) {
	ajouteEtape(e, "popupAjoutEtapeCeremonie")
};
var ajouteEtapeRepas = function(e) {
	ajouteEtape(e, "popupAjoutEtapeRepas")
};

var supprimeEtape = function(idEtape) {
	if (getIdMariage() === "") {
		return;
	}

	var req = $.ajax({
		type : "DELETE",
		url : REST_PREFIX + "/mariage/" + getIdMariage() + "/etape/" + idEtape
	});
	req.success(function() {
		chargeEtapes();
	});
	req.fail(function(jqXHR) {
		ajaxFailFunctionToDisplayWarn(jqXHR, "la suppression d'une étape");
	});
};

// Chargement des etapes
var chargeEtapes = function() {
	if (getIdMariage() === "") {
		return;
	}

	if (donneesDejaChargees) {
		$("#etapes").jqxGrid('updatebounddata', 'cells');
	} else {
		var dataAdapter = new $.jqx.dataAdapter({
			datatype : "json",
			url : REST_PREFIX + "/mariage/" + getIdMariage() + "/etape",
			datafields : [ {
				name : 'id',
				type : 'string'
			}, {
				name : 'lieu',
				type : 'string'
			}, {
				name : 'nom',
				type : 'string'
			}, {
				name : 'type',
				type : 'string'
			}, {
				name : 'dateHeure',
				type : 'string'
			}, {
				name : 'numOrdre',
				type : 'string'
			} ],
			id : 'id'
		});
		var rendererColonneBouton = function(rowIndex, columnfield, value) {
			return '<a href="javascript:supprimeEtape(' + value + ')" id="btn' + value
					+ '"><span class="ui-icon ui-icon-trash"></span></a>'
		};
		$("#etapes").jqxGrid({
			source : dataAdapter,
			columns : [ {
				text : 'Type',
				datafield : 'type',
				width : "15%",
				editable : false
			}, {
				text : 'Ordre',
				datafield : 'numOrdre',
				width : "5%"
			}, {
				text : 'Nom',
				datafield : 'nom',
				width : "25%"
			}, {
				text : 'Date et heure',
				datafield : 'dateHeure',
				width : "20%"
			}, {
				text : 'Lieu',
				datafield : 'lieu',
				width : "25%"
			}, {
				text : 'Actions',
				datafield : 'id',
				width : "10%",
				editable : false,
				sortable : false,
				menu : false,
				cellsrenderer : rendererColonneBouton
			} ],
			editable : true,
			sortable : true,
			filterable : true,
			autoheight : true,
			altrows : true,
			width : 950,
			ready : afficheContent
		});
		$("#etapes").on('cellendedit', modifieEtape);
		donneesDejaChargees = true;
	}
};

// Fonction JqxGrid
var modifieEtape = function(e) {
	majAttribute(REST_PREFIX + "/mariage/" + getIdMariage() + "/etape", e, chargeEtapes);
}

$(document).ready(function() {

	// Init
	chargeEtapes();

	// Ajout des evenements
	$("#button_afficher_popup_ajouter_ceremonie").on("click", affichePopupEtapeCeremonie);
	$("#button_afficher_popup_ajouter_repas").on("click", affichePopupEtapeRepas);
	$("#button_ajouter_etapeCeremonie").on("click", ajouteEtapeCeremonie);
	$("#button_ajouter_etapeRepas").on("click", ajouteEtapeRepas);
});
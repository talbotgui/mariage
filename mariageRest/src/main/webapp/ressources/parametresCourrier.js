var donneesDejaChargees = false;

// Affiche la popup des invités
var affichePopupCourrier = function() { $("#popupAjoutCourrier").jqxWindow('open'); };

// Fonction JqxGrid
var modifieCourrier = function(e) {
	majAttribute(REST_PREFIX + "/mariage/" + idMariage + "/courrier", e, chargeCourriers);
}

// Sauvegarde courrier par un POST
var ajouteCourrier = function(e) {
	e.preventDefault();
	
	valideForm("#popupAjoutCourrier form", function(data) {
		var req = $.ajax({ type: "POST", url: REST_PREFIX + "/mariage/" + idMariage + "/courrier", data: data});
		req.success(function(dataString) {
			$("#popupAjoutCourrier").jqxWindow("close");
			chargeCourriers();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("ajouteCourrier");});
	});
};


var supprimeCourrier = function(idCourrier) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/mariage/" + idMariage + "/courrier/" + idCourrier});
	req.success(function(dataString) { chargeCourriers(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("supprimeCourrier");});
};

// Chargement des courriers
var chargeCourriers = function() {
	if (donneesDejaChargees) {
		$("#courriers").jqxGrid('updatebounddata', 'cells');
	} else {
		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			url: REST_PREFIX + "/mariage/" + idMariage + "/courrier",
			datafields: [{ name: 'id', type: 'string' },{ name: 'nom', type: 'string' },{ name: 'dateEnvoiRealise', type: 'string' },{ name: 'datePrevisionEnvoi', type: 'string' }],
			id: 'id'
		});
		var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { return '<a href="javascript:supprimeCourrier(' + value + ')" id="btn' + value + '"><span class="ui-icon ui-icon-trash"></span></a>' };
		$("#courriers").jqxGrid({
			source: dataAdapter,
			columns: [
				{ text: 'Nom', datafield: 'nom', width: "30%" },
				{ text: 'Date envoi prévu', datafield: 'datePrevisionEnvoi', width: "30%" },
				{ text: 'Date envoi réalisé', datafield: 'dateEnvoiRealise', width: "30%" },
				{ text: 'Actions', datafield: 'id', width: "10%", editable: false, sortable: false, menu: false, cellsrenderer: rendererColonneBouton }
			],
			editable: true,
			sortable: true,
			filterable: true,
			autoheight: true,
			width: 950
		});
		$("#courriers").on('cellendedit', modifieCourrier);
		donneesDejaChargees = true;
	}
};

$(document).ready(function() {

	// Init
	chargeCourriers();
	
	// Ajout des evenements
	$("#button_afficher_popup_ajouter_courrier").on("click", affichePopupCourrier);
	$("#button_ajouter_courrier").on("click", ajouteCourrier);
});

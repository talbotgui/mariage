var donneesDejaChargees = false;

// Affiche la popup
var affichePopupUtilisateur = function() { $("#popupAjoutUtilisateur").jqxWindow('open'); };

// Sauvegarde utilisateur par un POST
var ajouteUtilisateur = function(e) {
	e.preventDefault();
	
	valideForm("#popupAjoutUtilisateur form", function(data) {
		var req = $.ajax({ type: "POST", url: REST_PREFIX + "/utilisateur", data: data});
		req.success(function(dataString) {
			$("#popupAjoutUtilisateur").jqxWindow("close");
			chargeUtilisateurs();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("ajouteUtilisateur");});
	});
};

var supprimeUtilisateur = function(id) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/utilisateur/" + id});
	req.success(function(dataString) { chargeUtilisateurs(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("supprimeUtilisateur");});
};

// Chargement des utilisateurs
var chargeUtilisateurs = function() {
	if (donneesDejaChargees) {
		$("#utilisateurs").jqxGrid('updatebounddata', 'cells');
	} else {
		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			url: REST_PREFIX + "/utilisateur",
			datafields: [{ name: 'login', type: 'string' },{ name: 'id', type: 'string' }],
			id: 'login'
		});
		var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { return "<a href='javascript:supprimeUtilisateur(\"" + value + "\")' id='btn" + value + "'><span class='ui-icon ui-icon-trash'></span></a>" };
		$("#utilisateurs").jqxGrid({
			source: dataAdapter,
			columns: [
				{ text: 'Login', datafield: 'login', width: "80%" },
				{ text: 'Actions', datafield: 'id', width: "20%", editable: false, sortable: false, menu: false, cellsrenderer: rendererColonneBouton }
			],
			sortable: true, filterable: true, autoheight: true, altrows: true,
			width: 950,
			ready: afficheContent
		});
		donneesDejaChargees = true;
	}
};


	$(document).ready(function() {
		
		// Init
		chargeUtilisateurs();
		
		// Ajout des evenements
		$("#button_afficher_popup_ajouter_utilisateur").on("click", affichePopupUtilisateur);
		$("#button_ajouter_utilisateur").on("click", ajouteUtilisateur);

	});
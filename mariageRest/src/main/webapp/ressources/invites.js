var donneesDejaChargees = false;

// Affiche la popup des invités
var affichePopupInvite = function() {
	$("#popupAjoutInvite").jqxWindow('open');
	window.scrollTo(500, 0);
};

// Fonction JqxGrid
var modifieInvite = function(e) {
	majAttribute(REST_PREFIX + "/mariage/" + idMariage + "/invite", e, chargeInvites);
}

// Sauvegarde invité par un POST
var ajouteInvite = function(e) {
	e.preventDefault();
	
	valideForm("#popupAjoutInvite form", function(data) {
		var req = $.ajax({ type: "POST", url: REST_PREFIX + "/mariage/" + idMariage + "/invite", data: data});
		req.success(function(dataString) {
			$("#popupAjoutInvite").jqxWindow("close");
			chargeInvites();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) { ajaxFailFunctionToDisplayWarn("ajouteInvite"); });
	});
};

var supprimeInvite = function(idInvite) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/mariage/" + idMariage + "/invite/" + idInvite});
	req.success(function(dataString) { chargeInvites(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("ajouteInvite");});
};

// Chargement des invites
var chargeInvites = function() {
	if (donneesDejaChargees) {
		$("#invites").jqxGrid('updatebounddata', 'cells');
	} else {
		
		// Render de la colonne des boutons pour un bouton supprimer se basant sur l'id de l'invite
		var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties, rowData) {
			return '<div class="center"><a href="javascript:supprimeInvite(' + value + ')" id="btnSupprimeInvite' + value + '"><span class="ui-icon ui-icon-trash"></span></a></div>'; 
		};
			
		// Chargement des ages
		var req = $.get( REST_PREFIX + "/parametres/age");
		req.success(function(dataString) {
			var ages = dataString;
			if (typeof dataString === "string") {
				ages = JSON.parse(dataString);
			}
			
			// Champs
			var datafields = [
				{ name: 'id', type: 'string' },
				{ name: 'groupe', type: 'string' },
				{ name: 'foyer', type: 'string' },
				{ name: 'nom', type: 'string' },
				{ name: 'prenom', type: 'string' },
				{ name: 'age', type: 'string' }
			];
				
			// Colonnes 
			var largeurAction = 7;
			var largeur = (100 - largeurAction) / 5 + "%";
			var columns = [
				{ text: 'Groupe', datafield: 'groupe', width: largeur+ "%", align: "center" },
				{ text: 'Foyer', datafield: 'foyer', width: largeur+ "%", align: "center" },
				{ text: 'Nom', datafield: 'nom', width: largeur+ "%", align: "center" },
				{ text: 'Prenom', datafield: 'prenom', width: largeur+ "%", align: "center" },
				{ text: 'Age', datafield: 'age', width: largeur+ "%", align: "center", columntype: 'dropdownlist', createeditor: function (row, cellvalue, editor) {editor.jqxDropDownList({ autoDropDownHeight: true, source: ages });} },
				{ text: 'Actions', datafield: 'id', width: largeurAction + "%", editable: false, sortable: false, menu: false, align: "center", cellsrenderer: rendererColonneBouton }
			];
				
			// Appel JqGrid
			var dataAdapter = new $.jqx.dataAdapter({ datatype: "json", url: REST_PREFIX + "/mariage/" + idMariage + "/invite", datafields: datafields, id: 'id' });
			$("#invites").jqxGrid({ 
				source: dataAdapter, columns: columns, 
				pageable: true, editable: true, sortable: true, filterable: true, autoheight: true, altrows: true, 
				width: 950, pagesizeoptions: JQX_GRID_PAGE_OPTIONS,
				ready: afficheContent
				});
			$("#invites").on('cellendedit', modifieInvite);
			donneesDejaChargees = true;
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("alimentationSelectBox");});
	}
};

$(document).ready(function() {
	// Init
	chargeInvites();
	
	// Ajout des evenements
	$("#button_afficher_popup_ajouter").on("click", affichePopupInvite);
	$("#button_ajouter").on("click", ajouteInvite);
});
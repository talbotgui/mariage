var donneesDejaChargees = false;

// Fonction JqxGrid
var modifieInvite = function(e) {
	if (getIdMariage() === "") { return; }

	majAttribute(REST_PREFIX + "/mariage/" + getIdMariage() + "/invite", e, chargeInvites);
}

// Chargement des invites
var chargeInvites = function() {
	if (getIdMariage() === "") { return; }

	if (donneesDejaChargees) {
		$("#invites").jqxGrid('updatebounddata', 'cells');
	} else {
		// Champs
		var datafields = [
			{ name: 'id', type: 'string' },
			{ name: 'foyer', type: 'string' },
			{ name: 'nom', type: 'string' },
			{ name: 'prenom', type: 'string' },
			{ name: 'commentaire', type: 'string' },
			{ name: 'participantAuxAnimations', type: 'boolean' },
			{ name: 'particularite', type: 'boolean' }
		];
					
		// Colonnes
		var columns = [
			{ text: 'Foyer', datafield: 'foyer', width: "15%", align: "center", editable: false },
			{ text: 'Nom', datafield: 'nom', width: "15%", align: "center", editable: false },
			{ text: 'Prenom', datafield: 'prenom', width: "15%", align: "center", editable: false },
			{ text: 'Joueur', datafield: 'participantAuxAnimations', width: "10%", align: "center", columntype: 'checkbox' },
			{ text: 'Particularité', datafield: 'particularite', width: "10%", align: "center", columntype: 'checkbox' },
			{ text: 'Commentaire', datafield: 'commentaire', width: "35%", align: "center" }
		];
					
		// Appel JqGrid
		var dataAdapter = new $.jqx.dataAdapter({ datatype: "json", url: REST_PREFIX + "/mariage/" + getIdMariage() + "/invite?present=true", datafields: datafields, id: 'id' });
		$("#invites").jqxGrid({ 
			source: dataAdapter, columns: columns, 
			pageable: true, editable: true, sortable: true, filterable: true, autoheight: true, altrows: true, 
			width: 950, pagesizeoptions: JQX_GRID_PAGE_OPTIONS,
			ready: afficheContent
		});
		$("#invites").on('cellendedit', modifieInvite);
		donneesDejaChargees = true;
	}
};

$(document).ready(function() {
	// Init
	chargeInvites();
	
});
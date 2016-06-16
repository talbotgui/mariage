var donneesDejaChargees = false;

// Fonction JqxGrid
var modifieInvite = function(e) {
	majAttribute(REST_PREFIX + "/mariage/" + idMariage + "/invite", e, chargeInvites);
}


// Chargement des invites
var chargeInvites = function() {
	if (donneesDejaChargees) {
		$("#invites").jqxGrid('updatebounddata', 'cells');
	} else {
		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			url: REST_PREFIX + "/mariage/" + idMariage + "/invite",
			datafields: [
				{ name: 'id', type: 'string' },
				{ name: 'groupe', type: 'string' }, { name: 'foyer', type: 'string' },
				{ name: 'nom', type: 'string' }, { name: 'prenom', type: 'string' },
				{ name: 'adresse', type: 'string' }, { name: 'telephone', type: 'string' }
			],
			id: 'id'
		});
		var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { return '<a href="javascript:supprimeInvite(' + value + ')" id="btn' + value + '"><span class="ui-icon ui-icon-trash"></span></a>' };
		$("#invites").jqxGrid({
			source: dataAdapter,
			columns: [
				{ text: 'Groupe', datafield: 'groupe', editable: false, width: "10%" }, { text: 'Foyer', datafield: 'foyer', editable: false, width: "10%" },
				{ text: 'Nom', datafield: 'nom', editable: false, width: "15%" }, { text: 'Prenom', datafield: 'prenom', editable: false, width: "15%" },
				{ text: 'Adresse', datafield: 'adresse', width: "37%" }, { text: 'Téléphone', datafield: 'telephone', width: "13%" }
			],
			editable: true,
			sortable: true,
			filterable: true,
			autoheight: true,
			width: 950
		});
		$("#invites").on('cellendedit', modifieInvite);
		donneesDejaChargees = true;
	}
};

$(document).ready(function() {
	// Init
	chargeInvites();
});

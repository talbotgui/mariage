var donneesDejaChargees = false;

// Fonction JqxGrid
var modifieFoyer = function(e) {
	if (getIdMariage() === "") {
		return;
	}

	majAttribute(REST_PREFIX + "/mariage/" + getIdMariage() + "/foyer", e, chargeFoyers);
}

// Chargement des invites
var chargeFoyers = function() {
	if (getIdMariage() === "") {
		return;
	}

	if (donneesDejaChargees) {
		$("#foyers").jqxGrid('updatebounddata', 'cells');
	} else {

		var datafields = [ {
			name : 'id',
			map : 'dto>id',
			type : 'string'
		}, {
			name : 'groupe',
			map : 'dto>groupe',
			type : 'string'
		}, {
			name : 'nom',
			map : 'dto>nom',
			type : 'string'
		}, {
			name : 'adresse',
			map : 'dto>adresse',
			type : 'string'
		}, {
			name : 'telephone',
			map : 'dto>telephone',
			type : 'string'
		}, {
			name : 'email',
			map : 'dto>email',
			type : 'string'
		} ];

		var columns = [ {
			text : 'Groupe',
			datafield : 'groupe',
			editable : false,
			width : "15%"
		}, {
			text : 'Foyer',
			datafield : 'nom',
			editable : false,
			width : "15%"
		}, {
			text : 'Adresse',
			datafield : 'adresse',
			width : "40%"
		}, {
			text : 'Téléphone',
			datafield : 'telephone',
			width : "10%"
		}, {
			text : 'Email',
			datafield : 'email',
			width : "20%"
		} ];

		var dataAdapter = new $.jqx.dataAdapter({
			datatype : "json",
			url : REST_PREFIX + "/mariage/" + getIdMariage() + "/foyer",
			root : 'dtos',
			datafields : datafields,
			id : 'id'
		});
		$("#foyers").jqxGrid({
			source : dataAdapter,
			columns : columns,
			pageable : true,
			pagesizeoptions : JQX_GRID_PAGE_OPTIONS,
			editable : true,
			sortable : true,
			filterable : true,
			autoheight : true,
			altrows : true,
			width : 950,
			ready : afficheContent
		});
		$("#foyers").on('cellendedit', modifieFoyer);
		donneesDejaChargees = true;
	}
};

$(document).ready(function() {
	// Init
	chargeFoyers();
});

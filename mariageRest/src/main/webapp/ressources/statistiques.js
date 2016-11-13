
var chargeStatistiquesPresence = function() {
	var datafields = [ { name: 'idEtape', type: 'number' }, { name: 'nomEtape', type: 'string' },
	                   { name: 'nbPresence', type: 'int' },{ name: 'nbPresenceConfirme', type: 'int' },
	                   { name: 'nbAbsence', type: 'int' },{ name: 'nbAbsenceConfirme', type: 'int' },
	                   { name: 'nbInconnu', type: 'int' }];
	var columns = [ { text: 'Etape', datafield: 'nomEtape', width: '25%' },
	                { text: 'Absence', datafield: 'nbAbsence', width: '15%' },
	                { text: 'dont confirmée', datafield: 'nbAbsenceConfirme', width: '15%' },
	                { text: 'Presence', datafield: 'nbPresence', width: '15%' },
	                { text: 'dont confirmée', datafield: 'nbPresenceConfirme', width: '15%' },
	                { text: 'Inconnu', datafield: 'nbInconnu', width: '15%' }
	];

	var dataAdapter = new $.jqx.dataAdapter({
		datatype: "json",
		url: REST_PREFIX + "/mariage/" + getIdMariage() + "/statistiquesPresence",
		datafields: datafields,
		id: 'id'
	});

	$("#presences").jqxGrid({
		source: dataAdapter,
		columns: columns,
		editable: false, sortable: false, filterable: true, autoheight: true, altrows: true,
		width: 950,
		ready:  function() { afficheContent(); }
	});
}

// Chargement des stats
var chargeStatistiques= function() {
	var req = $.get( REST_PREFIX + "/mariage/" + getIdMariage() + "/statistiques");
	req.success(function(dataString) {
		var data = dataString;
		if (typeof dataString === "string") {
			data = JSON.parse(dataString);
		}

		document.getElementById("nbFoyers").innerHTML = data.invites.nbFoyers;
		document.getElementById("nbGroupes").innerHTML = data.invites.nbGroupes;
		document.getElementById("nbInvites").innerHTML = data.invites.nbInvites;
		document.getElementById("nbInvitesIncomplets").innerHTML = data.invites.nbInvitesIncomplets;
		document.getElementById("nbInvitesSansAdresse").innerHTML = data.invites.nbInvitesSansAdresse;
		document.getElementById("nbInvitesSansAge").innerHTML = data.invites.nbInvitesSansAge;
		
		var dataAdapterAge = new $.jqx.dataAdapter({
            localdata: attributesToArray(data.repartitions.nbParAge),
            datatype: "array", datafields: [ { name: 'clef', type: 'string' }, { name: 'valeur', type: 'number' } ]
        });
		var dataAdapterFoyer = new $.jqx.dataAdapter({
            localdata: attributesToArray(data.repartitions.nbParFoyer),
            datatype: "array", datafields: [ { name: 'clef', type: 'string' }, { name: 'valeur', type: 'number' } ]
        });
		var dataAdapterGroupe = new $.jqx.dataAdapter({
            localdata: attributesToArray(data.repartitions.nbParGroupe),
            datatype: "array", datafields: [ { name: 'clef', type: 'string' }, { name: 'valeur', type: 'number' } ]
        });
		var dataAdapterInviteEtape = new $.jqx.dataAdapter({
            localdata: attributesToArray(data.repartitions.nbParEtape),
            datatype: "array", datafields: [ { name: 'clef', type: 'string' }, { name: 'valeur', type: 'number' } ]
        });
		var dataAdapterFoyerEtape = new $.jqx.dataAdapter({
            localdata: attributesToArray(data.repartitions.nbFoyersParEtape),
            datatype: "array", datafields: [ { name: 'clef', type: 'string' }, { name: 'valeur', type: 'number' } ]
        });

		$("#repartitionsAge").jqxGrid({
			source: dataAdapterAge, sortable: true, altrows: true,	
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition par age', datafield: 'clef' }, { text:'Nombre d\'invités', datafield: 'valeur' }],
			ready: afficheContent
		});
		$("#repartitionsFoyer").jqxGrid({
			source: dataAdapterFoyer, sortable: true, altrows: true,
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition par foyer', datafield: 'clef' }, { text:'Nombre d\'invités', datafield: 'valeur' }],
			ready: afficheContent
		});
		$("#repartitionsGroupe").jqxGrid({
			source: dataAdapterGroupe, sortable: true, altrows: true,
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition par groupe', datafield: 'clef' }, { text:'Nombre d\'invités', datafield: 'valeur' }],
			ready: afficheContent
		});
		$("#repartitionsInviteParEtape").jqxGrid({
			source: dataAdapterInviteEtape, sortable: true, altrows: true,
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition invités par etape', datafield: 'clef' }, { text:'Nombre d\'invités', datafield: 'valeur' }],
			ready: afficheContent
		});
		$("#repartitionsFoyerParEtape").jqxGrid({
			source: dataAdapterFoyerEtape, sortable: true, altrows: true,
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition foyers par etape', datafield: 'clef' }, { text:'Nombre de foyers', datafield: 'valeur' }],
			ready: afficheContent
		});
	});
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("chargeStatistiques");});
};

$(document).ready(function() {

	// Init
	chargeStatistiques();
	chargeStatistiquesPresence();
	chargeErreurs("#erreurs");
});

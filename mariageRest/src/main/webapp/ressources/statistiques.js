
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

		$("#repartitionsAge").jqxGrid({
			source: dataAdapterAge, sortable: true,	
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition par age', datafield: 'clef' }, { text:'Nombre d\'invités', datafield: 'valeur' }]
		});
		$("#repartitionsFoyer").jqxGrid({
			source: dataAdapterFoyer, sortable: true,	
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition par foyer', datafield: 'clef' }, { text:'Nombre d\'invités', datafield: 'valeur' }]
		});
		$("#repartitionsGroupe").jqxGrid({
			source: dataAdapterGroupe, sortable: true,	
			autoheight: true, autowidth: true,
			columns: [ { text:'Répartition par groupe', datafield: 'clef' }, { text:'Nombre d\'invités', datafield: 'valeur' }]
		});

	});
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("chargeStatistiques");});

};

$(document).ready(function() {

	// Init
	chargeStatistiques();
});

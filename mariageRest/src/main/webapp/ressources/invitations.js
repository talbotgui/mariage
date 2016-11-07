var changeLien = function(idFoyer, idCourrier, checkbox) {
	afficheDivAttente('#foyers');
	
	// Sauvegarde et annulation
	var valeur = checkbox.checked;
	checkbox.checked = !checkbox.checked;
	
	// Requete
	var data = { idCourrier: idCourrier, estInvite: valeur};
	var req = $.post( REST_PREFIX + "/mariage/" + idMariage + "/foyer/" + idFoyer, data);
	req.success(function(dataString) { checkbox.checked = valeur; masqueDivAttente('#foyers'); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("changeLien"); masqueDivAttente('#foyers'); });

};

// Chargement des foyers
var choixPossibles = [];
var chargeFoyers = function() {

	var req = $.get( REST_PREFIX + "/mariage/" + getIdMariage() + "/foyer");
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("chargeFoyers");});
	req.success(function(dataString) {
		
		// Avec les donnees
		var data = dataString;
		if (typeof dataString === "string") {
			data = JSON.parse(dataString);
		}

		// extraction des donnees
		choixPossibles = data.choixPossibles;
		var foyers = data.dtos;

		// Calcul des largeurs
		var largeurChoix = 10;
		var largeur = 10;
		if (choixPossibles.length > 0) { largeur = (100 - largeurChoix * choixPossibles.length) / 2; }
		largeur = largeur + "%";

		// Initialisation des fields et column fixes
		var datafields = [ { name: 'id', map: 'dto>id', type: 'string' }, { name: 'groupe', map: 'dto>groupe', type: 'string' }, { name: 'nom', map: 'dto>nom', type: 'string' } ];
		var columns = [ { text: 'Groupe', datafield: 'groupe', editable: false, width: largeur }, { text: 'Foyer', datafield: 'nom', editable: false, width: largeur } ];

		// Render des colonnes
		var rendererColonneLien = function (rowIndex, columnfield, value, defaulthtml, columnproperties, rowData) {
			var indexEtape = parseInt(columnfield.substring("choix".length)); 
			var idChoix = choixPossibles[indexEtape].id;
			var idFoyer = rowData["id"]; 
			return '<div class="center"><input type="checkbox" onchange="changeLien(' + idFoyer + ', ' + idChoix + ', this)" ' + (value?'checked':'') + '/></div>';
		};

		// Pour chaque courrier
		choixPossibles.forEach(function(e, i, array) {
			// un champ avec le lien (clef du champ 'choixI')
			datafields.push({ name: 'choix' + i, type: 'string'});
			// colonne utilisant le render custo
			columns.push({ text: e.nom, datafield: 'choix' + i, editable: false, width: largeurChoix + "%", align: "center", cellsrenderer: rendererColonneLien });
		});
		
		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			localdata: foyers,
			datafields: datafields, id: 'id'
		});
		$("#foyers").jqxGrid({
			source: dataAdapter,
			columns: columns,
			pageable: true, pagesizeoptions: JQX_GRID_PAGE_OPTIONS,
			editable: true, sortable: true, filterable: true, autoheight: true, altrows: true,
			width: 950,
			ready: afficheContent
		});
	});
};

$(document).ready(function() {
	// Init
	chargeFoyers();
});

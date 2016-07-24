
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

// Au changement d'état du lien
var changeLien = function(idCourrier, idEtape, checkbox) {
	afficheDivAttente('#courriers');
	
	// Sauvegarde et annulation
	var valeur = checkbox.checked;
	checkbox.checked = !checkbox.checked;
	
	// Requete
	var data = { idEtape: idEtape, lie: valeur};
	var req = $.post( REST_PREFIX + "/mariage/" + idMariage + "/courrier/" + idCourrier, data);
	req.success(function(dataString) { checkbox.checked = valeur; masqueDivAttente('#courriers'); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("changeLien"); masqueDivAttente('#courriers'); });
};

var supprimeCourrier = function(idCourrier) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/mariage/" + idMariage + "/courrier/" + idCourrier});
	req.success(function(dataString) { chargeCourriers(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("supprimeCourrier");});
};

// Chargement des courriers
var etapes = [];
var chargeCourriers = function() {
	// Chargement des courriers
	var req = $.get( REST_PREFIX + "/mariage/" + getIdMariage() + "/courrier");
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("chargeInvites");});
	req.success(function(dataString) {

		// Avec les donnees
		var data = dataString;
		if (typeof dataString === "string") {
			data = JSON.parse(dataString);
		}
		
		// extraction des donnees
		etapes = data.choixPossibles;
		var courriers = data.dtos;

		// Calcul des largeurs
		var largeurEtape = 5;
		var largeur = 10;
		var largeurAction = 7;
		if (etapes.length > 0) { largeur = (100 - largeurAction - largeurEtape * etapes.length) / 2; }
		largeur = largeur + "%";

		// Initialisation des fields et column fixes
		var datafields = [{ name: 'id', map: 'dto>id', type: 'string' }, { name: 'nom', map: 'dto>nom', type: 'string' },{ name: 'datePrevisionEnvoi', map: 'dto>datePrevisionEnvoi', type: 'string' }];
		var columns = [{ text: 'Nom', datafield: 'nom', width: largeur + "%" }, { text: 'Date envoi prévu', datafield: 'datePrevisionEnvoi', width: largeur + "%" }];

		// Render des colonnes d'étape
		var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { return '<a href="javascript:supprimeCourrier(' + value + ')" id="btn' + value + '"><span class="ui-icon ui-icon-trash"></span></a>' };
		var rendererColonneLien = function (rowIndex, columnfield, value, defaulthtml, columnproperties, rowData) {
			var indexEtape = parseInt(columnfield.substring("choix".length)); 
			var idEtape = etapes[indexEtape].id;
			var idCourrier = rowData["id"]; 
			return '<div class="center"><input type="checkbox" onchange="changeLien(' + idCourrier + ', ' + idEtape + ', this)" ' + (value?'checked':'') + '/></div>';
		};
		
		// Pour chaque étape
		etapes.forEach(function(e, i, array) {
			// un champ avec le lien (clef du champ 'choixI')
			datafields.push({ name: 'choix' + i, type: 'string'});
			// colonne utilisant le render custo
			columns.push({ text: e.nom, datafield: 'choix' + i, editable: false, width: largeurEtape + "%", align: "center", cellsrenderer: rendererColonneLien });
		});
		
		// Ajout de la colonne des actions
		columns.push({ text: 'Actions', datafield: 'id', width: largeurAction + "%", editable: false, sortable: false, menu: false, cellsrenderer: rendererColonneBouton });
		
		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			localdata: courriers,
			datafields: datafields, id: 'id'
		});
		var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { return '<a href="javascript:supprimeCourrier(' + value + ')" id="btn' + value + '"><span class="ui-icon ui-icon-trash"></span></a>' };
		$("#courriers").jqxGrid({
			source: dataAdapter,
			columns: columns,
			editable: true, sortable: true, filterable: true, altrows: true,
			autoheight: true, width: 950,
			ready: afficheContent
		});
		$("#courriers").on('cellendedit', modifieCourrier);
	});
};

$(document).ready(function() {

	// Init
	chargeCourriers();
	
	// Ajout des evenements
	$("#button_afficher_popup_ajouter_courrier").on("click", affichePopupCourrier);
	$("#button_ajouter_courrier").on("click", ajouteCourrier);
});

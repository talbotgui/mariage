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
var chargeCourriers = function() {
	if (donneesDejaChargees) {
		$("#courriers").jqxGrid('updatebounddata', 'cells');
	} else {
		
		// Chargement des étapes
		var req = $.get( REST_PREFIX + "/mariage/" + getIdMariage() + "/etape");
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("chargeInvites");});
		req.success(function(dataString) {

			// Avec les étapes
			var etapes = dataString;
			if (typeof dataString === "string") {
				etapes = JSON.parse(dataString);
			}

			// Calcul des largeurs
			var largeurEtape = 5;
			var largeur = 10;
			var largeurAction = 7;
			if (etapes.length > 0) { largeur = (100 - largeurAction - largeurEtape * etapes.length) / 2; }
			largeur = largeur + "%";

			// Initialisation des fields et column fixes
			var datafields = [{ name: 'id', type: 'string' }, { name: 'nom', type: 'string' },{ name: 'datePrevisionEnvoi', type: 'string' }];
			var columns = [{ text: 'Nom', datafield: 'nom', width: largeur + "%" }, { text: 'Date envoi prévu', datafield: 'datePrevisionEnvoi', width: largeur + "%" }];

			// Render des colonnes d'étape
			var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { return '<a href="javascript:supprimeCourrier(' + value + ')" id="btn' + value + '"><span class="ui-icon ui-icon-trash"></span></a>' };
			var rendererColonneLien = function (rowIndex, columnfield, value, defaulthtml, columnproperties, rowData) { var idEtape = columnfield.substring("etape".length); var idCourrier = rowData["id"]; return '<div class="center"><input type="checkbox" onchange="changeLien(' + idCourrier + ', ' + idEtape + ', this)" ' + (value?'checked':'') + '/></div>'; };
			
			// Pour chaque étape
			etapes.forEach(function(e, i, array) {
				// un champ avec le lien (clef du champ contenant l'idEtape pour être cohérent dans toutes les lignes)
				datafields.push({ name: 'etape' + e.id, type: 'string', map: 'liensCourrierEtape>' + i + '>lien' });
				// colonne utilisant le render custo
				columns.push({ text: e.nom, datafield: 'etape' + e.id, editable: false, width: largeurEtape + "%", align: "center", cellsrenderer: rendererColonneLien });
			});
			
			// Ajout de la colonne des actions
			columns.push({ text: 'Actions', datafield: 'id', width: largeurAction + "%", editable: false, sortable: false, menu: false, cellsrenderer: rendererColonneBouton });
			
			var dataAdapter = new $.jqx.dataAdapter({
				datatype: "json",
				url: REST_PREFIX + "/mariage/" + idMariage + "/courrier",
				datafields: datafields, id: 'id'
			});
			var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { return '<a href="javascript:supprimeCourrier(' + value + ')" id="btn' + value + '"><span class="ui-icon ui-icon-trash"></span></a>' };
			$("#courriers").jqxGrid({
				source: dataAdapter,
				columns: columns,
				editable: true, sortable: true, filterable: true,
				autoheight: true, width: 950,
				ready: afficheContent
			});
			$("#courriers").on('cellendedit', modifieCourrier);
			donneesDejaChargees = true;
		});
	}
};

$(document).ready(function() {

	// Init
	chargeCourriers();
	
	// Ajout des evenements
	$("#button_afficher_popup_ajouter_courrier").on("click", affichePopupCourrier);
	$("#button_ajouter_courrier").on("click", ajouteCourrier);
});

var donneesDejaChargees = false;

// Affiche la popup des invités
var affichePopupInvite = function() {
	$("#popupAjoutInvite").jqxWindow('open');
	window.scrollTo(500, 0);
};
var affichePopupInviteEnMasse = function() {
	$("#popupAjoutInviteEnMasse").jqxWindow('open');
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

var ajouteInviteEnMasse = function(e) {
	e.preventDefault();
	
	valideForm("#popupAjoutInviteEnMasse form", function(data) {
		var req = $.ajax({ type: "POST", url: REST_PREFIX + "/mariage/" + idMariage + "/inviteEnMasse", data: data});
		req.success(function(dataString) {
			$("#popupAjoutInviteEnMasse").jqxWindow("close");
			chargeInvites();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) { ajaxFailFunctionToDisplayWarn("ajouteInviteEnMasse"); });
	});
};

var supprimeInvite = function(idInvite) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/mariage/" + idMariage + "/invite/" + idInvite});
	req.success(function(dataString) { chargeInvites(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("ajouteInvite");});
};

// Au changement de presence
var changePresence = function(idPresenceEtape, checkbox) {
	afficheDivAttente('#invites');
	
	// Sauvegarde et annulation
	var valeur = checkbox.checked;
	checkbox.checked = !checkbox.checked;
	
	// Requete
	var data = { id: idPresenceEtape, presence: valeur};
	var req = $.post( REST_PREFIX + "/mariage/" + idMariage + "/presenceEtape", data);
	req.success(function(dataString) { checkbox.checked = valeur; masqueDivAttente('#invites'); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("changePresence"); masqueDivAttente('#invites'); });
};

var applyModel = function(idInvite, foyer) {

	// Recherche des input a modifier
	var rowToSearch;
	if (document.getElementById("appliquerAuFoyer").checked) {
		rowToSearch = $(".jqx-grid-cell:contains(" + foyer + ")").filter(function(index) {return $(this).text() === foyer;} );
	} else {
		rowToSearch = $("#btnApplyModel" + idInvite);
	}
	var inputsOfLine = rowToSearch.parents("div[id^='row']").find("input");
	
	// Recherche des input du modele
	var modele = $("#modele table input");
	
	// Application du modèle
	inputsOfLine.each(function (i, e) {
		if (e.checked != modele[i % modele.length].checked) {
			e.click();
		}
	});
};

// Chargement des invites
var chargeInvites = function() {
	if (donneesDejaChargees) {
		$("#invites").jqxGrid('updatebounddata', 'cells');
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
			
			// Render de la colonne des boutons pour un bouton supprimer se basant sur l'id de l'invite
			var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties, rowData) {  return '<div class="center"><a href="javascript:supprimeInvite(' + value + ')" id="btnSupprimeInvite' + value + '"><span class="ui-icon ui-icon-trash"></span></a><a href="javascript:applyModel(' + value + ',\'' + rowData.foyer + '\')" id="btnApplyModel' + value + '"><span class="ui-icon ui-icon-shuffle"></span></a></div>'; };
			
			// Render des colonnes d'étape se basant sur LES CHAMPS liés à une étape
			var rendererColonnePresence = function (rowIndex, columnfield, value, defaulthtml, columnproperties, rowData) { var idEtape = columnfield.substring("presencesEtape".length); var idPresenceEtape = rowData["presencesEtape_ID" + idEtape]; return '<div class="center"><input type="checkbox" onchange="changePresence(' + idPresenceEtape + ', this)" ' + (value?'checked':'') + '/></div>'; };
			
			// Valeurs possibles de la liste déroulante
			var ages = $("select#age option").map(function() {return $(this).val();}).get();
			
			// Champs fixes
			var datafields = [
				{ name: 'id', type: 'string' },
				{ name: 'groupe', type: 'string' },{ name: 'foyer', type: 'string' },
				{ name: 'nom', type: 'string' },{ name: 'prenom', type: 'string' },
				{ name: 'age', type: 'string' }
			];
			
			// Calcul des largeurs des 5 colonnes fixes (7% pour les actions et 10% par etape)
			var largeurEtape = 5;
			var largeur = 10;
			var largeurAction = 7
			if (etapes.length > 0) { largeur = (100 - largeurAction - largeurEtape * etapes.length) / 5; }
			largeur = largeur + "%";
			
			// Colonnes fixes
			var columns = [
				{ text: 'Groupe', datafield: 'groupe', width: largeur, align: "center" }, { text: 'Foyer', datafield: 'foyer', width: largeur, align: "center" },
				{ text: 'Nom', datafield: 'nom', width: largeur, align: "center" }, { text: 'Prenom', datafield: 'prenom', width: largeur, align: "center" },
				{ text: 'Age', datafield: 'age', width: largeur, align: "center", columntype: 'dropdownlist', createeditor: function (row, cellvalue, editor) {editor.jqxDropDownList({ autoDropDownHeight: true, source: ages });} }
			];
			
			// Pour chaque étape, 
			etapes.forEach(function(e, i, array) {
				// un champ avec la présence (clef du champ contenant l'idEtape pour être cohérent dans toutes les lignes)
				datafields.push({ name: 'presencesEtape' + e.id, type: 'string', map: 'presencesEtape>' + i + '>presence' });
				// un champ avec la valeur de l'idPresenceEtape (clef du champ contenant toujours l'idEtape)
				datafields.push({ name: 'presencesEtape_ID' + e.id, type: 'string', map: 'presencesEtape>' + i + '>idPresenceEtape' });
				// colonne utilisant le render custo
				columns.push({ text: e.nom, datafield: 'presencesEtape' + e.id, editable: false, width: largeurEtape + "%", align: "center", cellsrenderer: rendererColonnePresence });
			});
			
			// colonne action pour finir
			columns.push({ text: 'Actions', datafield: 'id', width: largeurAction + "%", editable: false, sortable: false, menu: false, align: "center", cellsrenderer: rendererColonneBouton });
			
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
			
			// Initialisation du modèle de saisie rapide
			var htmlModele = '';
			etapes.forEach(function(e, i, array) {
				htmlModele = htmlModele + '<td><input type="checkbox" data-id="' + e.id + '">&nbsp;' + e.nom + '&nbsp;</td>'
			});
			$("#modele tr:last").append(htmlModele);
		});
	}
};

$(document).ready(function() {
	// Init
	chargeInvites();
	
	// Ajout des evenements
	$("#button_afficher_popup_ajouter").on("click", affichePopupInvite);
	$("#button_afficher_popup_ajouter_en_masse").on("click", affichePopupInviteEnMasse);
	$("#button_ajouter").on("click", ajouteInvite);
	$("#button_ajouterEnMasse").on("click", ajouteInviteEnMasse);
});
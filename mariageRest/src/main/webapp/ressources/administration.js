var donneesDejaChargees = false;
var donneesDejaChargees2 = false;

// Affiche la popup
var affichePopupUtilisateur = function() { $("#popupAjoutUtilisateur").jqxWindow('open'); };
var affichePopupAutorisation = function() { $("#popupAjoutAutorisation").jqxWindow({height: 115}).jqxWindow('open'); };

//Fonction JqxGrid
var modifieUtilisateur= function(e) {
	majAttribute(REST_PREFIX + "/utilisateur", e, chargeUtilisateurs);
}

// Sauvegarde utilisateur par un POST
var ajouteUtilisateur = function(e) {
	e.preventDefault();
	
	valideForm("#popupAjoutUtilisateur form", function(data) {
		var req = $.ajax({ type: "POST", url: REST_PREFIX + "/utilisateur", data: data});
		req.success(function(dataString) {
			$("#popupAjoutUtilisateur").jqxWindow("close");
			chargeUtilisateurs();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "l'ajout de l'utilisateur");});
	});
};

// Sauvegarde autorisation par un POST
var ajouteAutorisation = function(e) {
	e.preventDefault();
	
	valideForm("#popupAjoutAutorisation form", function(data) {
		var req = $.ajax({ type: "POST", url: REST_PREFIX + "/autorisation", data: data});
		req.success(function(dataString) {
			$("#popupAjoutAutorisation").jqxWindow("close");
			chargeAutorisations();
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "l'ajout de l'autorisation");});
	});
};

var supprimeUtilisateur = function(id) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/utilisateur/" + id});
	req.success(function(dataString) { chargeUtilisateurs(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "la suppression de l'utilisateur");});
};
var supprimeAutorisation = function(id) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/autorisation/" + id});
	req.success(function(dataString) { chargeAutorisations(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "la suppression de l'autorisation");});
};

var deverouilleUtilisateur = function(id) {
	var req = $.ajax({ type: "PUT", url: REST_PREFIX + "/utilisateur/" + id + "/deverrouille"});
	req.success(function(dataString) { chargeUtilisateurs(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "le déverouillage de l'utilisateur");});
};

//Chargement des utilisateurs
var chargeUtilisateurs = function() {
	if (donneesDejaChargees) {
		$("#utilisateurs").jqxGrid('updatebounddata', 'cells');
	} else {
		var req = $.get( REST_PREFIX + "/parametres/role");
		req.success(function(dataString) {
			var roles = dataString;
			if (typeof dataString === "string") {
				roles = JSON.parse(dataString);
			}

			var dataAdapter = new $.jqx.dataAdapter({
				datatype: "json",
				url: REST_PREFIX + "/utilisateur",
				datafields: [{ name: 'login', type: 'string' },{ name: 'role', type: 'string' },{ name: 'id', type: 'string' },{ name: 'verrouille', type: 'boolean' }],
				id: 'login'
			});
			var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties, objet) {
				var contenuCase = "<a href='javascript:supprimeUtilisateur(\"" + value + "\")' id='btn" + value + "sup'><span class='ui-icon ui-icon-trash'></span></a>";
				if (objet.verrouille) {
					contenuCase += "<a href='javascript:deverouilleUtilisateur(\"" + value + "\")' id='btn" + value + "dev'><span class='ui-icon ui-icon-power'></span></a>";
				}
				return contenuCase; 
			};
			var rendererColonneStatut = function (rowIndex, columnfield, value, defaulthtml, columnproperties) { 
				if (value) { return "Compte verrouillé"; } else { return ""; }
			};
			$("#utilisateurs").jqxGrid({
				source: dataAdapter,
				columns: [
					{ text: 'Login', datafield: 'login', width: "60%", editable: false }, 
					{ text: 'Role', datafield: 'role', width: "15%", columntype: 'dropdownlist', createeditor: function (row, cellvalue, editor) {editor.jqxDropDownList({ autoDropDownHeight: true, source: roles });}  },
					{ text: 'Statut', datafield: 'verrouille', width: "15%", editable: false, cellsrenderer: rendererColonneStatut },
					{ text: 'Actions', datafield: 'id', width: "10%", editable: false, sortable: false, menu: false, cellsrenderer: rendererColonneBouton }
				],
				editable: true, sortable: true, filterable: true, autoheight: true, altrows: true,
				width: 950,
				ready: afficheContent
			});
			$("#utilisateurs").on('cellendedit', modifieUtilisateur);
			donneesDejaChargees = true;
		});
	}
};

//Chargement des utilisateurs
var chargeAutorisations = function() {
	if (donneesDejaChargees2) {
		$("#autorisations").jqxGrid('updatebounddata', 'cells');
	} else {
		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			url: REST_PREFIX + "/autorisation",
			datafields: [{ name: 'login', type: 'string' },{ name: 'idMariage', type: 'long' },{ name: 'id', type: 'long' },{ name: 'nomMaries', type: 'string' }],
			id: 'login'
		});
		var rendererColonneBouton = function (rowIndex, columnfield, value, defaulthtml, columnproperties, objet) {
			return "<a href='javascript:supprimeAutorisation(\"" + value + "\")' id='btn" + value + "sup'><span class='ui-icon ui-icon-trash'></span></a>";
		};
		$("#autorisations").jqxGrid({
			source: dataAdapter,
			columns: [
				{ text: 'Login', datafield: 'login', width: "40%" }, 
				{ text: 'Mariage', datafield: 'nomMaries', width: "50%" },
				{ text: 'Actions', datafield: 'id', width: "10%", cellsrenderer: rendererColonneBouton }
			],
			editable: false, sortable: true, filterable: true, autoheight: true, altrows: true,
			width: 950,
			ready: afficheContent
		});
		donneesDejaChargees2 = true;
	}
};


$(document).ready(function() {
	
	// Init
	chargeUtilisateurs();
	chargeAutorisations();
	
	// Ajout des evenements
	$("#button_afficher_popup_ajouter_utilisateur").on("click", affichePopupUtilisateur);
	$("#button_ajouter_utilisateur").on("click", ajouteUtilisateur);
	$("#button_afficher_popup_ajouter_autorisation").on("click", affichePopupAutorisation);
	$("#button_ajouter_autorisation").on("click", ajouteAutorisation);

});
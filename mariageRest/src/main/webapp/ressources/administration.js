var donneesDejaChargees = false;

// Affiche la popup
var affichePopupUtilisateur = function() { $("#popupAjoutUtilisateur").jqxWindow('open'); };

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
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("ajouteUtilisateur");});
	});
};

var supprimeUtilisateur = function(id) {
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/utilisateur/" + id});
	req.success(function(dataString) { chargeUtilisateurs(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("supprimeUtilisateur");});
};

var deverouilleUtilisateur = function(id) {
	var req = $.ajax({ type: "GET", url: REST_PREFIX + "/utilisateur/" + id + "/deverrouille"});
	req.success(function(dataString) { chargeUtilisateurs(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("deverouilleUtilisateur");});
};

// Chargement des utilisateurs
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


	$(document).ready(function() {
		
		// Init
		chargeUtilisateurs();
		
		// Ajout des evenements
		$("#button_afficher_popup_ajouter_utilisateur").on("click", affichePopupUtilisateur);
		$("#button_ajouter_utilisateur").on("click", ajouteUtilisateur);

	});
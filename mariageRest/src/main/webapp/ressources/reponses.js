var supprimePresence = function (rowIndex) {
	if (getIdMariage() === "") { return; }

	// get données
	var rowData = $("#reponses").jqxGrid('getrowdata', rowIndex);
	var idEtape = rowData.idEtape;
	var idInvite = rowData.idInvite;
	
	// Suppression presence
	var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/mariage/" + getIdMariage() + "/presence?idEtape=" + idEtape + "&idInvite=" + idInvite});
	req.success(function(dataString) { $("#reponses").jqxGrid('updatebounddata', 'data'); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "la suppression d'une présence");});
}

var modifiePresence = function(radio, rowIndex) {
	// get données
	var rowData = $("#reponses").jqxGrid('getrowdata', rowIndex);

	// creation données
	e = {args: {
		row: rowData,
		datafield: "present",
		value: radio.value,
		oldvalue: ""
	}};

	// modification données
	modifieReponse(e);
}

var modifieConfirme = function(checkbox, rowIndex) {
	// get données
	var rowData = $("#reponses").jqxGrid('getrowdata', rowIndex);

	// creation données
	e = {args: {
		row: rowData,
		datafield: "confirmee",
		value: checkbox.checked,
		oldvalue: ""
	}};

	// modification données
	modifieReponse(e);
}

var modifieReponse = function(e) {
	majAttribute(REST_PREFIX + "/mariage/" + getIdMariage() + "/presence", e, chargePresences);
}

// Chargement des présences
var donneesDejaChargees = false;
var chargePresences = function() {
	if (getIdMariage() === "") { return; }

	
	if (donneesDejaChargees) {
		//$("#reponses").jqxGrid('updatebounddata', 'cells');
	
	} else {
		
		var rendererColonnePresence = function (rowIndex, columnfield, value, defaulthtml, columnproperties, objet) {
			var checkedOui = "";
			var checkedNon = "";
			if (value === true) { checkedOui = "checked='checked'";
			} else if (value === false) { checkedNon = "checked='checked'"; }
			return "<input name='present" + rowIndex +"' value='true' type='radio' onchange='modifiePresence(this, " + rowIndex + ")' " + checkedOui + ">oui</input>" +
					"&nbsp;<input name='present" + rowIndex +"' value='false' type='radio' onchange='modifiePresence(this, " + rowIndex + ")' " + checkedNon + ">non</input>"; 
		};
		var rendererColonneConfirme = function (rowIndex, columnfield, value, defaulthtml, columnproperties, objet) {
			var checked = "";
			if (value === true) { checked = "checked='checked'"; }
			var contenuCase = "<div style='text-align:center'>";
			contenuCase += "<input name='confirme" + rowIndex +"' type='checkbox' onchange='modifieConfirme(this," + rowIndex + ")' " + checked + "></input>";
			contenuCase += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			if (objet.dateMaj) {
				contenuCase += "<a href='javascript:supprimePresence(\"" + rowIndex + "\")' id='supp" + rowIndex + "'><span class='ui-icon ui-icon-cancel'></span></a>";
			} else {
				contenuCase += "<a><span class='ui-icon ui-icon-blank'></span></a>";
			}
			contenuCase += "</div>";
			return contenuCase;
		};

		// Initialisation des fields et column fixes
		var datafields = [ { name: 'idEtape', type: 'number' }, { name: 'idInvite', type: 'number' }, { name: 'id', type: 'string' },
		                   { name: 'nomEtape', type: 'string' }, { name: 'nomInvite', type: 'string' }, { name: 'prenomInvite', type: 'string' },
		                   { name: 'dateMaj', type: 'date' }, { name: 'commentaire', type: 'string' }, { name: 'confirmee', type: 'boolean' }, { name: 'present', type: 'boolean' } ];
		var columns = [ { text: 'Nom', datafield: 'nomInvite', editable: false, width: '10%' },
						{ text: 'Prénom', datafield: 'prenomInvite', editable: false, width: '10%' }, 
		                { text: 'Etape', datafield: 'nomEtape', editable: false, width: '10%' },
						{ text: 'Mise à jour', datafield: 'dateMaj', editable: false, width: '12%', cellsformat: 'dd/MM/yy HH:mm' },
						{ text: 'Présence', datafield: 'present', editable: false, width: '10%', cellsrenderer: rendererColonnePresence },
						{ text: 'Confirmation', datafield: 'confirmee', editable: false, width: '10%', cellsrenderer: rendererColonneConfirme },
						{ text: 'Commentaire', datafield: 'commentaire', editable: true, width: '38%' }
		];

		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			url: REST_PREFIX + "/mariage/" + getIdMariage() + "/presence",
			datafields: datafields,
			id: 'id'
		});

		$("#reponses").jqxGrid({
			source: dataAdapter,
			columns: columns,
			pageable: true, pagesizeoptions: ['20', '50', '100'],
			editable: true, sortable: false, filterable: true, autoheight: true, altrows: true,
			width: 950,
			ready:  function() { afficheContent(); }
		});
		$("#reponses").on('cellendedit', modifieReponse);
		$("#reponses").on("pagechanged", function (event) { $("#reponses").jqxGrid('updatebounddata', 'data'); $("#reponses").jqxGrid('gotopage', event.args.pagenum); });
		$("#reponses").on("pagesizechanged", function (event) { $("#reponses").jqxGrid('updatebounddata', 'data'); });
		donneesDejaChargees = true;
	}
};

$(document).ready(function() {
	// Init
	chargePresences();
	chargeErreurs("#erreurs");
});

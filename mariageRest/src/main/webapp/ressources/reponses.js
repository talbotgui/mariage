var modifiePresence = function(radio) {
	// get données
	var rowIndex = radio.name.substring("present".length);
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

var modifieConfirme = function(checkbox) {
	// get données
	var rowIndex = checkbox.name.substring("confirme".length);
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
	majAttribute(REST_PREFIX + "/mariage/" + idMariage + "/presence", e, chargePresences);
}

// Chargement des présences
var donneesDejaChargees = false;
var chargePresences = function() {
	
	if (donneesDejaChargees) {
		//$("#reponses").jqxGrid('updatebounddata', 'cells');
	
	} else {
		
		var rendererColonnePresence = function (rowIndex, columnfield, value, defaulthtml, columnproperties, objet) {
			var name = "present" + rowIndex; var checkedOui = ""; var checkedNon = "";
			if (value === true) { checkedOui = "checked='checked'";
			} else if (value === false) { checkedNon = "checked='checked'"; }
			return "<input name='" + name +"' value='true' type='radio' onchange='modifiePresence(this)' " + checkedOui + ">oui</input>&nbsp;<input name='" + name +"' value='false' type='radio' onchange='modifiePresence(this)' " + checkedNon + ">non</input>"; 
		};
		var rendererColonneConfirme = function (rowIndex, columnfield, value, defaulthtml, columnproperties, objet) {
			var name = "confirme" + rowIndex; var checked = "";
			if (value === true) { checked = "checked='checked'"; }
			return "<div style='text-align:center'><input name='" + name +"' type='checkbox' onchange='modifieConfirme(this)' " + checked + "></input></div>"; 
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
});

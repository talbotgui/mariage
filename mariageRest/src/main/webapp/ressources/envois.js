var chargeSelectBox = function() {
	if (getIdMariage() === "") {
		return;
	}

	var req = $.get(REST_PREFIX + "/mariage/" + getIdMariage() + "/courrier");
	req.success(function(dataString) {
		var sel = $("#courrier");
		sel.append($('<option>', {
			value : "",
			text : ""
		}));

		var data = dataString;
		if (typeof dataString === "string") {
			data = JSON.parse(dataString);
		}

		data.dtos.forEach(function(e) {
			sel.append($('<option>', {
				value : e.dto.id,
				text : e.dto.nom,
				"data-object" : JSON.stringify(e.dto.id)
			}));
		});

		afficheContent();
	});
	req.fail(function(jqXHR) {
		ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement des courriers");
	});
};

var chargeTableauDuCourrier = function() {
	if (getIdMariage() === "") {
		return;
	}

	var idCourrier = $("#courrier").val();

	if (idCourrier == "") {
		$("#courrier").jqxGrid("clear");
	}

	// Initialisation des fields et column
	var datafields = [ {
		name : 'id',
		type : 'string'
	}, {
		name : 'groupe',
		type : 'string'
	}, {
		name : 'nom',
		type : 'string'
	} ];
	var columns = [ {
		text : 'Groupe',
		datafield : 'groupe',
		editable : false,
		width : "50%"
	}, {
		text : 'Foyer',
		datafield : 'nom',
		editable : false,
		width : "50%"
	} ];

	var dataAdapter = new $.jqx.dataAdapter({
		datatype : "json",
		url : REST_PREFIX + "/mariage/" + getIdMariage() + "/courrier/" + idCourrier + "/foyers",
		datafields : datafields,
		id : 'id'
	});

	$("#foyers").jqxGrid({
		source : dataAdapter,
		columns : columns,
		pagesizeoptions : JQX_GRID_PAGE_OPTIONS,
		editable : false,
		sortable : true,
		filterable : true,
		autoheight : true,
		altrows : true,
		width : 950
	});
};

var telechargeFichierPublipostage = function() {
	var idCourrier = $("#courrier").val();
	if (idCourrier != "") {
		window.open(REST_PREFIX + "/mariage/" + getIdMariage() + "/courrier/" + idCourrier + "/publipostage");
		window.open(REST_PREFIX + "/modelePublipostage.docx");
	}
};

$(document).ready(function() {

	chargeSelectBox();

	$("#courrier").on("change", chargeTableauDuCourrier);
	$("#button_publipostage").on("click", telechargeFichierPublipostage);
});
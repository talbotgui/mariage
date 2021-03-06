var afficheInfoMariage = function(data) {
	// Affichage / masquage de divs
	$("#accueil").hide();
	$("#infoMariage").show();

	// Affichage des infos
	$("#maries span:first").html(data.marie1);
	$("#maries span:last").html(data.marie2);
	$("#date span").html(data.dateCelebration);
	$("#maries input:first").val(data.marie1);
	$("#maries input:last").val(data.marie2);
	$("#date input").val(data.dateCelebration);
	$("#infoMariage input[type=hidden]").val(data.id);

	// Affichage du countDown
	if (data.dateCelebration) {
		var d = data.dateCelebration.split("/");
		d = d[2] + "-" + d[1] + "-" + d[0] + " 00:00:00";
		$("#dateCountdown").attr("data-date", d);

		$("#dateCountdown").TimeCircles({
			"animation" : "ticks",
			"bg_width" : 1.2,
			"fg_width" : 0.1,
			"circle_bg_color" : "#60686F",
			"time" : {
				"Days" : {
					"text" : "Jours",
					"color" : "#FFCC66",
					"show" : true
				},
				"Hours" : {
					"text" : "Heures",
					"color" : "#99CCFF",
					"show" : true
				},
				"Minutes" : {
					"text" : "Minutes",
					"color" : "#BBFFBB",
					"show" : true
				},
				"Seconds" : {
					"text" : "Secondes",
					"color" : "#FF9999",
					"show" : true
				}
			}
		});
	}
};

var afficheInfoMariageDepuisId = function() {
	if (getIdMariage() !== "") {
		var req = $.get(REST_PREFIX + "/mariage/" + getIdMariage());
		req.success(function(dataString) {
			afficheInfoMariage(dataString);
		});
		req.fail(function(jqXHR) {
			ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement des données du mariage");
		});
	}
};

var afficheInfoMariageDepuisSelect = function(e) {
	afficheInfoMariage(JSON.parse($(e.target).find("option:selected").attr("data-object"))[0]);

	setIdMariage(e.target.value);

	// refresh du menu
	$(".header > div").attr("data-w3-include-html", "./part_menu.html");
	myHTMLInclude();
	afficheMenu();
};

var clicBoutonModifier = function() {
	var divParente = $("#infoMariage");
	divParente.find("span").hide();
	divParente.find(".form-control").show();
	divParente.find("a").toggle();
};

var afficheInfoMariagePourNouveau = function() {
	afficheInfoMariage({
		marie1 : "",
		marie2 : "",
		dateCelebration : ""
	});
	clicBoutonModifier();
};

var supprimeMariage = function() {
	$("#dialogConfirmSuppression").dialog({
		resizable : false,
		height : "auto",
		width : 400,
		modal : true,
		buttons : {
			"Supprimer le mariage" : function() {
				var req = $.ajax({
					type : "DELETE",
					url : REST_PREFIX + "/mariage/" + getIdMariage()
				});
				req.success(function() {
					setIdMariage("");
					location.reload();
				});
				req.fail(function(jqXHR) {
					ajaxFailFunctionToDisplayWarn(jqXHR, "la suppression du mariage");
				});
				$(this).dialog("close");
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		}
	});
};

var sauvegardeInfoMariage = function() {
	valideForm("#infoMariage form", function(data) {
		var req = $.post(REST_PREFIX + "/mariage/", data);
		req.success(function(dataString) {
			setIdMariage(dataString);
			afficheInfoMariageDepuisId();

			var divParente = $("#infoMariage");
			divParente.find("span").show();
			divParente.find(".form-control").hide();
			divParente.find("a").toggle();
		});
		req.fail(function(jqXHR) {
			ajaxFailFunctionToDisplayWarn(jqXHR, "la modification des données du mariage");
		});
	});
};

$(document).ready(function() {
	// Events
	$(".btn-nouveau").on("click", afficheInfoMariagePourNouveau);
	$(".btn-sauvegarder").on("click", sauvegardeInfoMariage);
	$(".btn-supprimer").on("click", supprimeMariage);
	$(".btn-modifier").on("click", clicBoutonModifier);

	// Affichage / masquage des divs
	if (getIdMariage() === "") {
		$("#selectionMariage").on("change", afficheInfoMariageDepuisSelect);
	} else {
		afficheInfoMariageDepuisId();
	}
});
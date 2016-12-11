var donneesDejaChargees = false;
var dataInDhtmlXDataStore;
var filtres = {};

/** Init des filtres */
var initialisationFiltres = function(utilisateurs) {
	var div = $(".divCalendrierFiltre");

	utilisateurs.forEach(function(e, i, array) {
		filtres[e.login] = true;
		div.append("<label><input class='inputFiltre' type='checkbox' checked='checked' value='" + e.login + "'/>" + e.login + "</label><br/>");
	});

	$(".inputFiltre").on("click", function (e) { 
		filtres[e.target.value] = !filtres[e.target.value];
		scheduler.updateView();
	});
}

var filtreEvenements = function(id, event) {
	if (event) {
		for (var i in event.participants) {
			if (filtres[event.participants[i]]) {
				return true;
			}
		}
	}
	return false;
}

/** Creation / modification d'une évènement. */
var modificationEvenement = function(idEvent, ev) {

	// Les identifiants générés par DHTMLx sont calculé avec la date courante ((new Date()).valueOf())
	if (idEvent > 1480000000000) { idEvent = null; }

	var data = { id: idEvent, titre: ev.text, debut: ev.start_date, fin: ev.end_date, participants: ev.participants };

	var req = $.ajax({ type: "POST", url: REST_PREFIX + "/mariage/" + getIdMariage() + "/evenement", data: data});
	req.success(function(dataString) { chargeCalendrier(); });
	req.fail(function(jqXHR, textStatus, errorThrown) { ajaxFailFunctionToDisplayWarn(jqXHR, "la création/modification d'un évènement"); });

	return false;
}

/** Suppression d'un évènement */
var supprimerEvenement = function(idEvent, ev) {
	// Pas d'appel REST si l'identifiant est encore une valeur temporaire de DHTMLx
	if (idEvent < 1480000000000) {
		var req = $.ajax({ type: "DELETE", url: REST_PREFIX + "/mariage/" + getIdMariage() + "/evenement/" + idEvent});
		req.success(function(dataString) { chargeCalendrier(); });
		req.fail(function(jqXHR, textStatus, errorThrown) { ajaxFailFunctionToDisplayWarn(jqXHR, "la suppression d'un évènement"); });
	}
	return false;
}

/** Chargement */
var chargeCalendrier = function() {
	if (getIdMariage() === "") { return; }

	if (!donneesDejaChargees) { 

		// Pour fixer le debut et la fin du calendrier à la journée
		// scheduler.config.first_hour = 7;
		// scheduler.config.last_hour = 23;

		// Format des dates
		scheduler.config.xml_date="%d/%m/%Y %H:%i";

		// Pour afficher le formulaire complet à la création et à la modification
		scheduler.config.details_on_create=true;
		scheduler.config.details_on_dblclick=true;

		// Pour attraper les ajouts/modifications/suppressions
		scheduler.attachEvent("onEventAdded", modificationEvenement);
		scheduler.attachEvent("onEventChanged", modificationEvenement);
		scheduler.attachEvent("onEventDeleted", supprimerEvenement);
	
		// Pour placer le calendrier au jour d'aujourd'hui et en mode semaine
		scheduler.init("calendrier", new Date(), "week");
	
		// Validation du formulaire
		scheduler.attachEvent("onEventSave", function(id, ev){
			if (!ev.text) { dhtmlx.alert("Le titre ne peut être vide"); return false; }
			if (ev.participants.length === 0) { dhtmlx.alert("Au minimum un participant doit être sélectionné"); return false; }
			return true;
		});

		// Filtrage
		scheduler.filter_month = filtreEvenements;
		scheduler.filter_day = filtreEvenements;
		scheduler.filter_week = filtreEvenements;

		// Recherche des utilisateurs
		var req = $.ajax({ type: "GET", url: REST_PREFIX + "/utilisateur/"});
		req.success(function(data) {
			// Configuration des filtres
			initialisationFiltres(data);

			// Configuration du formulaire de détails
			var mapUtilisateurs = [];
			data.forEach(function(e, i, array) { mapUtilisateurs.push({key: e.login, label: e.login}); });
			scheduler.config.lightbox.sections = [	
				{ name:"Titre", height:50, map_to:"text", type:"textarea", focus:true }
				,{ name:"Participants", height:22, map_to:"participants", type:"multiselect", options: mapUtilisateurs }
				,{ name:"Horaire", height:72, type:"time", map_to:"auto"}	
	        ];
		});
		req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement des utilisateurs de l'application");});	
	}

	// Chargement des données depuis l'api REST
	req = $.ajax({ type: "GET", url: REST_PREFIX + "/mariage/" + getIdMariage() + "/evenement" });
	req.success(function(data) {
		data.forEach(function(e, i, array) {
			e.start_date = new Date(e.debut);
			e.end_date = new Date(e.fin);
			e.text = e.titre;
		});
		scheduler.clearAll();
		scheduler.parse(data, "json");
	});
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement des évènements");});	

	// Pour ne pas repasser par là
	donneesDejaChargees = true;
};

/** Affichage/masquage du minicalendrier en bouton */
function afficheMiniCalendrier(){
	if (scheduler.isCalendarVisible()) {
		scheduler.destroyCalendar();
	} else {
		scheduler.renderCalendar({
			position:"dhx_minical_icon", date:scheduler._date, navigation:true,
			handler:function(date,calendar) { scheduler.setCurrentView(date); scheduler.destroyCalendar(); }
		});
	}
}

$(document).ready(function() {
	chargeCalendrier();

	// Gestion du minicalendrier
	$(".dhx_minical_icon").on("click", afficheMiniCalendrier);
	$(".calendrierFiltreIcone").on("click", function() { $(".divCalendrierFiltre").toggle(); });
	$(".divCalendrierFiltre").on("mouseleave", function() { $(".divCalendrierFiltre").hide(); });
});

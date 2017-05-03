var afficheCommits = function() {

	// Pour modifier les commentaires de commits anciens : https://help.github.com/articles/changing-a-commit-message/
	var req = $.get("https://api.github.com/repos/talbotgui/mariage/commits");
	req.fail(function(jqXHR) {
		ajaxFailFunctionToDisplayWarn(jqXHR, "le chargement de l'historique du dépot GitHub");
	});
	req.success(function(data) {

		// Filtrage des commits
		var commitsAffichable = data.filter(function(element) {

			// recherche du type de code commité
			var message = element.commit.message.toUpperCase();
			var type = ""
			if (message.indexOf("[FEATURE]") != -1) {
				type = "fonctionnalité";
			} else if (message.indexOf("[FEATURE]") == -1) {
				type = "correction";
			}

			// Enrichissement des données
			element.type = type;
			element.commit.message = element.commit.message.replace("[FEATURE]", "").replace("[BUG]", "");

			// Filtrage
			return type !== "";
		})

		// Parametrage du tableau
		var datafields = [ {
			name : 'id',
			type : 'sha'
		}, {
			name : 'type',
			type : 'string'
		}, {
			name : 'message',
			type : 'string',
			map : 'commit>message'
		}, {
			name : 'date',
			type : 'date',
			map : 'commit>committer>date'
		}, {
			name : 'committer',
			type : 'string',
			map : 'commit>committer>name'
		} ];
		var columns = [ {
			text : 'Date',
			datafield : 'date',
			width : '15%',
			cellsformat : 'dd/MM/yy HH:mm'
		}, {
			text : 'Type',
			datafield : 'type',
			width : '15%'
		}, {
			text : 'Modification apportée',
			datafield : 'message',
			width : '70%'
		} /*
			 * , { text: 'Auteur', datafield: 'committer', width: '15%' }
			 */
		];

		// Creation du tableau
		var dataAdapter = new $.jqx.dataAdapter({
			datatype : "json",
			localdata : commitsAffichable,
			datafields : datafields,
			id : 'sha'
		});

		$("#releaseNotes").jqxGrid({
			source : dataAdapter,
			columns : columns,
			pageable : false,
			editable : false,
			sortable : false,
			filterable : true,
			autoheight : true,
			altrows : true,
			autorowheight : true,
			width : 950
		});
	});
};

$(document).ready(function() {
	afficheCommits();
});
#!groovy

// Define job properties
properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '15')), pipelineTriggers([])])

pipeline {
    tools {
        maven "M3"
    }
	
	agent none

	stages {
		
		stage ('Checkout') {
			agent label:''
			steps {
				git url: 'https://github.com/talbotgui/mariage.git'
				script {
					def v = version()
					if (v) { echo "Building version ${v}" }
				}
			}
		}

		stage ('Build') {
			agent label:''
			steps {
				script { env.PATH = "${tool 'M3'}/bin:${env.PATH}" }
				sh "mvn clean install -Dmaven.test.skip=true"
				sh "cp mariageRest/target/mariageRest-*.war ./mariageRest.war"
			}
		}

		stage ('Unit test') {
			agent label:''
			steps {
				sh "mvn -B -Dmaven.test.failure.ignore clean test-compile surefire:test"
				junit '**/TEST-*Test.xml'
			}
		}

		stage ('Integration test') {
			agent label:''
			steps {
				sh "mvn -B -Dmaven.test.failure.ignore clean test-compile failsafe:integration-test"
				junit '**/failsafe-reports/TEST-*.xml'
			}
		}
		
		stage ('Quality') {
			agent label:''
			steps {
				sh "mvn site -Dmaven.test.skip=true"
				//step([$class: 'FindBugsPublisher'])
				//step([$class: 'CheckStylePublisher'])
				step([$class: 'AnalysisPublisher'])
				step([$class: 'JavadocArchiver', javadocDir: 'mariageRest/target/site/apidocs', keepAll: false])
			}
		}
		
		stage ('Production') { 
			agent none
			
			steps {
				
				// Pour ne pas laisser trainer l'attente d'une saisie durant plus de 1 jour
				timeout(time:1, unit:'DAYS') {
					script {
					
						// Pour inhiber le contenu du stage 'production' si la branche n'est pas le master
						def deployable_branches = ["master"]
						if (deployable_branches.contains(env.BRANCH_NAME)) {

							// Demande de saisie avec milestone pour arrêter les builds précédents en attente au moment où un utilisateur répond à un build plus récent
							milestone(1)
							def userInput = input message: 'Production ?', parameters: [booleanParam(defaultValue: false, description: '', name: 'miseEnProduction')]
							milestone(2)

							// Installation en production et changement du nom indiquant le statut
							if (userInput) {
								node {
									currentBuild.displayName = currentBuild.displayName + " - deployed to production"
									sh "/var/lib/mariage/stopMariage.sh"
									sh "rm /var/lib/mariage/*.war || true"
									sh "cp ./mariageRest.war /var/lib/mariage/"
									sh "/var/lib/mariage/startMariage.sh"
									build 'Surveillant'
								}
							}
						} else {
						    currentBuild.displayName = currentBuild.displayName + " - not master - no production"
						}
					}
				}
			}
		}
	}
	
	post {
        success {
			node ('') { archiveArtifacts artifacts: 'mariageRest.war', fingerprint: true }
			node ('') { step([$class: 'WsCleanup', notFailBuild: true]) }
        }
        unstable {
			node ('') { archiveArtifacts artifacts: 'mariageRest.war', fingerprint: true }
			node ('') { step([$class: 'WsCleanup', notFailBuild: true]) }
		}
        failure {
			node ('') { emailext subject: "${env.JOB_NAME}#${env.BUILD_NUMBER} - Error during the build !", to: "talbotgui@gmail.com", body: "failure : ${e}"; }
			node ('') { step([$class: 'WsCleanup', notFailBuild: true]) }
        }
        //always {}
        //changed {}
    }
}

/* Fonctions utilitaires */

// Extraction du numero de version depuis le pom.xml present a la racine de la branche
def version() { def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'; matcher ? matcher[0][1] : null; };
// avec le plugin 'pipeline utility steps' => def version() { def pom = readMavenPom; pom.version; };

/* Variables disponibles : env.PATH, env.BUILD_TAG, env.BRANCH_NAME, currentBuild.result, currentBuild.displayName, currentBuild.description */

// Exemple de definition de parametres dans le job : https://issues.jenkins-ci.org/browse/JENKINS-32780


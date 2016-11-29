#!groovy

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
				step([$class: 'FindBugsPublisher'])
				step([$class: 'CheckStylePublisher'])
				step([$class: 'AnalysisPublisher'])
				step([$class: 'JavadocArchiver', javadocDir: 'mariageRest/target/site/apidocs', keepAll: false])
			}
		}
		
		stage ('Production') { 
			agent none
			
			steps {
				timeout(time:1, unit:'DAYS') {
					script {
						def deployable_branches = ["master"]
						if (deployable_branches.contains(env.BRANCH_NAME)) {
							def userInput = input message: 'Production ?', parameters: [booleanParam(defaultValue: false, description: '', name: 'miseEnProduction')]

							if (userInput) {
								node {
									currentBuild.description = "Deployer to production"
									sh "/var/lib/mariage/stopMariage.sh"
									sh "rm /var/lib/mariage/*.war || true"
									sh "cp ./mariageRest.war /var/lib/mariage/"
									sh "/var/lib/mariage/startMariage.sh"
									build 'Surveillant'
								}
							}
						} else {
						    currentBuild.description = "not master - no production"
						}
					}
				}
			}
		}
	}
	
	post {
        //always {}
        success {
			node ('') { archiveArtifacts artifacts: 'mariageRest.war', fingerprint: true }
        }
        unstable {
			node ('') { archiveArtifacts artifacts: 'mariageRest.war', fingerprint: true }
		}
        failure {
			node ('') { emailext subject: "${env.JOB_NAME}#${env.BUILD_NUMBER} - Error during the build !", to: "talbotgui@gmail.com", body: "failure : ${e}"; }
        }
        //changed {}
    }
}

/* Fonctions utilitaires */

// Extraction du numero de version depuis le pom.xml present a la racine de la branche
def version() { def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'; matcher ? matcher[0][1] : null; };
// avec le plugin 'pipeline utility steps' => def version() { def pom = readMavenPom; pom.version; };

/* Variables disponibles : env.PATH, env.BUILD_TAG, env.BRANCH_NAME, currentBuild.result, currentBuild.displayName, currentBuild.description */

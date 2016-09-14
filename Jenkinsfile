#!groovy

pipelineAvecMailSiUnstable('talbotgui@gmail.com') {

	node {
		def mvnHome = tool 'M3'

		stage ('Checkout') {
			git url: 'https://github.com/talbotgui/mariage.git'
			def v = version()
			if (v) { echo "Building version ${v}" }
		}

		stage ('Build') {
			sh "${mvnHome}/bin/mvn clean install -Dmaven.test.skip=true"
			sh "cp mariageRest/target/mariageRest-*.war ./mariageRest.war"
		}

		stage ('Unit test') {
			sh "${mvnHome}/bin/mvn -B -Dmaven.test.failure.ignore clean test-compile surefire:test"
			junit '**/TEST-*Test.xml'
		}

		stage ('Integration test') {
			sh "${mvnHome}/bin/mvn -B -Dmaven.test.failure.ignore clean test-compile failsafe:integration-test"
			junit '**/failsafe-reports/TEST-*.xml'
		}
		
		stage ('Quality') {
			sh "${mvnHome}/bin/mvn site -Dmaven.test.skip=true"
			step([$class: 'FindBugsPublisher'])
			step([$class: 'CheckStylePublisher'])
			step([$class: 'AnalysisPublisher'])
			step([$class: 'JavadocArchiver', javadocDir: 'mariageRest/target/site/apidocs', keepAll: false])
		}
		
		stage ('Archive') {
			archiveArtifacts artifacts: 'mariageRest.war', fingerprint: true
		}
	}

	promotionManuelle ('Go to production?') {
		node {
			stage ('Production') {
				sh "/var/lib/mariage/stopMariage.sh"
				sh "rm /var/lib/mariage/*.war || true"
				sh "cp ./mariageRest.war /var/lib/mariage/"
				sh "/var/lib/mariage/startMariage.sh"
			}
		}
	}
}

/* Fonctions utilitaires */

// Gestion des erreurs 
def pipelineAvecMailSiUnstable(String destinataireEmail, Closure pipeline) { try { pipeline.call(); } catch (Exception e) { node { emailext subject: "${env.JOB_NAME}#${env.BUILD_NUMBER} - Error during the build !", to: destinataireEmail, body: "Erreur : ${e}"; }; throw e;} };

// Extraction du numero de version depuis le pom.xml present a la racine de la branche
def version() { def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'; matcher ? matcher[0][1] : null; };

// Gestion de la promotion manuelle
def promotionManuelle(messagePromotion, bloc) { Boolean promotion = false; stage ('promotionManuelle') { try { timeout(time:1, unit:'DAYS') { input message: messagePromotion; }; promotion = true; } catch(err) {} }; if (promotion) { bloc(); } }

/* Variables disponibles : env.PATH, env.BUILD_TAG, env.BRANCH_NAME, currentBuild.result, currentBuild.displayName, currentBuild.description */

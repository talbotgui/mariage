#!groovy

node {
    stage 'Checkout'
    git url: 'https://github.com/talbotgui/mariage.git'
    def mvnHome = tool 'M3'

    stage 'Build'
    sh "${mvnHome}/bin/mvn clean install -Dmaven.test.skip=true"
    sh "cp mariageRest/target/mariageRest-*.war ./mariageRest.war"

    stage 'Unit test'

    stage 'Integration test'
    
    stage 'Quality'

}

stage 'Approve'
timeout(time:1, unit:'DAYS') {
	input message:'Go to production?'
}

if (currentBuild.result == null) {
	currentBuild.result = 'SUCCESS'
}

node {
    stage 'Production'
    sh "/var/lib/mariage/stopMariage.sh"
    sh "rm /var/lib/mariage/*.war || true"
    sh "cp ./mariageRest.war /var/lib/mariage/"
    sh "/var/lib/mariage/startMariage.sh"	
}


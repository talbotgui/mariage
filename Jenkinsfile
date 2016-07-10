node {
    stage 'Checkout'
    git url: 'https://github.com/talbotgui/mariage.git'
    def mvnHome = tool 'M3'

    stage 'Build'
    sh "${mvnHome}/bin/mvn clean install -Dmaven.test.skip=true"
    sh "cp mariageRest/target/mariageRest-*.war ./mariageRest.war"

    stage 'Unit test'
    sh "${mvnHome}/bin/mvn clean test-compile surefire:test"
    step([$class: 'JUnitResultArchiver', testResults: '**/TEST-*Test.xml'])

    stage 'Integration test'
    sh "${mvnHome}/bin/mvn clean test-compile failsafe:integration-test"
    step([$class: 'JUnitResultArchiver', testResults: '**/failsafe-reports/TEST-*.xml'])
    
    stage 'Quality'
    sh "${mvnHome}/bin/mvn site -Dmaven.test.skip=true"
    step([$class: 'FindBugsPublisher'])
    step([$class: 'CheckStylePublisher'])
    step([$class: 'AnalysisPublisher'])
    step([$class: 'JavadocArchiver', javadocDir: 'mariageRest/target/site/apidocs', keepAll: false])

    stage 'Approve'
    timeout(time:1, unit:'DAYS') {
        input message:'Go to production?'
    }

    stage 'Production'
    sh "/var/lib/mariage/stopMariage.sh"
    sh "rm /var/lib/mariage/*.war || true"
    sh "cp ./mariageRest.war /var/lib/mariage/"
    sh "/var/lib/mariage/startMariage.sh"
}
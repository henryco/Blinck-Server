pipeline {
  agent any
  stages {
    stage('Check and Prepare') {
      steps {
        sh 'rm -f src/main/resources/application.properties'
        sh 'cp /home/deploy-props/Blinck-Server/application.properties src/main/resources/application.properties'
        sh 'gradle check -x build -x test --stacktrace'
      }
    }
    stage('Build') {
      steps {
        sh 'gradle test --stacktrace'
        sh 'gradle build -x test --stacktrace'
      }
    }
    stage('Prepare results') {
      steps {
        archiveArtifacts(artifacts: 'build/libs/*', onlyIfSuccessful: true)
        junit 'build/reports/**/*'
        junit 'build/reports/*'
        junit 'build/test-results/*.xml'
      }
    }
  }
  post {
    always {
      junit(testResults: 'build/reports/**/*', allowEmptyResults: true)
      junit(testResults: 'build/test-results/*', allowEmptyResults: true)
      sh '(pkill -f gradle) || true'
      
    }
    
  }
}
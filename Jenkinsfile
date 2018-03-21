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
        sh 'gradle build --stacktrace'
      }
    }
    stage('Prepare results') {
      parallel {
        stage('Artifacts') {
          steps {
            archiveArtifacts(artifacts: 'build/libs/*', onlyIfSuccessful: true)
          }
        }
        stage('Tests') {
          steps {
            junit(testResults: 'build/reports/tests/*', allowEmptyResults: true)
            junit(testResults: 'build/test-results/*.xml', allowEmptyResults: true)
          }
        }
      }
    }
    stage('Clean') {
      steps {
        sh '(pkill -f gradle) || true'
      }
    }
  }
}
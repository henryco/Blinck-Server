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
    
    stage('test') {
      steps {
        sh '(gradle test --stacktrace) || true'
        junit 'build/test-results/*.xml'
        archiveArtifacts 'build/reports/**/*'
      }
    }
    
    stage('Build') {
      steps {
        sh 'gradle build --stacktrace'
      }
    }
    
    stage('Prepare artifacts') {
      steps {
        archiveArtifacts(artifacts: 'build/libs/*', onlyIfSuccessful: true)
      }
    }
    
    stage('Clean') {
      steps {
        sh 'gradle clean'
        sh '(pkill -f gradle) || true'
      }
    }
    
  }
}

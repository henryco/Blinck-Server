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
        junit 'build/reports/**/*'
        junit 'build/reports/*'
        junit 'build/test-results/*.xml'
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
  }
  
  post {
    always {
      junit 'build/reports/**/*'
      junit 'build/reports/*'
      junit 'build/test-results/*.xml'
     
      archiveArtifacts 'build/reports/*'
      archiveArtifacts 'build/test-results/*.xml'
      
      sh 'gradle clean'
      sh '(pkill -f gradle) || true'
    }
    
  }
}

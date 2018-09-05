pipeline {
  agent any
  stages {
    stage('Check and Prepare') {
      steps {
	sh 'gradle clean'
        sh 'rm -f src/main/resources/application.properties'
	sh 'rm -f src/main/resources/static/props/base.properties'
        sh 'cp /home/deploy-props/Blinck-Server/application.properties src/main/resources/application.properties'
	sh 'cp /home/deploy-props/Blinck-Server/base.properties src/main/resources/static/props/base.properties'
        sh 'gradle check -x build -x test --stacktrace'
      }
    }
    stage('Test') {
      steps {
        sh 'gradle test --stacktrace'
      }
    }
    stage('Build') {
      steps {
        sh 'gradle build -x test --stacktrace'
      }
    }
    stage('Prepare artifacts') {
      steps {
        archiveArtifacts(artifacts: 'build/libs/*', onlyIfSuccessful: true)
      }
    }
    stage('Clean') {
      steps {
        sh '(pkill -f gradle) || true'
      }
    }
  }

  post {
    always {
      sh '(pkill -f gradle) || true'

      junit 'build/test-results/*.xml'
      sh 'rm -f -r test-arch'
      sh 'mkdir test-arch'
      sh 'zip -r test-arch/test-report.zip build/reports'
      archiveArtifacts 'test-arch/*.zip'
    }
  }
}

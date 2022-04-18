node {
def buildNumber = BUILD_NUMBER

//Clonig my Git repo
stage("GitClone") {
 git credentialsId: '0e5ba7ec-4403-4d88-8aa8-5726ef20c864', url: 
'https://github.com/SUBHANSHAIK212/mavenrepo.git'
}

//Build Source Code 
 stage("MavenBuild") {
 def mavenHome= tool name: "maven",type: "maven"
 sh "${mavenHome}/bin/mvn clean package"
 }

// Build image using docker 
stage("Docker Build Image") {
 sh "docker build -t subhan2121/docker-depolyment-java-web-job:${buildNumber} ."
 }

//Logining to DockerHub Registry 
stage("Docker Login And Push") {
 withCredentials([string(credentialsId: 'docker_hub_pwd', variable: 'docker_hub_pwd')]) {
 sh "docker login -u subhan2121 -p ${docker_hub_pwd}"
   }
 sh "docker push subhan2121/docker-depolyment-java-web-job:${buildNumber}"
      }
 
 //Deploying my Application as Continer 
 stage("Deploy Application As Docker Container"){
 sshagent(['7e96d891-1121-402f-a750-c284300d1099']) {
 sh "ssh -o StrictHostKeyChecking=no ubuntu@13.126.51.78 docker rm -f 
dockerdepolymentjavawebjobcontainer || true"
 sh "ssh -o StrictHostKeyChecking=no ubuntu@13.126.51.78 docker run -d -p 8080:8080 --name 
dockerjavacontainer subhan2121/docker-depolyment-java-web-job:${buildNumber}"
   }
      }

//Here starting the alert Via email notified if any issue raised 
post {
  always {
    // One or more steps need to be included within each condition's block.
    emailtext body: '''build is over... always
Regards
subhan shaik
''', subject: 'Build is over', to: 'sksubhani2121@gmail.com'
  }
  aborted {
    // One or more steps need to be included within each condition's block.
    emailext body: '''build is over...aborted
Regards
subhan shaik
''', subject: 'Build is over', to: 'sksubhani2121@gmail.com'
  }
  success {
    // One or more steps need to be included within each condition's block.
    emailext body: '''build is over... success
Regards
subhan shaik
''', subject: 'Build is over', to: 'sksubhani2121@gmail.com'
  }
  failure {
    // One or more steps need to be included within each condition's block.
    emailext body: '''build is over...failure
Regards
subhan shaik
''', subject: 'Build is over', to: 'sksubhani2121@gmail.com'
  }
}
 
}//Node closing
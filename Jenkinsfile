pipeline {

    environment {
        registry = "nikolancaid/auth-service"
        registryCredential = 'dockerhub'
        dockerImage = ''

    }
    agent any

    stages {
        stage('Git clone') {
            steps {
                git 'https://github.com/NHristovski/WP-Project-AuthService.git'
            }
        }

        stage('Building docker image') {
            steps {
                script {
                    dockerImage = docker.build registry + ":latest"
                }
            }
        }

        stage('Deploy Image') {
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
        }

        stage('Remove Unused docker image') {
            steps {
                sh "docker rmi $registry:latest"
            }
        }


        stage('Pull docker image on host') {
            steps {
                sh 'ssh -o StrictHostKeyChecking=no nhristov@10.10.10.57 "cat ~/docker_password.txt | docker login --username nikolancaid --password-stdin"'

                script {
                    try {
                        sh 'ssh -o StrictHostKeyChecking=no nhristov@10.10.10.57 "docker rm -f auth-service"'
                    } catch (Exception e) {
                        sh 'echo "Failed to delete container auth-service"'
                    }
                }

                sh 'ssh -o StrictHostKeyChecking=no nhristov@10.10.10.57 "docker pull nikolancaid/auth-service:latest"'
            }
        }

        stage('Start the application'){
            steps {
                sh 'ssh -o StrictHostKeyChecking=no nhristov@10.10.10.57 "docker run --name auth-service -p 9100:9100 -d nikolancaid/auth-service:latest"'
            }
        }
    }
}

pipeline {
    agent {
        docker {
            image 'seimanu/jenkins-agent:ci'
            args '--network=host -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        DOCKER_IMAGE_NAME = "seimanu/matter-service"
        KUBECONFIG = credentials('my-kubeconfig')
    }
    triggers {
        pollSCM '* * * * *'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'github-creds', url: 'https://github.com/sei-manu/cdas-project.git'
            }
        }
        stage('Build') {
            steps {
                echo "Building..."
                sh 'docker --version'
            }
        }
        stage('Test') {
            steps {
                echo "Testing..."
            }
        }

        stage('Docker Build & Push') {
            steps {
                dir('matter-service') {
                    script {
                        sh "docker build -t $DOCKER_IMAGE_NAME:${BUILD_NUMBER} -t $DOCKER_IMAGE_NAME:latest ."
                        withDockerRegistry([credentialsId: 'dockerhub-creds']) {
                            sh "docker push $DOCKER_IMAGE_NAME:${BUILD_NUMBER}"
                            sh "docker push $DOCKER_IMAGE_NAME:latest"
                        }
                    }
                }
            }
        }

        // stage('Deploy to Kubernetes') {
        //     steps {
        //         echo "Deploying to Kubernetes..."
        //         // sh "kubectl --context $KUBE_CONTEXT apply -f k8s/deployment.yaml"
        //         sh """
        //         kubectl apply -f k8s/deployment.yaml
        //         kubectl set image deployment/cdas-project matter-service=$DOCKER_IMAGE_NAME:latest --namespace cdas-project
        //         """
        //     }
        // }
    }
}
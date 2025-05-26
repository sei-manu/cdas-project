pipeline {
    agent {
        docker {
        image 'seimanu/jenkins-agent:ci'
        args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        DOCKER_IMAGE = "seimanu/matter-service:${BUILD_NUMBER}"
        // KUBE_CONTEXT = 'my-kube-context'
    }

    triggers {
        pollSCM '* * * * *'
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: '4b190375-bf65-4746-b2d8-ee24a916ebc3', url: 'https://github.com/sei-manu/cdas-project.git'
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
                        sh "docker build -t $DOCKER_IMAGE ."
                        withDockerRegistry([credentialsId: '89c50cde-5b56-48e7-9204-dbeb99868623']) {
                            sh "docker push $DOCKER_IMAGE"
                        }
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo "Deploying to Kubernetes..."
                // sh "kubectl --context $KUBE_CONTEXT apply -f k8s/deployment.yaml"
            }
        }
    }

    post {
        failure {
            echo "Deployment failed. Rolling back..."
            sh 'kubectl rollout undo deployment/matter-service'
        }
    }
}
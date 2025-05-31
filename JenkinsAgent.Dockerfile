FROM jenkins/agent:latest-jdk21

USER root

# Install Docker CLI
RUN apt-get update && apt-get install -y \
    docker.io \
    curl \
    git \
    bash \
    && rm -rf /var/lib/apt/lists/*

# Install kubectl
RUN curl -LO "https://dl.k8s.io/release/v1.30.0/bin/linux/amd64/kubectl" && \
    install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl && \
    rm kubectl

# Add kubeconfig and certs
COPY /k8s/jenkins-kubeconfig /home/jenkins/.kube/config
COPY /k8s/files/minikube-ca.crt /home/jenkins/.kube/minikube-ca.crt
COPY /k8s/files/minikube-client.crt /home/jenkins/.kube/minikube-client.crt
COPY /k8s/files/minikube-client.key /home/jenkins/.kube/minikube-client.key

RUN chown -R jenkins:jenkins /home/jenkins/.kube

USER jenkins
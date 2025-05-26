FROM gradle:8.5-jdk21

USER root

# Install Docker CLI
RUN apt-get update && apt-get install -y \
    docker.io \
    curl \
    git \
    unzip \
    bash \
    && rm -rf /var/lib/apt/lists/*

# Install kubectl
#RUN curl -LO "https://dl.k8s.io/release/$(curl -sL https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" \
#    && install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

USER gradle

# docker build -t jenkins-agent:ci -f JenkinsAgent.Dockerfile .

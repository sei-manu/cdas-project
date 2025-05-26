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

USER jenkins
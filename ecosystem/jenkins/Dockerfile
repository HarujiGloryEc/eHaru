FROM jenkins/jenkins:lts-jdk21

USER root

# Install Node.js 20 and Maven
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    maven \
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install Docker CLI (for pipeline docker builds)
RUN curl -fsSL https://get.docker.com | sh

USER jenkins

# Pre-install recommended Jenkins plugins
RUN jenkins-plugin-cli --plugins \
    git \
    workflow-aggregator \
    pipeline-stage-view \
    docker-workflow \
    nodejs \
    jacoco \
    sonar \
    junit \
    blueocean

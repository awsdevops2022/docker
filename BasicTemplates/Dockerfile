FROM ubuntu:${imageVersion}
ARG imageVersion=18.04
RUN apt-get upgrade -y && \
    apt-get update -y && \
    apt-get install -y git 
CMD ["sh", "-c", "git --version"]
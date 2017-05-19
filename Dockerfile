FROM isuper/java-oracle:jdk_latest

COPY pom.xml /app/pom.xml

# Install some basic network tools
RUN apt-get update && apt-get install -y net-tools iproute iputils-ping dnsutils

# Install maven
RUN apt-get update && apt-get install -y --no-install-recommends maven

RUN groupadd -r hwms && useradd -r -m -g hwms hwms

WORKDIR /app

# Prepare by downloading dependencies
COPY pom.xml /app/pom.xml
COPY HelloWorldMicroserviceCommon/pom.xml /app/HelloWorldMicroserviceCommon/pom.xml
COPY HelloWorldMicroserviceCommon/src /app/HelloWorldMicroserviceCommon/src
COPY HelloWorldMicroserviceService/pom.xml /app/HelloWorldMicroserviceService/pom.xml
COPY HelloWorldMicroserviceService/src /app/HelloWorldMicroserviceService/src
COPY HelloWorldMicroserviceWeb/pom.xml /app/HelloWorldMicroserviceWeb/pom.xml
COPY HelloWorldMicroserviceWeb/src /app/HelloWorldMicroserviceWeb/src

RUN chown -R hwms. /app

USER hwms

# Compile and package all projects
RUN mvn package
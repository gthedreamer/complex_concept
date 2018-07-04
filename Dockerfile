# For reference only

FROM websphere-liberty
ADD target/CompoundConcept.war /opt/ibm/wlp/usr/servers/defaultServer/dropins/
ENV LICENSE accept
EXPOSE 9080

## Running the container locally
# mvn clean install
# docker build -t compoundconcept:latest .
# docker run -d --name myjavacontainer compoundconcept
# docker run -p 9080:9080 --name myjavacontainer compoundconcept
# Visit http://localhost:9080/

## Push container to IBM Cloud
# Install cli and dependencies: https://console.ng.bluemix.net/docs/containers/container_cli_cfic_install.html#container_cli_cfic_install
# docker tag compoundconcept:latest registry.ng.bluemix.net/<my_namespace>/compoundconcept:latest
# docker push registry.ng.bluemix.net/<my_namespace>/compoundconcept:latest
# bx ic images # Verify new image

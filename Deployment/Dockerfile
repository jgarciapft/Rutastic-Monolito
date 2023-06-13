FROM tomcat:9.0.31-jdk8-corretto
LABEL authors="Juan Pablo García Plaza Pérez - jgarciapft@alumnos.unex.es"

ENV HOME /root
ENV CATALINA_HOME /usr/local/tomcat

WORKDIR $HOME/sqliteDB
COPY JPGPP.db .

WORKDIR $HOME
COPY .keystore .

WORKDIR $CATALINA_HOME/conf
COPY server.xml .

WORKDIR $CATALINA_HOME/webapps
COPY Rutastic-Monolito_war.war .
RUN mv Rutastic-Monolito_war.war Rutastic.war

EXPOSE 8443/tcp

WORKDIR $CATALINA_HOME
ENTRYPOINT ["catalina.sh", "run"]
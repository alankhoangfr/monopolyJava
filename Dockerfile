FROM alpine
WORKDIR /root/monopoly
COPY *.java /root/monopoly/

#Install JDK
RUN apk add openjdk8
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:$JAVA_HOME/bin

# Compile
RUN javac *.java

ENTRYPOINT java Main:

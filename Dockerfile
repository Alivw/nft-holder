FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER java@cryptopayments.io

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime


WORKDIR /java

VOLUME /java/config

ADD ./build/libs/nft-holder-1.0-SNAPSHOT.jar ./app.jar

CMD java -Djava.security.egd=file:/dev/./urandom -jar -Xmx2g -Xms2g app.jar
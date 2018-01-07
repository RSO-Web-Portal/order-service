FROM openjdk:8

RUN mkdir /order

WORKDIR /order

COPY . ./order
ADD . /order

EXPOSE 8088

CMD ["java", "-jar", "target/order-service-2.5.0-SNAPSHOT.jar"]
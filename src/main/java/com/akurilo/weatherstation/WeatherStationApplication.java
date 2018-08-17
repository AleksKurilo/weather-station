package com.akurilo.weatherstation;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WeatherStationApplication implements CommandLineRunner {

    final ActorSystem actorSystem = ActorSystem.create("actorSystem");

    public static void main(String[] args) {
        SpringApplication.run(WeatherStationApplication.class, args);
    }

    @Override
    public void run(String... args) {


        try {
            final ActorRef slaveActor = actorSystem.actorOf(SlaveActor.props());
            final ActorRef masterActor = actorSystem.actorOf(MasterActor.props(slaveActor));

            masterActor.tell(new MessageDto("Hello Akka"), ActorRef.noSender());

            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ioe) {
        } finally {
            actorSystem.terminate();
        }

    }
}

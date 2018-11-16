package com.akurilo.weatherstation;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import com.akurilo.weatherstation.actor_system.MasterActor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scala.concurrent.duration.Duration;


@SpringBootApplication
public class Application {

    public static final ActorSystem ACTOR_SYSTEM = ActorSystem.create("ClusterSystem");// actor_system
    public static final Timeout TIMEOUT_GET_MESSAGE = new Timeout(Duration.create(100, "seconds"));
    public static ActorRef masterActor = ACTOR_SYSTEM.actorOf(MasterActor.props(), "masterActor");


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

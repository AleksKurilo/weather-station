package com.akurilo.weatherstation.controller;

import akka.actor.ActorRef;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.dto.CenterDto;
import com.akurilo.weatherstation.enums.RequestType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static com.akurilo.weatherstation.Application.ACTOR_SYSTEM;
import static com.akurilo.weatherstation.Application.TIMEOUT_GET_MESSAGE;

@Controller
@RequestMapping(path = "/centre")
public class CenterController {

    @PostMapping(path = "/create")
    @ResponseBody
    public CompletableFuture<Object> create(@RequestBody CenterDto centerDto) {
        centerDto.setRequestType(RequestType.POST);
        final ActorRef masterActor = ACTOR_SYSTEM.actorOf(MasterActor.props());
        return ask(masterActor, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
    }

    @PutMapping(path = "/update")
    @ResponseBody
    public CompletableFuture<Object> update(@RequestBody CenterDto centerDto) {
        centerDto.setRequestType(RequestType.PUT);
        final ActorRef masterActor = ACTOR_SYSTEM.actorOf(MasterActor.props());
        return ask(masterActor, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public CompletableFuture<Object> getById(@PathVariable Long id) {
        CenterDto centerDto = new CenterDto();
        centerDto.setId(id);
        centerDto.setRequestType(RequestType.GET);
        final ActorRef masterAcror = ACTOR_SYSTEM.actorOf(MasterActor.props());
        return ask(masterAcror, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
    }

    @GetMapping(path = "/list")
    @ResponseBody
    public CompletableFuture<Object> getList() {
        CenterDto centerDto = new CenterDto();
        centerDto.setMessage("Akka loves you");
        centerDto.setRequestType(RequestType.GET_LIST);
        final ActorRef masterActor = ACTOR_SYSTEM.actorOf(MasterActor.props());
        return ask(masterActor, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
    }
}

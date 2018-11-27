package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.AbstractActor;
import com.akurilo.weatherstation.entity.BaseEntity;
import com.akurilo.weatherstation.service.ApplicationContextService;
import com.akurilo.weatherstation.service.Service;
import enums.RequestType;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class BaseServiceActor<T extends Service, V extends BaseEntity> extends AbstractActor {

    private final Class<T> type;
    @Getter
    private T service = null;

    @SuppressWarnings("unchecked")
    public BaseServiceActor() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.service = ApplicationContextService.getApplicationContext().getBean(type);
    }

    public List<V> actions(V entity, RequestType requestType) {
        switch (requestType) {
            case POST:
                List<V> responsePost = new ArrayList<>();
                Optional<V> savedEntity = service.create(entity);
                savedEntity.ifPresent(o -> responsePost.add(savedEntity.get()));
                return responsePost;
            case PUT:
                List<V> responsePut = new ArrayList<>();
                Optional<V> updatedEntity = service.update(entity);
                updatedEntity.ifPresent(o -> responsePut.add(updatedEntity.get()));
                return responsePut;
            case GET:
                List<V> responseGet = new ArrayList<>();
                Optional<V> gotEntity = service.getById(entity.getId());
                gotEntity.ifPresent(o -> responseGet.add(gotEntity.get()));
                return responseGet;
            case GET_LIST:
                Stream<V> gotListEntity = service.getList();
                return gotListEntity.collect(Collectors.toList());
            case DELETE:
                List<V> responseDel = new ArrayList<>();
                Optional<V> deletedEntity = service.delete(entity.getId());
                deletedEntity.ifPresent(o -> responseDel.add(deletedEntity.get()));
                return responseDel;
            default:
                return null;
        }
    }
}

package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.AbstractActor;
import com.akurilo.weatherstation.dto.BasetEntityDto;
import com.akurilo.weatherstation.service.ApplicationContextService;
import com.akurilo.weatherstation.service.Service;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseServiceActor<T extends Service, V extends BasetEntityDto> extends AbstractActor {

    private final Class<T> type;
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

    public List<V> actions(V message) {
        switch (message.getRequestType()) {
            case POST:
                List<V> resultPost = new ArrayList<>();
                resultPost.add((V) service.create(message));
                return resultPost;
            case PUT:
                List<V> resultPut = new ArrayList<>();
                resultPut.add((V) service.update(message));
                return resultPut;
            case GET:
                List<V> resultGet = new ArrayList<>();
                resultGet.add((V) service.getById(message.getId()));
                return resultGet;
            case GET_LIST:
                List<V> resultGetList = (List<V>) service.getList();
                return resultGetList;
            case DELETE:
                List<V> resultDel = new ArrayList<>();
                resultDel.add((V) service.delete(message.getId()));
                return resultDel;
            default:
                return null;
        }
    }
}

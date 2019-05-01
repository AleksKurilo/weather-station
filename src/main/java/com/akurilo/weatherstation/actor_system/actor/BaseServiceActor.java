package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.AbstractActor;
import com.akurilo.weatherstation.entity.BaseEntity;
import com.akurilo.weatherstation.service.ApplicationContextService;
import com.akurilo.weatherstation.service.Service;
import dto.BaseEntityDto;
import enums.RequestType;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


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

    public List<BaseEntityDto> executeRestRequest(V entity, RequestType requestType) {
        List<BaseEntityDto> response = new ArrayList<>();
        switch (requestType) {
            case POST:
                response.add(service.create(entity));
                return response;
            case PUT:
                response.add(service.update(entity));
                return response;
            case GET:
                response.add(service.getById(entity.getId()));
                return response;
            case GET_LIST:
                return service.getList();
            case DELETE:
                response.add(service.delete(entity.getId()));
                return response;
            default:
                return null;
        }
    }
}

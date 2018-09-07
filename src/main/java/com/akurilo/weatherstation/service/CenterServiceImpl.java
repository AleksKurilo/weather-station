package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.dto.CenterDto;
import com.akurilo.weatherstation.enums.RequestType;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CenterServiceImpl extends BaseService<CenterDto> implements CenterService {


    @Override
    public CenterDto create(CenterDto entity) {
        CenterDto centerDto = new CenterDto();
        centerDto.setMessage("was created");
        return centerDto;
    }

    @Override
    public CenterDto update(CenterDto entity) {
        CenterDto centerDto = new CenterDto();
        centerDto.setMessage("was update");
        return centerDto;
    }

    @Override
    public CenterDto getById(long id) {
        CenterDto centerDto = new CenterDto();
        centerDto.setMessage("get by id: " + id);
        return centerDto;
    }

    @Override
    public List<CenterDto> getList() {
        CenterDto centerDto = new CenterDto();
        centerDto.setMessage("answer from service");
        centerDto.setRequestType(RequestType.GET_LIST);
        return new ArrayList<>(Arrays.asList(centerDto));
    }

    @Override
    public CenterDto delete(long id) {
        return null;
    }
}

package com.akurilo.weatherstation.dto;

import com.akurilo.weatherstation.enums.RequestType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasetEntityDto {

    private Long id;
    private RequestType requestType;
}

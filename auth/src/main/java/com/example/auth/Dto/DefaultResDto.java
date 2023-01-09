package com.example.auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultResDto<T> {

    private int statusCode;
    private String responseMessage;
    private T data;

    public DefaultResDto(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static<T> DefaultResDto<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static<T> DefaultResDto<T> res(final int statusCode, final String responseMessage, final T t) {
        return DefaultResDto.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}

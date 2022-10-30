package com.example.test.enetiy.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestBean<T> {
    int state;
    String reason;

    public RestBean(int state, String reason) {
        this.state = state;
        this.reason = reason;
    }

    T data;

    public RestBean() {

    }
}

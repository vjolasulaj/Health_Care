package com.healthcare.signup.utils;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Response<T> {
    private T body;
    private List<String> errors;
    private HttpStatus status;
}

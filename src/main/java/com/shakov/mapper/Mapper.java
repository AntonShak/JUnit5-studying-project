package com.shakov.mapper;

public interface Mapper<F, T> {

    T map(F object);
}

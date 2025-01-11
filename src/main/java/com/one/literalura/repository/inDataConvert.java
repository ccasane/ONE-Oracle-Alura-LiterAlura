package com.one.literalura.repository;

public interface inDataConvert {
    <T> T dataConvert(String json, Class<T> clazz);
}
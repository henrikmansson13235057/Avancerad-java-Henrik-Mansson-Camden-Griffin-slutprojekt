package com.example.BackendList.service;

import java.util.List;

public interface BaseService<T> {
    List<T> getAll();
    T add(T item);
    T update(int id, T item);
    boolean delete(int id);
}
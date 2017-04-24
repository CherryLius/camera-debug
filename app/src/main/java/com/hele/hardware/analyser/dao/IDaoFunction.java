package com.hele.hardware.analyser.dao;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface IDaoFunction<T> {
    long add(T data);

    void delete(long id);

    void delete(T data);

    void update(T data);

    T query(long id);

    long getTotalCount();

    List<T> getAll();

    void deleteAll();
}

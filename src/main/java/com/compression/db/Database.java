package com.compression.db;

import java.sql.Connection;

public interface Database {
    void create(Object model, Schema type);
    Object read(String hash);
    void insert(Object model);
}

package org.tu.varna.repositories;

import java.util.Collection;

public interface Repository<T> {
    public void save(T object);
    public void update(T object);
    public void delete(T object);
    public Collection<T> find(T template);
}

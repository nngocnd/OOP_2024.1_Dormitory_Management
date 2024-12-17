package DAO;

import java.util.List;

public interface Search<T> {
    List<T> searchByName(String name);
}

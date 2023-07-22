package com.example.REDAFI.models;
import java.util.List;

public class UserResponse {
    private int page;
    private int perPage;
    private int totalItems;
    private int totalPages;
    private List<User> items;

    // Agrega getters y setters para los atributos

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }
}

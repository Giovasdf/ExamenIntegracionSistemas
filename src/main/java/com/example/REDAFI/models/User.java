package com.example.REDAFI.models;

public class User {
    private String id;
    private String username;
    private String nombre;
    private String apellido;

    private String rut;
    // Agrega más atributos según los datos que obtengas de la API
    // Asegúrate de que los tipos de datos sean correctos para cada atributo

    // Agrega getters y setters para los atributos

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }
}

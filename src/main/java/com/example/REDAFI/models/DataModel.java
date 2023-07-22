package com.example.REDAFI.models;


public class DataModel {
    private String tipoCuenta;
    private String usuario;
    private String numeroCta;

    // Constructor
    public DataModel(String tipoCuenta, String usuario, String numeroCta) {
        this.tipoCuenta = tipoCuenta;
        this.usuario = usuario;
        this.numeroCta = numeroCta;
    }

    // Getters y Setters (puedes generarlos automáticamente en tu IDE)

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNumeroCta() {
        return numeroCta;
    }

    public void setNumeroCta(String numeroCta) {
        this.numeroCta = numeroCta;
    }
}

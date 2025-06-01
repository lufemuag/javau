package com.ferreteria.rf.models;

public class Client {
    private int id;
    private String cedula;
    private String nombre;
    private String direccion;
    private String telefono;
    private String createdAt;
    
    public Client() {}
    
    public Client(String cedula, String nombre, String direccion, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }
    
    public Client(int id, String cedula, String nombre, String direccion, String telefono, String createdAt) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getId() { return id; }
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCreatedAt() { return createdAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return nombre + " - " + cedula;
    }
}
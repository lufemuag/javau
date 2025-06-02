package com.ferreteria.rf.models;

public class Order {
    private int id;
    private String codigo;
    private String descripcion;
    private double total;
    private int clientId;
    private String estado;
    private String createdAt;

    public Order() {}

    public Order(String codigo, String descripcion, double total, int clientId, String estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.total = total;
        this.clientId = clientId;
        this.estado = estado;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
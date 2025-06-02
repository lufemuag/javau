package com.ferreteria.rf.models;

public class Invoice {
    private int id;
    private String numero;
    private double total;
    private int clientId;
    private String estado;
    private String createdAt;

    public Invoice() {}

    public Invoice(String numero, double total, int clientId, String estado) {
        this.numero = numero;
        this.total = total;
        this.clientId = clientId;
        this.estado = estado;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
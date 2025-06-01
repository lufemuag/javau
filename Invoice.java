package com.ferreteria.rf.models;

public class Invoice {
    private int id;
    private String numero;
    private String fecha;
    private double total;
    private int clientId;
    private String estado;
    private String createdAt;
    
    // Relaciones
    private String clienteNombre;
    
    public Invoice() {}
    
    public Invoice(String numero, double total, int clientId, String estado) {
        this.numero = numero;
        this.total = total;
        this.clientId = clientId;
        this.estado = estado;
    }
    
    public Invoice(int id, String numero, String fecha, double total, int clientId, String estado, String createdAt) {
        this.id = id;
        this.numero = numero;
        this.fecha = fecha;
        this.total = total;
        this.clientId = clientId;
        this.estado = estado;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getId() { return id; }
    public String getNumero() { return numero; }
    public String getFecha() { return fecha; }
    public double getTotal() { return total; }
    public int getClientId() { return clientId; }
    public String getEstado() { return estado; }
    public String getCreatedAt() { return createdAt; }
    public String getClienteNombre() { return clienteNombre; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setNumero(String numero) { this.numero = numero; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setTotal(double total) { this.total = total; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    
    @Override
    public String toString() {
        return numero + " - $" + total;
    }
}
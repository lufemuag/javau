package com.ferreteria.rf.models;

public class Product {
    private int id;
    private String codigo;
    private String descripcion;
    private double valor;
    private String categoria;
    private int stock;
    private String createdAt;
    
    public Product() {}
    
    public Product(String codigo, String descripcion, double valor, String categoria, int stock) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.valor = valor;
        this.categoria = categoria;
        this.stock = stock;
    }
    
    public Product(int id, String codigo, String descripcion, double valor, String categoria, int stock, String createdAt) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.valor = valor;
        this.categoria = categoria;
        this.stock = stock;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public double getValor() { return valor; }
    public String getCategoria() { return categoria; }
    public int getStock() { return stock; }
    public String getCreatedAt() { return createdAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setValor(double valor) { this.valor = valor; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setStock(int stock) { this.stock = stock; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return descripcion + " - " + codigo;
    }
}
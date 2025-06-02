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

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
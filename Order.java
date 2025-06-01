package com.ferreteria.rf.models;

public class Order {
    private int id;
    private String codigo;
    private String descripcion;
    private String fecha;
    private int cantidad;
    private int clientId;
    private int productId;
    private String estado;
    private String createdAt;
    
    // Relaciones
    private String clienteNombre;
    private String productoDescripcion;
    
    public Order() {}
    
    public Order(String codigo, String descripcion, int cantidad, int clientId, int productId, String estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.clientId = clientId;
        this.productId = productId;
        this.estado = estado;
    }
    
    public Order(int id, String codigo, String descripcion, String fecha, int cantidad, 
                 int clientId, int productId, String estado, String createdAt) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.clientId = clientId;
        this.productId = productId;
        this.estado = estado;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public String getFecha() { return fecha; }
    public int getCantidad() { return cantidad; }
    public int getClientId() { return clientId; }
    public int getProductId() { return productId; }
    public String getEstado() { return estado; }
    public String getCreatedAt() { return createdAt; }
    public String getClienteNombre() { return clienteNombre; }
    public String getProductoDescripcion() { return productoDescripcion; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public void setProductoDescripcion(String productoDescripcion) { this.productoDescripcion = productoDescripcion; }
    
    @Override
    public String toString() {
        return codigo + " - " + descripcion;
    }
}
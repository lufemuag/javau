package com.ferreteria.rf.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ferreteria.rf.database.DatabaseHelper;
import com.ferreteria.rf.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private DatabaseHelper dbHelper;
    
    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    public long insertOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_ORDER_CODIGO, order.getCodigo());
        values.put(DatabaseHelper.COLUMN_ORDER_DESCRIPCION, order.getDescripcion());
        values.put(DatabaseHelper.COLUMN_ORDER_CANTIDAD, order.getCantidad());
        values.put(DatabaseHelper.COLUMN_ORDER_CLIENT_ID, order.getClientId());
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID, order.getProductId());
        values.put(DatabaseHelper.COLUMN_ORDER_ESTADO, order.getEstado());
        
        long result = db.insert(DatabaseHelper.TABLE_ORDERS, null, values);
        db.close();
        return result;
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT o.*, c.nombre as cliente_nombre, p.descripcion as producto_descripcion " +
                      "FROM " + DatabaseHelper.TABLE_ORDERS + " o " +
                      "LEFT JOIN " + DatabaseHelper.TABLE_CLIENTS + " c ON o.client_id = c.id " +
                      "LEFT JOIN " + DatabaseHelper.TABLE_PRODUCTS + " p ON o.product_id = p.id " +
                      "ORDER BY o.created_at DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)));
                order.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CODIGO)));
                order.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DESCRIPCION)));
                order.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_FECHA)));
                order.setCantidad(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CANTIDAD)));
                order.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CLIENT_ID)));
                order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID)));
                order.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ESTADO)));
                order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CREATED_AT)));
                
                // Datos relacionados
                order.setClienteNombre(cursor.getString(cursor.getColumnIndexOrThrow("cliente_nombre")));
                order.setProductoDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("producto_descripcion")));
                
                orders.add(order);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return orders;
    }
    
    public Order getOrderById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Order order = null;
        
        String query = "SELECT o.*, c.nombre as cliente_nombre, p.descripcion as producto_descripcion " +
                      "FROM " + DatabaseHelper.TABLE_ORDERS + " o " +
                      "LEFT JOIN " + DatabaseHelper.TABLE_CLIENTS + " c ON o.client_id = c.id " +
                      "LEFT JOIN " + DatabaseHelper.TABLE_PRODUCTS + " p ON o.product_id = p.id " +
                      "WHERE o.id = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        
        if (cursor.moveToFirst()) {
            order = new Order();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)));
            order.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CODIGO)));
            order.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DESCRIPCION)));
            order.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_FECHA)));
            order.setCantidad(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CANTIDAD)));
            order.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CLIENT_ID)));
            order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID)));
            order.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ESTADO)));
            order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CREATED_AT)));
            
            order.setClienteNombre(cursor.getString(cursor.getColumnIndexOrThrow("cliente_nombre")));
            order.setProductoDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("producto_descripcion")));
        }
        
        cursor.close();
        db.close();
        return order;
    }
    
    public int updateOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_ORDER_CODIGO, order.getCodigo());
        values.put(DatabaseHelper.COLUMN_ORDER_DESCRIPCION, order.getDescripcion());
        values.put(DatabaseHelper.COLUMN_ORDER_CANTIDAD, order.getCantidad());
        values.put(DatabaseHelper.COLUMN_ORDER_CLIENT_ID, order.getClientId());
        values.put(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID, order.getProductId());
        values.put(DatabaseHelper.COLUMN_ORDER_ESTADO, order.getEstado());
        
        int result = db.update(DatabaseHelper.TABLE_ORDERS, values, 
                              DatabaseHelper.COLUMN_ORDER_ID + " = ?", 
                              new String[]{String.valueOf(order.getId())});
        db.close();
        return result;
    }
    
    public int deleteOrder(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DatabaseHelper.TABLE_ORDERS, 
                              DatabaseHelper.COLUMN_ORDER_ID + " = ?", 
                              new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
    
    public int getActiveOrdersCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_ORDERS + 
                      " WHERE " + DatabaseHelper.COLUMN_ORDER_ESTADO + " = 'pendiente'";
        Cursor cursor = db.rawQuery(query, null);
        
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}
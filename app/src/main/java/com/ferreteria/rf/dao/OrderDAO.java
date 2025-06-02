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
                order.setCantidad(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CANTIDAD)));
                order.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CLIENT_ID)));
                order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID)));
                order.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ESTADO)));

                // Set cliente and producto names if available
                int clienteNombreIndex = cursor.getColumnIndex("cliente_nombre");
                if (clienteNombreIndex >= 0) {
                    order.setClienteNombre(cursor.getString(clienteNombreIndex));
                }

                int productoDescripcionIndex = cursor.getColumnIndex("producto_descripcion");
                if (productoDescripcionIndex >= 0) {
                    order.setProductoDescripcion(cursor.getString(productoDescripcionIndex));
                }

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
                      "WHERE o." + DatabaseHelper.COLUMN_ORDER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            order = new Order();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)));
            order.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CODIGO)));
            order.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DESCRIPCION)));
            order.setCantidad(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CANTIDAD)));
            order.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CLIENT_ID)));
            order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID)));
            order.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ESTADO)));

            int clienteNombreIndex = cursor.getColumnIndex("cliente_nombre");
            if (clienteNombreIndex >= 0) {
                order.setClienteNombre(cursor.getString(clienteNombreIndex));
            }

            int productoDescripcionIndex = cursor.getColumnIndex("producto_descripcion");
            if (productoDescripcionIndex >= 0) {
                order.setProductoDescripcion(cursor.getString(productoDescripcionIndex));
            }
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

    public int getActiveOrders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_ORDERS + 
                           " WHERE " + DatabaseHelper.COLUMN_ORDER_ESTADO + " = 'pendiente'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getActiveOrdersCount() {
        return getActiveOrders();
    }

    public List<Order> searchOrders(String query) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuery = "SELECT o.*, c.nombre as cliente_nombre, p.descripcion as producto_descripcion " +
                         "FROM " + DatabaseHelper.TABLE_ORDERS + " o " +
                         "LEFT JOIN " + DatabaseHelper.TABLE_CLIENTS + " c ON o.client_id = c.id " +
                         "LEFT JOIN " + DatabaseHelper.TABLE_PRODUCTS + " p ON o.product_id = p.id " +
                         "WHERE o.codigo LIKE ? OR o.descripcion LIKE ? OR c.nombre LIKE ? " +
                         "ORDER BY o.created_at DESC";

        String[] selectionArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.rawQuery(sqlQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)));
                order.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CODIGO)));
                order.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DESCRIPCION)));
                order.setCantidad(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CANTIDAD)));
                order.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CLIENT_ID)));
                order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRODUCT_ID)));
                order.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ESTADO)));
                order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_CREATED_AT)));

                // Set related data
                int clienteNombreIndex = cursor.getColumnIndex("cliente_nombre");
                if (clienteNombreIndex != -1) {
                    order.setClienteNombre(cursor.getString(clienteNombreIndex));
                }

                int productoDescripcionIndex = cursor.getColumnIndex("producto_descripcion");
                if (productoDescripcionIndex != -1) {
                    order.setProductoDescripcion(cursor.getString(productoDescripcionIndex));
                }

                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }
}
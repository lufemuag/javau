package com.ferreteria.rf.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ferreteria.rf.database.DatabaseHelper;
import com.ferreteria.rf.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_PRODUCT_CODIGO, product.getCodigo());
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION, product.getDescripcion());
        values.put(DatabaseHelper.COLUMN_PRODUCT_VALOR, product.getValor());
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORIA, product.getCategoria());
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK, product.getStock());

        long result = db.insert(DatabaseHelper.TABLE_PRODUCTS, null, values);
        db.close();
        return result;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCTS + 
                      " ORDER BY " + DatabaseHelper.COLUMN_PRODUCT_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)));
                product.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CODIGO)));
                product.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION)));
                product.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_VALOR)));
                product.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORIA)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Product product = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCTS + 
                      " WHERE " + DatabaseHelper.COLUMN_PRODUCT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)));
            product.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CODIGO)));
            product.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION)));
            product.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_VALOR)));
            product.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORIA)));
            product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK)));
        }

        cursor.close();
        db.close();
        return product;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_PRODUCT_CODIGO, product.getCodigo());
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION, product.getDescripcion());
        values.put(DatabaseHelper.COLUMN_PRODUCT_VALOR, product.getValor());
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORIA, product.getCategoria());
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK, product.getStock());

        int result = db.update(DatabaseHelper.TABLE_PRODUCTS, values, 
                              DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", 
                              new String[]{String.valueOf(product.getId())});
        db.close();
        return result;
    }

    public int deleteProduct(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DatabaseHelper.TABLE_PRODUCTS, 
                              DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", 
                              new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String searchQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCTS + 
                           " WHERE " + DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION + " LIKE ? OR " +
                           DatabaseHelper.COLUMN_PRODUCT_CODIGO + " LIKE ? OR " +
                           DatabaseHelper.COLUMN_PRODUCT_CATEGORIA + " LIKE ?";

        String searchParam = "%" + query + "%";
        Cursor cursor = db.rawQuery(searchQuery, new String[]{searchParam, searchParam, searchParam});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)));
                product.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CODIGO)));
                product.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION)));
                product.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_VALOR)));
                product.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORIA)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    public int getTotalProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    public boolean isProductExists(String codigo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT 1 FROM " + DatabaseHelper.TABLE_PRODUCTS +
                " WHERE " + DatabaseHelper.COLUMN_PRODUCT_CODIGO + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{codigo});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public List<Product> getProductsByCategory(String categoria) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCTS +
                " WHERE " + DatabaseHelper.COLUMN_PRODUCT_CATEGORIA + " = ?" +
                " ORDER BY " + DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION;

        Cursor cursor = db.rawQuery(query, new String[]{categoria});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)));
                product.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CODIGO)));
                product.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPCION)));
                product.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_VALOR)));
                product.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORIA)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }
}
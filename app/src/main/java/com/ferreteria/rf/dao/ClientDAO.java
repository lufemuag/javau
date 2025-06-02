
package com.ferreteria.rf.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ferreteria.rf.database.DatabaseHelper;
import com.ferreteria.rf.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private DatabaseHelper dbHelper;
    
    public ClientDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    public long insertClient(Client client) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_CLIENT_CEDULA, client.getCedula());
        values.put(DatabaseHelper.COLUMN_CLIENT_NOMBRE, client.getNombre());
        values.put(DatabaseHelper.COLUMN_CLIENT_DIRECCION, client.getDireccion());
        values.put(DatabaseHelper.COLUMN_CLIENT_TELEFONO, client.getTelefono());
        
        long result = db.insert(DatabaseHelper.TABLE_CLIENTS, null, values);
        db.close();
        return result;
    }
    
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CLIENTS + 
                      " ORDER BY " + DatabaseHelper.COLUMN_CLIENT_CREATED_AT + " DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Client client = new Client();
                client.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_ID)));
                client.setCedula(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_CEDULA)));
                client.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_NOMBRE)));
                client.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_DIRECCION)));
                client.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_TELEFONO)));
                clients.add(client);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return clients;
    }
    
    public Client getClientById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Client client = null;
        
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CLIENTS + 
                      " WHERE " + DatabaseHelper.COLUMN_CLIENT_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        
        if (cursor.moveToFirst()) {
            client = new Client();
            client.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_ID)));
            client.setCedula(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_CEDULA)));
            client.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_NOMBRE)));
            client.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_DIRECCION)));
            client.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_TELEFONO)));
        }
        
        cursor.close();
        db.close();
        return client;
    }
    
    public int updateClient(Client client) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_CLIENT_CEDULA, client.getCedula());
        values.put(DatabaseHelper.COLUMN_CLIENT_NOMBRE, client.getNombre());
        values.put(DatabaseHelper.COLUMN_CLIENT_DIRECCION, client.getDireccion());
        values.put(DatabaseHelper.COLUMN_CLIENT_TELEFONO, client.getTelefono());
        
        int result = db.update(DatabaseHelper.TABLE_CLIENTS, values, 
                              DatabaseHelper.COLUMN_CLIENT_ID + " = ?", 
                              new String[]{String.valueOf(client.getId())});
        db.close();
        return result;
    }
    
    public int deleteClient(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DatabaseHelper.TABLE_CLIENTS, 
                              DatabaseHelper.COLUMN_CLIENT_ID + " = ?", 
                              new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
    
    public List<Client> searchClients(String query) {
        List<Client> clients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String searchQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CLIENTS + 
                           " WHERE " + DatabaseHelper.COLUMN_CLIENT_NOMBRE + " LIKE ? OR " +
                           DatabaseHelper.COLUMN_CLIENT_CEDULA + " LIKE ? OR " +
                           DatabaseHelper.COLUMN_CLIENT_TELEFONO + " LIKE ?";
        
        String searchParam = "%" + query + "%";
        Cursor cursor = db.rawQuery(searchQuery, new String[]{searchParam, searchParam, searchParam});
        
        if (cursor.moveToFirst()) {
            do {
                Client client = new Client();
                client.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_ID)));
                client.setCedula(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_CEDULA)));
                client.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_NOMBRE)));
                client.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_DIRECCION)));
                client.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT_TELEFONO)));
                clients.add(client);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return clients;
    }
    
    public int getTotalClients() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_CLIENTS;
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

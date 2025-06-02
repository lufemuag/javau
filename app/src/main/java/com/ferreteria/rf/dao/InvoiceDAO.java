package com.ferreteria.rf.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ferreteria.rf.database.DatabaseHelper;
import com.ferreteria.rf.models.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private DatabaseHelper dbHelper;

    public InvoiceDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertInvoice(Invoice invoice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_INVOICE_NUMERO, invoice.getNumero());
        values.put(DatabaseHelper.COLUMN_INVOICE_TOTAL, invoice.getTotal());
        values.put(DatabaseHelper.COLUMN_INVOICE_CLIENT_ID, invoice.getClientId());
        values.put(DatabaseHelper.COLUMN_INVOICE_ESTADO, invoice.getEstado());

        long result = db.insert(DatabaseHelper.TABLE_INVOICES, null, values);
        db.close();
        return result;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT i.*, c.nombre as cliente_nombre " +
                      "FROM " + DatabaseHelper.TABLE_INVOICES + " i " +
                      "LEFT JOIN " + DatabaseHelper.TABLE_CLIENTS + " c ON i.client_id = c.id " +
                      "ORDER BY i.created_at DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Invoice invoice = new Invoice();
                invoice.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ID)));
                invoice.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_NUMERO)));
                invoice.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_FECHA)));
                invoice.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_TOTAL)));
                invoice.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_CLIENT_ID)));
                invoice.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ESTADO)));

                int clienteNombreIndex = cursor.getColumnIndex("cliente_nombre");
                if (clienteNombreIndex >= 0) {
                    invoice.setClienteNombre(cursor.getString(clienteNombreIndex));
                }

                invoices.add(invoice);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return invoices;
    }

    public Invoice getInvoiceById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Invoice invoice = null;

        String query = "SELECT i.*, c.nombre as cliente_nombre " +
                      "FROM " + DatabaseHelper.TABLE_INVOICES + " i " +
                      "LEFT JOIN " + DatabaseHelper.TABLE_CLIENTS + " c ON i.client_id = c.id " +
                      "WHERE i." + DatabaseHelper.COLUMN_INVOICE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            invoice = new Invoice();
            invoice.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ID)));
            invoice.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_NUMERO)));
            invoice.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_FECHA)));
            invoice.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_TOTAL)));
            invoice.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_CLIENT_ID)));
            invoice.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ESTADO)));

            int clienteNombreIndex = cursor.getColumnIndex("cliente_nombre");
            if (clienteNombreIndex >= 0) {
                invoice.setClienteNombre(cursor.getString(clienteNombreIndex));
            }
        }

        cursor.close();
        db.close();
        return invoice;
    }

    public int updateInvoice(Invoice invoice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_INVOICE_NUMERO, invoice.getNumero());
        values.put(DatabaseHelper.COLUMN_INVOICE_TOTAL, invoice.getTotal());
        values.put(DatabaseHelper.COLUMN_INVOICE_CLIENT_ID, invoice.getClientId());
        values.put(DatabaseHelper.COLUMN_INVOICE_ESTADO, invoice.getEstado());

        int result = db.update(DatabaseHelper.TABLE_INVOICES, values, 
                              DatabaseHelper.COLUMN_INVOICE_ID + " = ?", 
                              new String[]{String.valueOf(invoice.getId())});
        db.close();
        return result;
    }

    public int deleteInvoice(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DatabaseHelper.TABLE_INVOICES, 
                              DatabaseHelper.COLUMN_INVOICE_ID + " = ?", 
                              new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public List<Invoice> searchInvoices(String query) {
        List<Invoice> invoices = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String searchQuery = "SELECT i.*, c.nombre as cliente_nombre " +
                           "FROM " + DatabaseHelper.TABLE_INVOICES + " i " +
                           "LEFT JOIN " + DatabaseHelper.TABLE_CLIENTS + " c ON i.client_id = c.id " +
                           "WHERE i." + DatabaseHelper.COLUMN_INVOICE_NUMERO + " LIKE ? OR " +
                           "c.nombre LIKE ?";

        String searchParam = "%" + query + "%";
        Cursor cursor = db.rawQuery(searchQuery, new String[]{searchParam, searchParam});

        if (cursor.moveToFirst()) {
            do {
                Invoice invoice = new Invoice();
                invoice.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ID)));
                invoice.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_NUMERO)));
                invoice.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_FECHA)));
                invoice.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_TOTAL)));
                invoice.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_CLIENT_ID)));
                invoice.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ESTADO)));

                int clienteNombreIndex = cursor.getColumnIndex("cliente_nombre");
                if (clienteNombreIndex >= 0) {
                    invoice.setClienteNombre(cursor.getString(clienteNombreIndex));
                }

                invoices.add(invoice);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return invoices;
    }

    public double getTotalBilling() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM(" + DatabaseHelper.COLUMN_INVOICE_TOTAL + ") FROM " + DatabaseHelper.TABLE_INVOICES;
        Cursor cursor = db.rawQuery(query, null);

        double total = 0.0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }
}
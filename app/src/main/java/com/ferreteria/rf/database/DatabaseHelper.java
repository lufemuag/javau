package com.ferreteria.rf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "ferreteria_rf.db";
    private static final int DATABASE_VERSION = 1;
    
    // Tabla Clientes
    public static final String TABLE_CLIENTS = "clients";
    public static final String COLUMN_CLIENT_ID = "id";
    public static final String COLUMN_CLIENT_CEDULA = "cedula";
    public static final String COLUMN_CLIENT_NOMBRE = "nombre";
    public static final String COLUMN_CLIENT_DIRECCION = "direccion";
    public static final String COLUMN_CLIENT_TELEFONO = "telefono";
    public static final String COLUMN_CLIENT_CREATED_AT = "created_at";
    
    // Tabla Productos
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_CODIGO = "codigo";
    public static final String COLUMN_PRODUCT_DESCRIPCION = "descripcion";
    public static final String COLUMN_PRODUCT_VALOR = "valor";
    public static final String COLUMN_PRODUCT_CATEGORIA = "categoria";
    public static final String COLUMN_PRODUCT_STOCK = "stock";
    public static final String COLUMN_PRODUCT_CREATED_AT = "created_at";
    
    // Tabla Pedidos
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "id";
    public static final String COLUMN_ORDER_CODIGO = "codigo";
    public static final String COLUMN_ORDER_DESCRIPCION = "descripcion";
    public static final String COLUMN_ORDER_CANTIDAD = "cantidad";
    public static final String COLUMN_ORDER_CLIENT_ID = "client_id";
    public static final String COLUMN_ORDER_PRODUCT_ID = "product_id";
    public static final String COLUMN_ORDER_ESTADO = "estado";
    public static final String COLUMN_ORDER_CREATED_AT = "created_at";
    
    // Tabla Facturas
    public static final String TABLE_INVOICES = "invoices";
    public static final String COLUMN_INVOICE_ID = "id";
    public static final String COLUMN_INVOICE_NUMERO = "numero";
    public static final String COLUMN_INVOICE_FECHA = "fecha";
    public static final String COLUMN_INVOICE_TOTAL = "total";
    public static final String COLUMN_INVOICE_CLIENT_ID = "client_id";
    public static final String COLUMN_INVOICE_ESTADO = "estado";
    public static final String COLUMN_INVOICE_CREATED_AT = "created_at";
    
    // Create table statements
    private static final String CREATE_TABLE_CLIENTS = "CREATE TABLE " + TABLE_CLIENTS + "("
            + COLUMN_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CLIENT_CEDULA + " TEXT NOT NULL UNIQUE,"
            + COLUMN_CLIENT_NOMBRE + " TEXT NOT NULL,"
            + COLUMN_CLIENT_DIRECCION + " TEXT,"
            + COLUMN_CLIENT_TELEFONO + " TEXT,"
            + COLUMN_CLIENT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PRODUCT_CODIGO + " TEXT NOT NULL UNIQUE,"
            + COLUMN_PRODUCT_DESCRIPCION + " TEXT NOT NULL,"
            + COLUMN_PRODUCT_VALOR + " REAL NOT NULL,"
            + COLUMN_PRODUCT_CATEGORIA + " TEXT,"
            + COLUMN_PRODUCT_STOCK + " INTEGER DEFAULT 0,"
            + COLUMN_PRODUCT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    
    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_CODIGO + " TEXT NOT NULL UNIQUE,"
            + COLUMN_ORDER_DESCRIPCION + " TEXT NOT NULL,"
            + COLUMN_ORDER_CANTIDAD + " INTEGER NOT NULL,"
            + COLUMN_ORDER_CLIENT_ID + " INTEGER,"
            + COLUMN_ORDER_PRODUCT_ID + " INTEGER,"
            + COLUMN_ORDER_ESTADO + " TEXT DEFAULT 'activo',"
            + COLUMN_ORDER_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_ORDER_CLIENT_ID + ") REFERENCES " + TABLE_CLIENTS + "(" + COLUMN_CLIENT_ID + "),"
            + "FOREIGN KEY(" + COLUMN_ORDER_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")"
            + ")";
    public static final String COLUMN_ORDER_FECHA = "fecha";
    public static final String COLUMN_ORDER_CANTIDAD = "cantidad";
    public static final String COLUMN_ORDER_CLIENT_ID = "client_id";
    public static final String COLUMN_ORDER_PRODUCT_ID = "product_id";
    public static final String COLUMN_ORDER_ESTADO = "estado";
    public static final String COLUMN_ORDER_CREATED_AT = "created_at";
    
    // Tabla Facturas
    public static final String TABLE_INVOICES = "invoices";
    public static final String COLUMN_INVOICE_ID = "id";
    public static final String COLUMN_INVOICE_NUMERO = "numero";
    public static final String COLUMN_INVOICE_FECHA = "fecha";
    public static final String COLUMN_INVOICE_TOTAL = "total";
    public static final String COLUMN_INVOICE_CLIENT_ID = "client_id";
    public static final String COLUMN_INVOICE_ESTADO = "estado";
    public static final String COLUMN_INVOICE_CREATED_AT = "created_at";
    
    // Crear tabla Clientes
    private static final String CREATE_TABLE_CLIENTS = "CREATE TABLE " + TABLE_CLIENTS + "("
            + COLUMN_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CLIENT_CEDULA + " TEXT NOT NULL UNIQUE,"
            + COLUMN_CLIENT_NOMBRE + " TEXT NOT NULL,"
            + COLUMN_CLIENT_DIRECCION + " TEXT NOT NULL,"
            + COLUMN_CLIENT_TELEFONO + " TEXT NOT NULL,"
            + COLUMN_CLIENT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    
    // Crear tabla Productos
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PRODUCT_CODIGO + " TEXT NOT NULL UNIQUE,"
            + COLUMN_PRODUCT_DESCRIPCION + " TEXT NOT NULL,"
            + COLUMN_PRODUCT_VALOR + " REAL NOT NULL,"
            + COLUMN_PRODUCT_CATEGORIA + " TEXT,"
            + COLUMN_PRODUCT_STOCK + " INTEGER DEFAULT 0,"
            + COLUMN_PRODUCT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    
    // Crear tabla Pedidos
    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_CODIGO + " TEXT NOT NULL UNIQUE,"
            + COLUMN_ORDER_DESCRIPCION + " TEXT NOT NULL,"
            + COLUMN_ORDER_FECHA + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_ORDER_CANTIDAD + " INTEGER NOT NULL,"
            + COLUMN_ORDER_CLIENT_ID + " INTEGER,"
            + COLUMN_ORDER_PRODUCT_ID + " INTEGER,"
            + COLUMN_ORDER_ESTADO + " TEXT DEFAULT 'pendiente',"
            + COLUMN_ORDER_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_ORDER_CLIENT_ID + ") REFERENCES " + TABLE_CLIENTS + "(" + COLUMN_CLIENT_ID + "),"
            + "FOREIGN KEY(" + COLUMN_ORDER_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")"
            + ")";
    
    // Crear tabla Facturas
    private static final String CREATE_TABLE_INVOICES = "CREATE TABLE " + TABLE_INVOICES + "("
            + COLUMN_INVOICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_INVOICE_NUMERO + " TEXT NOT NULL UNIQUE,"
            + COLUMN_INVOICE_FECHA + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_INVOICE_TOTAL + " REAL NOT NULL,"
            + COLUMN_INVOICE_CLIENT_ID + " INTEGER,"
            + COLUMN_INVOICE_ESTADO + " TEXT DEFAULT 'pendiente',"
            + COLUMN_INVOICE_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_INVOICE_CLIENT_ID + ") REFERENCES " + TABLE_CLIENTS + "(" + COLUMN_CLIENT_ID + ")"
            + ")";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CLIENTS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_INVOICES);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);
        onCreate(db);
    }
}
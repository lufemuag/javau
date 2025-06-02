package com.ferreteria.rf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.ferreteria.rf.activities.ClientActivity;
import com.ferreteria.rf.activities.ProductActivity;
import com.ferreteria.rf.activities.OrderActivity;
import com.ferreteria.rf.activities.InvoiceActivity;
import com.ferreteria.rf.dao.ClientDAO;
import com.ferreteria.rf.dao.ProductDAO;
import com.ferreteria.rf.dao.OrderDAO;
import com.ferreteria.rf.dao.InvoiceDAO;

public class MainActivity extends AppCompatActivity {
    
    private TextView tvTotalClientes, tvTotalProductos, tvPedidosActivos, tvFacturacionTotal;
    private CardView cardClientes, cardProductos, cardPedidos, cardFacturas;
    
    private ClientDAO clientDAO;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private InvoiceDAO invoiceDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        initializeDAOs();
        setupClickListeners();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateDashboardStats();
    }
    
    private void initializeViews() {
        tvTotalClientes = findViewById(R.id.tv_total_clientes);
        tvTotalProductos = findViewById(R.id.tv_total_productos);
        tvPedidosActivos = findViewById(R.id.tv_pedidos_activos);
        tvFacturacionTotal = findViewById(R.id.tv_facturacion_total);
        
        cardClientes = findViewById(R.id.card_clientes);
        cardProductos = findViewById(R.id.card_productos);
        cardPedidos = findViewById(R.id.card_pedidos);
        cardFacturas = findViewById(R.id.card_facturas);
    }
    
    private void initializeDAOs() {
        clientDAO = new ClientDAO(this);
        productDAO = new ProductDAO(this);
        orderDAO = new OrderDAO(this);
        invoiceDAO = new InvoiceDAO(this);
    }
    
    private void setupClickListeners() {
        cardClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });
        
        cardProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
        
        cardPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
        
        cardFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InvoiceActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void updateDashboardStats() {
        // Update client count
        int totalClientes = clientDAO.getTotalClients();
        tvTotalClientes.setText(String.valueOf(totalClientes));
        
        // Update product count
        int totalProductos = productDAO.getTotalProducts();
        tvTotalProductos.setText(String.valueOf(totalProductos));
        
        // Update active orders count
        int pedidosActivos = orderDAO.getActiveOrders();
        tvPedidosActivos.setText(String.valueOf(pedidosActivos));
        
        // Update total billing
        double facturacionTotal = invoiceDAO.getTotalBilling();
        tvFacturacionTotal.setText(String.format("$%.2f", facturacionTotal));
    }
    
    private void setupClickListeners() {
        cardClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });
        
        cardProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
        
        cardPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
        
        cardFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InvoiceActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void updateDashboardStats() {
        // Total de clientes
        int totalClientes = clientDAO.getClientCount();
        tvTotalClientes.setText(String.valueOf(totalClientes));
        
        // Total de productos
        int totalProductos = productDAO.getProductCount();
        tvTotalProductos.setText(String.valueOf(totalProductos));
        
        // Pedidos activos (pendientes)
        int pedidosActivos = orderDAO.getActiveOrdersCount();
        tvPedidosActivos.setText(String.valueOf(pedidosActivos));
        
        // Facturación total
        double facturacionTotal = invoiceDAO.getTotalRevenue();
        tvFacturacionTotal.setText(String.format("$%.2f", facturacionTotal));
    }
}
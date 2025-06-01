package com.ferreteria.rf.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.OrderDAO;
import com.ferreteria.rf.models.Order;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private OrderDAO orderDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Pedidos");
        }
        
        initializeViews();
        loadOrders();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderDAO = new OrderDAO(this);
    }
    
    private void loadOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        Toast.makeText(this, "Pedidos cargados: " + orders.size(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package com.ferreteria.rf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.OrderDAO;
import com.ferreteria.rf.models.Order;
import com.ferreteria.rf.adapters.OrderAdapter;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {
    
    private RecyclerView recyclerView;
    private OrderDAO orderDAO;
    private OrderAdapter adapter;
    private SearchView searchView;
    private FloatingActionButton fabAdd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Pedidos");
        }
        
        initializeViews();
        setupRecyclerView();
        setupSearchView();
        loadOrders();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_orders);
        searchView = findViewById(R.id.search_orders);
        fabAdd = findViewById(R.id.fab_add_order);
        orderDAO = new OrderDAO(this);
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, AddEditOrderActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }
    
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadOrders();
                } else {
                    List<Order> filteredOrders = orderDAO.searchOrders(newText);
                    adapter.setOrders(filteredOrders);
                }
                return true;
            }
        });
    }
    
    private void loadOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        adapter.setOrders(orders);
        Toast.makeText(this, "Pedidos cargados: " + orders.size(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onOrderClick(Order order) {
        Intent intent = new Intent(this, AddEditOrderActivity.class);
        intent.putExtra("ORDER_ID", order.getId());
        intent.putExtra("ORDER_CODIGO", order.getCodigo());
        intent.putExtra("ORDER_DESCRIPCION", order.getDescripcion());
        intent.putExtra("ORDER_CANTIDAD", order.getCantidad());
        intent.putExtra("ORDER_CLIENT_ID", order.getClientId());
        intent.putExtra("ORDER_PRODUCT_ID", order.getProductId());
        intent.putExtra("ORDER_ESTADO", order.getEstado());
        startActivity(intent);
    }
    
    @Override
    public void onOrderDelete(Order order) {
        int result = orderDAO.deleteOrder(order.getId());
        if (result > 0) {
            Toast.makeText(this, "Pedido eliminado correctamente", Toast.LENGTH_SHORT).show();
            loadOrders();
        } else {
            Toast.makeText(this, "Error al eliminar pedido", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

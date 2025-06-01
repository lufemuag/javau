package com.ferreteria.rf.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.ProductDAO;
import com.ferreteria.rf.models.Product;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private ProductDAO productDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Productos");
        }
        
        initializeViews();
        loadProducts();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productDAO = new ProductDAO(this);
    }
    
    private void loadProducts() {
        List<Product> products = productDAO.getAllProducts();
        Toast.makeText(this, "Productos cargados: " + products.size(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
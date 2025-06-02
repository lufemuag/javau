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
import com.ferreteria.rf.dao.ProductDAO;
import com.ferreteria.rf.models.Product;
import com.ferreteria.rf.adapters.ProductAdapter;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductDAO productDAO;
    private ProductAdapter adapter;
    private SearchView searchView;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Productos");
        }

        initializeViews();
        setupRecyclerView();
        setupSearchView();
        loadProducts();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_products);
        searchView = findViewById(R.id.search_products);
        fabAdd = findViewById(R.id.fab_add_product);
        productDAO = new ProductDAO(this);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, AddEditProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, this);
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
                    loadProducts();
                } else {
                    List<Product> filteredProducts = productDAO.searchProducts(newText);
                    adapter.setProducts(filteredProducts);
                }
                return true;
            }
        });
    }

    private void loadProducts() {
        List<Product> products = productDAO.getAllProducts();
        adapter.setProducts(products);
        Toast.makeText(this, "Productos cargados: " + products.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, AddEditProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra("PRODUCT_CODIGO", product.getCodigo());
        intent.putExtra("PRODUCT_DESCRIPCION", product.getDescripcion());
        intent.putExtra("PRODUCT_VALOR", product.getValor());
        startActivity(intent);
    }

    @Override
    public void onProductDelete(Product product) {
        int result = productDAO.deleteProduct(product.getId());
        if (result > 0) {
            Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
            loadProducts();
        } else {
            Toast.makeText(this, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
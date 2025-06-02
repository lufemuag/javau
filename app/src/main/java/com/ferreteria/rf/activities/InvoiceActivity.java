
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
import com.ferreteria.rf.dao.InvoiceDAO;
import com.ferreteria.rf.models.Invoice;
import com.ferreteria.rf.adapters.InvoiceAdapter;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity implements InvoiceAdapter.OnInvoiceClickListener {
    
    private RecyclerView recyclerView;
    private InvoiceDAO invoiceDAO;
    private InvoiceAdapter adapter;
    private SearchView searchView;
    private FloatingActionButton fabAdd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Facturas");
        }
        
        initializeViews();
        setupRecyclerView();
        setupSearchView();
        loadInvoices();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_invoices);
        searchView = findViewById(R.id.search_invoices);
        fabAdd = findViewById(R.id.fab_add_invoice);
        invoiceDAO = new InvoiceDAO(this);
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this, AddEditInvoiceActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InvoiceAdapter(this, this);
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
                    loadInvoices();
                } else {
                    List<Invoice> filteredInvoices = invoiceDAO.searchInvoices(newText);
                    adapter.setInvoices(filteredInvoices);
                }
                return true;
            }
        });
    }
    
    private void loadInvoices() {
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        adapter.setInvoices(invoices);
        Toast.makeText(this, "Facturas cargadas: " + invoices.size(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onInvoiceClick(Invoice invoice) {
        Intent intent = new Intent(this, AddEditInvoiceActivity.class);
        intent.putExtra("INVOICE_ID", invoice.getId());
        intent.putExtra("INVOICE_NUMERO", invoice.getNumero());
        intent.putExtra("INVOICE_TOTAL", invoice.getTotal());
        intent.putExtra("INVOICE_CLIENT_ID", invoice.getClientId());
        intent.putExtra("INVOICE_ESTADO", invoice.getEstado());
        startActivity(intent);
    }
    
    @Override
    public void onInvoiceDelete(Invoice invoice) {
        int result = invoiceDAO.deleteInvoice(invoice.getId());
        if (result > 0) {
            Toast.makeText(this, "Factura eliminada correctamente", Toast.LENGTH_SHORT).show();
            loadInvoices();
        } else {
            Toast.makeText(this, "Error al eliminar factura", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadInvoices();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

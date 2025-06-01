package com.ferreteria.rf.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.InvoiceDAO;
import com.ferreteria.rf.models.Invoice;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private InvoiceDAO invoiceDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Facturas");
        }
        
        initializeViews();
        loadInvoices();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_invoices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        invoiceDAO = new InvoiceDAO(this);
    }
    
    private void loadInvoices() {
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        Toast.makeText(this, "Facturas cargadas: " + invoices.size(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
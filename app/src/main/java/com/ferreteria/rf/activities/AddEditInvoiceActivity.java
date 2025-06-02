package com.ferreteria.rf.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;

public class AddEditInvoiceActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_invoice);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar/Editar Factura");
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
package com.ferreteria.rf.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.InvoiceDAO;
import com.ferreteria.rf.dao.ClientDAO;
import com.ferreteria.rf.models.Invoice;
import com.ferreteria.rf.models.Client;
import java.util.List;

public class AddEditInvoiceActivity extends AppCompatActivity {
    
    private EditText etNumero, etTotal;
    private Spinner spinnerCliente, spinnerEstado;
    private Button btnSave, btnCancel;
    private InvoiceDAO invoiceDAO;
    private ClientDAO clientDAO;
    private int invoiceId = -1;
    private boolean isEditMode = false;
    private List<Client> clients;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_invoice);
        
        initializeViews();
        setupSpinners();
        checkEditMode();
        setupClickListeners();
    }
    
    private void initializeViews() {
        etNumero = findViewById(R.id.et_numero);
        etTotal = findViewById(R.id.et_total);
        spinnerCliente = findViewById(R.id.spinner_cliente);
        spinnerEstado = findViewById(R.id.spinner_estado);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        
        invoiceDAO = new InvoiceDAO(this);
        clientDAO = new ClientDAO(this);
    }
    
    private void setupSpinners() {
        // Setup clients spinner
        clients = clientDAO.getAllClients();
        ArrayAdapter<Client> clientAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, clients);
        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(clientAdapter);
        
        // Setup status spinner
        String[] estados = {"pendiente", "pagada", "cancelada"};
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, estados);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);
    }
    
    private void checkEditMode() {
        if (getIntent().hasExtra("INVOICE_ID")) {
            isEditMode = true;
            invoiceId = getIntent().getIntExtra("INVOICE_ID", -1);
            
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Factura");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            
            // Fill fields with existing data
            etNumero.setText(getIntent().getStringExtra("INVOICE_NUMERO"));
            etTotal.setText(String.valueOf(getIntent().getDoubleExtra("INVOICE_TOTAL", 0.0)));
            
            // Set spinner selections
            int clientId = getIntent().getIntExtra("INVOICE_CLIENT_ID", -1);
            String estado = getIntent().getStringExtra("INVOICE_ESTADO");
            
            setSpinnerSelections(clientId, estado);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nueva Factura");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
    
    private void setSpinnerSelections(int clientId, String estado) {
        // Set client selection
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == clientId) {
                spinnerCliente.setSelection(i);
                break;
            }
        }
        
        // Set status selection
        String[] estados = {"pendiente", "pagada", "cancelada"};
        for (int i = 0; i < estados.length; i++) {
            if (estados[i].equals(estado)) {
                spinnerEstado.setSelection(i);
                break;
            }
        }
    }
    
    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInvoice();
            }
        });
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void saveInvoice() {
        String numero = etNumero.getText().toString().trim();
        String totalStr = etTotal.getText().toString().trim();
        
        if (numero.isEmpty() || totalStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (spinnerCliente.getSelectedItem() == null) {
            Toast.makeText(this, "Por favor seleccione un cliente", Toast.LENGTH_SHORT).show();
            return;
        }
        
        double total;
        try {
            total = Double.parseDouble(totalStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Total inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Client selectedClient = (Client) spinnerCliente.getSelectedItem();
        String selectedEstado = (String) spinnerEstado.getSelectedItem();
        
        Invoice invoice = new Invoice();
        invoice.setNumero(numero);
        invoice.setTotal(total);
        invoice.setClientId(selectedClient.getId());
        invoice.setEstado(selectedEstado);
        
        long result;
        if (isEditMode) {
            invoice.setId(invoiceId);
            result = invoiceDAO.updateInvoice(invoice);
            if (result > 0) {
                Toast.makeText(this, "Factura actualizada correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar factura", Toast.LENGTH_SHORT).show();
            }
        } else {
            result = invoiceDAO.insertInvoice(invoice);
            if (result > 0) {
                Toast.makeText(this, "Factura guardada correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar factura", Toast.LENGTH_SHORT).show();
            }
        }
        
        if (result > 0) {
            finish();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

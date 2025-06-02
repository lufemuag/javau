package com.ferreteria.rf.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;

public class AddEditClientActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_client);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar/Editar Cliente");
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.ClientDAO;
import com.ferreteria.rf.models.Client;

public class AddEditClientActivity extends AppCompatActivity {
    
    private EditText etCedula, etNombre, etDireccion, etTelefono;
    private Button btnSave;
    private ClientDAO clientDAO;
    private Client currentClient;
    private boolean isEdit = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_client);
        
        initializeViews();
        
        // Check if we're editing an existing client
        int clientId = getIntent().getIntExtra("client_id", -1);
        if (clientId != -1) {
            isEdit = true;
            loadClient(clientId);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Cliente");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Cliente");
            }
        }
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void initializeViews() {
        etCedula = findViewById(R.id.et_cedula);
        etNombre = findViewById(R.id.et_nombre);
        etDireccion = findViewById(R.id.et_direccion);
        etTelefono = findViewById(R.id.et_telefono);
        btnSave = findViewById(R.id.btn_save_client);
        
        clientDAO = new ClientDAO(this);
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClient();
            }
        });
    }
    
    private void loadClient(int clientId) {
        currentClient = clientDAO.getClientById(clientId);
        if (currentClient != null) {
            etCedula.setText(currentClient.getCedula());
            etNombre.setText(currentClient.getNombre());
            etDireccion.setText(currentClient.getDireccion());
            etTelefono.setText(currentClient.getTelefono());
        }
    }
    
    private void saveClient() {
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        
        if (cedula.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Cédula y nombre son campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (isEdit && currentClient != null) {
            // Update existing client
            currentClient.setCedula(cedula);
            currentClient.setNombre(nombre);
            currentClient.setDireccion(direccion);
            currentClient.setTelefono(telefono);
            
            int result = clientDAO.updateClient(currentClient);
            if (result > 0) {
                Toast.makeText(this, "Cliente actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Create new client
            Client client = new Client(cedula, nombre, direccion, telefono);
            long result = clientDAO.insertClient(client);
            
            if (result != -1) {
                Toast.makeText(this, "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar cliente", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

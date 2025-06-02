package com.ferreteria.rf.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.ClientDAO;
import com.ferreteria.rf.models.Client;

public class AddEditClientActivity extends AppCompatActivity {

    private EditText etCedula, etNombre, etDireccion, etTelefono;
    private Button btnSave, btnCancel;
    private ClientDAO clientDAO;
    private boolean isEditMode = false;
    private int clientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_client);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeViews();
        loadClientData();
        setupButtons();
    }

    private void initializeViews() {
        etCedula = findViewById(R.id.et_cedula);
        etNombre = findViewById(R.id.et_nombre);
        etDireccion = findViewById(R.id.et_direccion);
        etTelefono = findViewById(R.id.et_telefono);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        clientDAO = new ClientDAO(this);
    }

    private void loadClientData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("CLIENT_ID")) {
            isEditMode = true;
            clientId = extras.getInt("CLIENT_ID");
            etCedula.setText(extras.getString("CLIENT_CEDULA"));
            etNombre.setText(extras.getString("CLIENT_NOMBRE"));
            etDireccion.setText(extras.getString("CLIENT_DIRECCION"));
            etTelefono.setText(extras.getString("CLIENT_TELEFONO"));

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Cliente");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Agregar Cliente");
            }
        }
    }

    private void setupButtons() {
        btnSave.setOnClickListener(v -> saveClient());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveClient() {
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (TextUtils.isEmpty(cedula) || TextUtils.isEmpty(nombre) || 
            TextUtils.isEmpty(direccion) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Client client = new Client(cedula, nombre, direccion, telefono);

        long result;
        if (isEditMode) {
            client.setId(clientId);
            result = clientDAO.updateClient(client);
            if (result > 0) {
                Toast.makeText(this, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show();
            }
        } else {
            result = clientDAO.insertClient(client);
            if (result != -1) {
                Toast.makeText(this, "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al agregar cliente", Toast.LENGTH_SHORT).show();
            }
        }

        if (result > 0 || result != -1) {
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
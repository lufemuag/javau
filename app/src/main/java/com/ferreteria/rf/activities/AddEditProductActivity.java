package com.ferreteria.rf.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;

public class AddEditProductActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar/Editar Producto");
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
import com.ferreteria.rf.dao.ProductDAO;
import com.ferreteria.rf.models.Product;

public class AddEditProductActivity extends AppCompatActivity {
    
    private EditText etCodigo, etDescripcion, etValor;
    private Button btnSave, btnCancel;
    private ProductDAO productDAO;
    private int productId = -1;
    private boolean isEditMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        
        initializeViews();
        checkEditMode();
        setupClickListeners();
    }
    
    private void initializeViews() {
        etCodigo = findViewById(R.id.et_codigo);
        etDescripcion = findViewById(R.id.et_descripcion);
        etValor = findViewById(R.id.et_valor);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        
        productDAO = new ProductDAO(this);
    }
    
    private void checkEditMode() {
        if (getIntent().hasExtra("PRODUCT_ID")) {
            isEditMode = true;
            productId = getIntent().getIntExtra("PRODUCT_ID", -1);
            
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Producto");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            
            // Llenar campos con datos existentes
            etCodigo.setText(getIntent().getStringExtra("PRODUCT_CODIGO"));
            etDescripcion.setText(getIntent().getStringExtra("PRODUCT_DESCRIPCION"));
            etValor.setText(String.valueOf(getIntent().getDoubleExtra("PRODUCT_VALOR", 0.0)));
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Producto");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
    
    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void saveProduct() {
        String codigo = etCodigo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String valorStr = etValor.getText().toString().trim();
        
        if (codigo.isEmpty() || descripcion.isEmpty() || valorStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Product product = new Product();
        product.setCodigo(codigo);
        product.setDescripcion(descripcion);
        product.setValor(valor);
        
        long result;
        if (isEditMode) {
            product.setId(productId);
            result = productDAO.updateProduct(product);
            if (result > 0) {
                Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar producto", Toast.LENGTH_SHORT).show();
            }
        } else {
            result = productDAO.insertProduct(product);
            if (result > 0) {
                Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar producto", Toast.LENGTH_SHORT).show();
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

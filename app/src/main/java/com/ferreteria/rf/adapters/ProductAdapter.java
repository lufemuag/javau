
package com.ferreteria.rf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    
    private List<Product> products;
    private OnProductClickListener listener;
    private Context context;
    
    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onProductDelete(Product product);
    }
    
    public ProductAdapter(OnProductClickListener listener) {
        this.products = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }
    
    @Override
    public int getItemCount() {
        return products.size();
    }
    
    class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCodigo, tvDescripcion, tvValor;
        private ImageButton btnEdit, btnDelete;
        
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tv_product_codigo);
            tvDescripcion = itemView.findViewById(R.id.tv_product_descripcion);
            tvValor = itemView.findViewById(R.id.tv_product_valor);
            btnEdit = itemView.findViewById(R.id.btn_edit_product);
            btnDelete = itemView.findViewById(R.id.btn_delete_product);
        }
        
        public void bind(Product product) {
            tvCodigo.setText("Código: " + product.getCodigo());
            tvDescripcion.setText(product.getDescripcion());
            tvValor.setText("$" + String.format("%.2f", product.getValor()));
            
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
            
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Está seguro de que desea eliminar este producto?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        if (listener != null) {
                            listener.onProductDelete(product);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            });
        }
    }
}
package com.ferreteria.rf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    
    private List<Product> products = new ArrayList<>();
    private Context context;
    private OnProductClickListener listener;
    
    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onProductDelete(Product product);
    }
    
    public ProductAdapter(Context context, OnProductClickListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, listener);
    }
    
    @Override
    public int getItemCount() {
        return products.size();
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
    
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCodigo, tvDescripcion, tvValor;
        private Button btnEdit, btnDelete;
        
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tv_codigo);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion);
            tvValor = itemView.findViewById(R.id.tv_valor);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
        
        public void bind(Product product, OnProductClickListener listener) {
            tvCodigo.setText(product.getCodigo());
            tvDescripcion.setText(product.getDescripcion());
            tvValor.setText(String.format("$%.2f", product.getValor()));
            
            btnEdit.setOnClickListener(v -> listener.onProductClick(product));
            btnDelete.setOnClickListener(v -> listener.onProductDelete(product));
        }
    }
}

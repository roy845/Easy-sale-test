package com.example.easysaletest.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easysaletest.R;
import com.example.easysaletest.adapter.ProductAdapter;
import com.example.easysaletest.adapter.SwipeToDeleteCallback;
import com.example.easysaletest.constants.Constants;
import com.example.easysaletest.models.Product;
import com.example.easysaletest.viewmodel.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ProductViewModel productViewModel;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private TextView emptyResultsTextView;
    private ImageView emptyResultsImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupViewModel();
        setupSearchEditText();
        initRecyclerViewAndAdapter();
        navigateToAddProductActivity();
        observeAllProducts();
        observeLoadingInitialProducts();
        enableSwipeToDeleteAndUndo();
    }

    private void setupSearchEditText(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                    productViewModel.searchProducts(s.toString()).observe(MainActivity.this,products -> {
                        productAdapter.submitData(getLifecycle(),products);
                    });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideWhenLoading(){
        recyclerView.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showWhenNotLoading(){
        recyclerView.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void observeLoadingInitialProducts(){
        productViewModel.getLoadingInitialProducts().observe(this,loading->{
            if(loading){
                hideWhenLoading();
            }else{
                showWhenNotLoading();
            }
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(MainActivity.this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int itemPosition = viewHolder.getAbsoluteAdapterPosition();
                Product product = productAdapter.getProduct(itemPosition);
                if(direction == ItemTouchHelper.LEFT) {

                    Snackbar snackbar = Snackbar.make(viewHolder.itemView,"You removed "+product.getTitle(),Snackbar.LENGTH_LONG);
                    snackbar.setAction(Constants.UNDO, v -> {

                        productViewModel.insertProduct(product).observe(MainActivity.this,result->{
                            if (Constants.SUCCESS.equals(result)) {
                                Toast.makeText(MainActivity.this, product.getTitle() + " restored successfully!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Unable to restore product: " + product.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    });

                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Do you want to delete \"" + product.getTitle() + "\"?")
                            .setView(R.layout.custom_dialog_buttons)
                            .setCancelable(false)
                            .create();

                    dialog.setOnShowListener(dialogInterface -> {

                        Button positiveButton = dialog.findViewById(R.id.dialog_positive_button);
                        Button negativeButton = dialog.findViewById(R.id.dialog_negative_button);

                        positiveButton.setOnClickListener(v -> {
                            if(itemPosition!=-1){

                                productViewModel.deleteProduct(product).observe(MainActivity.this, success -> {

                                    if (Constants.SUCCESS.equals(success)) {
                                        Toast.makeText(MainActivity.this, product.getTitle() + " removed successfully!", Toast.LENGTH_SHORT).show();
                                        snackbar.show();
                                        dialog.dismiss();
                                    }else {
                                        Toast.makeText(MainActivity.this, "Failed to remove " + product.getTitle() + ". Please try again.", Toast.LENGTH_SHORT).show();
                                        productAdapter.notifyItemChanged(itemPosition);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                        negativeButton.setOnClickListener(v -> {
                            productAdapter.notifyItemChanged(itemPosition);
                            dialog.dismiss();
                        });
                    });

                    dialog.show();
                }
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void navigateToAddProductActivity(){
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddProductActivity.class))
        );
    }

    private void initRecyclerViewAndAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(ProductAdapter.DIFF_CALLBACK);
        recyclerView.setAdapter(productAdapter);
    }

    private void initViews(){
        emptyResultsTextView = findViewById(R.id.empty_results_text);
        emptyResultsImageView = findViewById(R.id.empty_results_image);
        searchEditText = findViewById(R.id.search_edit_text);
        progressBar = findViewById(R.id.progress_bar);
        floatingActionButton = findViewById(R.id.fab_add_user);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void setupViewModel() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void observeAllProducts(){
        productViewModel.loadAllProducts().observe(this, products -> {
            productAdapter.submitData(getLifecycle(), products);
        });

        productAdapter.addLoadStateListener(loadStates -> {
            if (loadStates.getRefresh() instanceof LoadState.NotLoading && productAdapter.getItemCount() == 0) {
                // No products to display
                emptyResultsTextView.setVisibility(View.VISIBLE);
                emptyResultsImageView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                // Products are present
                emptyResultsTextView.setVisibility(View.GONE);
                emptyResultsImageView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            return null;
        });
    }

}
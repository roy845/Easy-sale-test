package com.example.easysaletest.ui.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.easysaletest.R;
import com.example.easysaletest.constants.Constants;
import com.example.easysaletest.models.Product;
import com.example.easysaletest.utils.ValidationsUtils;
import com.example.easysaletest.viewmodel.ProductViewModel;
import com.google.android.material.button.MaterialButton;


public class AddProductActivity extends AppCompatActivity {

    MaterialButton addProductButton,cancelButton;
    private EditText editTextTitle, editTextPrice, editTextCategory,editTextDescription;
    ProductViewModel productsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupViewModel();
        setGravityEditTexts();
        initAddButtonClickListener();
        initCancelButtonClickListener();
    }

    private void initViews(){
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        addProductButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void setupViewModel() {
        productsViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void setGravityEditTexts() {

        editTextTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
        editTextPrice.setTextDirection(View.TEXT_DIRECTION_LTR);
        editTextCategory.setTextDirection(View.TEXT_DIRECTION_LTR);
        editTextDescription.setGravity(View.TEXT_DIRECTION_LTR);
    }

    private void initAddButtonClickListener() {
        addProductButton.setOnClickListener(v -> {
            double price;

            String title = editTextTitle.getText().toString().trim();
            String priceText = editTextPrice.getText().toString().trim();
            String category = editTextCategory.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (ValidationsUtils.validateProductDetails(title,priceText,category,description)) {
                Toast.makeText(this, Constants.FILL_ALL_THE_PRODUCT_DETAILS_MESSAGE, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, Constants.INVALID_PRICE_FORMAT, Toast.LENGTH_SHORT).show();
                return;
            }

            final String imageUrl = Constants.IMAGE_URL;

            Product newProduct = new Product(title, price, description, category, imageUrl);

            productsViewModel.insertProduct(newProduct).observe(AddProductActivity.this,result->{
                if (Constants.SUCCESS.equals(result)) {
                    Toast.makeText(this, "Product " + newProduct.getTitle() + " added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (Constants.ERROR.equals(result)) {
                    Toast.makeText(this, "Unable to add product:" + newProduct.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initCancelButtonClickListener(){
        cancelButton.setOnClickListener(v->{
            finish();
        });
    }

}
package com.example.easysaletest.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagingData;

import com.example.easysaletest.constants.Constants;
import com.example.easysaletest.models.Product;
import com.example.easysaletest.network.ApiClient;
import com.example.easysaletest.network.ApiService;
import com.example.easysaletest.repository.implementation.ProductsRepository;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class ProductViewModel extends AndroidViewModel {
    private final ProductsRepository productsRepository;
    private final ExecutorService executorService;
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Boolean> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingInitialProductsLiveData = new MutableLiveData<>();
    public LiveData<Boolean> getErrorLiveData() {
        return errorLiveData;
    }
    public LiveData<Boolean> getLoadingInitialProducts(){
        return loadingInitialProductsLiveData;
    }

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productsRepository = new ProductsRepository(application);
        this.executorService = Executors.newSingleThreadExecutor();
        this.sharedPreferences = application.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        checkAndFetchProductsFromApi();
    }

    public LiveData<String> insertProduct(Product product) {
        return productsRepository.insertProduct(product);
    }

    public LiveData<String> deleteProduct(Product product) {
        return productsRepository.deleteProduct(product);
    }

    public void insertAllProducts(List<Product> products) {
        productsRepository.insertAllProduct(products);
    }

    public LiveData<PagingData<Product>> loadAllProducts() {
        return productsRepository.loadAllProducts();
    }

    public LiveData<PagingData<Product>> searchProducts(String searchQuery) {
        return productsRepository.searchProducts(searchQuery);
    }

    private void checkAndFetchProductsFromApi() {
        boolean hasFetchedProducts = sharedPreferences.getBoolean(Constants.PREF_FETCHED_PRODUCTS, false);
        if (!hasFetchedProducts) {
            fetchAndStoreProducts();
            errorLiveData.setValue(false);
        } else {
            errorLiveData.setValue(false);
            loadingInitialProductsLiveData.setValue(false);
        }
    }

    private void fetchAndStoreProducts() {
        loadingInitialProductsLiveData.setValue(true);
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productsFromApi = response.body();

                    executorService.execute(() -> insertAllProducts(productsFromApi));

                    loadingInitialProductsLiveData.setValue(false);
                    errorLiveData.setValue(false);
                    sharedPreferences.edit().putBoolean(Constants.PREF_FETCHED_PRODUCTS, true).apply();
                    Toast.makeText(getApplication().getApplicationContext(), Constants.PRODUCT_FETCHED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                handleApiFailure(t);
                errorLiveData.setValue(true);
                loadingInitialProductsLiveData.setValue(false);
            }
        });
    }

    private void handleApiFailure(Throwable t) {
        String errorMessage;
        if (t instanceof IOException) {
            errorMessage = Constants.NETWORK_ERROR;
        } else if (t instanceof HttpException) {
            errorMessage = Constants.SERVER_ERROR;
        } else {
            errorMessage = Constants.UNEXPECTED_ERROR;
        }
        Toast.makeText(getApplication().getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
}

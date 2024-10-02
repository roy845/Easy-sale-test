package com.example.easysaletest.repository.implementation;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.example.easysaletest.constants.Constants;
import com.example.easysaletest.database.AppDatabase;
import com.example.easysaletest.models.Product;
import com.example.easysaletest.repository.interfaces.IProductsRepository;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductsRepository implements IProductsRepository {
    private final AppDatabase appDatabase;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ProductsRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    @Override
    public LiveData<String> insertProduct(Product product) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                appDatabase.productDao().insertProduct(product);
                result.postValue(Constants.SUCCESS);
            } catch (Exception e) {
                result.postValue(Constants.ERROR);
            }
        });

        return result;
    }

    @Override
    public LiveData<String> insertAllProduct(List<Product> products) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                appDatabase.productDao().insertAllProducts(products);
                result.postValue(Constants.SUCCESS);
            } catch (Exception e) {
                result.postValue(Constants.ERROR);
            }
        });

        return result;
    }

    @Override
    public LiveData<String> deleteProduct(Product product) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                int rowsDeleted = appDatabase.productDao().deleteProduct(product);
                if (rowsDeleted > 0) {
                    result.postValue(Constants.SUCCESS);
                } else {
                    result.postValue(Constants.ERROR);
                }
            } catch (Exception e) {
                result.postValue(Constants.ERROR);
            }
        });

        return result;
    }

    @Override
    public LiveData<PagingData<Product>> loadAllProducts() {
        return PagingLiveData.getLiveData(new Pager<>(
                new PagingConfig(
                        6,0,true
                ),
                () -> appDatabase.productDao().loadAllProducts()
        ));
    }

    @Override
    public LiveData<PagingData<Product>> searchProducts(String searchQuery){
        return PagingLiveData.getLiveData(new Pager<>(
                new PagingConfig(
                        6,0,true
                ),
                () -> appDatabase.productDao().searchProducts(searchQuery)
        ));
    }
}

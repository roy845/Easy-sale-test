package com.example.easysaletest.repository.interfaces;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;
import androidx.paging.PagingSource;

import com.example.easysaletest.models.Product;
import java.util.List;

public interface IProductsRepository {
    LiveData<String> insertProduct(Product product);
    LiveData<String> insertAllProduct(List<Product> products);
    LiveData<String> deleteProduct(Product product);
    LiveData<PagingData<Product>> loadAllProducts();
    LiveData<PagingData<Product>> searchProducts(String searchQuery);
}

package com.example.easysaletest.database.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.easysaletest.models.Product;
import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllProducts(List<Product> products);

    @Query("SELECT * FROM products ORDER BY id ASC")
    PagingSource<Integer,Product> loadAllProducts();

    @Delete
    int deleteProduct(Product product);

    @Query("SELECT * FROM products WHERE LOWER(title) LIKE '%' || :searchQuery || '%' ORDER BY id ASC")
    PagingSource<Integer,Product> searchProducts(String searchQuery);
}

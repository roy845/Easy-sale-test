package com.example.easysaletest.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.easysaletest.constants.Constants;
import com.example.easysaletest.database.dao.ProductDao;
import com.example.easysaletest.models.Product;

@Database(entities = {Product.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract ProductDao productDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, Constants.DATABASE_NAME)
                            .build();
                }
            }
        }

        return instance;
    }
}

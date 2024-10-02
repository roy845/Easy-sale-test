package com.example.easysaletest.utils;

public class ValidationsUtils {

    public static boolean validateProductDetails(final String title,final String priceText,final String category,final String description){
        return title.isEmpty() || priceText.isEmpty() || category.isEmpty() || description.isEmpty();
    }
}

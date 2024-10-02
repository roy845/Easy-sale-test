package com.example.easysaletest.models;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

@Entity(tableName = "products")
public class Product {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("category")
    private String category;

    @SerializedName("image")
    private String image;

    public Product(int id, String title, double price, String description, String category, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }
    @Ignore
    public Product(String title, double price, String description, String category, String image) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return id == product.id &&
                Objects.equals(title, product.title) &&
                Objects.equals(price, product.price) &&
                Objects.equals(description, product.description) &&
                Objects.equals(category, product.category) &&
                Objects.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, description, category, image);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
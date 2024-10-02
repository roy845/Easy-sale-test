package com.example.easysaletest.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.easysaletest.R;
import com.example.easysaletest.constants.Constants;
import com.example.easysaletest.models.Product;
import java.io.IOException;
import java.util.Locale;

public class ProductAdapter extends PagingDataAdapter<Product, ProductAdapter.ProductViewHolder> {

    public ProductAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback) {
        super(diffCallback);
    }

    public static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Product>() {
                @Override
                public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                    return oldItem.getId() == (newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = getItem(position);
        if(product != null){

            try {
                holder.bindData(
                        product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getImage()
                );
            } catch (IOException e) {
                Log.e(Constants.PRODUCTS_ADAPTER, Constants.ERROR_BINDING_PRODCUT_DATA, e);
            }
        }
    }

    public Product getProduct(int position){
        return getItem(position);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        private final Context context;
        private final View productItem;
        private final ImageView avatarImageView;
        private final TextView idTextView;
        private final TextView titleTextView;
        private final TextView priceTextView;
        private final TextView descriptionTextView;
        private final TextView categoryTextView;
        private final LinearLayout row_linearlayout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.productItem = itemView.findViewById(R.id.product_item);
            this.avatarImageView = itemView.findViewById(R.id.avatar);
            this.idTextView = itemView.findViewById(R.id.product_id);
            this.titleTextView = itemView.findViewById(R.id.product_title);
            this.priceTextView = itemView.findViewById(R.id.product_price);
            descriptionTextView = itemView.findViewById(R.id.product_description);
            categoryTextView = itemView.findViewById(R.id.product_category);
            row_linearlayout = itemView.findViewById(R.id.linearLayout);
        }

        public void bindData(int id,String title, double price,String description,String category,String image) throws IOException {
            idTextView.setText(String.valueOf(id));
            titleTextView.setText(title);
            priceTextView.setText(String.format(Locale.US, "$%.2f", price));
            descriptionTextView.setText(description);
            categoryTextView.setText(category);

            Glide.with(context)
                    .load(Uri.parse(image))
                    .into(avatarImageView);
        }
    }
}


//package com.example.easysaletest.adapter;

//import android.net.Uri;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.example.easysaletest.R;
//import com.example.easysaletest.constants.Constants;
//import com.example.easysaletest.models.Product;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
//
//    private List<Product> productList = new ArrayList<>();
//    private final DiffUtil.ItemCallback<Product> diffCallback;
//
//    public ProductAdapter(DiffUtil.ItemCallback<Product> diffCallback) {
//        this.diffCallback = diffCallback;
//    }
//
//    public static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK =
//            new DiffUtil.ItemCallback<Product>() {
//                @Override
//                public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
//                    return oldItem.getId() == (newItem.getId());
//                }
//
//                @Override
//                public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
//                    return oldItem.equals(newItem);
//                }
//            };
//
//    @NonNull
//    @Override
//    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
//        return new ProductViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//        Product product = productList.get(position);
//        if (product != null) {
//            try {
//                holder.bindData(
//                        product.getId(),
//                        product.getTitle(),
//                        product.getPrice(),
//                        product.getDescription(),
//                        product.getCategory(),
//                        product.getImage()
//                );
//            } catch (IOException e) {
//                Log.e(Constants.PRODUCTS_ADAPTER, Constants.ERROR_BINDING_PRODCUT_DATA, e);
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return productList.size();
//    }
//
//    public Product getProduct(int position) {
//        return productList.get(position);
//    }
//
//    public void updateProductList(List<Product> newProductList) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//            @Override
//            public int getOldListSize() {
//                return productList.size();
//            }
//
//            @Override
//            public int getNewListSize() {
//                return newProductList.size();
//            }
//
//            @Override
//            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                return diffCallback.areItemsTheSame(productList.get(oldItemPosition), newProductList.get(newItemPosition));
//            }
//
//            @Override
//            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                return diffCallback.areContentsTheSame(productList.get(oldItemPosition), newProductList.get(newItemPosition));
//            }
//        });
//
//        productList.clear();
//        productList.addAll(newProductList);
//        diffResult.dispatchUpdatesTo(this);
//    }
//
//    public static class ProductViewHolder extends RecyclerView.ViewHolder {
//
//        private final ImageView avatarImageView;
//        private final TextView idTextView;
//        private final TextView titleTextView;
//        private final TextView priceTextView;
//        private final TextView descriptionTextView;
//        private final TextView categoryTextView;
//        private final LinearLayout rowLinearLayout;
//
//        public ProductViewHolder(@NonNull View itemView) {
//            super(itemView);
//            avatarImageView = itemView.findViewById(R.id.avatar);
//            idTextView = itemView.findViewById(R.id.product_id);
//            titleTextView = itemView.findViewById(R.id.product_title);
//            priceTextView = itemView.findViewById(R.id.product_price);
//            descriptionTextView = itemView.findViewById(R.id.product_description);
//            categoryTextView = itemView.findViewById(R.id.product_category);
//            rowLinearLayout = itemView.findViewById(R.id.linearLayout);
//        }
//
//        public void bindData(int id, String title, double price, String description, String category, String image) throws IOException {
//            idTextView.setText(String.valueOf(id));
//            titleTextView.setText(title);
//            priceTextView.setText(String.format(Locale.US, "$%.2f", price));
//            descriptionTextView.setText(description);
//            categoryTextView.setText(category);
//
//            Glide.with(itemView.getContext())
//                    .load(Uri.parse(image))
//                    .into(avatarImageView);
//        }
//    }
//}






package ru.realityfamily.examprepeare.Models;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.realityfamily.examprepeare.R;

public class Buyer {
    private int id;
    private int want_to_buy_products;
    private List<String> cart;
    private double sum;
    private Store store;
    private String status;
    private Context context;

    public Buyer(int id, int want_to_buy_products, Context context) {
        this.id = id;
        this.want_to_buy_products = want_to_buy_products;
        this.cart = new ArrayList<>();
        this.sum = 0.0;
        this.context = context;
    }

    public void enterTheShop(Store new_store) {
       store = new_store;
       status = context.getString(R.string.go_to_store);
        SystemClock.sleep(2000);
    }

    public void addProduct(int product_id) {
        Product product = store.getProduct(product_id);
        Log.d("tut_product", String.valueOf(product==null));
        if (product!=null){
            cart.add(product.getName());
            sum+=product.getPrice();
            Log.d("tut_product", product.getName());
            Log.d("tut_sum", String.valueOf(sum));
        }
        want_to_buy_products--;
        status = context.getString(R.string.select_product);
        SystemClock.sleep(3000);
    }

    public void payForProducts(){
        status = context.getString(R.string.pay_products);
        SystemClock.sleep(1000);
    }

    public void outStore(){
        status = context.getString(R.string.out_store);
        SystemClock.sleep(1000);
    }

    public int getWant_to_buy_products() {
        return want_to_buy_products;
    }

    public double getSum() {
        return sum;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public synchronized List<String> getCart() {
        return cart;
    }
}

package ru.realityfamily.examprepeare.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Store {
    private int id;
    private Map<Product, Integer> products;


    public Store(int id, List<Product> new_products) {
        this.id = id;
        products = new HashMap<>();

        for (Product p : new_products) {
            addProduct(p);
        }
    }

//    public boolean checkProduct(int id) {
//        for (Product product: products.keySet()){
//            if (product.getId()==id ){
//                return true;
//            }
//        }
//        return false;
//    }

    public Product getProduct(int id) {
        for (Product product:products.keySet()){
            if (product.getId() == id && products.get(product)>0){
                return product;
            }
        }
        return null;
    }

    private void addProduct(Product product) {
        products.put(product, new Random().nextInt()%5+1);
    }

    public List<Product> getProducts() {
        // ваш код здесь

        return null;
    }

    public int getId() {
        return id;
    }
}

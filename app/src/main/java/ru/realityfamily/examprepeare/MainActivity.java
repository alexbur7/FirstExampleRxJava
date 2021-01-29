package ru.realityfamily.examprepeare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.realityfamily.examprepeare.Models.Buyer;
import ru.realityfamily.examprepeare.Models.Product;
import ru.realityfamily.examprepeare.Models.Store;
import ru.realityfamily.examprepeare.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AtomicInteger countBuyers = new AtomicInteger(20);
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        List<Buyer> buyers = new ArrayList<>();
        for (int i=0; i<20;i++){
            buyers.add(new Buyer(i,Math.abs(new Random().nextInt()%5)+1,this));
        }
        binding.setCountBuyers(countBuyers.get());
        Collections.shuffle(buyers);
        workObservable(buyers.subList(0,5),1,binding.store1);
        workObservable(buyers.subList(5,10),2,binding.store2);
        workObservable(buyers.subList(10,15),3,binding.store3);
        workObservable(buyers.subList(15,20),4,binding.store4);
    }


    private void workObservable(List<Buyer> buyers, int idStore, TextView storeText){
        Observable<Buyer> observable = Observable.fromIterable(buyers)
                .observeOn(Schedulers.newThread());
        final Store store = setStore(idStore);
        DisposableObserver<Buyer> observer = new DisposableObserver<Buyer>() {
            @Override
            public void onNext(@NonNull Buyer buyer) {
                buyer.enterTheShop(store);
                workWithBuyer(storeText,buyer,store);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                runOnUiThread(()->
                storeText.setText("автомат отпахал своё"));
            }
        };
        observable.subscribe(observer);

        PublishSubject<Buyer> subject = PublishSubject.create();
        observable.subscribe(subject);
        subject.subscribe(observer);
    }

    private void workWithBuyer(TextView storeText, Buyer buyer, Store store){
        buyer.enterTheShop(store);
        setText(storeText, buyer,false);
        while(buyer.getWant_to_buy_products()>0) {
            buyer.addProduct(Math.abs(new Random().nextInt() % 7));
            setText(storeText, buyer,false);
        }
        buyer.payForProducts();
        setText(storeText, buyer,false);
        buyer.outStore();
        setText(storeText, buyer,true);
    }

    @SuppressLint("SetTextI18n")
    private synchronized void setText(TextView storeText, Buyer buyer, boolean isOut){
        runOnUiThread(()-> {
            //binding.countBuyer.setText();
            if (isOut) {
                binding.setCountBuyers(countBuyers.decrementAndGet());
            }
            storeText.setText(buyer.getId() + "\nстатус: " + buyer.getStatus() + "\nвыбрал продукты: " + buyer.getCart());
        });
    }

    private Store setStore(int idStore){
        List<Product> products = new ArrayList<>();
        products.add(new Product(0,"Молоко",61));
        products.add(new Product(1,"Бумага",610));
        products.add(new Product(2,"Хлеб",19));
        products.add(new Product(3,"Сыр",315));
        products.add(new Product(4,"Колбаса",1999));
        products.add(new Product(5,"Жувачка",2));
        products.add(new Product(6,"Чипсы",40));
        return  new Store(idStore, products);
    }
}
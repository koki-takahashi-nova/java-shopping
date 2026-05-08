package com.example.shopping.repository;

import com.example.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);

    // 商品名に指定した文字列が含まれる商品を大文字・小文字を区別せずに検索する
    List<Product> findByNameContainingIgnoreCase(String name);

    // 価格が指定した最低価格から最高価格の範囲内にある商品を検索する
    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    // 価格が指定した最低価格以上の商品を検索する
    List<Product> findByPriceGreaterThanEqual(double minPrice);

    // 価格が指定した最高価格以下の商品を検索する
    List<Product> findByPriceLessThanEqual(double maxPrice);

    // 商品名の部分一致と価格帯の両方で絞り込んで商品を検索する
    List<Product> findByNameContainingIgnoreCaseAndPriceBetween(String name, double minPrice, double maxPrice);

    // 商品名の部分一致と最低価格以上で絞り込んで商品を検索する
    List<Product> findByNameContainingIgnoreCaseAndPriceGreaterThanEqual(String name, double minPrice);

    // 商品名の部分一致と最高価格以下で絞り込んで商品を検索する
    List<Product> findByNameContainingIgnoreCaseAndPriceLessThanEqual(String name, double maxPrice);
}
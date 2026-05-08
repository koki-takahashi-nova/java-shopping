package com.example.shopping.service;

import com.example.shopping.entity.Product;
import com.example.shopping.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        Product laptop = new Product();
        laptop.setId(1L);
        laptop.setName("Laptop");
        laptop.setPrice(999.99);
        laptop.setCategory("Electronics");

        Product mouse = new Product();
        mouse.setId(2L);
        mouse.setName("Mouse");
        mouse.setPrice(29.99);
        mouse.setCategory("Electronics");

        mockProducts = Arrays.asList(laptop, mouse);
    }

    // キーワード・最低価格・最高価格すべて指定
    @Test
    void searchProducts_WithKeywordAndPriceRange_CallsCorrectRepository() {
        when(productRepository.findByNameContainingIgnoreCaseAndPriceBetween("laptop", 100.0, 1000.0))
                .thenReturn(Collections.singletonList(mockProducts.get(0)));

        List<Product> result = productService.searchProducts("laptop", 100.0, 1000.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByNameContainingIgnoreCaseAndPriceBetween("laptop", 100.0, 1000.0);
        verifyNoMoreInteractions(productRepository);
    }

    // キーワードと最低価格のみ
    @Test
    void searchProducts_WithKeywordAndMinPrice_CallsCorrectRepository() {
        when(productRepository.findByNameContainingIgnoreCaseAndPriceGreaterThanEqual("mouse", 20.0))
                .thenReturn(Collections.singletonList(mockProducts.get(1)));

        List<Product> result = productService.searchProducts("mouse", 20.0, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByNameContainingIgnoreCaseAndPriceGreaterThanEqual("mouse", 20.0);
        verifyNoMoreInteractions(productRepository);
    }

    // キーワードと最高価格のみ
    @Test
    void searchProducts_WithKeywordAndMaxPrice_CallsCorrectRepository() {
        when(productRepository.findByNameContainingIgnoreCaseAndPriceLessThanEqual("mouse", 50.0))
                .thenReturn(Collections.singletonList(mockProducts.get(1)));

        List<Product> result = productService.searchProducts("mouse", null, 50.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByNameContainingIgnoreCaseAndPriceLessThanEqual("mouse", 50.0);
        verifyNoMoreInteractions(productRepository);
    }

    // キーワードのみ
    @Test
    void searchProducts_WithKeywordOnly_CallsCorrectRepository() {
        when(productRepository.findByNameContainingIgnoreCase("laptop"))
                .thenReturn(Collections.singletonList(mockProducts.get(0)));

        List<Product> result = productService.searchProducts("laptop", null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByNameContainingIgnoreCase("laptop");
        verifyNoMoreInteractions(productRepository);
    }

    // 最低価格・最高価格のみ（キーワードなし）
    @Test
    void searchProducts_WithPriceRangeOnly_CallsCorrectRepository() {
        when(productRepository.findByPriceBetween(20.0, 100.0))
                .thenReturn(Collections.singletonList(mockProducts.get(1)));

        List<Product> result = productService.searchProducts(null, 20.0, 100.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByPriceBetween(20.0, 100.0);
        verifyNoMoreInteractions(productRepository);
    }

    // 最低価格のみ
    @Test
    void searchProducts_WithMinPriceOnly_CallsCorrectRepository() {
        when(productRepository.findByPriceGreaterThanEqual(500.0))
                .thenReturn(Collections.singletonList(mockProducts.get(0)));

        List<Product> result = productService.searchProducts(null, 500.0, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByPriceGreaterThanEqual(500.0);
        verifyNoMoreInteractions(productRepository);
    }

    // 最高価格のみ
    @Test
    void searchProducts_WithMaxPriceOnly_CallsCorrectRepository() {
        when(productRepository.findByPriceLessThanEqual(50.0))
                .thenReturn(Collections.singletonList(mockProducts.get(1)));

        List<Product> result = productService.searchProducts(null, null, 50.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByPriceLessThanEqual(50.0);
        verifyNoMoreInteractions(productRepository);
    }

    // パラメータなし（全件取得）
    @Test
    void searchProducts_WithNoParameters_ReturnsAllProducts() {
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> result = productService.searchProducts(null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    // エッジケース：空文字キーワード → キーワードなし扱い
    @Test
    void searchProducts_WithEmptyKeyword_TreatsAsNoKeyword() {
        when(productRepository.findByPriceBetween(10.0, 100.0)).thenReturn(mockProducts);

        List<Product> result = productService.searchProducts("", 10.0, 100.0);

        assertNotNull(result);
        verify(productRepository).findByPriceBetween(10.0, 100.0);
        verifyNoMoreInteractions(productRepository);
    }

    // エッジケース：空白のみのキーワード → キーワードなし扱い
    @Test
    void searchProducts_WithWhitespaceOnlyKeyword_TreatsAsNoKeyword() {
        when(productRepository.findByPriceBetween(10.0, 100.0)).thenReturn(mockProducts);

        List<Product> result = productService.searchProducts("   ", 10.0, 100.0);

        assertNotNull(result);
        verify(productRepository).findByPriceBetween(10.0, 100.0);
        verifyNoMoreInteractions(productRepository);
    }

    // エッジケース：結果が空リスト
    @Test
    void searchProducts_WhenNoMatch_ReturnsEmptyList() {
        when(productRepository.findByNameContainingIgnoreCase("nonexistent"))
                .thenReturn(Collections.emptyList());

        List<Product> result = productService.searchProducts("nonexistent", null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findByNameContainingIgnoreCase("nonexistent");
    }

    // エッジケース③：maxPrice < minPrice（価格の大小が逆転）→ IllegalArgumentException をスロー
    @Test
    void searchProducts_WithInvertedPriceRange_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> productService.searchProducts(null, 1000.0, 10.0));
        assertEquals("minPrice must be <= maxPrice", ex.getMessage());
        verifyNoInteractions(productRepository);
    }

    // エッジケース④：minPrice に負値 → IllegalArgumentException をスロー
    @Test
    void searchProducts_WithNegativeMinPrice_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> productService.searchProducts(null, -1.0, 100.0));
        assertEquals("minPrice must be >= 0", ex.getMessage());
        verifyNoInteractions(productRepository);
    }

    // エッジケース⑤：maxPrice に負値 → IllegalArgumentException をスロー
    @Test
    void searchProducts_WithNegativeMaxPrice_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> productService.searchProducts(null, null, -1.0));
        assertEquals("maxPrice must be >= 0", ex.getMessage());
        verifyNoInteractions(productRepository);
    }

    // エッジケース⑥：前後に空白を含むキーワード → トリムされてリポジトリに渡される
    @Test
    void searchProducts_WithPaddedKeyword_TrimsBeforePassingToRepository() {
        when(productRepository.findByNameContainingIgnoreCase("laptop"))
                .thenReturn(Collections.singletonList(mockProducts.get(0)));

        List<Product> result = productService.searchProducts("  laptop  ", null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        // "  laptop  " ではなく "laptop" でリポジトリが呼ばれることを確認
        verify(productRepository).findByNameContainingIgnoreCase("laptop");
        verifyNoMoreInteractions(productRepository);
    }

    // 境界値：minPrice == maxPrice（同値）→ 有効な入力として findByPriceBetween が呼ばれる
    @Test
    void searchProducts_WithEqualMinAndMaxPrice_CallsPriceBetween() {
        when(productRepository.findByPriceBetween(100.0, 100.0))
                .thenReturn(Collections.singletonList(mockProducts.get(0)));

        List<Product> result = productService.searchProducts(null, 100.0, 100.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByPriceBetween(100.0, 100.0);
        verifyNoMoreInteractions(productRepository);
    }

    // 境界値：minPrice = 0.0 → バリデーションを通過して正常に検索される
    @Test
    void searchProducts_WithZeroMinPrice_DoesNotThrow() {
        when(productRepository.findByPriceGreaterThanEqual(0.0))
                .thenReturn(mockProducts);

        List<Product> result = productService.searchProducts(null, 0.0, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findByPriceGreaterThanEqual(0.0);
        verifyNoMoreInteractions(productRepository);
    }
}

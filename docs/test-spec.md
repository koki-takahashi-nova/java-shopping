# テスト仕様書 - ProductService

対象クラス: `com.example.shopping.service.ProductService`  
テストクラス: `com.example.shopping.service.ProductServiceTest`  
作成日: 2026-05-08

---

## テストケース一覧

| テストID | テストケース名 | 前提条件 | 操作 | 期待結果 |
|----------|---------------|----------|------|----------|
| TC-001 | キーワード・最低価格・最高価格すべて指定して検索 | `ProductRepository` がモック済み。モック商品として Laptop (¥999.99) と Mouse (¥29.99) が存在する | `searchProducts("laptop", 100.0, 1000.0)` を呼び出す | 結果は null でなく件数が 1 件。`findByNameContainingIgnoreCaseAndPriceBetween("laptop", 100.0, 1000.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-002 | キーワードと最低価格のみ指定して検索 | `ProductRepository` がモック済み | `searchProducts("mouse", 20.0, null)` を呼び出す | 結果は null でなく件数が 1 件。`findByNameContainingIgnoreCaseAndPriceGreaterThanEqual("mouse", 20.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-003 | キーワードと最高価格のみ指定して検索 | `ProductRepository` がモック済み | `searchProducts("mouse", null, 50.0)` を呼び出す | 結果は null でなく件数が 1 件。`findByNameContainingIgnoreCaseAndPriceLessThanEqual("mouse", 50.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-004 | キーワードのみ指定して検索 | `ProductRepository` がモック済み | `searchProducts("laptop", null, null)` を呼び出す | 結果は null でなく件数が 1 件。`findByNameContainingIgnoreCase("laptop")` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-005 | 最低価格・最高価格のみ指定して検索（キーワードなし） | `ProductRepository` がモック済み | `searchProducts(null, 20.0, 100.0)` を呼び出す | 結果は null でなく件数が 1 件。`findByPriceBetween(20.0, 100.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-006 | 最低価格のみ指定して検索 | `ProductRepository` がモック済み | `searchProducts(null, 500.0, null)` を呼び出す | 結果は null でなく件数が 1 件。`findByPriceGreaterThanEqual(500.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-007 | 最高価格のみ指定して検索 | `ProductRepository` がモック済み | `searchProducts(null, null, 50.0)` を呼び出す | 結果は null でなく件数が 1 件。`findByPriceLessThanEqual(50.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-008 | パラメータをすべて null にして全件取得 | `ProductRepository` がモック済み。`findAll()` がモック商品 2 件を返す | `searchProducts(null, null, null)` を呼び出す | 結果は null でなく件数が 2 件。`findAll()` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-009 | 空文字キーワードはキーワードなしとして扱われる | `ProductRepository` がモック済み | `searchProducts("", 10.0, 100.0)` を呼び出す | 結果は null でない。キーワードなし扱いとなり `findByPriceBetween(10.0, 100.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-010 | 空白のみのキーワードはキーワードなしとして扱われる | `ProductRepository` がモック済み | `searchProducts("   ", 10.0, 100.0)` を呼び出す | 結果は null でない。キーワードなし扱いとなり `findByPriceBetween(10.0, 100.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-011 | 検索結果が 0 件の場合は空リストを返す | `ProductRepository` がモック済み。`findByNameContainingIgnoreCase("nonexistent")` が空リストを返す | `searchProducts("nonexistent", null, null)` を呼び出す | 結果は null でなく空リスト。`findByNameContainingIgnoreCase("nonexistent")` が1回呼ばれる |
| TC-012 | 最高価格 < 最低価格（逆転）の場合は例外をスロー | `ProductRepository` がモック済み | `searchProducts(null, 1000.0, 10.0)` を呼び出す | `IllegalArgumentException` がスローされ、メッセージは `"minPrice must be <= maxPrice"`。リポジトリメソッドは一切呼ばれない |
| TC-013 | 最低価格に負値を指定した場合は例外をスロー | `ProductRepository` がモック済み | `searchProducts(null, -1.0, 100.0)` を呼び出す | `IllegalArgumentException` がスローされ、メッセージは `"minPrice must be >= 0"`。リポジトリメソッドは一切呼ばれない |
| TC-014 | 最高価格に負値を指定した場合は例外をスロー | `ProductRepository` がモック済み | `searchProducts(null, null, -1.0)` を呼び出す | `IllegalArgumentException` がスローされ、メッセージは `"maxPrice must be >= 0"`。リポジトリメソッドは一切呼ばれない |
| TC-015 | 前後に空白を含むキーワードはトリムしてリポジトリに渡す | `ProductRepository` がモック済み | `searchProducts("  laptop  ", null, null)` を呼び出す | 結果は null でなく件数が 1 件。`findByNameContainingIgnoreCase("laptop")` (トリム済み) が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-016 | 最低価格と最高価格が同値の場合は有効な入力として検索 | `ProductRepository` がモック済み | `searchProducts(null, 100.0, 100.0)` を呼び出す | 結果は null でなく件数が 1 件。`findByPriceBetween(100.0, 100.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |
| TC-017 | 最低価格に 0.0 を指定した場合はバリデーションを通過して正常に検索 | `ProductRepository` がモック済み。`findByPriceGreaterThanEqual(0.0)` がモック商品 2 件を返す | `searchProducts(null, 0.0, null)` を呼び出す | 例外はスローされず、結果は null でなく件数が 2 件。`findByPriceGreaterThanEqual(0.0)` が1回呼ばれ、それ以外のリポジトリメソッドは呼ばれない |

# 商品検索機能 機能仕様書

## 1. 機能概要

ショッピングサイトにおける商品の検索・絞り込み機能。ユーザーはキーワード・最低価格・最高価格を組み合わせて商品を検索できる。また、価格帯ごとのクイックフィルターも提供する。

---

## 2. エンドポイント一覧

| URL | HTTPメソッド | 説明 |
|-----|-------------|------|
| `/` | GET | 全商品一覧を表示する（トップページ） |
| `/search` | GET | キーワード・価格範囲による商品検索 |
| `/category/{category}` | GET | カテゴリ別商品一覧を表示する |
| `/filter/low` | GET | クイックフィルター：10,000円以下の商品を表示 |
| `/filter/mid` | GET | クイックフィルター：10,001円〜50,000円の商品を表示 |
| `/filter/high` | GET | クイックフィルター：50,001円以上の商品を表示 |

---

## 3. リクエストパラメータ

### `GET /search`

| パラメータ名 | 型 | 必須/任意 | 説明 |
|------------|-----|----------|------|
| `keyword` | String | 任意 | 商品名の部分一致検索キーワード（大文字小文字を区別しない）。前後の空白はトリムされる |
| `minPrice` | Double | 任意 | 検索対象の最低価格（円）。0以上の値を指定する |
| `maxPrice` | Double | 任意 | 検索対象の最高価格（円）。0以上の値を指定する |

### `GET /category/{category}`

| パラメータ名 | 型 | 必須/任意 | 説明 |
|------------|-----|----------|------|
| `category` | String | 必須 | 絞り込むカテゴリ名（パスパラメータ） |

### `GET /filter/low` / `GET /filter/mid` / `GET /filter/high`

パラメータなし。価格範囲はサーバー側で固定値を使用する。

| フィルター | 適用される価格範囲 |
|-----------|-----------------|
| `/filter/low` | 0円 〜 10,000円 |
| `/filter/mid` | 10,001円 〜 50,000円 |
| `/filter/high` | 50,001円 以上 |

---

## 4. レスポンス

### 返却データ（Modelに設定される属性）

#### `GET /` （トップページ）

| 属性名 | 型 | 説明 |
|-------|-----|------|
| `products` | `List<Product>` | 全商品のリスト |

#### `GET /search`

| 属性名 | 型 | 説明 |
|-------|-----|------|
| `products` | `List<Product>` | 検索結果の商品リスト |
| `resultCount` | int | 検索結果の件数 |
| `keyword` | String | 入力されたキーワード（画面への引き渡し用） |
| `minPrice` | Double | 入力された最低価格（画面への引き渡し用） |
| `maxPrice` | Double | 入力された最高価格（画面への引き渡し用） |

#### `GET /category/{category}`

| 属性名 | 型 | 説明 |
|-------|-----|------|
| `products` | `List<Product>` | 該当カテゴリの商品リスト |
| `category` | String | 指定されたカテゴリ名（画面への引き渡し用） |

#### `GET /filter/low`

| 属性名 | 型 | 説明 |
|-------|-----|------|
| `products` | `List<Product>` | 10,000円以下の商品リスト |
| `resultCount` | int | 検索結果の件数 |
| `maxPrice` | Double | `10000.0`（固定） |

#### `GET /filter/mid`

| 属性名 | 型 | 説明 |
|-------|-----|------|
| `products` | `List<Product>` | 10,001円〜50,000円の商品リスト |
| `resultCount` | int | 検索結果の件数 |
| `minPrice` | Double | `10001.0`（固定） |
| `maxPrice` | Double | `50000.0`（固定） |

#### `GET /filter/high`

| 属性名 | 型 | 説明 |
|-------|-----|------|
| `products` | `List<Product>` | 50,001円以上の商品リスト |
| `resultCount` | int | 検索結果の件数 |
| `minPrice` | Double | `50001.0`（固定） |

### 画面遷移先

すべての検索・フィルターエンドポイントは `index` テンプレート（`templates/index.html`）を返却する。

---

## 5. ビジネスロジック

### 検索条件の組み合わせパターン（`ProductService#searchProducts`）

`keyword`・`minPrice`・`maxPrice` の有無に応じて、以下の 8 通りの処理が行われる。

| keyword | minPrice | maxPrice | 使用されるリポジトリメソッド | 説明 |
|:-------:|:--------:|:--------:|--------------------------|------|
| あり | あり | あり | `findByNameContainingIgnoreCaseAndPriceBetween` | キーワード一致かつ価格範囲内 |
| あり | あり | なし | `findByNameContainingIgnoreCaseAndPriceGreaterThanEqual` | キーワード一致かつ最低価格以上 |
| あり | なし | あり | `findByNameContainingIgnoreCaseAndPriceLessThanEqual` | キーワード一致かつ最高価格以下 |
| あり | なし | なし | `findByNameContainingIgnoreCase` | キーワード一致のみ |
| なし | あり | あり | `findByPriceBetween` | 価格範囲内のみ |
| なし | あり | なし | `findByPriceGreaterThanEqual` | 最低価格以上のみ |
| なし | なし | あり | `findByPriceLessThanEqual` | 最高価格以下のみ |
| なし | なし | なし | `findAll` | 全商品を返す |

**補足：**
- `keyword` は前後の空白がトリムされ、空文字列・空白のみの場合は「なし」として扱われる。
- キーワード検索は大文字小文字を区別しない（`IgnoreCase`）。

---

## 6. エラーケース

| 条件 | 発生する例外 | メッセージ |
|------|------------|-----------|
| `minPrice` が 0 未満 | `IllegalArgumentException` | `minPrice must be >= 0` |
| `maxPrice` が 0 未満 | `IllegalArgumentException` | `maxPrice must be >= 0` |
| `minPrice` が `maxPrice` より大きい | `IllegalArgumentException` | `minPrice must be <= maxPrice` |

> **注意：** 現状、上記例外はコントローラー側でキャッチ・ハンドリングされていない。フロントエンドのバリデーションまたはグローバル例外ハンドラーによる対処が推奨される。

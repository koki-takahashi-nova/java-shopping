# Java Shopping

## プロジェクト概要

Spring Boot で構築したシンプルなオンラインショッピングアプリケーションです。
商品の閲覧・検索・カート管理・注文確定までの一連の購買フローをサポートします。
データベースにはインメモリ型の H2 を採用しており、追加インフラなしですぐに起動・試用できます。

---

## 技術スタック

| カテゴリ | 技術・バージョン |
|---|---|
| 言語 | Java 17 |
| フレームワーク | Spring Boot 3.2.3 |
| Web | Spring MVC |
| テンプレートエンジン | Thymeleaf |
| ORM / DB アクセス | Spring Data JPA (Hibernate) |
| データベース | H2 (インメモリ) |
| バリデーション | Spring Validation (jakarta.validation) |
| コード生成 | Lombok |
| ビルドツール | Gradle 8.8 |
| テスト | JUnit 5 / Spring Boot Test |

---

## セットアップ手順

### 前提条件

- Java 17 以上がインストールされていること
- インターネット接続（初回ビルド時に Gradle が依存ライブラリを自動ダウンロードします）

### リポジトリのクローン

```bash
git clone https://github.com/nova-system-com/java-shopping.git
cd java-shopping
```

### 依存ライブラリの取得（任意）

初回起動前に依存関係を明示的に解決したい場合は以下を実行します。

```bash
./gradlew dependencies
```

---

## 起動方法

### アプリケーションの起動

```bash
./gradlew bootRun
```

起動後、ブラウザで以下の URL にアクセスしてください。

- **トップページ**: http://localhost:8080/
- **H2 コンソール** (DB 確認用): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:shoppingdb`
  - ユーザー名: `sa`
  - パスワード: (空欄)

### テストの実行

```bash
./gradlew test
```

### ビルド (JAR 生成)

```bash
./gradlew bootJar
```

生成された JAR は `app/build/libs/` に出力されます。

---

## 主な機能一覧

### 商品閲覧・検索

| 機能 | エンドポイント | 説明 |
|---|---|---|
| 商品一覧表示 | `GET /` | 全商品をトップページに表示 |
| キーワード・価格検索 | `GET /search` | 商品名部分一致・最低/最高価格での絞り込み |
| カテゴリ別表示 | `GET /category/{category}` | 指定カテゴリの商品一覧を表示 |
| 価格帯フィルター（低） | `GET /filter/low` | 10,000 円以下の商品を表示 |
| 価格帯フィルター（中） | `GET /filter/mid` | 10,001 円〜50,000 円の商品を表示 |
| 価格帯フィルター（高） | `GET /filter/high` | 50,001 円以上の商品を表示 |

### カート管理

| 機能 | エンドポイント | 説明 |
|---|---|---|
| カート表示 | `GET /cart` | カートの中身と合計金額を表示 |
| カートに追加 | `POST /cart/add/{id}` | 指定商品をカートに追加（同一商品は数量+1） |
| カートから削除 | `POST /cart/remove/{id}` | 指定商品をカートから削除 |

### 注文

| 機能 | エンドポイント | 説明 |
|---|---|---|
| 注文フォーム表示 | `GET /checkout` | 顧客情報入力フォームを表示 |
| 注文確定 | `POST /order` | 顧客情報を保存し注文を確定、カートをクリア |
| 注文完了画面 | `GET /order/confirmation` | 注文完了メッセージを表示 |

### 初期データ

アプリケーション起動時に `data.sql` から以下カテゴリのサンプル商品 10 件が自動投入されます。

- 電化製品（スマートフォン、ノートパソコン、ワイヤレスイヤホン、デジタルカメラ、スマートウォッチ）
- 家電（コーヒーメーカー）
- 衣類（スニーカー）
- バッグ（バックパック）
- PC 周辺機器（キーボード、マウス）

---

## ディレクトリ構成

```
java-shopping/
├── gradlew                  # Gradle ラッパー (Linux/macOS)
├── gradlew.bat              # Gradle ラッパー (Windows)
├── settings.gradle          # Gradle ルート設定
├── gradle/
│   ├── libs.versions.toml   # 依存ライブラリバージョン管理
│   └── wrapper/
│       └── gradle-wrapper.properties
├── docs/
│   ├── api-spec.yaml        # OpenAPI 3.0 仕様書
│   ├── er-diagram.md        # ER 図 (Mermaid)
│   └── feature-spec.md      # 機能仕様書
└── app/
    ├── build.gradle         # アプリモジュールのビルド設定
    └── src/
        ├── main/
        │   ├── java/com/example/shopping/
        │   │   ├── ShoppingApplication.java     # Spring Boot エントリポイント
        │   │   ├── controller/
        │   │   │   └── ShoppingController.java  # 全エンドポイントを管理
        │   │   ├── service/
        │   │   │   ├── ProductService.java       # 商品検索ロジック
        │   │   │   └── OrderService.java         # 注文処理ロジック
        │   │   ├── entity/
        │   │   │   ├── Product.java              # 商品エンティティ
        │   │   │   ├── Customer.java             # 顧客エンティティ
        │   │   │   ├── Order.java                # 注文エンティティ
        │   │   │   └── OrderDetail.java          # 注文明細エンティティ
        │   │   ├── model/
        │   │   │   ├── Cart.java                 # セッションスコープカート
        │   │   │   └── CartItem.java             # カートアイテム
        │   │   └── repository/
        │   │       ├── ProductRepository.java    # 商品リポジトリ (JPA)
        │   │       ├── CustomerRepository.java   # 顧客リポジトリ (JPA)
        │   │       └── OrderRepository.java      # 注文リポジトリ (JPA)
        │   └── resources/
        │       ├── application.properties        # アプリ設定 (DB・JPA・Thymeleaf)
        │       ├── data.sql                      # 初期サンプルデータ
        │       └── templates/
        │           ├── index.html                # 商品一覧・検索結果ページ
        │           ├── cart.html                 # カートページ
        │           ├── checkout.html             # 注文フォームページ
        │           └── orderConfirmation.html    # 注文完了ページ
        └── test/
            └── java/com/example/shopping/
                └── service/
                    └── ProductServiceTest.java   # ProductService 単体テスト
```
- [ ] 在庫管理機能の追加
- [ ] 支払い処理の実装
- [ ] 注文履歴の表示機能

## ライセンス
MIT License
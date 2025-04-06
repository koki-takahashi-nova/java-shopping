# ショッピングサイト

## 概要
Spring Bootを使用したオンラインショッピングサイトです。商品の閲覧、カートへの追加、注文処理までの一連の購買フローを実現したECサイトです。

## 使用技術
- フロントエンド
  - HTML/CSS
  - Thymeleaf
  - Bootstrap 5.3.0

- バックエンド
  - Java 17
  - Spring Boot 3.2.3
  - Spring Data JPA
  - Lombok

- データベース
  - H2 Database (インメモリ)

- ビルドツール
  - Gradle 8.8

## セットアップ手順
1. リポジトリのクローン
```bash
git clone [your-repository-url]
```

2. プロジェクトディレクトリへ移動
```bash
cd shopping
```

3. Gradleでビルド
```bash
./gradlew build
```

4. アプリケーションの起動
```bash
./gradlew bootRun
```

5. ブラウザでアクセス
```
http://localhost:8080
```

## ディレクトリ構成
```
shopping/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── shopping/
│   │   │   │               ├── entity/      # エンティティクラス
│   │   │   │               ├── repository/  # リポジトリインターフェース
│   │   │   │               ├── model/       # モデルクラス
│   │   │   │               └── controller/  # コントローラークラス
│   │   │   └── resources/
│   │   │       ├── templates/  # Thymeleafテンプレート
│   │   │       ├── static/     # 静的ファイル
│   │   │       └── application.properties  # アプリケーション設定
│   │   └── test/               # テストコード
│   └── build.gradle            # ビルド設定
└── README.md

```

## 機能一覧
### 閲覧系機能
- 商品一覧表示
  - 全商品の表示
  - カテゴリ毎の表示
  - カートへの追加機能

### 購入系機能
- カート機能
  - 商品の追加・削除
  - カート内容の表示
- 注文処理
  - 顧客情報入力
  - 注文確認
  - 注文完了

## データベース設計
### テーブル構成
- customers（顧客情報）
- products（商品情報）
- orders（注文情報）
- order_details（注文明細）

## 今後の改善点
- [ ] ユーザー認証・認可機能の実装
- [ ] 商品検索機能の追加
- [ ] 商品レビュー機能の実装
- [ ] 在庫管理機能の追加
- [ ] 支払い処理の実装
- [ ] 注文履歴の表示機能

## ライセンス
MIT License
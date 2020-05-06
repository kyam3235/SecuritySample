# SecuritySample

[![AndroidStudio](https://img.shields.io/badge/AndroidStudio-v3.6.3-green)](https://developer.android.com/studio)

## 概要

AndroidX Securityのサンプルアプリ

## スクリーンショット

|暗号化ファイル画面|暗号化SharedPreferences画面|
|:---:|:---:|
|![Screenshot_1588757489](https://user-images.githubusercontent.com/11660859/81162864-d51e8600-8fc8-11ea-9738-bdbf658fbc1a.png)|![Screenshot_1588757498](https://user-images.githubusercontent.com/11660859/81162877-d8197680-8fc8-11ea-9505-526fb745faad.png)|

## できること

- 暗号化ファイルの読み書き
- 暗号化した/されたSharedPreferencesの読み書き
- 暗号化されたファイルを読み込む時にユーザ認証を求める

## 備考

- 暗号化されたファイルの書き込みは１度しかできません
  - 2回目以降に暗号化ファイルの書き込みをおこなうとエラーが発生します
  - Securityに関するサンプルなので実装簡略化のため意図的にエラーが発生するようにしています

## 参考

- [Security](https://developer.android.com/jetpack/androidx/releases/security?hl=ja)
- [データをより安全に取り扱う](https://developer.android.com/topic/security/data?hl=ja)

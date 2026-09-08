package vn.example.ch29hilt;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * @HiltAndroidApp sinh ra "container" gốc chứa mọi Module (như AppModule) của
 * cả app — BẮT BUỘC phải có annotation này trên đúng class Application, và
 * khai báo đúng class này trong AndroidManifest.xml (android:name=".MyApplication").
 */
@HiltAndroidApp
public class MyApplication extends Application {
}

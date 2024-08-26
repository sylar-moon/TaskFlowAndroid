package com.example.taskflow;

import static com.example.taskflow.utils.LogPrinter.print;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class RedirectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Получение Intent и URI
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri != null) {
            Log.d("RedirectActivity", "Received URI: " + uri.toString());

            // Обработка URI
            String uriString = uri.toString();
            Toast.makeText(this, "URI: " + uriString, Toast.LENGTH_LONG).show();
        } else {
            Log.e("RedirectActivity", "No data received");
        }

        // Завершение активности
        finish();
    }
}


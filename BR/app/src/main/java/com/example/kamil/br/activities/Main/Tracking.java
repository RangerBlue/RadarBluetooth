package com.example.kamil.br.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kamil.br.R;

/**
 * aktywność służaca do pobrania nazwy urządzenia od użytkownika,
 * które ma być "śledzone"
 */
public class Tracking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
    }
}

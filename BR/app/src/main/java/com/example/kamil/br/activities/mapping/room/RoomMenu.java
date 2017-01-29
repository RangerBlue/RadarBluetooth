package com.example.kamil.br.activities.mapping.room;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;
import java.util.List;

/**
 * Aktywność służaca do wyboru opcji związanych z pokojami czyli
 * utworzenie, wyświetlenie,usunięcie
 * Created by Kamil
 */
public class RoomMenu extends AppCompatActivity {

    /**
     * Przycisk do utworzenia pokoju
     */
    private ImageButton buttonCreate;
    /**
     * Przycisk do edycji pokoju
     */
    private ImageButton buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_menu);

        buttonCreate = (ImageButton) findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        buttonEdit = (ImageButton) findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        ArrayList<Rooms> nowa = (ArrayList<Rooms>) RoomsController.selectAll(getApplicationContext());
        RoomsController.printAllTableToLog(nowa);

    }

    /**
     * Przejście do aktwyności dodawania pokoju
     */
    private void create()
    {
        Intent intent = new Intent(this, RoomCreator.class);
        startActivity(intent);
    }

    /**
     * Przycisk do aktywności edycji pokoju
     */
    private void edit()
    {
        Intent intent = new Intent(this, RoomChoose.class);
        startActivity(intent);
    }
}

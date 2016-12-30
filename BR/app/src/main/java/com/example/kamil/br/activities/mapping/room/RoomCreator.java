package com.example.kamil.br.activities.mapping.room;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kamil.br.R;
import com.example.kamil.br.activities.mapping.path.PathCreator;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Aktywność służaca do dodania nowego pokoju
 * Created by Kamil
 */
public class RoomCreator extends AppCompatActivity {
    /**
     * Przycisk do potwierdzenia dodania pokoju
     */
    private ImageButton confirmButton;
    /**
     * Pole tekstowe na nazwę pokoju
     */
    private EditText roomNameEditText;
    /**
     * Rozwijana lista na typ pokoju
     */
    private Spinner roomTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_creator);

        //odebranie paczki z aktywności procesu
        final int process = getIntent().getIntExtra("process",-1);

        roomNameEditText = (EditText) findViewById(R.id.editTextRoomName);

        roomTypeSpinner = (Spinner) findViewById(R.id.room_creator_spinner);
        //tworzenie Array adaptera ze tablicy wartosci i defaultowych elementów spinera
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.room_type_arrays, android.R.layout.simple_spinner_item);
        //wybranie layoutu który zostanie wykorzystany gdy pojawi sie lista
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(adapter);

        roomTypeSpinner.setSelection(0);

        confirmButton = (ImageButton) findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if( !roomNameEditText.getText().toString().isEmpty() && !RoomsController.ifNameExists(getApplicationContext(), roomNameEditText.getText().toString()))
                {
                    Rooms room = new Rooms();
                    room.setName(roomNameEditText.getText().toString());
                    Log.d("agsg", String.valueOf(getRoomType(roomTypeSpinner.getSelectedItemPosition())));
                    room.setType(getRoomType(roomTypeSpinner.getSelectedItemPosition()));
                    RoomsController roomController = new RoomsController();
                    roomController.insert(room, getApplicationContext());
                    Toast.makeText(getApplicationContext(), R.string.added_room, Toast.LENGTH_SHORT).show();
                    roomNameEditText.setFocusable(false);
                    roomNameEditText.setClickable(false);
                    roomTypeSpinner.setFocusableInTouchMode(false);
                    roomTypeSpinner.setFocusable(false);
                    roomTypeSpinner.setClickable(false);
                    v.setClickable(false);

                    if(process == 1)
                    launchPathEditor(process);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.wrong_input, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Zwraca wspólczynnik w zależności od numeru typu pomieszczenia
     * @param listPosition numer typu pomieszczenia
     * @return wartość współczynnika
     */
    private float getRoomType(int listPosition)
    {
        float returnValue = 0;

        switch (listPosition)
        {
            case 0:
                returnValue =  1.6f;
                break;
            case 1:
                returnValue = 2.7f;
                break;
            case 2:
                returnValue = 2f;
                break;
        }

        return returnValue;
    }

    /**
     * Przechodzi do aktywnosci rysowania kształtu pomieszczenia
     * @param process parametr procesu
     */
    private void launchPathEditor(int process)
    {
        int idRooms = RoomsController.getLastRecord(getApplicationContext()).getIdRooms();
        Intent intent = new Intent(this, PathCreator.class);
        intent.putExtra("id", idRooms);
        intent.putExtra("process", process);
        startActivity(intent);
    }
}

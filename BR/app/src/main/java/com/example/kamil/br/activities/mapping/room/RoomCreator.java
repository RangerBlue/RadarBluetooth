package com.example.kamil.br.activities.mapping.room;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kamil.br.R;
import com.example.kamil.br.activities.mapping.path.PathCreator;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_creator);

        //odebranie paczki z aktywności procesu
        final int process = getIntent().getIntExtra("process",-1);

        roomNameEditText = (EditText) findViewById(R.id.editTextRoomName);



        confirmButton = (ImageButton) findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if( !roomNameEditText.getText().toString().isEmpty() && !RoomsController.ifNameExists(getApplicationContext(), roomNameEditText.getText().toString()))
                {
                    Rooms room = new Rooms();
                    room.setName(roomNameEditText.getText().toString());
                    room.setWalkRatio(0);
                    RoomsController roomController = new RoomsController();
                    roomController.insert(room, getApplicationContext());
                    Toast.makeText(getApplicationContext(), R.string.added_room, Toast.LENGTH_SHORT).show();
                    roomNameEditText.setFocusable(false);
                    roomNameEditText.setClickable(false);
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

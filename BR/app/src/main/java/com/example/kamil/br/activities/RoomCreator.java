package com.example.kamil.br.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;

import java.util.List;

public class RoomCreator extends AppCompatActivity {

    private Button confirmButton;
    private EditText roomNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_creator);

        roomNameEditText = (EditText) findViewById(R.id.editTextRoomName);

        confirmButton = (Button) findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Rooms room = new Rooms();
                room.setName(roomNameEditText.getText().toString());
                RoomsController roomController = new RoomsController();
                roomController.insert(room, getApplicationContext());
            }
        });


        //wypisanie całej bazy
        List<Rooms> all = new RoomsController().selectAll(getApplicationContext());
        for ( Rooms element : all )
        {
            Log.d("Elementy: ", "ID "+String.valueOf(element.getIdRooms()) + " Name "+ element.getName() ) ;
        }



    }
}

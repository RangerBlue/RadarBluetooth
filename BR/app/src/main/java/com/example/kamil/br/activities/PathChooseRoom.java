package com.example.kamil.br.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kamil.br.R;
import com.example.kamil.br.RoomViewerAdapter;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;

public class PathChooseRoom extends AppCompatActivity {

    private ListView list;
    private RoomViewerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_choose_room);

        list = (ListView) findViewById(R.id.listViewPathChooseRoom);
        ArrayList<Rooms> all = (ArrayList<Rooms>) new RoomsController().selectAll(getApplicationContext());

        adapter = new RoomViewerAdapter(this, all);
        list.setAdapter(adapter);

        list.setClickable(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Rooms room = adapter.getItem(position);
                int idToPass = room.getIdRooms();
                Intent intent = new Intent(PathChooseRoom.this, PathCreator.class);
                intent.putExtra("id", idToPass);
                startActivity(intent);
            }
        });
    }
}

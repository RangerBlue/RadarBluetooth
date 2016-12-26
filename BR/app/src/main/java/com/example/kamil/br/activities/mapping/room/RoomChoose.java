package com.example.kamil.br.activities.mapping.room;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.RoomViewerAdapter;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;


import java.util.ArrayList;

/**
 * Aktywność służaca wybrania pokoju, który chcemy edytować: zmienić nazwę, typ lub usunąć
 * Created by Kamil
 */
public class RoomChoose extends AppCompatActivity {

    /**
     * Lista elementów
     */
    private ListView list;
    /**
     * Adapter listy
     */
    private RoomViewerAdapter adapter;
    private static String TAG = RoomChoose.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_viewer);

        list = (ListView) findViewById(R.id.listViewRoomViewer);
        registerForContextMenu(list);
        ArrayList<Rooms> all = (ArrayList<Rooms>) new RoomsController().selectAll(getApplicationContext());

        adapter = new RoomViewerAdapter(this, all);
        list.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //ustawienie menu
        if(v.getId() == R.id.listViewRoomViewer)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.room_choose_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //obłsuga menu
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Rooms room = adapter.getItem(info.position);
        int idToPass = room.getIdRooms();
        switch (item.getItemId())
        {
            case R.id.menu_delete:
                RoomsController controller = new RoomsController();
                controller.deleteRoomAndAllDependences(idToPass, getApplicationContext());
                Toast.makeText(getApplicationContext(), R.string.deleted_room, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "delete");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}

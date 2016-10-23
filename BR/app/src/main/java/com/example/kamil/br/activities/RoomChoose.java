package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.RoomViewerAdapter;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;


import java.util.ArrayList;

/**
 * fdffgdfgdfgfdssg
 */
public class RoomChoose extends AppCompatActivity {

    private ListView list;
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
        if(v.getId() == R.id.listViewRoomViewer)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.room_choose_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Rooms room = adapter.getItem(info.position);
        int idToPass = room.getIdRooms();
        switch (item.getItemId())
        {
            case R.id.menu_show:
                Intent intent = new Intent(RoomChoose.this, PathViewer.class);
                intent.putExtra("id", idToPass);
                startActivity(intent);
                Log.d(TAG, "show");
                return true;
            case R.id.menu_edit:
                Log.d(TAG, "edit");
                return true;
            case R.id.menu_delete:
                RoomsController controller = new RoomsController();
                controller.deleteRoomAndAllDependences(idToPass, getApplicationContext());
                Log.d(TAG, "delete");
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}

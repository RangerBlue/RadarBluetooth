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
import com.example.kamil.br.activities.mapping.measurement.MeasurementChooseOption;
import com.example.kamil.br.activities.mapping.measurement.MeasurementChooseRoom;
import com.example.kamil.br.activities.mapping.measurement.MeasurementCreate;
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

    /**
     * Parametr procesu
     */
    private int process;

    private static String TAG = RoomChoose.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_viewer);

        //odebranie paczki z aktywności procesu
        process = getIntent().getIntExtra("process",-1);

        list = (ListView) findViewById(R.id.listViewRoomViewer);
        registerForContextMenu(list);
        ArrayList<Rooms> all = (ArrayList<Rooms>) new RoomsController().selectAll(getApplicationContext());

        adapter = new RoomViewerAdapter(this, all);
        list.setAdapter(adapter);

        if( process == 1)
        {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Rooms room = adapter.getItem(position);
                    int idToPass = room.getIdRooms();
                    Intent intent = new Intent(RoomChoose.this, MeasurementCreate.class);
                    intent.putExtra("idRooms", idToPass);
                    intent.putExtra("process", process);
                    startActivity(intent);
                }
            });
        }

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
                controller.deleteRoomAndAllDependencies(idToPass, getApplicationContext());
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

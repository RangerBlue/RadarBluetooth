package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.RoomViewerAdapter;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Measurements;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;
import java.util.Date;

public class MeasurementChooseRoom extends AppCompatActivity {

    private ListView list;
    private RoomViewerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_choose_room);

        list = (ListView) findViewById(R.id.listViewMeasurementChooseRoom);
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
                Intent intent = new Intent(MeasurementChooseRoom.this, MeasurementCreate.class);
                intent.putExtra("idRooms", idToPass);
                createMeasurement(idToPass);
                intent.putExtra("idMeasurements", getLastRecord().getIdMeasurements());


                startActivity(intent);
            }
        });
    }

    public void createMeasurement(int idToPass)
    {
        Measurements measurement = new Measurements();
        Date data = new Date();
        measurement.setName(data.toString());
        measurement.setIdRooms(idToPass);
        MeasurementsController controller = new MeasurementsController();
        controller.insert(measurement, getApplicationContext());

    }

    public Measurements getLastRecord()
    {
        MeasurementsController controller = new MeasurementsController();
        Measurements measurement = controller.getLastRecord(getApplicationContext());
        return measurement;
    }
}

package com.example.kamil.br;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

public class DataBaseValues extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mAdapterKursor;
    private ListView mLista;

    DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_v2);
        Button przyciskDelete = (Button) findViewById(R.id.button_delete_records);
       // mLista = (ListView) findViewById(R.id.lista);
        //uruchomLoader();
        //mLista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
       // mLista.setMultiChoiceModeListener(wyborWieluElementowListy());

        List<Record> elements = db.getAll();

        for (Record item : elements) {
            String log = "Id: " + item.getId()
                    + " ,Name: " + item.getName()
                    + " ,RSSI: " + item.getRssi()
                    + " ,Time: " + item.getTime()
                    + " ,Direction: " + item.getDirection();
            // Writing shops  to log
            Log.d("Item: : ", log);
        }


        przyciskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                List<Record> elements = db.getAll();
                for (Record item : elements)
                {
                    db.delete(item);
                    Toast.makeText(DataBaseValues.this, "Usunięto rekordy", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private AbsListView.MultiChoiceModeListener wyborWieluElementowListy()
    {
        AbsListView.MultiChoiceModeListener choiceModeListener = new AbsListView.MultiChoiceModeListener()
        {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.pasek_kontekstowy, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.kasuj_menu:
                        kasujZaznaczone();
                        return true;
                }

                return false;
            }

            private void kasujZaznaczone()
            {
                long [] zaznaczone = mLista.getCheckedItemIds();

                for(int i=0; i<zaznaczone.length;i++){
                    getContentResolver().delete(ContentUris.withAppendedId(Provider.URI_ZAWARTOSC, zaznaczone[i]), null, null);
                }

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
            {
                // TODO Auto-generated method stub

            }
        };
        return choiceModeListener;
    }

    @SuppressWarnings("deprecation")
    private void uruchomLoader()
    {
        getLoaderManager().initLoader(0, null,this);

        String [] mapujZ = new String[]{DBHandler.NAME,DBHandler.RSSI,DBHandler.TIME};
        int [] mapujDo = new int[]{R.id.Kol1,R.id.Kol2, R.id.Kol3};

        mAdapterKursor = new SimpleCursorAdapter(this, R.layout.list_row, null, mapujZ, mapujDo);
        mLista.setAdapter(mAdapterKursor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projekcja = {DBHandler.NAME,DBHandler.RSSI,DBHandler.TIME};
        CursorLoader loaderKursor = new CursorLoader(this,Provider.URI_ZAWARTOSC,projekcja,null,null,null);
        return loaderKursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mAdapterKursor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mAdapterKursor.swapCursor(null);
    }

}

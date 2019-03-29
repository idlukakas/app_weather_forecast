package br.com.bossini.fatec_ipi_noite_weather_forecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText locationEditText;
    private ListView cityListView;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private ArrayList<String> list;

    private ArrayAdapter<String> cityAdapter;
    String city;

    private Integer listPos;

    List<String> al = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = new String();

        cityListView = findViewById(R.id.cityListView);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("cities");
        list = new ArrayList<>();
        cityAdapter = new ArrayAdapter<String>(this,R.layout.city_list_item,R.id.cityTextView, list);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer i = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    city = ds.getValue().toString();
                    list.add(city);
                    al.add(i, ds.getKey());
                    i++;
                }
                cityListView.setAdapter(cityAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        registerForContextMenu(cityListView);

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MainActivity.this, WeatherActivity.class);

                String val = (String) parent.getItemAtPosition(position);

//                myIntent.putExtra("city", "São Paulo");
                myIntent.putExtra("city", val);
                startActivity(myIntent);
            }
        });

        cityListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                registerForContextMenu( arg0 );
                openContextMenu( arg0 );

                listPos = pos;

                return true;
            }
        });


        locationEditText = findViewById(R.id.locationEditText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((v)->{
            String cidade = locationEditText.
                    getEditableText().toString();

//            String key = ref.push().getKey();

            DatabaseReference tasksRef = ref.push();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    tasksRef.setValue(cidade);
                    tasksRef.setValue(cidade);
                    list.add(cidade);
                    Toast.makeText(MainActivity.this,"Data inserted...", Toast.LENGTH_LONG).show();
                    cityListView.setAdapter(cityAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
//        if (v.getId()==R.id.list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
//        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String itemCity = cityAdapter.getItem(listPos);
        cityAdapter.remove(itemCity);
        cityAdapter.notifyDataSetChanged();
//        ref.child(al.get(listPos)).removeValue();
        return true;
    }
}

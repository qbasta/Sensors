package com.example.sensors;

import static com.example.sensors.SensorDetailsActivity.EXTRA_SENSOR_TYPE_PARAMETER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter adapter;
    private boolean subtitleVisible = false;
    private final List<Integer> favourSensors = Arrays.asList(Sensor.TYPE_GRAVITY, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
    public static final int SENSOR_DETAILS_ACTIVITY_REQUEST_CODE = 1;

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensor_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.subtitle_visible);
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.subtitle_visible:
                subtitleVisible = !subtitleVisible;
                invalidateOptionsMenu();
                updateSubtitle();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder {
        private final TextView sensorNameTextView;
        private final ImageView sensorTypeImageView;
        private Sensor sensor;



        public SensorHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.sensor_list_item,parent,false));
            sensorNameTextView = itemView.findViewById(R.id.sensor_name);
            sensorTypeImageView = itemView.findViewById(R.id.sensor_image);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorNameTextView.setText(sensor.getName());
            View itemContainer = itemView.findViewById(R.id.list_item_sensor);
            if (favourSensors.contains(sensor.getType())) {
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE_PARAMETER, sensor.getType());
                    startActivityForResult(intent, SENSOR_DETAILS_ACTIVITY_REQUEST_CODE);
                });
            } else if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE_PARAMETER, sensor.getType());
                    startActivityForResult(intent, SENSOR_DETAILS_ACTIVITY_REQUEST_CODE);});
            }
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensorList;

        public SensorAdapter(List<Sensor> sensorList) {
            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new SensorHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }

    public void updateSubtitle() {
        List<Sensor> sensorList = getSensorList();
        int sensorsCount = 0;
        for (Sensor sensor : sensorList) {
            sensorsCount++;
        }
        String subtitle = getString(R.string.sensors_count, sensorsCount);
        if (!subtitleVisible) {
            subtitle = null;
        }
        getSupportActionBar().setSubtitle(subtitle);
    }
}
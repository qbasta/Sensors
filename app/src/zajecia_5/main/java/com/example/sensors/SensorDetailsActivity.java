package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String EXTRA_SENSOR_TYPE_PARAMETER = "EXTRA_SENSOR_TYPE";

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorNameTextView;

    @Override
    protected void onStart() {
        super.onStart();

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_GRAVITY:
                sensorNameTextView.setText(getResources().getString(R.string.Gravity_sensor_label, currentValue));
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                sensorNameTextView.setText(getResources().getString(R.string.Geomagnetic_rotation_vector_sensor_label, currentValue));

            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorNameTextView = findViewById(R.id.sensor_gravity_label);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        if (sensor == null) {
            sensorNameTextView.setText(R.string.missing_sensor);
        }
    }
}
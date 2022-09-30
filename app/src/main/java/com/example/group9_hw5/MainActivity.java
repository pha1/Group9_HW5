/**
 * Homework 5
 * Group9_HW5
 * Phi Ha
 * Srinath Dittakavi
 */

package com.example.group9_hw5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.group9_hw5.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    final static String TAG = "test";

    Handler handler;

    // Create Thread Pool
    ExecutorService threadPool;
    final static int THREAD_POOL_SIZE = 2;
    int complexityHeavyWork = 0;

    ArrayList<Double> numbers = new ArrayList<>();

    SeekBar seekBarComplexity;
    Button buttonGenerate;
    TextView complexity;
    TextView currentProgress;
    ListView listView;
    ArrayAdapter<Double> adapter;
    ConstraintLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        complexity = binding.textViewComplexity;
        currentProgress = binding.currentProgress;

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                if (message.getData().containsKey("numbers")){
                    numbers = (ArrayList<Double>) message.getData().getSerializable("numbers");
                    adapter.notifyDataSetChanged();
                }
                if (message.getData().containsKey("progress")) {
                    int progress = message.getData().getInt("progress");
                    Log.d(TAG, "handleMessage: Progress" + progress);
                    ((TextView) currentProgress).setText(progress);
                }
                return true;
            }
        });

        listView = binding.listView;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, numbers);

        listView.setAdapter(adapter);

        // SeekBar function
        seekBarComplexity = binding.seekBarComplexity;
        seekBarComplexity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                complexityHeavyWork = i;
                complexity.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        progressLayout = binding.progressLayout;

        buttonGenerate = binding.buttonGenerate;
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
                progressLayout.setVisibility(View.VISIBLE);
                threadPool.execute(new HeavyWork(complexityHeavyWork));
            }
        });
    }
}
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
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.group9_hw5.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    final static String TAG = "test";

    // Handler used for communicating between threads
    public static Handler handler;

    // Create Thread Pool
    ExecutorService threadPool;
    final static int THREAD_POOL_SIZE = 2;

    // Data used to determine how many times to call the HeavyWork method
    int complexityHeavyWork = 0;

    // The average of all numbers retrieved
    double average;

    ArrayList<Double> numbers = new ArrayList<>();

    // UI components used throughout the Main Activity
    ProgressBar progressBar;
    SeekBar seekBarComplexity;
    Button buttonGenerate;
    TextView complexity;
    TextView currentProgress;
    TextView maxProgress;
    TextView textViewAverage;
    ListView listView;
    ArrayAdapter<Double> adapter;

    // Used to make the progress section visible when clicking on "Generate"
    ConstraintLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Main Activity");

        complexity = binding.textViewComplexity;

        // Progress Layout Components
        currentProgress = binding.currentProgress;
        textViewAverage = binding.textViewRetrievedAverage;
        progressBar = binding.progressBar;

        // Initial ListView
        listView = binding.listView;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, numbers);
        listView.setAdapter(adapter);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Log.d(TAG, "handleMessage: " + message.getData().getInt("progress"));
                // Update number array
                if (message.getData().containsKey("numbers")){
                    numbers = (ArrayList<Double>) message.getData().getSerializable("numbers");

                    // This method call updates the ListView
                    updateList(numbers);
                }
                // Update progress information
                if (message.getData().containsKey("progress")) {
                    int progress = message.getData().getInt("progress") + 1;

                    // Update the current progress and max progress
                    progressBar.setMax(complexityHeavyWork);
                    progressBar.setProgress(progress);
                    currentProgress.setText(String.valueOf(progress));
                }

                // Calculate the average of the numbers in the array
                for (double number: numbers) {
                    average += number;
                }
                average = average/numbers.size();
                textViewAverage.setText(String.valueOf(average));

                return true;
            }
        });

        // SeekBar function
        // Sets the complexity values
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
        maxProgress = binding.maxProgress;

        // Set thread pool size to 2
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUi();

                // Make the progress visible
                progressLayout.setVisibility(View.VISIBLE);
                threadPool.execute(new HeavyWork(complexityHeavyWork));
            }
        });
    }

    /**
     * This method is used to update the ListView every time the ArrayList is updated.
     * @param numbers The updated ArrayList
     */
    public void updateList(ArrayList<Double> numbers) {

        listView = binding.listView;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, numbers);
        listView.setAdapter(adapter);

        // Testing
        Log.d(TAG, "Array size: " + this.numbers.size());
    }

    /**
     * This is used to reset the UI Data in the case the user clicks "Generate" more than once.
     */
    public void clearUi() {
        // Clear the ArrayList
        average = 0;

        // Clear the ListView UI
        adapter.clear();
        adapter.notifyDataSetChanged();

        // Reset the progress layout
        textViewAverage.setText("");
        currentProgress.setText("0");
        maxProgress.setText(String.valueOf(complexityHeavyWork));
        progressBar.setProgress(0);
    }
}
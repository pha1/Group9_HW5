/**
 * Homework 5
 * Group9_HW5
 * Phi Ha
 * Srinath Dittakavi
 */

package com.example.group9_hw5;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;


public class HeavyWork extends MainActivity implements Runnable{
	public static final long DELAY_MILLI_SECS = 2000;

	final static String TAG = "test";

	private int progress;
	private double number;
	private ArrayList<Double> numbers = new ArrayList<>();
	int complexityNumber;

	public HeavyWork(int complexityNumber) {
		this.complexityNumber = complexityNumber;
	}

	public static double getNumber(){
		addSomeDelay(DELAY_MILLI_SECS);
		Random rand = new Random();
		return rand.nextDouble();
	}

	private static void addSomeDelay(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sends a message to the Main Thread with the progress and ArrayList
	 * @param progress The current number retrieved
	 * @param numbers The ArrayList of numbers retrieved
	 */
	private void sendMessage(int progress, ArrayList<Double> numbers){
		Bundle bundle = new Bundle();
		bundle.putInt("progress", (Integer)progress);
		bundle.putSerializable("numbers",(ArrayList<Double>) numbers);
		Message message = new Message();
		message.setData(bundle);
		MainActivity.handler.sendMessage(message);
		Log.d(TAG, "sendMessage: " + message.getData().getInt("progress"));
	}

	@Override
	public void run() {
		for (int i = 0; i < complexityNumber; i++) {
			// Count the progress
			progress = i;
			// Get the number and add it to the ArrayList
			number = getNumber();
			numbers.add(number);
			// Send the message to the Main Thread
			sendMessage(progress, numbers);
			// Testing
			Log.d(TAG, "run: " + progress);
		}
	}
}
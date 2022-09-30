/**
 * Homework 5
 * Group9_HW5
 * Phi Ha
 * Srinath Dittakavi
 */

package com.example.group9_hw5;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


public class HeavyWork extends MainActivity implements Runnable{
	public static final long DELAY_MILLI_SECS = 2000;

	final static String TAG = "test";

	private Handler handler = new Handler();
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

	public void sendMessage(int progress, ArrayList<Double> numbers){
		Bundle bundle = new Bundle();
		bundle.putInt("progress", progress);
		bundle.putSerializable("numbers", numbers);
		Message message = new Message();
		message.setData(bundle);
		handler.sendMessage(message);
	}

	@Override
	public void run() {
		for (int i = 0; i < complexityNumber; i++) {
			progress = i;
			number = getNumber();
			numbers.add(number);
			sendMessage(progress, numbers);
			Log.d(TAG, "run: " + progress);
		}
	}
}
package Ejercicios;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopwatchLogic {

	//Atributos 
	    private Timer timer;
	    private long startTime;
	    private long stoppedTime = 0;
	    private boolean running = false;
	    private TimeUpdateListener listener;

	    public StopwatchLogic(TimeUpdateListener listener) {
	        this.listener = listener;
	        timer = new Timer(1000, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if (running) {
	                    long elapsedTime = System.currentTimeMillis() - startTime + stoppedTime;
	                    listener.onTimeUpdate(formatTime(elapsedTime));
	                }
	            }
	        });
	    }

	    public void start() {
	        if (!running) {
	            startTime = System.currentTimeMillis();
	            timer.start();
	            running = true;
	        }
	    }

	    public void stop() {
	        if (running) {
	            stoppedTime += System.currentTimeMillis() - startTime;
	            timer.stop();
	            running = false;
	        }
	    }

	    public void reset() {
	        timer.stop();
	        startTime = 0;
	        stoppedTime = 0;
	        running = false;
	        listener.onTimeUpdate("00:00:00");
	    }

	    private String formatTime(long millis) {
	        long seconds = (millis / 1000) % 60;
	        long minutes = (millis / (1000 * 60)) % 60;
	        long hours = (millis / (1000 * 60 * 60));
	        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	    }

	    public interface TimeUpdateListener {
	        void onTimeUpdate(String time);
	    }
	}



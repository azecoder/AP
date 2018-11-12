/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moisturesensor;

import controller.Controller;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author azecoder
 */
public class MoistureSensor {
    
    public final int MAX_HUMIDITY = 100;
    public final int MIN_HUMIDITY = 0;
    
    boolean decreasing = false; // default is false for temperature
    int currentHumidity = 0; // between 0 and 100, default is zero depend on decreasing default value

    private final PropertyChangeSupport property_listener;
    public final String PROP_SAMPLE_PROPERTY = "Property Changed..";
    
    Controller c = new Controller();
    
    public MoistureSensor() {
        property_listener = new PropertyChangeSupport(this);
    }
    
    public void fireHumidityChange(int lastHumidity, int curHumidity) {
        property_listener.firePropertyChange(PROP_SAMPLE_PROPERTY, lastHumidity, curHumidity);
    }

    public void start() {
        // this method is for activating the sensors
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random rand = new Random();
                // generate random number
                int rand_number = rand.nextInt(20);
                // sending this random number for checking and changing humidity of the sensor
                setCurrentHumidity(currentHumidity, rand_number);
            }
        }, 0, 1000); // send data to sensors each second
    }

    public void setCurrentHumidity(int curHumidity, int changeVal) {
        int lastHumidity = curHumidity;
        if(decreasing) {
            if(curHumidity < changeVal) {
                changeVal = curHumidity;
            }
            curHumidity -= changeVal;
        } else {
            if(curHumidity + changeVal > MAX_HUMIDITY) {
                changeVal = MAX_HUMIDITY - curHumidity;
            }
            curHumidity += changeVal;
        }
        currentHumidity = curHumidity;
        // set locHumidity for controller
        c.setLocHumidity(currentHumidity);
        // and get controller status for irrigation
        decreasing = c.getOn() ^ true;
        fireHumidityChange(lastHumidity, currentHumidity);
    }

    public int getCurrentHumidity() {
        // get current humidity of the moisture sensor
        return currentHumidity;
    }

    public boolean getDecreasingStatus() {
        // get decreaing status of the moisture sensor
        return decreasing;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        property_listener.addPropertyChangeListener(propertyChangeListener);
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}

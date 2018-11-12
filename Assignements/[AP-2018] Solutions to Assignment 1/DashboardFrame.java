/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irrigationdashboard;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import controller.Controller;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import moisturesensor.MoistureSensor;

/**
 *
 * @author azecoder
 */
public class DashboardFrame extends JFrame {
        
    MoistureSensor moisturesensor = new MoistureSensor();
    Controller controller = new Controller();
        
    // Creating the Dashboard Frame
    JFrame frame = new JFrame("Dashboard Frame");
    JLabel humidity_label = new JLabel();
    JLabel status_label = new JLabel();
    JButton sensing_button = new JButton();
    
    public DashboardFrame() {
        init();
    }
    
    private void init() {
        // simple frame 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 400));
        frame.setLocation(new Point(100, 100));
        
        // simple label
        humidity_label.setText("Current Humidity : ");
        humidity_label.setBounds(new Rectangle(10, 10, 250, 50));
        frame.add(humidity_label);
        
        // simple label
        status_label.setText("Decreasing status : ");
        status_label.setBounds(new Rectangle(10, 40, 250, 50));
        frame.add(status_label);
        
        // simple button
        sensing_button.setText("Start Sensing");
        sensing_button.setSize(new Dimension(150, 30));
        sensing_button.setLocation(new Point(10, 100));
        frame.add(sensing_button);
        
        // adding action listener for button
        sensing_button.addActionListener(this::buttonActionPerformed);
        
        frame.setLayout(null);
        frame.setVisible(true);
    }
    
    public void buttonActionPerformed(ActionEvent ae) {
        // we start the sensor when click on the button
        moisturesensor.start();
    
        moisturesensor.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            // get value of the current humidity from moisture sensor and set it our label for humidity
            humidity_label.setText("Current Humidity : " + String.valueOf(moisturesensor.getCurrentHumidity()));
            // get value of the decreasing status from moisture sensor and set it our label for status
            status_label.setText("Decreasing status : " + String.valueOf(moisturesensor.getDecreasingStatus()));
        });
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DashboardFrame program = new DashboardFrame();
        program.init();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author azecoder
 */
public class Controller {

    boolean on;
    int locHumidity;
    
    private void checkOn() {
        if(locHumidity < 30) {
            setOn(true);
        } else if(locHumidity >= 90) {
            setOn(false);
        }
    }

    private void setOn(boolean val) {
        on = val;
    }
    
    public boolean getOn() {
        return on;
    }
    
    public void setLocHumidity(int val) {
        locHumidity = val;
        checkOn();
    }
    
    public int getLocHumidity() {
        return locHumidity;
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}

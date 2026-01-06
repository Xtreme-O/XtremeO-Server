package org.example.xtremo.ui;

import javafx.event.ActionEvent;

/** * 
 * @author Abdelrahman
 */
public class ActionHandler {
    
    private boolean stopButtonActive = false;
    
    public boolean handleStopAction() {
        stopButtonActive = !stopButtonActive;
        System.out.println("Stop button toggled: " + (stopButtonActive ? "ACTIVE (Green)" : "INACTIVE (Red)"));
        return stopButtonActive;
    }
    
 
    public boolean isStopButtonActive() {
        return stopButtonActive;
    }
    
    public void handleRestartAction() {
        System.out.println("Restart action triggered");
    }
    
    public void handleSwitchToSecondary(ActionEvent event) {
        
    }
    
    public void handleMatchmakingToggle(boolean enabled) {
        // TODO: Implement matchmaking toggle logic
        System.out.println("Matchmaking " + (enabled ? "enabled" : "disabled"));
    }
    
    public void handleChatToggle(boolean enabled) {
        // TODO: Implement chat toggle logic
        System.out.println("Chat " + (enabled ? "enabled" : "disabled"));
    }
    
    public void handleMaintenanceToggle(boolean enabled) {
        // TODO: Implement maintenance mode toggle logic
        System.out.println("Maintenance mode " + (enabled ? "enabled" : "disabled"));
    }
}


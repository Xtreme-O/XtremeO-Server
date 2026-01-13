package org.example.xtremo.ui;

import javafx.event.ActionEvent;
import org.example.xtremo.logging.LoggerManager;
import org.example.xtremo.network.Server;
import static org.example.xtremo.network.Server.logger;



/** *
 * @author Abdelrahman
 */
public class ActionHandler {
    
    private boolean stopButtonActive = false;
    
    public boolean handleStopAction() {
        stopButtonActive = !stopButtonActive;
        Server.stop();
        logger.info("Server has been stopped, no additional clients can join right now");
        return stopButtonActive;
    }
    
 
    public boolean isStopButtonActive() {
        if (!stopButtonActive) {
            Server.start();
            logger.info("Server has been started, additional clients can join now");

        }
        return stopButtonActive;
    }

    public void handleRestartAction(){
       Server.restart();
    }
    
    public void handleSwitchToSecondary(ActionEvent event) {
        
    }
    
    public void handleMatchmakingToggle(boolean enabled) {
        // TODO: Implement matchmaking toggle logic
        System.out.println("Matchmaking " + (enabled ? "enabled" : "disabled"));
    }
    
    public void handleChatToggle(boolean enabled) {
        // TODO: Implement chat toggle logic
        StringBuilder bigMessage = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            bigMessage.append("This is a very long test message segment #").append(i).append(" ");
        }

        LoggerManager.getInstance().success("Chat Toggled : " + bigMessage.toString());
        System.out.println("Chat " + (enabled ? "enabled" : "disabled"));
    }
    
    public void handleMaintenanceToggle(boolean enabled) {
        // TODO: Implement maintenance mode toggle logic
        System.out.println("Maintenance mode " + (enabled ? "enabled" : "disabled"));
    }
}


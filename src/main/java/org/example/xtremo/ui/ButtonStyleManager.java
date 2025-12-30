
package org.example.xtremo.ui;

import javafx.animation.RotateTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *
 * @author Abdelrahman
 */
public class ButtonStyleManager {
    private ButtonStyleManager() {};
    
    private static RotateTransition loadingAnimation;
    
    public static void updateStopButtonStyle(Button stopBtn, FontIcon icon,boolean isActive) {
        if (stopBtn == null) {
            return;
        }
        
        stopBtn.getStyleClass().removeAll("btn-danger", "btn-success");
        
        if (isActive) {
            stopBtn.getStyleClass().add("btn-success");
            stopBtn.setText("Start");
            
            if (icon != null) {
                icon.setIconLiteral("fas-play");
            }
        } else {
            stopBtn.getStyleClass().add("btn-danger");
            stopBtn.setText("Stop");
            if (icon != null) {
                icon.setIconLiteral("fas-stop");
            }
        }
    }
    
    public static void showRestartButtonLoading(Button restartBtn, FontIcon icon) {
        if (restartBtn == null) {
            return;
        }
        
        restartBtn.setDisable(true);
        
        if (icon != null) {
            icon.setIconLiteral("fas-spinner");
            
            loadingAnimation = new RotateTransition(Duration.millis(1000), icon);
            loadingAnimation.setByAngle(360);
            loadingAnimation.setCycleCount(RotateTransition.INDEFINITE);
            loadingAnimation.play();
        }
    }
    
    public static void hideRestartButtonLoading(Button restartBtn, FontIcon icon) {
        if (restartBtn == null) {
            return;
        }
        
        restartBtn.setDisable(false);
        
        if (loadingAnimation != null) {
            loadingAnimation.stop();
            loadingAnimation = null;
        }
        
        if (icon != null) {
            icon.setIconLiteral("fas-redo");
            icon.setRotate(0);
        }
    }
}

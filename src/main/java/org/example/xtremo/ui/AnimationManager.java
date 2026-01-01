package org.example.xtremo.ui;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.example.xtremo.ui.animation.*;

/**
 * 
 * @author Abdelrahman
 */
public class AnimationManager {
    
    private final Button referenceNode;
    private final CardAnimator cardAnimator;
    private final ButtonAnimator buttonAnimator;
    
    public AnimationManager(Button referenceNode) {
        this.referenceNode = referenceNode;
        this.cardAnimator = new CardAnimator();
        this.buttonAnimator = new ButtonAnimator();
    }
    
    public void initializeAnimations() {
        if (referenceNode != null && referenceNode.getScene() != null) {
            Parent root = referenceNode.getScene().getRoot();
            cardAnimator.animateStatCards(root);
            buttonAnimator.enhanceButtons(root);
        }
    }
    
    
}

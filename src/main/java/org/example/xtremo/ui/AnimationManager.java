package org.example.xtremo.ui;

import javafx.scene.Parent;
import org.example.xtremo.ui.animation.*;

/**
 * 
 * @author Abdelrahman
 */
public class AnimationManager {
    
    private final Parent root;
    private final CardAnimator cardAnimator;
    private final ButtonAnimator buttonAnimator;
    
    public AnimationManager(Parent root) {
        this.root = root;
        this.cardAnimator = new CardAnimator();
        this.buttonAnimator = new ButtonAnimator();
    }
    
    public void initializeAnimations() {
        cardAnimator.animateStatCards(root);
        buttonAnimator.enhanceButtons(root);
    }
    
    
}

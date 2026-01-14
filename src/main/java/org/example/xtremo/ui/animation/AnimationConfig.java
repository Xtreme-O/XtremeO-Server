package org.example.xtremo.ui.animation;

import javafx.util.Duration;

/**
 * Configuration constants for smooth animations.
 * 
 * @author Abdelrahman
 */
public final class AnimationConfig {
    
    private AnimationConfig() {
    }
    
    public static final Duration FAST = Duration.millis(150);
    
    public static final Duration NORMAL = Duration.millis(250);
    
    public static final Duration SLOW = Duration.millis(400);
    
    public static final Duration VERY_SLOW = Duration.millis(600);
    
    public static final double BUTTON_HOVER_SCALE = 1.03;
    
    public static final double BUTTON_PRESSED_SCALE = 0.97;
    
    public static final double LINK_BUTTON_HOVER_SCALE = 1.02;
    
    public static final double CARD_HOVER_LIFT = -6;
    
    public static final Duration CARD_STAGGER_DELAY = Duration.millis(100);
    
    public static final double CARD_ANIMATION_SPEED = 0.8;
    
}


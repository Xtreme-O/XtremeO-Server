package org.example.xtremo.ui.animation;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;

import java.util.Set;
import javafx.util.Duration;

import static org.example.xtremo.ui.animation.AnimationConfig.*;

/** *
 * @author Abdelrahman
 */
public class CardAnimator {

    public void animateStatCards(Node root) {
        Set<Node> cards = root.lookupAll(".stat-card");
        if (cards == null || cards.isEmpty()) return;

        for (Node card : cards) {
            installHoverAnimation(card, CARD_HOVER_LIFT, NORMAL);
        }
    }

    private void installHoverAnimation(Node node, double liftY, Duration duration) {
        TranslateTransition lift = createTransition(node, liftY, duration);
        TranslateTransition drop = createTransition(node, 0, duration);

        node.setOnMouseEntered(e -> {
            drop.stop();
            lift.playFromStart();
        });

        node.setOnMouseExited(e -> {
            lift.stop();
            drop.playFromStart();
        });
    }

    private TranslateTransition createTransition(Node node, double toY, javafx.util.Duration duration) {
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setToY(toY);
        tt.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));
        return tt;
    }
}

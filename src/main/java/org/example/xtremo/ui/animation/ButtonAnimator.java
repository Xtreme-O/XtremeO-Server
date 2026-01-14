package org.example.xtremo.ui.animation;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.Duration;

import static org.example.xtremo.ui.animation.AnimationConfig.*;

/** * 
 * @author Abdelrahman
 */
public class ButtonAnimator {

    private static final Interpolator SMOOTH =
            Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0);

    private static final Interpolator SHARP =
            Interpolator.SPLINE(0.4, 0.0, 0.2, 1.0);

    public void enhanceButtons(Node root) {
        enhance(root, ".btn-primary", this::installDefaultAnimation);
        enhance(root, ".btn-restart", this::installDefaultAnimation);
        enhance(root, ".btn-danger", this::installDefaultAnimation);
        enhance(root, ".link-btn", this::installLinkAnimation);
    }

    private void enhance(Node root, String selector,
                         java.util.function.Consumer<Button> installer) {

        root.lookupAll(selector).forEach(node -> {
            if (node instanceof Button button) {
                installer.accept(button);
            }
        });
    }

    private void installDefaultAnimation(Button button) {
        ScaleTransition hover = scale(button, BUTTON_HOVER_SCALE, NORMAL, SMOOTH);
        ScaleTransition press = scale(button, BUTTON_PRESSED_SCALE, FAST, SHARP);
        ScaleTransition reset = scale(button, 1.0, NORMAL, SMOOTH);

        button.setOnMouseEntered(e -> play(hover));
        button.setOnMouseExited(e -> play(reset));
        button.setOnMousePressed(e -> play(press));
        button.setOnMouseReleased(e ->
                play(button.isHover() ? hover : reset)
        );
    }


    private void installLinkAnimation(Button button) {
        ScaleTransition hover = scale(button, LINK_BUTTON_HOVER_SCALE, NORMAL, SMOOTH);
        ScaleTransition reset = scale(button, 1.0, NORMAL, SMOOTH);

        button.setOnMouseEntered(e -> play(hover));
        button.setOnMouseExited(e -> play(reset));
    }


    private ScaleTransition scale(Button b, double s, Duration d, Interpolator i) {
        ScaleTransition st = new ScaleTransition(d, b);
        st.setToX(s);
        st.setToY(s);
        st.setInterpolator(i);
        return st;
    }

    private void play(ScaleTransition st) {
        st.stop();
        st.playFromStart();
    }
}

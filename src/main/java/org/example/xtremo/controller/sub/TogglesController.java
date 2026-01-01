
package org.example.xtremo.controller.sub;

import org.controlsfx.control.ToggleSwitch;
import org.example.xtremo.ui.ActionHandler;
/**
 *
 * @author Abdelrahman
 */
public class TogglesController {

    private final ToggleSwitch matchmakingToggle;
    private final ToggleSwitch chatToggle;
    private final ActionHandler actionHandler;

    public TogglesController(ToggleSwitch matchmakingToggle, ToggleSwitch chatToggle, ActionHandler actionHandler) {
        this.matchmakingToggle = matchmakingToggle;
        this.chatToggle = chatToggle;
        this.actionHandler = actionHandler;
    }

    public void initialize() {
        if (matchmakingToggle != null) {
            matchmakingToggle.selectedProperty().addListener((obs, oldVal, newVal) ->
                    actionHandler.handleMatchmakingToggle(newVal));
        }
        if (chatToggle != null) {
            chatToggle.selectedProperty().addListener((obs, oldVal, newVal) ->
                    actionHandler.handleChatToggle(newVal));
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;
import org.example.xtremo.network.protocol.RequestHeader;
import org.example.xtremo.utils.DateTimeGsonAdapter;
import org.example.xtremo.utils.RequestHeaderAdapter;

/**
 *
 * @author wahid
 */
public class PlayerNetworkOperations {

    private PlayerNetworkOperations() {
        throw new IllegalAccessError();
    }

    private static Gson gsonConverter = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(RequestHeader.class, new RequestHeaderAdapter())
            .registerTypeAdapter(LocalDateTime.class, new DateTimeGsonAdapter())
            .create();

    public static Gson getGsonConverter() {
        return PlayerNetworkOperations.gsonConverter;
    }

    public static void sendResponse(ProtocolMessageEnvelope message, DataOutputStream out) throws IOException {
        String responseString = PlayerNetworkOperations.gsonConverter.toJson(message);
        out.writeUTF(responseString);
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.utils;

/**
 *
 * @author wahid
 */
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.example.xtremo.network.protocol.RequestHeader;

public class RequestHeaderAdapter extends TypeAdapter<RequestHeader> {
    @Override
    public void write(JsonWriter out, RequestHeader value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("protocol").value(value.protocol);
        out.name("action").value(value.action);
        out.endObject();
    }

    @Override
    public RequestHeader read(JsonReader in) throws IOException {
        RequestHeader header = new RequestHeader();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "protocol":
                    header.protocol = in.nextString();
                    break;
                case "action":
                    header.action = in.nextString();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        return header;
    }
}

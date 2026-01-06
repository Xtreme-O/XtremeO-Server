package org.example.xtremo.network.protocol;

/**
 *
 * @author wahid
 */

public class ProtocolMessageEnvelope<T> {
    public RequestHeader header;
    T body;

    public ProtocolMessageEnvelope(){
        
    }
    public ProtocolMessageEnvelope(RequestHeader header, T body) {
        this.header = header;
        this.body = body;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public T getBody() {
        return body;
    }
    
}



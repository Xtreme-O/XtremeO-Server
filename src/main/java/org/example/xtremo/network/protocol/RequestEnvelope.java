package org.example.xtremo.network.protocol;

/**
 *
 * @author wahid
 */

public class RequestEnvelope<T> {
    public RequestHeader header;
    T body;

    public RequestEnvelope(){
        
    }
    public RequestEnvelope(RequestHeader header, T body) {
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



package org.example.xtremo.network.protocol;



/**
 *
 * @author wahid
 */
public class RequestHeader {
    public String protocol;
    public String action;
    
    public RequestHeader(){}

    public RequestHeader(String protocol, String action) {
        this.protocol = protocol;
        this.action = action;
    }

    @Override
    public String toString() {
        return "RequestHeader{" + "protocol=" + protocol + ", action=" + action + '}';
    }
    
    
    
    
}
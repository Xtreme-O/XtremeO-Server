package org.example.xtremo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants {
    final static int serverPortNumber = 5555;
    static InetAddress serverId;
//    TODO add the database constanst

    static{
        try{
            serverId = InetAddress.getLocalHost();
        }
        catch(UnknownHostException e){
            System.out.println("utils.Constants.methodName()");
        }
    }
    
    private Constants(){}
    
    
    
    
    
}

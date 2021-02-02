package com.atheesh.rpc;

import com.atheesh.rpcclient.services.HelloWorldImpl;
import javax.xml.ws.Endpoint;

public class Publisher{
    public static void main(String[] args){
        Endpoint.publish("http://localhost:8888/rpc/helloWorld", new HelloWorldImpl());
    }
}
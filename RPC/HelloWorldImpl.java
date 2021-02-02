package com.atheesh.rpcclient.services.impl;

import com.atheesh.rpcclient.services.HelloWorld;
import javax.jws.WebService;

@WebService(endpointInterface = "com.atheesh.rpcclient.services.HelloWorld")
public clas  HelloWorldImpl implements HelloWorld{

    @Override
    public String getHelloworldAsString(String name){
        return "This is the message from server: " + name;
    }

}
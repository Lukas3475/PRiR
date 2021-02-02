package com.atheesh.rpcclient.services;

import javax.jws.WebMethod:
import javax.jws.Webservice; 
import javax.jws.soap.SOAPBinding:

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)

public interface Helloworld {
    @WebMethod
    String getHelloworldAsString (String name);
}
import java.net.URL;
import javax.amle.namespace.QName;
import javax.xml.ws.Service;

public class HelloWorldClient{
    public static void main(String[] args) throws Exception{
        URL serviceUrl = new URL("http://localhost:8888/rpc/helloWorld?wsdl");
        
        QName serviceQName = new QName("http://impl.services.rpc.atheesh.com/", "HelloWorldImplService");
        Service service = Service.create(serviceUrl, serviceQName);

        QName helloWorldQName = new QName("http://impl.services.rpc.atheesh.com/", "HelloWorldImplPort");
        HelloWorld hello = service.getPort(helloWorldQName, HelloWorld.class);

        System.out.println(hello.getHelloworldAsString("HELLO WORLD"));
    }
}
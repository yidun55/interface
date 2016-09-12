package lakala.onLineSearch.Tools;


import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.omg.CORBA.portable.UnknownException;



/**
 * Created by dyh on 2016/7/18.
 */
public class connectAndQuery {

    /*public Client buildClientByTransport() throws UnknownException{
        final String clusterName = "lakala3";
        String strIp = "10.1.80.75";
        String [] ip = strIp.split("\\.");
        byte [] ipBuf = new byte[4];
        for(int i=0; i<4; i++){
            ipBuf[i] = (byte)(Integer.parseInt(ip[i])&0xff);
        }

        Settings settings = Settings.settingsBuilder().put("cluster.name", "lakala3").build();
        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.80.74"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.80.75"), 9300));

        return client;
    }*/


}

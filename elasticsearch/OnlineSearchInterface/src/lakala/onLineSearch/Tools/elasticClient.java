package lakala.onLineSearch.Tools;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by yn on 2016/7/19.
 */
public class elasticClient {
    private static TransportClient client;

    private static Client initEs() throws UnknownHostException {
        Settings settings = Settings.settingsBuilder().put("cluster.name", "lakala3").build();
        client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.80.75"), 9300));
        return client;
    }

    public static TransportClient getClient(){
        return client;
    }
}

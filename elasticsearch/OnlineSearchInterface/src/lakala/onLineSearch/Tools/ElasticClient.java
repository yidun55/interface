package lakala.onLineSearch.Tools;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import org.apache.http.conn.HttpClientConnectionManager;

/**
 * Created by yn on 2016/7/19.
 */
public class elasticClient {
    private static JestClientFactory factory = null;
    static {
        factory = new JestClientFactory();
        HttpClientConfig httpClientConfig = new HttpClientConfig.Builder("http://10.1.80.75:9200")
                .maxTotalConnection(500).connTimeout(150).readTimeout(300).multiThreaded(true).build();
        factory.setHttpClientConfig(httpClientConfig);
    }

    public static JestHttpClient getClient(){

        return  (JestHttpClient) factory.getObject();

    }
}

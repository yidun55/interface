package lakala.onLineSearch.Tools;


import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;



/**
 * Created by dyh on 2016/7/20.
 */


public class ElasticSearchFactory {
    private static JestClientFactory factory = null;
    static {
        factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(
                "http://10.1.80.75:9200")
                .maxTotalConnection(500)
                .connTimeout(150)    //150
                .readTimeout(200)    //200
                .build());
    }

    public static JestHttpClient getClient(){
        return  (JestHttpClient) factory.getObject();
    }
}


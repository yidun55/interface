package lakala.onLineSearch.Tools;


import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;



/**
 * Created by dyh on 2016/7/20.
 */
public class elasticSearchFactory {

    private static JestHttpClient client;

    private elasticSearchFactory(){

    }

    public synchronized static JestHttpClient getClient(){
        if(client == null){
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder(
                    "http://10.1.80.75:9200").multiThreaded(true).build());
            client = (JestHttpClient) factory.getObject();
        }
        return client;
    }
}

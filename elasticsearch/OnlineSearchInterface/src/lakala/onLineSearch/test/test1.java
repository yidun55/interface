package lakala.onLineSearch.test;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * Created by dyh on 2016/7/18.
 * Java原生接口，生成Json查询语句。
 */
public class test1 {
    public static void main(String[] args) {
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("pk_mobile", "123445555");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String queryParamJson = searchSourceBuilder.query(queryBuilder).toString();
        System.out.println(queryParamJson);
    }
}

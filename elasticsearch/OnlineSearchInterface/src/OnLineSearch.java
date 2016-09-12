

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.*;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.index.query.QueryBuilder;




import lakala.onLineSearch.Tools.EsResultProcess;
import lakala.onLineSearch.Tools.ElasticSearchFactory;
import lakala.onLineSearch.Tools.JestResultProcess;
import lakala.onLineSearch.domain.Article;



/**
 * 在线查询
 */

@WebServlet("/onlineSearch")
public class OnLineSearch  extends HttpServlet{

    private static final Logger log = Logger.getLogger(OnLineSearch.class);
//    elasticSearchFactory elasticSearchFactory1 = new elasticSearchFactory();
    private  JestHttpClient client = ElasticSearchFactory.getClient();

    @Override
    public void init() throws ServletException{
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException{

        doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        request.setCharacterEncoding("UTF-8");      //config
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String []> map = request.getParameterMap();
        Map<String, String> mapStr = new HashMap<>();
        for (String  key:map.keySet()) {
            mapStr.put(key, StringUtils.join(map.get(key)));
        }
        Gson gson1 = new GsonBuilder().create();
        String parameters = gson1.toJson(mapStr);
        log.info("host: "+request.getRemoteAddr()+", user-agent: "+
                request.getHeader("user-agent")+", path: "+request.getRequestURI()+", parameters: "
                + parameters);

        try {
            String mobNum = request.getParameter("mobile_num");   //get params
            Integer page = Integer.parseInt(request.getParameter("page"));
            Integer size = Integer.parseInt(request.getParameter("size"));

            //Restful API
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(page*size);   //页码从零开始
            searchSourceBuilder.size(size);
            searchSourceBuilder.sort("trans_date", SortOrder.DESC);
            searchSourceBuilder.query(QueryBuilders.termQuery("mobile_num", mobNum));

//            QueryBuilder query = QueryBuilders.boolQuery()
//                    .must(QueryBuilders.termQuery("mobile_num", mobNum))
//                    .must(QueryBuilders.rangeQuery("trans_date").gte("now-3y"));
//
//            searchSourceBuilder.query(query);

            System.out.print(searchSourceBuilder.toString());
            Search search = new Search.Builder(searchSourceBuilder.toString())  //multiple index or types can be added
                    .addIndex("yxt")
                    .build();
            JestResult result = client.execute(search);
//            String s5 = jestResultProcess.getJsonSource(result);
            JsonObject jsonObject= JestResultProcess.getJsonSource(result);
            jsonObject.addProperty("pageNum", page.toString());
            jsonObject.addProperty("size", size.toString());
//            String s5 = jestResultProcess.getJsonSource(result).toString();
            String s5 = jsonObject.toString();

            out.println(s5);
            out.close();

        }catch (NumberFormatException e){

            log.error("error in parseInt " + e);
            String s = "对不起!您输入的参数有误";
            out.print(s);
            out.close();
        }
    }

    @Override
    public void destroy(){

        super.destroy();
        client.shutdownClient();
    }
}

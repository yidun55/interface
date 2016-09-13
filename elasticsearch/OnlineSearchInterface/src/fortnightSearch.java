
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import io.searchbox.client.http.JestHttpClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.apache.log4j.Logger;

import com.google.gson.*;



import lakala.onLineSearch.Tools.EsResultProcess;
import lakala.onLineSearch.Tools.elasticSearchFactory;
import lakala.onLineSearch.Tools.jestResultProcess;
import lakala.onLineSearch.domain.Article;



/**
 * Created by dyh on 2016/9/2.
 */


@WebServlet("/fortnightSearch")
public class fortnightSearch extends HttpServlet{

    private static final Logger log = Logger.getLogger(onLineSearch.class);
    private  JestHttpClient client = elasticSearchFactory.getClient();


    @Override
    public void init() throws ServletException{

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException{
        //设置响应内容类型

        doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        //log info
        long t1 = System.currentTimeMillis();
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


        request.setCharacterEncoding("UTF-8");      //config
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        try {
            String mobNum = request.getParameter("mobile_num");   //get params
            Integer page = Integer.parseInt(request.getParameter("page"));
            Integer size = Integer.parseInt(request.getParameter("size"));
            if(size<=0||size>50){
                throw new ServletException("=======页面大小参数:size要大于0小于50==========");
            }

            //Restful API
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(page*size);   //页码从零开始
            searchSourceBuilder.size(size);
            searchSourceBuilder.sort("trans_date", SortOrder.DESC);
            QueryBuilder query = QueryBuilders.boolQuery()
                    .must(QueryBuilders.rangeQuery("trans_date").gte("now-14d"))
                    .must(QueryBuilders.termQuery("mobile_num", mobNum));
//        searchSourceBuilder.query(QueryBuilders.termQuery("mobile_num", mobNum));
            searchSourceBuilder.query(query);

            Search search = new Search.Builder(searchSourceBuilder.toString())  //multiple index or types can be added
                    .addIndex("yxt")
                    .build();
            JestResult result = client.execute(search);
            JsonObject jsonObject= jestResultProcess.getJsonSource(result);
            jsonObject.addProperty("pageNum", page.toString());
            jsonObject.addProperty("size", size.toString());

            Gson gson = new GsonBuilder().create();
            String s5 = gson.toJson(jsonObject);
            out.println(s5);
            out.close();
            System.out.print(s5+"-------------------------------------------------------------");
            long t2 = System.currentTimeMillis();

            System.out.print(t2-t1 + "==========================================================");

        }catch (NumberFormatException e){

            String s = "对不起!您输入的参数有误";
//            response.setStatus(400);               //服务器不理解请求
            out.println(s);
            out.close();
            log.error("error in parseInt " + e);
        }catch (ServletException e){

            String s = "对不起!您输入的参数有误";
//            response.setStatus(400);                //服务器不理解请求
            out.println(s+"  "+e.getMessage());
            out.close();
            log.error("error in parameter " + e);
        }
    }

    @Override
    public void destroy(){
        super.destroy();
    }

}

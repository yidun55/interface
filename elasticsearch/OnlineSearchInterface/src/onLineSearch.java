

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import io.searchbox.client.http.JestHttpClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import lakala.onLineSearch.Tools.EsResultProcess;
import lakala.onLineSearch.Tools.elasticSearchFactory;
import lakala.onLineSearch.Tools.jestResultProcess;
import lakala.onLineSearch.domain.Article;



/**
 * 在线查询
 */

@WebServlet("/onlineSearch")
public class onLineSearch  extends HttpServlet{

    private static final Logger log = LoggerFactory.getLogger(onLineSearch.class);
    private  JestHttpClient client = elasticSearchFactory.getClient();

    @Override
    public void init() throws ServletException{
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException{
        //设置响应内容类型
        resp.setContentType("text/html;charset=UTF-8");
        String mobNum = req.getParameter("mobile_num");   //get params

        //Restful API
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("mobile_num", mobNum));

        Search search = new Search.Builder(searchSourceBuilder.toString())  //multiple index or types can be added
                                        .addIndex("yxt*")
                                        .build();
        JestResult result = client.execute(search);
        String s5 = jestResultProcess.getJsonSource(result);

        PrintWriter out = resp.getWriter();

        //设置逻辑实现
//        PrintWriter out = resp.getWriter();
//        SearchHits searchR = response.getHits();
//        ArrayList<String> arr;
//        EsResultProcess p = new EsResultProcess();
//        arr = p.getSource(searchR);
//        String s = StringUtils.join(arr);

        out.println(s5);
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        request.setCharacterEncoding("UTF-8");      //config
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        try {
            String mobNum = request.getParameter("mobile_num");   //get params
            int page = Integer.parseInt(request.getParameter("page"));
            int size = Integer.parseInt(request.getParameter("size"));

            //Restful API
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(page*size);   //页码从零开始
            searchSourceBuilder.size(size);
            searchSourceBuilder.query(QueryBuilders.termQuery("mobile_num", mobNum));

            Search search = new Search.Builder(searchSourceBuilder.toString())  //multiple index or types can be added
                    .addIndex("yxt*")
                    .build();
            JestResult result = client.execute(search);
            String s5 = jestResultProcess.getJsonSource(result);

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
    }
}

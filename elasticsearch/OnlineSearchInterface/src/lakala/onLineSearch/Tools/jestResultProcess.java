package lakala.onLineSearch.Tools;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by dyh on 2016/7/22.
 */
public class jestResultProcess {

    private static final Logger log = LoggerFactory.getLogger(jestResultProcess.class);

    public static String getJsonSource(JestResult result){

        String re = "";
        if(result.isSucceeded()){
            try{
                JsonArray resp = result.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
                ArrayList<String> container = new ArrayList<>();
                for (JsonElement je: resp) {
                    JsonObject te = je.getAsJsonObject().get("_source").getAsJsonObject();
                    JsonObject te1 = rmoveEsTz(te);    //把ES的时间格式做成标准格式
//                    String tmp = je.getAsJsonObject().get("_source").toString();
//                    container.add(tmp);
                    container.add(te1.toString());
                }
                re = StringUtils.join(container);
           }catch (Exception e){
                re = "error in parse es result  " + e.toString();
                log.error("error in parse es result " + e.toString());
            }
        }else {
            re = "对不起！您查询的用户不存在";
        }
        return re;
    }

    private static JsonObject rmoveEsTz(JsonObject source){
        if(source.has("trans_date")){
            String date = source.get("trans_date").toString();
            String s = date.replace("T", " ").split("\\.")[0];
            String fs1 = EsResultProcess.myStrip(s, "Z");
            String fs = StringUtils.strip(fs1,"\"");    //去掉两端的双引号
            source.addProperty("trans_date", fs);
        }
        return source;
    }


}

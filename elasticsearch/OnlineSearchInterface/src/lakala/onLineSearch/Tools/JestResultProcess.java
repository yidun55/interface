package lakala.onLineSearch.Tools;

import java.util.ArrayList;
import java.text.DecimalFormat;


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
public class JestResultProcess {

    private static final Logger log = LoggerFactory.getLogger(JestResultProcess.class);

    public static JsonObject getJsonSource(JestResult result){

//        String re = "";
        JsonObject re = new JsonObject();
        if(result.isSucceeded()){
            try{
                String totalStr = result.getJsonObject().get("hits").getAsJsonObject().get("total").toString();
                JsonArray resp = result.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
                ArrayList<String> container = new ArrayList<>();
                JsonArray jsonArray = new JsonArray();
                for (JsonElement je: resp) {
                    JsonObject te = je.getAsJsonObject().get("_source").getAsJsonObject();
                    JsonObject te1 = rmoveEsTz(te);    //把ES的时间格式做成标准格式
                    te1 = encryption(te1);
                    te1 = toYuan(te1);
                    jsonArray.add(te1);
                    container.add(te1.toString());
                }
//                re = StringUtils.join(container);
                re.add("result", jsonArray);
                re.addProperty("total", totalStr);
                re.addProperty("error", "");
                re.addProperty("status", "101");
            }catch (Exception e){
                String info = "error in parse es result  " + e.toString();
                re.addProperty("result", "");
                re.addProperty("total", "0");
                re.addProperty("error", info);
                re.addProperty("status", "001");
                log.error("error in parse es result " + e.toString());
            }
        }else {
//            re = "对不起！您查询的用户不存在";
            re.addProperty("result", "对不起！您查询的用户不存在");
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

    private static JsonObject toYuan(JsonObject source){

        if(source.has("total_am")){
            DecimalFormat df = new DecimalFormat();
            String style = "0.00";
            df.applyPattern(style);
            String rawS = source.get("total_am").toString();
            String s = StringUtils.strip(rawS ,"\"");
            int total = Integer.parseInt(s);
            double f = total/100.0;
            String tmp = df.format(f);
            tmp = StringUtils.strip(tmp,"\"");
            if(tmp.equals("0.00")) tmp="0";
            source.addProperty("total_am", tmp);
        }
        return source;
    }

    private static JsonObject encryption(JsonObject source){

        if(source.has("cardno")){
            String cardno = source.get("cardno").toString();
            String tmp = "******";
            try {
                tmp = cardno.substring(0, 7) + "******" + cardno.substring(cardno.length() - 5, cardno.length());
                tmp = StringUtils.strip(tmp, "\"");
            }catch (Exception e){

            }
            source.addProperty("cardno", tmp);
        }
        return source;
    }

}

package lakala.onLineSearch.Tools;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;



/**
 * Created by dyh on 2016/7/14.
 */
public class EsResultProcess {
    public Map<String, Object> rmoveEsTz(Map<String, Object> hit){
        /*将es的时间格式转换成普通的格式：2016-04-05T23:13:59.0Z   =》   2016-04-05 23:13:59*/
        if(hit.containsKey("trans_date")){
            String date = hit.get("trans_date").toString();
            String s = date.replace("T", " ").split("\\.")[0];
            String fs = myStrip(s, "Z");
            hit.put("trans_date", fs);
        }
        return hit;
    }

    public static String myStrip(String oldStr, String rm){
        if(oldStr.endsWith(rm)){
            int site = oldStr.lastIndexOf(rm)==-1 ? oldStr.length() : oldStr.lastIndexOf(rm) ;

            String s = oldStr.substring(0, site);
            return s;
        }else{
            return oldStr;
        }
    }

    public ArrayList<String> getSource(SearchHits searchR) {

        Gson gson = new Gson();
        ArrayList<String> arr = new ArrayList<>();
        for (SearchHit hit : searchR) {
            Map<String, Object> hs = hit.getSource();
            Map hs1 = rmoveEsTz(hs);
            String tmp = gson.toJson(hs1);
            arr.add(tmp);
        }

        return arr;
    }
}

package ind.xwm.imooc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class JsonString2JSONObject {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\XuWeiman\\Desktop\\CommodityTable.json");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int readByte;
            while ((readByte = in.read(buffer)) != -1) {
                stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, readByte)));
            }
            in.close();
            System.out.println(stringBuilder.toString());
            String jsonStr = stringBuilder.toString();
            // 过滤掉 html相关内容
            jsonStr = jsonStr.replaceAll("&nbsp;", "");
            jsonStr = jsonStr.replaceAll("&quot;", "\"");
            jsonStr = jsonStr.replaceAll("</?[^>]+>", "");
            System.out.println("==========================================");
            System.out.println(jsonStr);
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            System.out.println(JSON.toJSONString(jsonObject.getJSONArray("columns")));
            System.out.println(JSON.toJSONString(jsonObject.getJSONArray("datas")));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

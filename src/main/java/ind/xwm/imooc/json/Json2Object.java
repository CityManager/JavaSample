package ind.xwm.imooc.json;

import com.alibaba.fastjson.JSON;
import ind.xwm.imooc.json.model.PersonModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by XuWeiman on 2016/11/17.
 * 从文件中读取json字符串，并转化为Object对象
 */
public class Json2Object {
    public static void main(String... args) {
        getJsonFromFile();

    }

    private static void getJsonFromFile() {
        File file = new File(Json2Object.class.getResource("/persion.json").getFile());
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                stringBuilder.append(new String(buffer));
            }
            in.close();
            System.out.println(stringBuilder.toString());
            // json 解析字符串不能存在多余空格，需要将字符串trim一把
            PersonModel person = JSON.parseObject(stringBuilder.toString().trim(), PersonModel.class);
            System.out.println(person);
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

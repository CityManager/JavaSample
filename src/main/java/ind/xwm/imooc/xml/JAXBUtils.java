package ind.xwm.imooc.xml;

import com.alibaba.fastjson.JSON;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Arrays;

/**
 * Created by XuWeiman on 2017/7/6.
 * Xml内容与java bean互换工具
 */
public class JAXBUtils {
    public static <T> String marshal(T t, Class<T> tClass) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
        Marshaller marshaller = jaxbContext.createMarshaller();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.marshal(t, outputStream);
        outputStream.close();
        return new String(outputStream.toByteArray());
    }

    public static <T> T unmarshal(String xmlStr, Class<T> tClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        BufferedReader reader = new BufferedReader(new StringReader(xmlStr));
        return (T) unmarshaller.unmarshal(reader);
    }

    public static void main(String... args) throws JAXBException, IOException {
        String[] strings1 = new String[]{"篮球", "王者荣耀", "品酒"};
        String[] strings2 = new String[]{"跳舞", "唱歌", "画画", "追剧"};
        ClassStudent student = new ClassStudent();
        student.setName("AAA");
        student.setAge(18);
        student.setGender("男");
        student.setAddress("广州大学城");
        student.setHabits(Arrays.asList(strings1));

        ClassStudent student2 = new ClassStudent();
        student2.setName("BBB");
        student2.setAge(18);
        student2.setGender("女");
        student2.setAddress("广州天河");
        student2.setHabits(Arrays.asList(strings2));

        ClassRoom classRoom = new ClassRoom();
        classRoom.setName("三年二班");
        classRoom.setClassStudents(Arrays.asList(student, student2));

        String xmlStr = marshal(classRoom, ClassRoom.class);
        System.out.println("Object to XML: " + xmlStr);
        ClassRoom classRoomFromXml = unmarshal(xmlStr, ClassRoom.class);
        System.out.println("XML to Object:" + JSON.toJSONString(classRoomFromXml));
    }
}

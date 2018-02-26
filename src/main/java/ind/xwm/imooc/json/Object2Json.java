package ind.xwm.imooc.json;

import com.alibaba.fastjson.JSON;
import ind.xwm.imooc.json.model.PersonModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XuWeiman on 2016/11/17.
 * 对象转换json
 */
public class Object2Json {
	public static void main(String... args) throws ParseException {
		PersonModel person = getPerson();
		String personString = JSON.toJSONStringWithDateFormat(person, "yyyy-MM-dd");
		System.out.println(personString);
	}

	private static PersonModel getPerson() throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		PersonModel person = new PersonModel();
		person.setName("xuweiman");
		person.setAge(27);
		person.setCar(null);
		person.setHouse(null);
		person.setWeight(68.6);
		person.setHeight(173.5);
		person.setBirthdate(format.parse("1989-05-20"));
		person.setSchool("仲恺农业工程学院");
		ArrayList<String> major = new ArrayList<String>();
		major.add("自动化");
		major.add("软件开发");
		person.setMajor(major);
		HashMap<String, String> computer = new HashMap<String, String>();
		computer.put("brand", "apple");
		computer.put("type", "macbook pro 2015");
		computer.put("price", "9980.00");
		person.setComputer(computer);
		person.setBankBalance(100000000);
		return person;
	}
}

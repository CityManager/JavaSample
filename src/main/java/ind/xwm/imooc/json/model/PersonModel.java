package ind.xwm.imooc.json.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by XuWeiman on 2016/11/17.
 * java bean
 */
public class PersonModel {
	private String name;
	private int age;
	private double height;
	private double weight;
	private Date birthdate;
	private String school;
	private List<String> major;
	private boolean has_girlfriend;
	private Object car;
	private Object house;
	private Map<String, String> computer;

	@JSONField(serialize = false)
	private double bankBalance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public List<String> getMajor() {
		return major;
	}

	public void setMajor(List<String> major) {
		this.major = major;
	}

	public boolean isHas_girlfriend() {
		return has_girlfriend;
	}

	public void setHas_girlfriend(boolean has_girlfriend) {
		this.has_girlfriend = has_girlfriend;
	}

	public Object getCar() {
		return car;
	}

	public void setCar(Object car) {
		this.car = car;
	}

	public Object getHouse() {
		return house;
	}

	public void setHouse(Object house) {
		this.house = house;
	}

	public Map<String, String> getComputer() {
		return computer;
	}

	public void setComputer(Map<String, String> computer) {
		this.computer = computer;
	}

	public double getBankBalance() {
		return bankBalance;
	}

	public void setBankBalance(double bankBalance) {
		this.bankBalance = bankBalance;
	}


	/*private String name;
	private int age;
	private double height;
	private double weight;
	private Date birthdate;
	private String school;
	private List<String> major;
	private boolean has_girlfriend;
	private Object car;
	private Object house;
	private Map<String, String> computer;*/
	@Override
	public String toString() {
		return "PersonModel: {" +
				"name:" + name +
				", age:" + age +
				", height:" + height +
				", weight:" + weight +
				", birthdate:" + (birthdate == null ? "":birthdate.toString()) +
				", school:" + school +
				", major" + major +
				", has_girlfriend:" + has_girlfriend +
				", car:" + (car == null ? "null" : car) +
				", house:" + (house == null ? "null" : house) +
				", computer:" + computer +
				"}";
	}
}

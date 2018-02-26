package ind.xwm.imooc.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by XuWeiman on 2017/7/6.
 * 学生
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"address", "gender", "age", "habits"}
)
public class ClassStudent {
    @XmlAttribute(name = "name")
    private String name;
    private int age;
    private String gender;
    private String address;

    @XmlElementWrapper(name = "habits")
    @XmlElement(name = "habit")
    private List<String> habits;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getHabits() {
        return habits;
    }

    public void setHabits(List<String> habits) {
        this.habits = habits;
    }
}

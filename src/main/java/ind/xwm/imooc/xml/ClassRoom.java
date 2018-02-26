package ind.xwm.imooc.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by XuWeiman on 2017/7/6.
 * 教室
 */
@XmlRootElement(name = "class")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClassRoom {
    private String name;

    @XmlElement(name = "student")
    private List<ClassStudent> classStudents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassStudent> getClassStudents() {
        return classStudents;
    }

    public void setClassStudents(List<ClassStudent> classStudents) {
        this.classStudents = classStudents;
    }
}

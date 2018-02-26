package ind.xwm.imooc.pdf;

import com.alibaba.fastjson.JSON;

public enum TestEnum {
    STORE(1, "aaaa"),
    CITY(2, "bbbb");

    private int id;
    private String value;

    TestEnum(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }



    public static TestEnum findByName(String name) {
        if ("STORE".equals(name.toUpperCase())) {
            return TestEnum.STORE;
        } else if ("CITY".equals(name.toUpperCase())) {
            return TestEnum.CITY;
        }
        return null;
    }

    public static void main(String[] args) {
        TestEnum testEnum = TestEnum.findByName("STORE");
        if(testEnum == null) {
            System.out.println("Null");
        } else {
            System.out.println(testEnum.getValue());
        }

    }
}

package ind.xwm.imooc.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XuWeiman on 2017/9/29.
 * 组件工厂
 */
public class ComponentBuilder {
    private static Logger logger = LogManager.getLogger(ComponentBuilder.class);

    /**
     * 单位下来框控件
     * @return JComboBox
     */
    public JComboBox getUintComboBox() {
        // 获取 单位 数据
        List<String> uints = new ArrayList<>();
        uints.add("本");
        uints.add("件");
        uints.add("箱");
        JComboBox jcb = new JComboBox();
        jcb.setEditable(true);
        for(String unit: uints) {
            jcb.addItem(unit);
        }

        return jcb;
    }
}

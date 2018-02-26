package ind.xwm.imooc.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by XuWeiman on 2017/9/29.
 * JTable 单元格按钮渲染器
 */
public class MyButton implements TableCellRenderer {
    private JButton jButton;

    public MyButton(String text) {
        this.jButton = new JButton(text);
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this.jButton;
    }
}

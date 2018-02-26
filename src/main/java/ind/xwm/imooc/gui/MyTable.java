package ind.xwm.imooc.gui;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/9/
 */
public class MyTable extends JTable {
    private static Logger logger = LogManager.getLogger(MyTable.class);

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
        if(columnIndex == 6) {
            ((DefaultTableModel)this.getModel()).removeRow(rowIndex);
            return;
        }
        super.editCellAt(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0 && column != 5 && column != 6 && super.isCellEditable(row, column);
    }



    public static MyTable getInstance() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        MyTable table = new MyTable();
        table.setRowHeight(30);
        DefaultTableModel tableModel = new DefaultTableModel(null, new String[]{"序号", "商品", "数量", "单位", "单价", "金额", "操作"});
        table.setModel(tableModel);
        tableModel.addRow(new Object[]{"1", "Spring", "2", "本", "80.00", "80.00"});

        JComboBox jcb = componentBuilder.getUintComboBox();

        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jcb));
        table.getColumnModel().getColumn(6).setCellRenderer(new MyButton("删除"));
        tableModel.addTableModelListener(e -> {
            DefaultTableModel model = (DefaultTableModel) e.getSource();
            int row = e.getFirstRow();
            int rowLast = e.getLastRow();

            int col = e.getColumn();
            logger.info("修改行{}{}-{}", row, rowLast, col);
            if ((col == 2 || col == 4) && e.getType() == TableModelEvent.UPDATE) {
                String numValue = (String) model.getValueAt(row, 2);
                String priceValue = (String) model.getValueAt(row, 4);
                if(StringUtils.isNumeric(numValue) && StringUtils.isNumeric(priceValue.replace(".", ""))) {
                    Integer num = Integer.valueOf(numValue);
                    Float price = Float.valueOf(priceValue);
                    model.setValueAt(num * price, row, 5);
                }
            }
        });

        Vector<Vector> dataVector = tableModel.getDataVector();

        for (Vector rowVector : dataVector) {
            StringBuilder rowData = new StringBuilder();
            for (Object dataObj : rowVector) {
                if (dataObj != null) {
                    rowData.append(dataObj.toString());
                }
            }
            logger.info("表格数据:{}", rowData.toString());
        }

        return table;
    }
}

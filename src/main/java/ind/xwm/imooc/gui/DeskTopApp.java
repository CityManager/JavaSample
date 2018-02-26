package ind.xwm.imooc.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/9/27.
 * 桌面应用
 */
public class DeskTopApp {
    private static Logger logger = LogManager.getLogger(DeskTopApp.class);


    public static void main(String[] args) {
        JFrame frame = new JFrame("测试");
        HelloForm form = new HelloForm();
        final MyTable table = MyTable.getInstance();
        JButton button = form.getCheckTableBtn();
        button.addActionListener(e -> {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            Vector<Vector> dataVector = ((DefaultTableModel) table.getModel()).getDataVector();
            for (Vector rowVector : dataVector) {
                StringBuilder rowData = new StringBuilder();
                for (Object dataObj : rowVector) {
                    if (dataObj != null) {
                        rowData.append(dataObj.toString());
                    }
                }
                logger.info("表格数据:{}", rowData.toString());
            }
            logger.info("是否EventDispatchThread-{}", SwingUtilities.isEventDispatchThread());
        });

        JButton addRecordBtn = form.getAddRecordBtn();
        addRecordBtn.addActionListener(e -> {
            String[] rowData = new String[]{"5", "", "", "", "", "", ""};
            ((DefaultTableModel) table.getModel()).addRow(rowData);
        });


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.getjScrollPane().setViewportView(table);
        frame.setContentPane(form.getPanel1());
        frame.setSize(600, 400);
        frame.pack();
        frame.setVisible(true);
    }

}

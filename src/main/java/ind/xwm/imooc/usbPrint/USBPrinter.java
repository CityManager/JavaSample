package ind.xwm.imooc.usbPrint;

import java.awt.*;
import java.awt.print.*;

/**
 * Created by XuWeiman on 2017/10/3.
 * 网上找的打印小票工具类， 就是直接当成图片来打印
 */
public class USBPrinter {

    public static void main(String[] args) {
        int height = 175 + 3 * 15 + 20;

        Book book = new Book();

        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);

        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper p = new Paper();
        p.setSize(230, height);
        p.setImageableArea(0, -20, 230, height + 20);

        pf.setPaper(p);

        // 把 PageFormat 和 Printable 添加到书中，组成一个页面
        book.append(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setFont(new Font("Default", Font.PLAIN, 14));
                g2d.drawString("等位排单号", 50, 10);
                g2d.drawString("-------------------------------------", 7, 20);
                g2d.drawString("手机号码：" + "11111111111", 7, 35);
                g2d.drawString("领号日期：" + "11111", 7, 65);
                g2d.drawString("-------------------------------------", 7, 80);
                g2d.setFont(new Font("Default", Font.PLAIN, 25));
                g2d.drawString("小号", 7, 105);
                g2d.setFont(new Font("Default", Font.PLAIN, 14));
                g2d.drawString("您之前还有" + 5 + "桌客人在等待", 7, 130);
                g2d.drawString("-------------------------------------", 7, 145);
                g2d.drawString("*打印时间:" + "1111" + "*", 7, 160);
                g2d.drawString("店名：" + "11", 7, 175);
                return Printable.PAGE_EXISTS;
            }
        }, pf);

        // 获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(book);
        try {
            job.print();
        } catch (PrinterException e) {
            System.out.println("================打印出现异常");
        }
    }


}

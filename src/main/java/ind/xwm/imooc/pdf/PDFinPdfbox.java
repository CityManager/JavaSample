package ind.xwm.imooc.pdf;

import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class PDFinPdfbox {
	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\XuWeiman\\Desktop\\", "showDetail.pdf");
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		PDDocument doc = PDDocument.load(file);

		PDFRenderer renderer = new PDFRenderer(doc);

		BufferedImage image = renderer.renderImageWithDPI(0, 300, ImageType.RGB);
		ImageIOUtil.writeImage(image, "C:\\Users\\XuWeiman\\Desktop\\test.jpg", 300);

		PDFParser pdfParser = new PDFParser(new RandomAccessBufferedFileInputStream(file));
		pdfParser.parse();
		PDDocument document = pdfParser.getPDDocument();
		PDFTextStripper stripper = new PDFTextStripper();

		System.out.println(stripper.getText(document));
	}
}

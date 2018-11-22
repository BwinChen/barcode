package com.liantuyun.barcode.util;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.util.StringUtils;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * @see <a href="https://www.cnblogs.com/littleatp/p/4815921.html">Java条形码生成技术</a>
 * @see <a href="https://blog.csdn.net/c1052981766/article/details/53694537">java 生成条码并保存为SVG</a>
 */
public class BarcodeUtil {

    public static void main(String[] args) {
        String msg = "6923450657713";
        String path = "barcode.svg";
        generateSVG(msg, path);
    }

    /**
     * 生成条形码svg文件
     *
     * @param msg 信息
     * @param path 文件路径
     */
    public static void generateSVG(String msg, String path) {
        if (StringUtils.isEmpty(msg) || StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException();
        }
        // 分辨率
        final int resolution = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0 / resolution);
        Code39Bean bean = new Code39Bean();
        bean.setModuleWidth(moduleWidth);
        bean.setWideFactor(3);
//        bean.setBarHeight(30);
//        bean.setHeight(30);
        bean.doQuietZone(false);
        try {
            SVGCanvasProvider canvas = new SVGCanvasProvider(false, 0);
            // 生成条形码
            bean.generateBarcode(canvas, msg);
            // 生成svg
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Source source = new DOMSource(canvas.getDOMFragment());
            Result target = new StreamResult(new File(path));
            transformer.transform(source, target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
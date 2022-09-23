package media.converter.controller;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import media.converter.fileservice.FileProcessingService;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.simple.Graphics2DRenderer;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@RestController
public class MediaController {

    @Autowired
    FileProcessingService service;

    @PostMapping(value = "/converthtmlimage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> convertHTMLImage(@RequestParam("webURL") String webURL, @RequestParam("fileName") String fileName){

        String baseUri = System.getProperty("user.dir") + File.separator + "output" + File.separator;
        String outputFile = System.getProperty("user.dir") + File.separator + "output" + File.separator + fileName+".pdf";
        Map<String, String> response = new HashMap<String, String>();

        try {
            //int WIDTH = 1024;
            //String IMAGE_FORMAT = "png";
            //FileOutputStream fos = new FileOutputStream(outputFile);
            //BufferedImage image = Graphics2DRenderer.renderToImage(new File("~/Desktop/Resume.mht").toURI().toURL().toExternalForm(), WIDTH, BufferedImage.TYPE_INT_ARGB);
            //ImageIO.write(image, IMAGE_FORMAT, new File(outputFile));

            int width = 1024;
            int height = 1024;
            File file = new File("~/Desktop/Resume.mht");
            Java2DRenderer render = new Java2DRenderer(file, width, height);
            BufferedImage image = render.getImage();
            ImageIO.write(image, "png", new File(outputFile));

            response.put("status", "success");
            response.put("message", "HTML successfully to convert into Image");
            return  response;
        } catch (Exception e) {
            response.put("status", "failed");
            response.put("message", e.getMessage());
            return  response;
        }
    }


    @PostMapping(value = "/converthtmlpdf", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> convertFile(@RequestParam("webURL")String webURL, @RequestParam("fileName") String fileName){
        Map<String, String> response = new HashMap<>();
        String outputFile = System.getProperty("user.dir") + File.separator + "output" + File.separator + fileName+".pdf";

        try {
            String baseUri = System.getProperty("user.dir") + File.separator + "output" + File.separator;
            FileOutputStream fos = new FileOutputStream(outputFile);
            org.jsoup.nodes.Document doc =  Jsoup.parse(new URL(webURL), 10000);
            doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

            //using librari pdfrenderbuilder
            /*
            PdfRendererBuilder builder =  new PdfRendererBuilder();
            builder.withUri(outputFile);
            builder.toStream(fos);
            builder.withW3cDocument(new W3CDom().fromJsoup(doc), baseUri);
            builder.run();
            */

            //using library itextrender
            ITextRenderer render = new ITextRenderer();
            SharedContext sharedContext = render.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            render.setDocumentFromString(doc.html(), baseUri);
            render.layout();
            render.createPDF(fos);

            response.put("status", "success");
            response.put("message", "Convert file success");
            return  response;
        } catch (Exception e) {
            response.put("status", "failed");
            response.put("message", e.getMessage());
            return  response;
        }
    }

    @PostMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> convertMedia(@RequestParam("file")MultipartFile file,
                               @RequestParam("fromType") String fromType,
                               @RequestParam("toType") String toType){

        try {
            String fileName = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(new Date());
            String inputFile = System.getProperty("user.dir") + File.separator + "input" + File.separator + fileName+".mov";
            String outputFile = System.getProperty("user.dir") + File.separator + "output" + File.separator + fileName+".gif";
            service.queueFileConvert(inputFile, outputFile, file.getBytes());
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "success");
            map.put("message", "Convert file success");
            return map;
        }catch (Exception e){
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failed");
            map.put("message", e.getMessage());
            return map;
        }
    }


    @GetMapping(value = "/historyConvert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> convertMedia(){
       File folderFile =  new File(System.getProperty("user.dir") + File.separator + "output");
       List<String> listFile = new ArrayList<>();
       for(String file:folderFile.list()){
           listFile.add(System.getProperty("user.dir") + File.separator+file);
       }
       Map<String, Object> map = new HashMap<String, Object>();
       map.put("listFile", listFile);
       return map;
    }


}

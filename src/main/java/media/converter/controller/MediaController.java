package media.converter.controller;

import media.converter.fileservice.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

@RestController
public class MediaController {

    @Autowired
    FileProcessingService service;

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

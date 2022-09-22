package media.converter.fileservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileProcessRunable implements Runnable {
    private final String inputFile;
    private final String outputFile;
    private final byte[] sendfile;

    public FileProcessRunable(String inputFile, String outputFile, byte[] sendFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.sendfile = sendFile;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Process File "+inputFile);
    }

    private void process() throws IOException {
        //prcessing convert from mp4 to gif with 2 ways
        //1. using process builder
        //ProcessBuilder builder = new ProcessBuilder();
        //builder.command("ffmpeg","-i",inputFile,"-pix_fmt","rgb24",outputFile);
        //Process process = builder.start();
        //int exitCode = process.waitFor();

        //2. using runtime command
        //Runtime runtime = Runtime.getRuntime();
        //Process process = runtime.exec("ffmpeg -i "+inputFile+" -pix_fmt rgb24 "+outputFile);
        //int exitCode = process.waitFor();

        //if (exitCode == 0) {
        //return "Convert file success";
        //}else{
        //    return "Convert file failed";
        //}

        File fileDest = new File(inputFile);
        FileOutputStream fos = new FileOutputStream(fileDest);
        fos.write(sendfile);

        try{
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("ffmpeg","-i",inputFile,"-pix_fmt","rgb24",outputFile);
            Process process = builder.start();
            int exitCode = process.waitFor();
            System.out.println("Success processing file");
        }catch (Exception e){
            System.out.println("Terjadi Error "+ e.getMessage());
        }
    }
}

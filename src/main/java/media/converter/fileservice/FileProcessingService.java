package media.converter.fileservice;

import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Component
public class FileProcessingService {
    private final ExecutorService executors = Executors.newFixedThreadPool(200);

    public void queueFileConvert(String inputFileName, String outputFileName, byte[] byteSendFile) throws ExecutionException, InterruptedException {
        executors.submit(new FileProcessRunable(inputFileName, outputFileName, byteSendFile));
    }

}

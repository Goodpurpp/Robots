package robots.gui.input;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;

public class InputCompiler {
    private static final AtomicInteger counter = new AtomicInteger();

    public void process(String inputText) throws IOException {
        saveFile(inputText);
    }

    private Path saveFile(String inputText) throws IOException {
        Path savePath = Path.of("robots", "src", "main", "resources", "classes", "class" + counter.getAndIncrement() + ".java");
        try(Writer writer = Files.newBufferedWriter(savePath, StandardOpenOption.CREATE)) {
            writer.write(inputText);
        }
        return savePath;
    }
}

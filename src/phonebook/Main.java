package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws IOException {
    List<String> directoryLines = Files.readAllLines(Paths.get("data/directory.txt"));
    List<String> names = new ArrayList<>();
    List<String> numbers = new ArrayList<>();
    directoryLines.forEach(line -> {
      numbers.add(line.substring(0, line.indexOf(" ")));
      names.add(line.substring(line.indexOf(" ") + 1));
    });
    List<String> requests = Files.readAllLines(Paths.get("data/find.txt"));
    long start = System.currentTimeMillis();
    for (String request : requests) {
      for (int i = 0; i < names.size(); i++) {
        if (request.equals(names.get(i))) {
          System.out.printf("%s has number %s\n", request, numbers.get(i));
          break;
        }
      }
    }
    System.out.printf("To find all entries it taken %d ms\n", System.currentTimeMillis() - start);
  }
}
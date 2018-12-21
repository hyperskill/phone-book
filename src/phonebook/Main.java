package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

  static final String CONTACT_DATA_DIRECTORY = "data/directory.txt";
  static final String FIND_REQUESTS_DIRECTORY = "data/find.txt";

  public static void main(String[] args) throws IOException {
    List<Contact> contacts = parseContacts();
    List<String> requests = Files.readAllLines(Paths.get(FIND_REQUESTS_DIRECTORY));

    long start = System.currentTimeMillis();
    int cnt = 1;
    for (String request : requests) {
      Contact contact;
      if ((contact = search(contacts, request)) != null) {
        System.out.printf("%d. %s has number %s\n", cnt++, request, contact.getNumber());
      }
    }
    System.out.printf("To find all entries it taken %d ms\n", System.currentTimeMillis() - start);
  }

  private static Contact search(List<Contact> contacts, String name) {
    int sqr = (int) Math.sqrt(contacts.size());
    int lo = 0, hi = Math.min(sqr, contacts.size() - 1);
    while (lo < hi) {
      Contact contact = contacts.get(hi);
      if (contact.getName().compareTo(name) == 0) {
        return contact;
      } else if (contact.getName().compareTo(name) < 0) {
        lo = hi + 1;
        hi = Math.min(hi + sqr + 1, contacts.size() - 1);
      } else {
        for (int i = hi; i >= lo; i--) {
          if (contacts.get(i).getName().compareTo(name) == 0) {
            return contact;
          }
        }
        return null;
      }
    }
    return null;
  }

  private static List<Contact> parseContacts() throws IOException {
    Stream<String> directoryLines = Files.lines(Paths.get(CONTACT_DATA_DIRECTORY));
    return directoryLines.map(line -> new Contact(line.substring(0, line.indexOf(" ")),
        line.substring(line.indexOf(" ") + 1)))
        .sorted(Comparator.comparing(Contact::getName))
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
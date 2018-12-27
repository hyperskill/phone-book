package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

  private static final String CONTACT_DATA_DIRECTORY = "data/directory.txt";
  private static final String FIND_REQUESTS_DIRECTORY = "data/find.txt";

  public static void main(String[] args) throws IOException {
    List<Contact> contacts = parseContacts();
    bubbleSort(contacts);

    List<String> requests = Files.readAllLines(Paths.get(FIND_REQUESTS_DIRECTORY));

    long start = System.currentTimeMillis();
    int cnt = 1;
    for (String request : requests) {
      Optional<Contact> contact = search(contacts, request);
      if (contact.isPresent()) {
        System.out.printf("%d. %s has number %s\n", cnt++, request, contact.get().getNumber());
      }
    }
    System.out.printf("To find all entries it taken %d ms\n", System.currentTimeMillis() - start);
  }

  private static Optional<Contact> search(List<Contact> contacts, String name) {
    int sqr = (int) Math.sqrt(contacts.size());
    int lo = 0, hi = Math.min(sqr, contacts.size() - 1);
    while (lo < hi) {
      Contact contact = contacts.get(hi);
      int comparation = Objects.compare(contact.getName(), name, Comparator.naturalOrder());
      if (comparation == 0) {
        return Optional.of(contact);
      } else if (comparation < 0) {
        lo = hi + 1;
        hi = Math.min(hi + sqr + 1, contacts.size() - 1);
      } else {
        for (int i = hi; i >= lo; i--) {
          Contact current = contacts.get(i);
          if (Objects.equals(current.getName(), name)) {
            return Optional.of(contact);
          }
        }
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  private static void bubbleSort(List<Contact> contacts) {
    for (int i = 0; i < contacts.size(); i++) {
      for (int j = contacts.size() - 1; j > i; j--) {
        int comparing = Objects.compare(contacts.get(j), contacts.get(j - 1),
            Comparator.comparing(Contact::getName));
        if (comparing < 0) {
          Collections.swap(contacts, j, j - 1);
        }
      }
    }
  }

  private static List<Contact> parseContacts() throws IOException {
    Stream<String> directoryLines = Files.lines(Paths.get(CONTACT_DATA_DIRECTORY));
    return directoryLines.map(line -> new Contact(line.substring(0, line.indexOf(" ")),
        line.substring(line.indexOf(" ") + 1)))
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
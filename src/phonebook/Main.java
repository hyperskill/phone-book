package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

  private static final String CONTACT_DATA_DIRECTORY = "data/directory.txt";
  private static final String FIND_REQUESTS_DIRECTORY = "data/find.txt";

  public static void main(String[] args) throws IOException {
    List<Contact> contacts = parseContacts();
    quickSort(contacts);

    List<String> requests = Files.readAllLines(Paths.get(FIND_REQUESTS_DIRECTORY));

    long start = System.currentTimeMillis();
    int cnt = 1;
    for (String request : requests) {
      Contact contact = search(contacts, request);
      if ((Objects.nonNull(contact))) {
        System.out.printf("%d. %s has number %s\n", cnt++, request, contact.getNumber());
      }
    }
    System.out.printf("To find all entries it taken %d ms\n", System.currentTimeMillis() - start);
  }

  private static Contact search(List<Contact> contacts, String name) {
    int lo = 0, hi = contacts.size() - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      Contact contact = contacts.get(mid);
      int comparing = Objects.compare(contact.getName(), name, Comparator.naturalOrder());
      if (comparing == 0) {
        return contact;
      } else if (comparing < 0) {
        lo = mid + 1;
      } else {
        hi = mid - 1;
      }
    }
    return null;
  }

  private static void quickSort(List<Contact> contacts) {
    quickSort(contacts, 0, contacts.size() - 1);
  }

  private static void quickSort(List<Contact> contacts, int lo, int hi) {
    if (lo >= hi) {
      return;
    }
    int p = pivot(contacts, lo, hi);
    quickSort(contacts, lo, p - 1);
    quickSort(contacts, p + 1, hi);
  }

  private static int pivot(List<Contact> contacts, int lo, int hi) {
    Contact pivot = contacts.get(lo);
    int j = lo + 1;
    for (int i = lo + 1; i <= hi; i++) {
      int comparing = Objects
          .compare(pivot, contacts.get(i), Comparator.comparing(Contact::getName));
      if (comparing >= 0) {
        Collections.swap(contacts, i, j);
        j++;
      }
    }
    Collections.swap(contacts, lo, j - 1);
    return j - 1;
  }

  private static List<Contact> parseContacts() throws IOException {
    Stream<String> directoryLines = Files.lines(Paths.get(CONTACT_DATA_DIRECTORY));
    return directoryLines.map(line -> new Contact(line.substring(0, line.indexOf(" ")),
        line.substring(line.indexOf(" ") + 1)))
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
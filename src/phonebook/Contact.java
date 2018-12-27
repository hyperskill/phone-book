package phonebook;

public class Contact {

  private final String number;
  private final String name;

  public Contact(String number, String name) {
    this.number = number;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getNumber() {
    return number;
  }
}

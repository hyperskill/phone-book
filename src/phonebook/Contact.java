package phonebook;

public class Contact {

  private String number;
  private String name;

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

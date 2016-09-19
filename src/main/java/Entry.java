import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Entry{
  private String name;
  private String mailing_address;
  private String email;
  private String phone_number;
  private int id;
  private int contact_id;


  public Entry (String name, String mailing_address, String email, String phone_number, int contact_id) {
    this.name = name;
    this.mailing_address = mailing_address;
    this.email = email;
    this.phone_number = phone_number;
    this.contact_id = contact_id;
  }

  public String getName() {
    return name;
  }

  public String getMailingAddress () {
    return mailing_address;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone_number;
  }

  public int getContact_id() {
    return contact_id;
  }

  public static List<Entry> all() {
    String sql = "SELECT id, name, mailing_address, email, phone_number FROM entries";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Entry.class);
    }
  }

  public static Entry find(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM entries where id=:id";
      Entry entry = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Entry.class);
      return entry;
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO entries (name, mailing_address, email, phone_number, contact_id) VALUES (:name, :mailing_address, :email, :phone_number, :contact_id)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("mailing_address", this.mailing_address)
      .addParameter("email", this.email)
      .addParameter("phone_number", this.phone_number)
      .addParameter("contact_id", this.contact_id)
      .executeUpdate()
      .getKey();
    }
  }

  public int getId () {
    return id;
  }

  @Override
  public boolean equals(Object otherEntry){
    if (!(otherEntry instanceof Entry)) {
    return false;
  } else {
    Entry newEntry = (Entry) otherEntry;
    return this.getName().equals(newEntry.getName()) &&
           this.getMailingAddress().equals(newEntry.getMailingAddress()) &&
           this.getEmail().equals(newEntry.getEmail()) &&
           this.getPhone().equals(newEntry.getPhone())
          && this.getId() == newEntry.getId();
         }
  }
}

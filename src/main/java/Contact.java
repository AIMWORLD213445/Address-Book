import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Contact{
  String name;
  int id;

  public Contact(String name){
    this.name = name;
  }

  public String getName(){
    return name;
  }

  public int getId(){
    return id;
  }

  @Override
  public boolean equals(Object otherContact){
    if(!(otherContact instanceof Contact)){
      return false;
    } else {
      Contact newContact = (Contact) otherContact;
        return this.getName().equals(newContact.getName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO contacts (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Contact> all(){
    String sql = "SELECT id, name FROM contacts";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Contact.class);
    }
  }

  public static Contact find(int id) {
    try(Connection con = DB.sql2o.open()) {
          String sql = "SELECT * FROM contacts where id=:id";
          Contact contact = con.createQuery(sql)
            .addParameter("id", id)
            .executeAndFetchFirst(Contact.class);
          return contact;
        }
      }

  public List<Entry> getEntries() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM entries where contact_id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Entry.class);
    }
  }


}

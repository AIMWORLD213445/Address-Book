import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class EntryTest{
  Entry entry;
  @Before
  public void setUp() {
     DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/address_book_test", null, null);
     entry = new Entry ("Brian", "1 street tr", "email@email.com", "1111111111", 1);
  }

  @After
  public void tearDown() {
   try(Connection con = DB.sql2o.open()) {
     String deleteEntriesQuery = "DELETE FROM entries *;";
     String deleteContactsQuery = "DELETE FROM contacts *;";
     con.createQuery(deleteEntriesQuery).executeUpdate();
     con.createQuery(deleteContactsQuery).executeUpdate();
    }
  }

  @Test
  public void entry_instantiates_true() {
    assertTrue(entry instanceof Entry);
  }
  @Test
  public void getName_instantiatesWithAName_String() {
    assertEquals("Brian", entry.getName());
  }
  @Test
  public void getAddress_instantiatesWithAAddress_String() {
    assertEquals("1 street tr", entry.getMailingAddress());
  }
  @Test
  public void getEmail_instantiatesWithAEmail_String() {
    assertEquals("email@email.com", entry.getEmail());
  }
  @Test
  public void getPhone_instantiatesWithAPhoneNumber_String() {
    assertEquals("1111111111", entry.getPhone());
  }

  @Test
  public void getContactId_instantiatesWithAnId_int() {
    assertEquals(1, entry.getContact_id());
  }

  @Test
  public void getId_entriesInstantiateWithAnID_1() {
    entry.save();
    assertTrue(entry.getId()> 0);
  }

  @Test
  public void all_returnsAllInstancesOfEntry_true() {
    entry.save();
    Entry secondEntry = new Entry("Brian's Summer Home", "1 summer st", "briansnothere@summer.com", "2222222", 1);
    secondEntry.save();
    assertEquals(true, Entry.all().get(0).equals(entry));
    assertEquals(true, Entry.all().get(1).equals(secondEntry));
  }

  @Test
  public void equals_returnsTrueIfFieldsAretheSame() {
    Entry secondEntry = new Entry("Brian", "1 street tr", "email@email.com", "1111111111", 1);
    assertTrue(entry.equals(secondEntry));
  }

  @Test
  public void save_returnsTrueIfFieldssAretheSame() {
    entry.save();
    assertTrue(Entry.all().get(0).equals(entry));
  }

  @Test
  public void save_assignsIdToObject() {
    entry.save();
    Entry savedEntry = Entry.all().get(0);
    assertEquals(entry.getId(), savedEntry.getId());
  }

  @Test
    public void save_savesCategoryIdIntoDB_true() {
      Contact myContact = new Contact("Brian");
      myContact.save();
      entry = new Entry("Brian", "1 street tr", "email@email.com", "1111111111", myContact.getId());
      entry.save();
      Entry savedEntry = Entry.find(entry.getId());
      assertEquals(savedEntry.getContact_id(), myContact.getId());
    }

  @Test
  public void find_returnsEntryWithSameId_secondEntry() {
    entry.save();
    Entry secondEntry = new Entry("Brian's Summer Home", "1 summer st", "briansnothere@summer.com", "2222222", 1);
    secondEntry.save();
    assertEquals(Entry.find(secondEntry.getId()), secondEntry);
  }

}

import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sql2o.*;

public class ContactTest{
  Contact myContact;
  @Before
  public void setUp() {
     DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/address_book_test", null, null);
     myContact = new Contact ("Brian");
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
  public void contact_instantiatesCorrectly_true(){
    assertTrue(myContact instanceof Contact);
  }

  @Test
  public void getName_instantiatesNameCorrectly_true(){
    assertEquals("Brian", myContact.getName());
  }

  @Test
  public void getId_instantiatesIdCorrectly_true(){
    myContact.save();
    assertTrue(myContact.getId()>0);
  }

  @Test
  public void equals_returnsTrueIfFieldsAretheSame_true(){
    Contact secondContact = new Contact("Brian");
    assertTrue(myContact.equals(secondContact));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    myContact.save();
    assertTrue(Contact.all().get(0).equals(myContact));
  }

  @Test
  public void all_returnsAllInstancesOfContact_true() {
    myContact.save();
    Contact secondContact = new Contact("Ewa");
    secondContact.save();
    assertEquals(true, Contact.all().contains(myContact));
    assertEquals(true, Contact.all().contains(secondContact));
  }
  @Test
  public void find_returnsContactWithSameId_secondContact() {
    Contact firstContact = new Contact("Brian");
    firstContact.save();
    Contact secondContact = new Contact("Ewa");
    secondContact.save();
    assertEquals(secondContact, Contact.find(secondContact.getId()));
  }

  @Test
 public void getEntries_retrievesALlEntriesFromDatabase_entriesList() {
   myContact.save();
   Entry firstEntry = new Entry("Home", "1 street tr", "email@email.com", "1111111111", myContact.getId());
   firstEntry.save();
   Entry secondEntry = new Entry("Work", "1 street tr", "email@email.com", "1111111111", myContact.getId());
   secondEntry.save();
   Entry[] entries = new Entry[] { firstEntry, secondEntry };
   assertTrue(myContact.getEntries().containsAll(Arrays.asList(entries)));
 }
}

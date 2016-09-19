import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("contacts", Contact.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/contacts", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Contact contact = new Contact(name);
      contact.save();
      model.put("contacts", Contact.all());
      model.put("success", true);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/contacts/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Contact contact = Contact.find(Integer.parseInt(request.params("id")));
      model.put("contact", contact);
      model.put("addresses", contact.getEntries());
      model.put("template", "templates/contact.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/contacts/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Contact contact = Contact.find(Integer.parseInt(request.params("id")));
      String name = request.queryParams("name");
      String mailing = request.queryParams("mailing-address");
      String email = request.queryParams("email");
      String phone = request.queryParams("phone");
      Entry entry = new Entry(name, mailing, email, phone, contact.getId());
      entry.save();
      model.put("contact", contact);
      model.put("addresses", contact.getEntries());
      model.put("success", true);
      model.put("template", "templates/contact.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}

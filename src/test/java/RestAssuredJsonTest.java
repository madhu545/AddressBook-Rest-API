import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


public class RestAssuredJsonTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public Response getContactsList(){
        Response response = RestAssured.get("/AddressBook");
        return response;
    }

    private Contacts[] getContactDetails() {
        Response response = RestAssured.get(RestAssured.baseURI + "/AddressBook");
        System.out.println("Employees in JSON Server are: \n" + response.asString());
        return new Gson().fromJson(response.asString(), Contacts[].class);
    }

    private Response addContactToJSONServer(Contacts contactsData) {
        String contactsJSON = new Gson().toJson(contactsData);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(contactsJSON);
        return requestSpecification.post(RestAssured.baseURI + "/AddressBook");
    }

    @Test
    public void onCallingList_ReturnContactsList(){
        Response response = getContactsList();
        System.out.println("AT FIRST: " + response.asString());
        response.then().body("firstName", Matchers.hasItem("Rufus"));
    }

    @Test
    void givenContactsInJSONServer_WhenFetched_ShouldMatchCount() {
        Contacts[] contactData = getContactDetails();
        ContactsRestAPI contactsRestAPI = new ContactsRestAPI(Arrays.asList(contactData));
        long entries = contactsRestAPI.countEntries();
        Assertions.assertEquals(4, entries);
    }

    @Test
    void givenANewContact_WhenAdded_ShouldMatchCount() {
        ContactsRestAPI contactRestAPI;
        Contacts[] dataArray = getContactDetails();
        contactRestAPI = new ContactsRestAPI(Arrays.asList(dataArray));

        Contacts contactData;
        contactData = new Contacts(5, "chris", "Adams", "house number 23", "New York", "NY", 745698, "7894561230", "chris@gmail.com");
        Response response = addContactToJSONServer(contactData);

        contactData = new Gson().fromJson(response.asString(), Contacts.class);
        contactRestAPI.addContact(contactData);
        System.out.println("------ After Adding Into JSON Server ------\n" + getContactDetails());
        long entries = contactRestAPI.countEntries();
        Assertions.assertEquals(5, entries);
    }
}


import java.util.ArrayList;
import java.util.List;

public class ContactsRestAPI {

    List<contacts> dataList;

    public ContactsRestAPI(List<contacts> dataList) {
        this.dataList = new ArrayList<>(dataList);
    }

    public long countEntries() {
        return this.dataList.size();
    }

    public void addContact(contacts contactsData) {
        this.dataList.add(contactsData);
    }
}


import java.util.ArrayList;
import java.util.Scanner;

class ContactList {
    private ArrayList<Contact> contactList = new ArrayList<>();
    private String saveStatus = "saved";
    private int listSize = contactList.size();

    public ContactList() {

    }

    public void setSaveStatus(String saveStatus) {
        if (saveStatus == "saved" || saveStatus == "unsaved")
        this.saveStatus = saveStatus;
    }

    public String getSaveStatus() {
        return saveStatus;
    }

    private void setListSize() {
        this.listSize = contactList.size();
    }

    public int getListSize() {
        return listSize;
    }
    //return a contact in the list by its index
    public Contact getContact(int index) {
        return contactList.get(index);
    }
    //load contact from file
    public void loadNewContact(String[] contactInfo) {
        Contact newContact = new Contact(contactInfo[0], contactInfo[1], contactInfo[2], contactInfo[3]); //create a temporary contact to test duplicate
        addNewContact(newContact, "load from file");
    }
    //create a new contact by user
    public void createNewContact() {
        System.out.println("...Creating new contact...");
        System.out.println("Please enter required information.");
        Contact newContact = new Contact(); //create a temporary contact

        newContact.setFullName();
        newContact.setPhoneNumber();
        newContact.setEmailAddress();
        newContact.setHomeAddress();
        System.out.println();
        addNewContact(newContact, "new input");
    }

    //add new contact to the list
    private void addNewContact(Contact newContact, String addType) {
        if (!isDuplicate(newContact)) { //add if it is not duplicate
            contactList.add(listSize, newContact);
            if (addType == "new input") {
                System.out.println("Contact added.");
            }
            setListSize(); //update the changed list size
            this.saveStatus = "unsaved"; //changes happen and haven't been saved
        } else if (addType == "new input") { //announce if contact is already exist when user add a new contact
            System.out.println("This contact already exists.");
        }
    }

    public void viewContactList() {
        System.out.println("...Contacts List...");
        if (listSize == 0) { //announce if contact list is empty
            System.out.println("Contact list is empty. Please add more contacts to view (enter 3).");
        } else {
            for (int i = 0; i < listSize; ++i) {
                System.out.printf("%d. ", i + 1);
                contactList.get(i).displayContactInfo();
            }
        }
    }
    //function to display match search contacts and get their indices for later use
    public ArrayList<Integer> searchContacts() {
        Scanner newString = new Scanner(System.in);
        String stringToSearch;
        ArrayList<Integer> indexOfMatchContacts = new ArrayList<>();

        if (listSize == 0) {    //announce if contact list is empty
            System.out.println("Contact list is empty. Maybe add a new one(enter 3)?");
        } else {
            System.out.println("...Searching for contacts...");
            System.out.println("Search for: ");
            stringToSearch = newString.nextLine().trim().toLowerCase(); //get search string, non case-sensitive, ignore white spaces at two ends
            if (printAndGetIndexOfMatchContact(stringToSearch, indexOfMatchContacts) == 0) { //announce if no contact matches
                System.out.println("Ooops! No contacts match. Maybe add a new one (enter 3)?");
            }
        }
        return indexOfMatchContacts;
    }
    //function display match search contacts and return the list of match indices for later use
    public int printAndGetIndexOfMatchContact(String searchString, ArrayList<Integer> indexList) {
        int count = 0;

        for (int i = 0; i < listSize; ++i) {
            if (contactList.get(i).getContactInfo().toLowerCase().indexOf(searchString) == -1) { //can not find the given string inside the contact
                continue;
            } else {
                indexList.add(indexList.size(), i); //save match contact index
                System.out.printf("%d. %s\n", count + 1, contactList.get(i).getContactInfo()); //display match contact
                ++count;
            }
        }
        return count;
    }

    public void editContact() {
        if (listSize == 0) { //announce when contact list is empty
            System.out.println("Contact list is empty. Maybe add a new one(input 3)?");
        } else {
            Contact temporaryContact = new Contact(); //create a temporary contact
            int index = chooseOneContact();
            int cancelSignal = -1;

            if (index != cancelSignal) { //if user choose an available contact
                System.out.printf("Information to be edited: %s \n", contactList.get(index).getContactInfo());
                getEditName(temporaryContact, index);
                getEditPhone(temporaryContact, index);
                getEditEmailAddress(temporaryContact, index);
                getEditHomeAddress(temporaryContact, index);
                checkAndSetEditInfo(temporaryContact, index);
            } else { //announce when user change mind
                System.out.println("Cancel editing.");
            }
        }
    }

    private void getEditName(Contact temporaryContact, int index) {
        if (Main.makeDecision("name")) { //is user want to change name
            temporaryContact.setFullName(); //get the new name
        } else { //if user doesn't want to change name
            temporaryContact.setFullName(contactList.get(index).getFullName()); //keep the old name
        }
    }

    private void getEditPhone(Contact temporaryContact, int index) {
        if (Main.makeDecision("phone")) { //is user want to change phone
            temporaryContact.setPhoneNumber(); //get the new phone
        } else { //if user doesn't want to change phone
            temporaryContact.setPhoneNumber(contactList.get(index).getPhoneNumber()); //keep the old phone
        }
    }

    private void getEditEmailAddress(Contact temporaryContact, int index) {
        if (Main.makeDecision("Email address")) { //is user want to change email
            temporaryContact.setEmailAddress(); //get the new email
        } else { //if user doesn't want to change email
            temporaryContact.setEmailAddress(contactList.get(index).getEmailAddress()); //keep the old email
        }
    }

    private void getEditHomeAddress(Contact temporaryContact, int index) {
        if (Main.makeDecision("home address")) { //is user want to change home address
            temporaryContact.setHomeAddress(); //get the new home address
        } else { //if user doesn't want to change home address
            temporaryContact.setHomeAddress(contactList.get(index).getHomeAddress()); //keep the old home address
        }
    }

    private void checkAndSetEditInfo(Contact temporaryContact, int index) {
        if (!temporaryContact.getContactInfo().toLowerCase().equals(contactList.get(index).getContactInfo().toLowerCase())) { //check if edit info is different from the current info (non case sensitive)
            if (!isDuplicate(temporaryContact)) { //check if edit info is duplicate or not (non case sensitive)
                contactList.set(index, temporaryContact);
                System.out.println("Contact edited.");
                this.saveStatus = "unsaved"; //changes happen and haven't been saved
            } else { //announce when contact already exists
                System.out.println("Contact already exists. Do you want to delete this one instead(enter 5)?");
            }
        } else { //announce when nothing is changed
            System.out.println("Contact remains the same.");
        }
    }

    public void deleteContact() {
        if (listSize == 0) { //announce when contact list is empty
            System.out.println("Contact list is empty. Maybe add a new one(enter 3)?");
        } else {
            int index = chooseOneContact();
            int cancelSignal = -1;
            if (index != cancelSignal) { //if user choose an available contact
                System.out.printf("Information to be deleted: %s \n", contactList.get(index).getContactInfo());
                if (Main.makeDecision("delete")) { //confirm decision
                    contactList.remove(index);
                    System.out.println("Contact deleted");
                    setListSize(); //update the changed list size
                    this.saveStatus = "unsaved";  //changes happen and haven't been saved
                } else {  //announce when user change mind
                    System.out.println("Cancel deleting.");
                }
            } else { //announce when user change mind
                System.out.println("Cancel deleting.");
            }
        }
    }

    private int chooseOneContact() {
        Scanner newOption = new Scanner(System.in);
        String option;
        ArrayList<Integer> searchIndex = new ArrayList<>();

        if(Main.makeDecision("view")) {
            viewContactList();
        }
        if (Main.makeDecision("search")) {
            searchIndex = searchContacts();
            if (searchIndex.size() == 0) {
                return -1;
            } else {
                int index = getValidContactIndex(searchIndex.size());
                return (index == -1) ? -1 : searchIndex.get(index);
            }
        }
        return getValidContactIndex(listSize);
    }

    private int getValidContactIndex(int max) {
        Scanner newIndex = new Scanner(System.in);
        String index;
        String indexRegex = "^[0-9]+[0-9]*$";
        System.out.printf("Please enter index of the desired contact between 1 and %d (enter 0 to cancel): \n", max);
        index = newIndex.nextLine().trim(); //get input ignoring white spaces at two ends

        while (!index.matches(indexRegex) || (Integer.valueOf(index) > max)) {
            System.out.println("Invalid index.");
            System.out.printf("Please enter index of the desired contact between 1 and %d (enter 0 to cancel): \n", max);
            index = newIndex.nextLine().trim();
        }
        return Integer.valueOf(index) - 1; //real index is 1 smaller than displayed index
    }

    public void sortContacts() {
        String field;
        Contact temporary = new Contact();

        if (listSize == 0) { //announce when contact list is empty
            System.out.println("Contact list is empty. Please add more contacts to sort(enter 3).");
        } else if (listSize == 1) {
            System.out.println("There is only one contact in the list.");
        } else {
            {
                field = getValidField(); //get field to sort by
                for (int i = listSize; i > 0; --i) {
                    for (int j = 0; j < i - 1; ++j) {
                        if (!checkOrder(field, j)) { //compare two contacts then swap if needed
                            temporary = contactList.get(j);
                            contactList.set(j, contactList.get(j + 1));
                            contactList.set(j + 1, temporary);
                        }
                    }
                }
                this.saveStatus = "unsaved"; //changes have been made and have't been save
                System.out.printf("Contacts sorted by %s.\n", field);
            }
        }
    }

    private String getValidField() {
        Scanner newField = new Scanner(System.in);
        System.out.println("Sort by (name/phone/email/home): ");
        String field = newField.nextLine().trim().toLowerCase(); //get user input ignoring spaces at 2 ends and case

        while (!field.equals("name") && !field.equals("phone") && !field.equals("email") && !field.equals("home")) {
            System.out.println("Invalid input.");
            System.out.println("Sort by (name/phone/email/home): ");
            field = newField.nextLine().trim().toLowerCase(); //get user input ignoring spaces at 2 ends and case
        }
        return field;
    }
    //function to compare 2 specific field info
    public boolean checkOrder(String field, int index) {
        switch (field) {
            case "name":
                return !(contactList.get(index).getFullName().toLowerCase().compareTo(contactList.get(index + 1).getFullName().toLowerCase()) > 0);
            case "phone":
                return !(contactList.get(index).getPhoneNumber().toLowerCase().compareTo(contactList.get(index + 1).getPhoneNumber().toLowerCase()) > 0);
            case "email":
                return !(contactList.get(index).getEmailAddress().toLowerCase().compareTo(contactList.get(index + 1).getEmailAddress().toLowerCase()) > 0);
            case "home":
                return !(contactList.get(index).getHomeAddress().toLowerCase().compareTo(contactList.get(index + 1).getHomeAddress().toLowerCase()) > 0);
            default:
                return true;
        }
    }
    //check duplicate with non case sensitive
    public boolean isDuplicate(Contact newContact) {
        if (listSize == 0) { //first contact to add
            return false;
        } else {
            for (int i = 0; i < listSize; ++i) {
                if (!newContact.getFullName().toLowerCase().matches(contactList.get(i).getFullName().toLowerCase())) {
                    continue;
                } else if (!newContact.getPhoneNumber().toLowerCase().matches(contactList.get(i).getPhoneNumber().toLowerCase())) {
                    continue;
                } else if (!newContact.getEmailAddress().toLowerCase().matches(contactList.get(i).getEmailAddress().toLowerCase())) {
                    continue;
                } else if (!newContact.getHomeAddress().toLowerCase().matches(contactList.get(i).getHomeAddress().toLowerCase())) {
                    continue;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
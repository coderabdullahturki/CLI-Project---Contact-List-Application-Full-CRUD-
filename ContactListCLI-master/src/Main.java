

import java.io.*;
import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ContactList amarContactList = new ContactList();
        File contactFile = new File("ContactListCLI-master/contacts.txt");
        int function;
        String[] functionName = {"","LOAD_FROM_FILE","VIEW_CONTACTS", "ADD_CONTACT", "EDIT_CONTACT", "DELETE_CONTACT",
                "SEARCH_CONTACTS", "SORT_CONTACTS", "SAVE_TO_FILE", "QUIT"}; //match function name with it's index number to avoid magic number

        checkOpenFile(contactFile, "ContactListCLI-master/contacts.txt");
        do {
            displayMenu();
            function = getFunctionNumber();
        } while (performFunction(functionName[function], amarContactList, contactFile)); //run until quit (false)
    }

    public static void checkOpenFile(File contactFile, String fileName) {
        if (!contactFile.exists()) {
            System.out.println("Source file contacts.txt does not exist. Please include file (rename file to) contacts.txt to run program.");
            System.exit(1);
        }
    }

    public static void displayMenu() {
        System.out.println("1. Load contacts");
        System.out.println("2. View all contacts");
        System.out.println("3. Add new contact");
        System.out.println("4. Edit a contact");
        System.out.println("5. Delete a contact");
        System.out.println("6. Search contact list");
        System.out.println("7. Sort contact list");
        System.out.println("8. Save contacts to file");
        System.out.println("9. Exit");

        System.out.println("Select a function (1-9): ");
    }
    //get valid function number 1- 9
    public static int getFunctionNumber() {
        Scanner inputInitial = new Scanner(System.in);
        String number = inputInitial.nextLine().trim(); //get user input ignoring spaces at two ends

        while (number.length() != 1 || !Character.isDigit(number.charAt(0))) {
            System.out.println("Invalid function number.");
            System.out.println("Select a function (1-9): ");
            number = inputInitial.nextLine().trim();
        }
        return Integer.valueOf(number);
    }

    public static boolean performFunction(String function, ContactList contactList, File contactFile) throws FileNotFoundException {//, File outputFile) throws FileNotFoundException {
        boolean checkRun = true;

        System.out.println();
        switch (function) {
            case "LOAD_FROM_FILE":
                loadFromFile(contactList, contactFile);
                break;
            case "VIEW_CONTACTS":
                contactList.viewContactList();
                break;
            case "ADD_CONTACT":
                contactList.createNewContact();
                break;
            case "EDIT_CONTACT":
                contactList.editContact();
                break;
            case "DELETE_CONTACT":
                contactList.deleteContact();
                break;
            case "SEARCH_CONTACTS":
                contactList.searchContacts();
                break;
            case "SORT_CONTACTS":
                contactList.sortContacts();
                break;
            case "SAVE_TO_FILE":
                saveToFile(contactList, contactFile);
                break;
            case "QUIT":
                checkRun = quitProgram(contactList);
            default:
                break;
        }
        if (checkRun) {
            System.out.println("-------------------------------");
            System.out.println();
        }
        return checkRun;
    }

    public static void loadFromFile(ContactList contactList, File contactFile) throws FileNotFoundException {
        String splitRegex = "; ";
        Scanner fScan = new Scanner(contactFile);
        String[] newContactInfo;
        int validNumberOfInfo = 4;
        int count = 0;

        while(fScan.hasNextLine()) {
            String newContact = fScan.nextLine();
            newContactInfo = newContact.split(splitRegex);
            //skip if contact is not in valid format
            if (newContactInfo.length != validNumberOfInfo || !Contact.checkValidContactInfo(newContactInfo[0], newContactInfo[1], newContactInfo[2], newContactInfo[3])) {
                continue;
            } else {
                contactList.loadNewContact(newContactInfo);
                ++count;
            }
        }
        if (count == 0) { //no contact in the file
            System.out.println("There is no contact in the file.");
        } else {
            System.out.println("Contacts with invalid format or including ; might not be able to load to contact list.");
            System.out.println("Contacts loaded.");
        }
        fScan.close();
    }

    public static void saveToFile(ContactList contactList, File contactFile) throws FileNotFoundException {
        if (contactList.getListSize() == 0) { //alert no contacts in the list
            System.out.println("No contacts in the list. Saving may cause lost contact in file.");
        }
        if(makeDecision("save")) { //confirm decision
            PrintWriter output = new PrintWriter(contactFile);
            for (int i = 0; i < contactList.getListSize(); ++i) {
                output.println(contactList.getContact(i).getContactInfo());
            }
            output.close();
            contactList.setSaveStatus("saved");
            System.out.println("Contacts saved.");
        }
    }

    public static boolean quitProgram(ContactList contactList) {
        Scanner newOptionToQuit = new Scanner(System.in);

        if (contactList.getSaveStatus() == "unsaved" && makeDecision("unsaved")) { ////alert unsaved list
            return true;
        } else if (makeDecision("exit")) { //confirm decision
            System.out.println("Thank you very much! Good bye!");
            return false;
        }
        return true;
    }
    //prompt user to input Y or N with appropriate message
    public static boolean makeDecision(String information) {
        if(information == "save") {
            System.out.println("Current file can not be retrieved once saved. Are you sure you want to save(Y/N)?: ");
        } else {
            if (information == "exit") {
                System.out.println("Are you sure you want to exit(Y/N)?: ");
            } else if (information == "unsaved") {
                System.out.println("Changes are unsaved. Do you want to save first(Y/N)?: ");
            } else if (information == "view") {
                System.out.println("Do you want to view the contact list(Y/N)?: ");
            } else if (information == "search") {
                System.out.println("Do you want to search for contacts(Y/N)?: ");
            } else if (information == "delete") {
                System.out.println("Are you sure you want to delete this contact(Y/N)?:  ");
            } else { //confirm decision for edit a specific field
                System.out.printf("Change %s (Y/N)?: \n", information);
            }
        }
        return checkDecision();
    }
    //get Y or N
    public static boolean checkDecision() {
        Scanner newDecision = new Scanner(System.in);
        String decision;

        decision = newDecision.nextLine().trim().toUpperCase(); //non case-sensitive, ignore white spaces at two ends
        while (!decision.equals("Y") &&!decision.equals("N")) {
            System.out.println("Invalid option.");
            System.out.println("Please enter Y or N: ");
            decision = newDecision.nextLine().trim().toUpperCase();
        }
        return (decision.equals("Y"));
    }
}

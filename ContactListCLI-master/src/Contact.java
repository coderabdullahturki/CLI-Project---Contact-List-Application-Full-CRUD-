
import java.util.Scanner;

class Contact {
    private String fullName = "", phoneNumber = "", emailAddress = "", homeAddress = "", contactInfo = "";

    public Contact() {
    }

    public Contact(String fullName, String phoneNumber, String emailAddress, String homeAddress) {
        if (checkValidContactInfo(fullName, phoneNumber, emailAddress, homeAddress)) {
            this.fullName = fullName;
            this.phoneNumber = phoneNumber;
            this.emailAddress = emailAddress;
            this.homeAddress = homeAddress;
            setContactInfo();
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName() {
        System.out.println("Full name: ");
        this.fullName = getValidInput("Full name");
        setContactInfo();
    }

    public void setFullName(String name) {
        if (name.matches(Contact.getRegex("Full name"))) {
            this.fullName = name;
            setContactInfo();
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber() {
        System.out.println("Phone number (minimum 5 numbers, maximum 15 numbers, " +
                "with or without the leading +, no more than 1 space in a row): ");
        this.phoneNumber = getValidInput("Phone number");
        setContactInfo();
    }

    public void setPhoneNumber(String phone) {
        if (phone.matches(Contact.getRegex("Phone number"))) {
            this.phoneNumber = phone;
            setContactInfo();
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress() {
        System.out.println("Email address (an array of characters with one and only one '@' between them and at least 1 dot after @ (not next to @)): ");
        this.emailAddress = getValidInput("Email address");
        setContactInfo();
    }

    public void setEmailAddress(String email) {
        if (email.matches(Contact.getRegex("Email address"))) {
            this.emailAddress = email;
            setContactInfo();
        }
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress() {
        System.out.println("Home address: ");
        this.homeAddress = getValidInput("Home address");
        setContactInfo();
    }

    public void setHomeAddress(String home) {
        if (home.matches(Contact.getRegex("Home address"))) {
            this.homeAddress = home;
            setContactInfo();
        }
    }

    public String getContactInfo() {
        return contactInfo;
    }

    private void setContactInfo() {
        this.contactInfo = fullName + "; " + phoneNumber + "; " + emailAddress + "; " + homeAddress;
    }

    public void displayContactInfo(){
        System.out.println(contactInfo);
    }

    public static String getRegex(String type) {
        String regex = "";
        switch (type) {
            case "Full name":
            case "Home address":
                return ".+";
            case "Phone number":
                return "^\\+?\\s?(\\d\\s?){5,15}$";
            case "Email address":
                return "^[^@.]+([.][^@.]+||[^@.]*)*@[^@.]+[.][^@.]+([.][^@.]+||[^@.]*)*$";
            default:
                return regex;
        }
    }

    private String getValidInput(String type) {
        Scanner input = new Scanner(System.in);
        String newInfo = input.nextLine().trim(); //get user input ignoring spaces at two ends

        while (!newInfo.matches(getRegex(type))) {
            System.out.println("Invalid input");
            System.out.printf("%s: ", type);
            newInfo = input.nextLine().trim(); //get user input ignoring spaces at two ends
        }
        return newInfo;
    }

    public static boolean checkValidContactInfo(String fullName, String phoneNumber, String emailAddress, String homeAddress) {
        return (fullName.matches(Contact.getRegex("Full name")) && phoneNumber.matches(Contact.getRegex("Phone number"))
                && emailAddress.matches(Contact.getRegex("Email address")) && homeAddress.matches(Contact.getRegex("Home address")));
    }
}
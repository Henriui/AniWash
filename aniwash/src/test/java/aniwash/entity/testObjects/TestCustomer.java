package aniwash.entity.testObjects;

import aniwash.entity.Customer;
public class TestCustomer {

    static final String[] names = {"John", "Jane", "Jack", "James", "Jill", "Nelly", "Nina", "Kim", "Kate", "Mark", "Mary", "Marry", "Mia", "Molly", "Monica", "Lily", "Lilly", "Linda", "Liisa", "Lida", "Rita"};

    static final String[] phoneNumbers = {"0401234567", "0401234568", "0401234569", "0401234570", "0401234571", "0401234572", "0401234573", "0401234574", "0401234575", "0401234576", "0401234577", "0401234578", "0401234579", "0401234580", "0401234581", "0401234582", "0401234583", "0401234584", "0401234585", "0401234586", "0401234587"};

    static final String[] emails = {"yep@email.com", "waddap@email.com", "okay@email.fi", "email@email.com", "genericEmail@email.com", "whySerious@email.com", "thisIsOkay@email.com", "john@email.com", "jane@email.com", "jack.E@email.com", "james.O@email.com", "lily.a@email.com", "aoeu@email.com", "mmmm@email.com", "adsf@email.com", "mouseevent@email.com", "diehard@email.com", "kalkkuna@email.com", "lempielain@email.com", "huutonauru@email.com", "lemmikki@email.com"};

    static final String[] address = {"John street 1", "Michael Street 2", "James Street 3", "Lily Street 4", "Linda Street 5", "Lilly Street 6", "Lida Street 7", "Liisa Street 8", "Rita Street 9", "Kate Street 10", "Kim Street 11", "Nina Street 12", "Nelly Street 13", "Jill Street 14", "Jill Street 15", "James Street 16", "Jack Street 17", "Jane Street 18", "John Street 19", "Mary Street 20", "Mark Street 21"};

    static final String[] zipCodes = {"12345", "12346", "12347", "12348", "12349", "12350", "12351", "12352", "12353", "12354", "12355", "12356", "12357", "12358", "12359", "12360", "12361", "12362", "12363", "12364", "12365"};

    static String customerName = "John";
    static String customerPhoneNumber = "0401234567";
    static String customerEmail = "john.johna@email.com";
    static String customerAddress = "John street 1";
    static String customerZipCode = "12345";

    public static Customer getTestCustomer() {
        resetTestCustomer();
        return new Customer(customerName, customerPhoneNumber, customerEmail, customerAddress, customerZipCode);
    }

    private static void resetTestCustomer() {
        customerName = "John";
        customerPhoneNumber = "0401234567";
        customerEmail = "john.johna@email.com";
        customerAddress = "John street 1";
        customerZipCode = "12345";
    }

    public static Customer generateTestCustomer() {
        customerName = names[(int) (Math.random() * names.length)];
        customerPhoneNumber = phoneNumbers[(int) (Math.random() * phoneNumbers.length)];
        customerEmail = emails[(int) (Math.random() * emails.length)];
        customerAddress = address[(int) (Math.random() * address.length)];
        customerZipCode = zipCodes[(int) (Math.random() * zipCodes.length)];
        return new Customer(customerName, customerPhoneNumber, customerEmail, customerAddress, customerZipCode);
    }

}

package aniwash.view.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

public class LanguageSave {

    public static void writeLanguageToFile(Locale locale) {
        try (FileOutputStream stream = new FileOutputStream("aniwash\\data\\language.data");
             ObjectOutputStream oos = new ObjectOutputStream(stream)) {
            oos.writeObject(locale);
        } catch (Exception e) {
            System.out.println("Writing to file failed");
            System.err.println(e);
        }
    }

    public static Locale readLanguageFromFile() {
        try (FileInputStream stream = new FileInputStream("aniwash\\data\\language.data");
             ObjectInputStream ois = new ObjectInputStream(stream)) {
            return (Locale) ois.readObject();
        } catch (Exception e) {
            System.out.println("Reading from file failed");
            System.err.println(e);
            return null;
        }
    }
}


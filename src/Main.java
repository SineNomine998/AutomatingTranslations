import java.io.*;
import java.util.*;

public class Main {

    private static String filename;
    private static String newFilename;
    private static Map<String, String> translations;
    private static Queue<String> allLines;
    private static String targetLanguage;
    private static Writer writer;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");
        Scanner input = new Scanner(System.in);

        translations = new HashMap<>();
        allLines = new LinkedList<>();

//        filename = input.next();
        // TODO: Replace the hardcoded variables (nl, lv, ro)
        // Hardcoded variables for testing
//        filename = "django.po";
        filename = "django.po";
        targetLanguage = "ro";
//        newFilename = "new_django.po";
        newFilename = "new_django7.po";


        readAllLines();
        writer = new BufferedWriter(new FileWriter(newFilename));
        writePrologue();
        extractTexts();
        getTranslations();

        System.out.println(translations.size() + " original texts");

        for (String key : translations.keySet()) {
            System.out.println(key + " -> " + translations.get(key) + "\n");
        }

        Translator translator = new Translator(targetLanguage, "Hello, World!");
        String translatedText = translator.translate();
        System.out.println(translatedText);

        writeToFile();
        writer.close();
    }

    /**
     * Writes the translations to a new file
     */
    private static void writeToFile() throws IOException {
        for (String key : translations.keySet()) {
            writer.write("msgid \"" + key + "\"\n");
            writer.write("msgstr \"" + translations.get(key) + "\"\n\n");
        }
    }

    /**
     * Writes the first 20 lines of the file to the new file
     * @throws IOException if an I/O error occurs
     */
    private static void writePrologue() throws IOException {
        for(int i = 0; i < 21; i++) {
            String currentLine = allLines.poll();
            writer.write(currentLine + "\n");
        }
    }

    /**
     * Gets the translations for the texts that are not already translated, from the API and updates the translations map
     */
    private static void getTranslations() {
        for (String key : translations.keySet()) {
//            if (translations.get(key) == null || translations.get(key).isEmpty()) {
                Translator translator = new Translator(targetLanguage, key);
                String translatedText = translator.translate();
                translations.put(key, translatedText);
//            }
        }
    }

    /**
     * Reads all lines from the file
     */
    public static void readAllLines() {
        try {
            Scanner fileScanner = new Scanner(new File(filename));

            while (fileScanner.hasNextLine()) {
                allLines.add(fileScanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Enter a valid file.");
            System.exit(2);
        }
    }

    /**
     * Extracts the texts and couples them with their translations (if the translation exists)
     */
    public static void extractTexts() {
        String currentMsgId = null;
        while (!allLines.isEmpty()) {
            String line = allLines.poll();
            if (line.startsWith("msgid ")) {
                if (line.equals("msgid \"\"")) {
                    assert allLines.peek() != null;
                    if(allLines.peek().startsWith("msgstr"))
                        continue;
                    StringBuilder original = new StringBuilder();
                    while (!allLines.isEmpty()) {
                        String nextLine = allLines.peek();
                        if (nextLine == null || !nextLine.startsWith("\"")) {
                            break;
                        }
                        nextLine = allLines.poll();
                        original.append(nextLine, 1, nextLine.length() - 1);
                    }
                    currentMsgId = original.toString();
                    translations.put(currentMsgId, null);
                } else {
                    currentMsgId = line.substring(7, line.length() - 1);
                    translations.put(currentMsgId, null);
                }
            }
            else if (line.startsWith("msgstr ")) {
                if (line.equals("msgstr \"\"")) {
                    StringBuilder translated = new StringBuilder();
                    while (!allLines.isEmpty()) {
                        String nextLine = allLines.peek();
                        if (nextLine == null || !nextLine.startsWith("\"")) {
                            break;
                        }
                        nextLine = allLines.poll();
                        translated.append(nextLine, 1, nextLine.length() - 1);
                    }
                    if (currentMsgId != null) {
                        translations.put(currentMsgId, translated.toString());
                    }
                } else {
                    String translatedText = line.substring(8, line.length() - 1);
                    if (currentMsgId != null) {
                        translations.put(currentMsgId, translatedText);
                    }
                }
            }
        }
    }
}
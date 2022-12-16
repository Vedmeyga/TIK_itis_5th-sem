package encoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LZW {

    public static final String fileName = "src\\files\\inputLZW.txt";

    public String encodeLzw(String text) {
        List<String> alphabet = Arrays.stream(readFromFile().split(" ")).collect(Collectors.toList());
        int dictSize = alphabet.size();
        Map<String, String> dictionary = new HashMap<>();
        for (int i = 0; i < alphabet.size(); i++) {
            dictionary.put(alphabet.get(i), convertToBinary(i));
        }
        System.out.println(dictionary);

        String foundChars = "";
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (!alphabet.contains(String.valueOf(character))) {
                System.out.printf("No such symbol '%s' in alphabet: %s\n", character, alphabet);
                return null;
            }
            String charsToAdd = foundChars + character;
            if (dictionary.containsKey(charsToAdd)) {
                foundChars = charsToAdd;
            } else {
                result.append(dictionary.get(foundChars));
                dictionary.put(charsToAdd, convertToBinary(dictSize++));
                foundChars = String.valueOf(character);
            }
        }
        if (!foundChars.equals("")) {
            result.append(dictionary.get(foundChars));
        }
        return result.toString();
    }

    private String readFromFile() {
        try {
            return Files.lines(Paths.get(fileName)).toList().get(0);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String convertToBinary(int codeNumber) {
        StringBuilder code = new StringBuilder(Integer.toBinaryString(codeNumber));
        return "00000000".substring(0, 8 - code.length()) + code;
    }
}

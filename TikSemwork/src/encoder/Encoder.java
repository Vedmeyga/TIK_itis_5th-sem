package encoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Encoder {
    public static void main(String[] args) throws IOException {
        //bwt
        List<String> bwtResultList = algBWT();
        String transformedWord = bwtResultList.get(0);
        String wordIndex = bwtResultList.get(1);

        //lzw
        LZW lzw = new LZW();
        String result = lzw.encodeLzw(transformedWord);

        //sout
        System.out.print(result + "\n" + wordIndex);
    }

    public static List<String> algBWT() {
        //entering a line
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        int size = message.length();

        //adding words to a list after shifting the first symbol of a message
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String transform = message.substring(i, size) + message.substring(0, i);
            list.add(transform);
            System.out.println(transform);
        }
        //sorting words lexicographically
        List<String> sortedList = list.stream().sorted().collect(Collectors.toList());
        //building a sequence from the last symbols of each word
        StringBuilder resultSequence = new StringBuilder();
        int position = -1;
        for (int i = 0; i < size; i++) {
            resultSequence.append(sortedList.get(i).charAt(size - 1));

            //if a word from the sorted list matches the original one, then remember its position
            if (sortedList.get(i).equals(message)) {
                position = i;
            }
        }
        //passing the transformed word and position of the original message
        List<String> results = new ArrayList<>();
        results.add(resultSequence.toString());
        results.add(String.valueOf(position));

        return results;
    }
}

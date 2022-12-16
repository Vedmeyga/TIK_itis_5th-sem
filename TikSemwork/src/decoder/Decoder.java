package decoder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Decoder {
    public static void main(String[] args) throws IOException {
//        final String file_path = "C:\\Users\\Матвей\\IdeaProjects\\TikSemwork\\src\\files\\inputArithmetic.txt";

        //reading file
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("src\\files\\inputArithmetic.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //getting alphabet, probability distribution, length of the segment and word length
        List<String> alphabet = Arrays.stream(reader.readLine().split(" ")).collect(Collectors.toList());
        List<Double> probabilities = Arrays.stream(reader.readLine().split(" ")).map(Double::valueOf).toList();
        Double segmentLength = Double.valueOf(reader.readLine());
        int wordLength = Integer.parseInt(reader.readLine());

        //getting an original word by decoding a binary sequence by arithmetic coding
        String originalWord = arithmetic(alphabet, probabilities, segmentLength, wordLength);

        //outputting
        System.out.println(originalWord);
    }

    public static String arithmetic(List<String> alphabet, List<Double> probabilities, Double segmentLength, int wordLength) {
        StringBuilder result = new StringBuilder();
        Double[] segmentsBorders = new Double[probabilities.size()+1];
        segmentsBorders[0] = 0.0;
        for (int i = 1; i < probabilities.size(); i++) {
            segmentsBorders[i] = segmentsBorders[i-1] + probabilities.get(i-1);
        }

        //looking for a segment that includes a number of encoded word
        for (int i = 0; i < wordLength; i++) {
            for (int j = 0; j < segmentsBorders.length-1; j++) {
                if (segmentLength >= segmentsBorders[j] && segmentLength < segmentsBorders[j+1]) {

                    //first symbol
                    result.append(alphabet.get(j));

                    //then shifting the left and right borders to the found segment
                    segmentsBorders[0] = segmentsBorders[j];
                    segmentsBorders[segmentsBorders.length-1] = segmentsBorders[j+1];

                    for (int l = 1; l < segmentsBorders.length-1; l++) {
                        segmentsBorders[l] = segmentsBorders[l -1] + (probabilities.get(l -1) *
                                (segmentsBorders[segmentsBorders.length-1] - segmentsBorders[0]));
                    }
                    break;
                }
            }
        }
        return result.toString();
    }
}

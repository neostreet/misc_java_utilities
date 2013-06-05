import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;
import java.lang.Math;
import java.io.IOException;
 
public class FlopQuiz {
    public static boolean flush(int[] suit)
    {
        return ((suit[0] == suit[1]) && (suit[0] == suit[2]));
    }

    public static boolean straight(int[] denom)
    {
        if (pair_or_better(denom))
            return false;

        int min = Math.min(Math.min(denom[0],denom[1]),denom[2]);
        int max = Math.max(Math.max(denom[0],denom[1]),denom[2]);
        int max2;

        // handle wheel case
        if (max == 12) {
          if (denom[0] == 12)
            max2 = Math.max(denom[1],denom[2]);
          else if (denom[1] == 12)
            max2 = Math.max(denom[0],denom[2]);
          else
            max2 = Math.max(denom[0],denom[1]);

          if (max2 <= 3)
            return true;
        }

        return (max - min <= 4);
    }

    public static boolean pair_or_better(int[] denom)
    {
        return ((denom[0] == denom[1]) || (denom[0] == denom[2]) || (denom[1] == denom[2]));
    }

    public static boolean isWet(int[] suit,int[] denom)
    {
        if (flush(suit))
          return true;

        if (straight(denom))
          return true;

        if (pair_or_better(denom))
          return true;

        return false;
    }

    public static void main(String[] args) throws IOException {
        int[] card_ix = new int[3];
        int[] suit = new int[3];
        int[] denom = new int[3];
        boolean is_wet;
        String suit_str = "cdhs";
        String denom_str = "23456789TJQKA";
        int num_quizzes;
        int num_dry_flops;
        int num_wet_flops;

        if (args.length != 1) {
            System.out.println("usage: FlopQuiz filename");
            return;
        }

        Path path = FileSystems.getDefault().getPath(args[0]);
        Charset charset = Charset.forName("US-ASCII");

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line = null;
            num_quizzes = num_wet_flops = num_dry_flops = 0;

            while ((line = reader.readLine()) != null) {
                Scanner s = new Scanner(line);

                for (int i = 0; i < 3; i++) {
                    card_ix[i] = s.nextInt();
                    suit[i] = card_ix[i] / 13;
                    denom[i] = card_ix[i] % 13;
                }

                is_wet = isWet(suit,denom);

                num_quizzes++;

                if (is_wet)
                    num_wet_flops++;
                else
                    num_dry_flops++;

                System.out.println(
                  denom_str.substring(denom[0],denom[0]+1) + suit_str.substring(suit[0],suit[0]+1) + " " +
                  denom_str.substring(denom[1],denom[1]+1) + suit_str.substring(suit[1],suit[1]+1) + " " +
                  denom_str.substring(denom[2],denom[2]+1) + suit_str.substring(suit[2],suit[2]+1) + " " +
                  (is_wet ? "wet" : "dry"));
            }

            System.out.println("num_quizzes   = " + num_quizzes);
            System.out.println("num_dry_flops = " + num_dry_flops);
            System.out.println("num_wet_flops = " + num_wet_flops);
        }
        catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}

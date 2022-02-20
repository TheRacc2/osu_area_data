package live.kazutree;

import com.sun.javafx.geom.Vec2f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static String input(String output) {
        System.out.println(output);

        return new Scanner(System.in).nextLine();
    }

    static List<String> read_lines(File in) {
        List<String> return_list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(in));

            String line;
            while ((line = reader.readLine()) != null)
                return_list.add(line);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return return_list;
    }

    public static Vec2f calculate_average_area_ranked(List<Response> responses, int rank) {
        Vec2f return_vec = new Vec2f();
        int count = 0;

        for (Response r : responses) {
            if (rank == 0 || r.rank == rank) {
                return_vec.x += r.used_area.x;
                return_vec.y += r.used_area.y;

                count++;
            }
        }

        return new Vec2f(return_vec.x / count, return_vec.y / count);
    }

    public static void main(String[] args) {
        File cvs_file = new File(input("Path to '.csv' file"));

        List<String> lines = read_lines(cvs_file);
        List<Response> responses = new ArrayList<>();

        for (String line : lines) {
            line = line.substring(line.indexOf('\"', 2) + 2); // Remove timestamp
            line = line.replace(" ", "");
            line = line.replace(",", "."); // europeans causing issues :(
            line = line.replace("mm", "");
            line = line.replace("Digit", "");

            line = line.replace("х", "x"); // https://imgur.com/a/6Og4T82 FUCK, he only did it in the resolution too.
            line = line.replace("×", "x"); // i literally told you how to format it stop using special characters

            List<String> response_data = new ArrayList<>();

            Pattern p = Pattern.compile("\"([^\"]*)\""); // Black magic from https://stackoverflow.com/questions/1473155/how-to-get-data-between-quotes-in-java
            Matcher m = p.matcher(line);
            while (m.find())
                response_data.add(m.group(1));

            responses.add(new Response(response_data));
        }

        System.out.println("Average area: " + Response.average_used_area().x + "x" + Response.average_used_area().y + "\n");
        System.out.println("Average area ratio: " + Response.average_area_ratio() + "\n");
        System.out.println("Average screen ratio: " + Response.average_screen_ratio().x + "x" + Response.average_screen_ratio().y + "\n"); // for every 1 unit, you move x pixels on the screen

        Response big = Response.biggest_area;
        Response sml = Response.smallest_area;
        System.out.println("Largest area: " + big.used_area.x + "x" + big.used_area.y +
                        (!big.username.equals("") ? " (" + big.username + ")" : "") +
                "\n");

        System.out.println("Smallest area: " + sml.used_area.x + "x" + sml.used_area.y +
                        (!sml.username.equals("") ? " (" + sml.username + ")" : "") +
                "\n");

        System.out.println("");
        System.out.println("========================");
        System.out.println("");

        for (int i = 6; i > 2; i--) {
            System.out.println("Average area for " + i + " Digit: " + calculate_average_area_ranked(responses, i).x + "x" + calculate_average_area_ranked(responses, i).y);
        }
    }
}

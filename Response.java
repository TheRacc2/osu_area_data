package live.kazutree;

import com.sun.javafx.geom.Vec2f;

import java.util.List;

public class Response {
    // survey areas
    public static Response biggest_area = null;
    public static Response smallest_area = null;
    public static Response most_average = null;

    private static Vec2f average_used_area = new Vec2f();
    private static float average_area_ratio = 0;
    private static Vec2f average_screen_ratio = new Vec2f();
    private static Vec2f average_aspect_ratio = new Vec2f();

    static int response_count = 0;

    // IntelliJ automatic Getters
    public static Vec2f average_used_area() {
        return new Vec2f(average_used_area.x / response_count, average_used_area.y / response_count);
    }

    public static float average_area_ratio() {
        return average_area_ratio / response_count;
    }

    public static Vec2f average_screen_ratio() {
        return new Vec2f(average_screen_ratio.x / response_count, average_screen_ratio.y / response_count);
    }

    public static Vec2f average_aspect_ratio() {
        Vec2f temp = new Vec2f(Math.round(average_aspect_ratio.x / response_count), Math.round(average_aspect_ratio.y / response_count));

        long gcd = gcd((long) temp.x, (long) temp.y);
        temp.x /= gcd;
        temp.y /= gcd; // round again. im tired and on 3 hrs of sleep implementing this, so its gonna be shit i dont want complaints

        return temp;
    }

    public Vec2f used_area;
    public Vec2f max_area;
    public Vec2f res;
    public int rank;
    public String username;

    public float area_ratio;
    public Vec2f screen_ratio;
    public Vec2f aspect_ratio;

    public Vec2f string_to_vec(String in) {
        Vec2f return_vec = new Vec2f();
        in = in.toLowerCase();

        try {
            return_vec.x = Float.parseFloat(in.substring(0, in.indexOf('x')));
            return_vec.y = Float.parseFloat(in.substring(in.indexOf('x') + 1));
        } catch (Exception ex) {
            // Someone entered something wrong
        }

        return return_vec;
    }

    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public Response(final List<String> response_data) {
        used_area = string_to_vec(response_data.get(0));

        if (!response_data.get(1).equals("")) // Not a required question, check to see if it was provided, putting "idk" or something similar in here makes me hate you on a personal level
            max_area = string_to_vec(response_data.get(1));

        rank = Integer.parseInt(response_data.get(2));
        username = response_data.get(3);
        res = string_to_vec(response_data.get(4));

        if (used_area.x > 0 && used_area.y > 0) { // fuck everyone who put 0 in the area thing
            average_used_area.x += used_area.x;
            average_used_area.y += used_area.y;

            if (biggest_area == null || (used_area.x * used_area.y > biggest_area.used_area.x * biggest_area.used_area.y))
                biggest_area = this;

            if (smallest_area == null || (used_area.x * used_area.y < smallest_area.used_area.x * smallest_area.used_area.y))
                smallest_area = this;

            area_ratio = Math.round(used_area.x / used_area.y);
            average_area_ratio += area_ratio;

            screen_ratio = new Vec2f(res.x / used_area.x, res.y / used_area.y);

            if (screen_ratio.x > 0 && screen_ratio.y > 0) {
                average_screen_ratio.x += screen_ratio.x;
                average_screen_ratio.y += screen_ratio.y;
            }

            aspect_ratio = new Vec2f(Math.round(res.x / used_area.x), Math.round(res.y / used_area.y)); // fuck it we're not doing decimal aspect ratio that shit isnt real

            //simplify
            long gcd = gcd((long) aspect_ratio.x, (long) aspect_ratio.y);
            aspect_ratio.x /= gcd;
            aspect_ratio.y /= gcd;

            if (aspect_ratio.x > 0 && aspect_ratio.y > 0) {
                average_aspect_ratio.x += aspect_ratio.x;
                average_aspect_ratio.y += aspect_ratio.y;
            }
        }

        response_count++;
    }
}

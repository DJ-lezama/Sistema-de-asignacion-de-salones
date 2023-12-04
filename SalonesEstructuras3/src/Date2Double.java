import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Date2Double {

    public static double Convert(String date)
    {
        return convertToDecimal(date);
    }
    private static   double  convertToDecimal(String timeString) {
        try {
            // Parse the time string
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(timeString);

            // Convert the time to milliseconds
            long milliseconds = date.getTime();
            System.out.println("millis " + milliseconds);
            // Convert milliseconds to hours and minutes
            double hours = (double) milliseconds / (60 * 60 * 1000) - 6;

            return hours;
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception
            return 0.0; // Return a default value or handle the error as needed
        }
    }
}

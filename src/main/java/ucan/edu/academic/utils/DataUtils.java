package ucan.edu.academic.utils;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

public class DataUtils {
    public static LocalDate escolherAleatoriamenteDataNascimento(int minIdade, int maxIdade) {
        Random random = new Random();
        
        // Calculate min and max birth years based on current date and age range
        int currentYear = LocalDate.now().getYear();
        int minBirthYear = currentYear - maxIdade;
        int maxBirthYear = currentYear - minIdade;
        
        // Randomly select a year within the range
        int birthYear = random.nextInt(maxBirthYear - minBirthYear + 1) + minBirthYear;
        
        // Randomly select month and day
        Month randomMonth = Month.of(random.nextInt(12) + 1);
        int maxDayOfMonth = randomMonth.length(LocalDate.now().isLeapYear());
        int randomDay = random.nextInt(maxDayOfMonth) + 1;
        
        return LocalDate.of(birthYear, randomMonth, randomDay);
    }
}
// PregnancyTracker.java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class PregnancyTracker {
    private static final Map<Integer, double[]> FETAL_DATA = new HashMap<>();
    static {
        FETAL_DATA.put(4, new double[]{0.4, 0.5, 2.0, 0.3});
        FETAL_DATA.put(5, new double[]{0.7, 1.0, 3.0, 0.5});
        FETAL_DATA.put(6, new double[]{1.1, 2.0, 4.0, 0.7});
        FETAL_DATA.put(7, new double[]{1.5, 3.0, 6.0, 1.0});
        FETAL_DATA.put(8, new double[]{2.0, 5.0, 8.0, 1.5});
        FETAL_DATA.put(9, new double[]{2.5, 8.0, 10.0, 2.0});
        FETAL_DATA.put(10, new double[]{3.1, 12.0, 12.0, 2.5});
        FETAL_DATA.put(11, new double[]{4.1, 17.0, 15.0, 3.5});
        FETAL_DATA.put(12, new double[]{5.4, 25.0, 18.0, 4.5});
        FETAL_DATA.put(13, new double[]{6.7, 35.0, 21.0, 5.5});
        FETAL_DATA.put(14, new double[]{8.1, 50.0, 24.0, 6.5});
        FETAL_DATA.put(15, new double[]{9.4, 70.0, 27.0, 7.5});
        FETAL_DATA.put(16, new double[]{10.8, 100.0, 30.0, 8.5});
        FETAL_DATA.put(17, new double[]{12.2, 140.0, 33.0, 9.5});
        FETAL_DATA.put(18, new double[]{13.6, 190.0, 36.0, 10.5});
        FETAL_DATA.put(19, new double[]{15.0, 250.0, 39.0, 11.5});
        FETAL_DATA.put(20, new double[]{16.4, 320.0, 42.0, 12.5});
        FETAL_DATA.put(21, new double[]{17.8, 400.0, 45.0, 13.5});
        FETAL_DATA.put(22, new double[]{19.2, 490.0, 48.0, 14.5});
        FETAL_DATA.put(23, new double[]{20.6, 590.0, 51.0, 15.5});
        FETAL_DATA.put(24, new double[]{22.0, 700.0, 54.0, 16.5});
        FETAL_DATA.put(25, new double[]{23.4, 820.0, 57.0, 17.5});
        FETAL_DATA.put(26, new double[]{24.8, 960.0, 60.0, 18.5});
        FETAL_DATA.put(27, new double[]{26.2, 1110.0, 63.0, 19.5});
        FETAL_DATA.put(28, new double[]{27.6, 1280.0, 66.0, 20.5});
        FETAL_DATA.put(29, new double[]{29.0, 1460.0, 69.0, 21.5});
        FETAL_DATA.put(30, new double[]{30.4, 1660.0, 72.0, 22.5});
        FETAL_DATA.put(31, new double[]{31.8, 1880.0, 75.0, 23.5});
        FETAL_DATA.put(32, new double[]{33.2, 2120.0, 78.0, 24.5});
        FETAL_DATA.put(33, new double[]{34.6, 2380.0, 81.0, 25.5});
        FETAL_DATA.put(34, new double[]{36.0, 2660.0, 84.0, 26.5});
        FETAL_DATA.put(35, new double[]{37.4, 2960.0, 87.0, 27.5});
        FETAL_DATA.put(36, new double[]{38.8, 3280.0, 90.0, 28.5});
        FETAL_DATA.put(37, new double[]{40.2, 3620.0, 93.0, 29.5});
        FETAL_DATA.put(38, new double[]{41.6, 3980.0, 96.0, 30.5});
        FETAL_DATA.put(39, new double[]{43.0, 4360.0, 99.0, 31.5});
        FETAL_DATA.put(40, new double[]{44.4, 4760.0, 102.0, 32.5});
        FETAL_DATA.put(41, new double[]{45.8, 5180.0, 105.0, 33.5});
        FETAL_DATA.put(42, new double[]{47.2, 5620.0, 108.0, 34.5});
    }

    private static double[] getWeekData(int week) {
        if (week < 1 || week > 42) return null;
        return FETAL_DATA.get(week);
    }

    private static String trimester(int week) {
        if (week < 13) return "First";
        if (week < 27) return "Second";
        return "Third";
    }

    private static LocalDate calculateEDD(LocalDate lmp) {
        return lmp.plusDays(280);
    }

    private static void displayInfo(int week, int days) {
        double[] data = getWeekData(week);
        if (data == null) {
            System.out.println("Invalid week. Must be between 1 and 42.");
            return;
        }
        String tri = trimester(week);
        System.out.println("\n🤰 Pregnancy Tracker");
        System.out.printf("Week: %d + %d days%n", week, days);
        System.out.printf("Trimester: %s%n", tri);
        System.out.printf("Fetal length: %.1f cm%n", data[0]);
        System.out.printf("Fetal weight: %.0f g%n", data[1]);
        System.out.printf("BPD: %.1f mm%n", data[2]);
        System.out.printf("CRL: %.1f cm%n", data[3]);
    }

    private static void showAll() {
        System.out.println("\n📊 Complete Fetal Growth Table (1-42 weeks)");
        System.out.println("Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)");
        for (int w = 1; w <= 42; w++) {
            double[] d = FETAL_DATA.get(w);
            if (d != null) {
                System.out.printf("%d\t%.1f\t\t%.0f\t\t%.1f\t%.1f%n", w, d[0], d[1], d[2], d[3]);
            }
        }
    }

    public static void main(String[] args) {
        Integer weeks = null, days = 0;
        String lmp = null;
        boolean today = false, all = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--weeks":
                    weeks = Integer.parseInt(args[++i]);
                    break;
                case "--days":
                    days = Integer.parseInt(args[++i]);
                    break;
                case "--lmp":
                    lmp = args[++i];
                    break;
                case "--today":
                    today = true;
                    break;
                case "--all":
                    all = true;
                    break;
                case "--help":
                    System.out.println("Usage: java PregnancyTracker [--weeks N] [--days N] [--lmp YYYY-MM-DD] [--today] [--all]");
                    System.exit(0);
            }
        }

        if (all) {
            showAll();
            return;
        }

        if (lmp != null) {
            try {
                LocalDate lmpDate = LocalDate.parse(lmp, DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate edd = calculateEDD(lmpDate);
                if (today) {
                    LocalDate todayDate = LocalDate.now();
                    if (todayDate.isBefore(lmpDate)) {
                        System.out.println("LMP date cannot be in the future.");
                        return;
                    }
                    long daysBetween = ChronoUnit.DAYS.between(lmpDate, todayDate);
                    int w = (int)(daysBetween / 7);
                    int d = (int)(daysBetween % 7);
                    displayInfo(w, d);
                } else {
                    System.out.printf("\n📅 Estimated Due Date (from LMP): %s%n", edd);
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
            return;
        }

        if (weeks != null) {
            displayInfo(weeks, days != null ? days : 0);
        } else {
            System.out.println("Please specify --weeks, --lmp, or --all");
        }
    }
}

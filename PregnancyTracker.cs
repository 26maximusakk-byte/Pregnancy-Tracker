// PregnancyTracker.cs
using System;
using System.Collections.Generic;

class PregnancyTracker
{
    static Dictionary<int, double[]> fetalData = new Dictionary<int, double[]>()
    {
        {4, new double[]{0.4, 0.5, 2.0, 0.3}}, {5, new double[]{0.7, 1.0, 3.0, 0.5}},
        {6, new double[]{1.1, 2.0, 4.0, 0.7}}, {7, new double[]{1.5, 3.0, 6.0, 1.0}},
        {8, new double[]{2.0, 5.0, 8.0, 1.5}}, {9, new double[]{2.5, 8.0, 10.0, 2.0}},
        {10, new double[]{3.1, 12.0, 12.0, 2.5}}, {11, new double[]{4.1, 17.0, 15.0, 3.5}},
        {12, new double[]{5.4, 25.0, 18.0, 4.5}}, {13, new double[]{6.7, 35.0, 21.0, 5.5}},
        {14, new double[]{8.1, 50.0, 24.0, 6.5}}, {15, new double[]{9.4, 70.0, 27.0, 7.5}},
        {16, new double[]{10.8, 100.0, 30.0, 8.5}}, {17, new double[]{12.2, 140.0, 33.0, 9.5}},
        {18, new double[]{13.6, 190.0, 36.0, 10.5}}, {19, new double[]{15.0, 250.0, 39.0, 11.5}},
        {20, new double[]{16.4, 320.0, 42.0, 12.5}}, {21, new double[]{17.8, 400.0, 45.0, 13.5}},
        {22, new double[]{19.2, 490.0, 48.0, 14.5}}, {23, new double[]{20.6, 590.0, 51.0, 15.5}},
        {24, new double[]{22.0, 700.0, 54.0, 16.5}}, {25, new double[]{23.4, 820.0, 57.0, 17.5}},
        {26, new double[]{24.8, 960.0, 60.0, 18.5}}, {27, new double[]{26.2, 1110.0, 63.0, 19.5}},
        {28, new double[]{27.6, 1280.0, 66.0, 20.5}}, {29, new double[]{29.0, 1460.0, 69.0, 21.5}},
        {30, new double[]{30.4, 1660.0, 72.0, 22.5}}, {31, new double[]{31.8, 1880.0, 75.0, 23.5}},
        {32, new double[]{33.2, 2120.0, 78.0, 24.5}}, {33, new double[]{34.6, 2380.0, 81.0, 25.5}},
        {34, new double[]{36.0, 2660.0, 84.0, 26.5}}, {35, new double[]{37.4, 2960.0, 87.0, 27.5}},
        {36, new double[]{38.8, 3280.0, 90.0, 28.5}}, {37, new double[]{40.2, 3620.0, 93.0, 29.5}},
        {38, new double[]{41.6, 3980.0, 96.0, 30.5}}, {39, new double[]{43.0, 4360.0, 99.0, 31.5}},
        {40, new double[]{44.4, 4760.0, 102.0, 32.5}}, {41, new double[]{45.8, 5180.0, 105.0, 33.5}},
        {42, new double[]{47.2, 5620.0, 108.0, 34.5}}
    };

    static double[] GetWeekData(int week)
    {
        if (week < 1 || week > 42) return null;
        return fetalData.TryGetValue(week, out var d) ? d : null;
    }

    static string Trimester(int week)
    {
        if (week < 13) return "First";
        if (week < 27) return "Second";
        return "Third";
    }

    static DateTime CalculateEDD(DateTime lmp)
    {
        return lmp.AddDays(280);
    }

    static void DisplayInfo(int week, int days)
    {
        var data = GetWeekData(week);
        if (data == null)
        {
            Console.WriteLine("Invalid week. Must be between 1 and 42.");
            return;
        }
        string tri = Trimester(week);
        Console.WriteLine("\n🤰 Pregnancy Tracker");
        Console.WriteLine($"Week: {week} + {days} days");
        Console.WriteLine($"Trimester: {tri}");
        Console.WriteLine($"Fetal length: {data[0]:F1} cm");
        Console.WriteLine($"Fetal weight: {data[1]:F0} g");
        Console.WriteLine($"BPD: {data[2]:F1} mm");
        Console.WriteLine($"CRL: {data[3]:F1} cm");
    }

    static void ShowAll()
    {
        Console.WriteLine("\n📊 Complete Fetal Growth Table (1-42 weeks)");
        Console.WriteLine("Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)");
        for (int w = 1; w <= 42; w++)
        {
            if (fetalData.TryGetValue(w, out var d))
            {
                Console.WriteLine($"{w}\t{d[0]:F1}\t\t{d[1]:F0}\t\t{d[2]:F1}\t{d[3]:F1}");
            }
        }
    }

    static void Main(string[] args)
    {
        int? weeks = null;
        int days = 0;
        string lmp = null;
        bool today = false, all = false;

        for (int i = 0; i < args.Length; i++)
        {
            switch (args[i])
            {
                case "--weeks": weeks = int.Parse(args[++i]); break;
                case "--days": days = int.Parse(args[++i]); break;
                case "--lmp": lmp = args[++i]; break;
                case "--today": today = true; break;
                case "--all": all = true; break;
                case "--help":
                    Console.WriteLine("Usage: dotnet run -- [--weeks N] [--days N] [--lmp YYYY-MM-DD] [--today] [--all]");
                    return;
            }
        }

        if (all)
        {
            ShowAll();
            return;
        }

        if (lmp != null)
        {
            if (!DateTime.TryParse(lmp, out DateTime lmpDate))
            {
                Console.WriteLine("Invalid date format. Use YYYY-MM-DD.");
                return;
            }
            DateTime edd = CalculateEDD(lmpDate);
            if (today)
            {
                DateTime todayDate = DateTime.Today;
                if (todayDate < lmpDate)
                {
                    Console.WriteLine("LMP date cannot be in the future.");
                    return;
                }
                int daysBetween = (int)(todayDate - lmpDate).TotalDays;
                int w = daysBetween / 7;
                int d = daysBetween % 7;
                DisplayInfo(w, d);
            }
            else
            {
                Console.WriteLine($"\n📅 Estimated Due Date (from LMP): {edd:yyyy-MM-dd}");
            }
            return;
        }

        if (weeks.HasValue)
        {
            DisplayInfo(weeks.Value, days);
        }
        else
        {
            Console.WriteLine("Please specify --weeks, --lmp, or --all");
        }
    }
}

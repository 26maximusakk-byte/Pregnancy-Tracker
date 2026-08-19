// pregnancy_tracker.go
package main

import (
	"flag"
	"fmt"
	"time"
)

type FetalData struct {
	Length float64
	Weight float64
	BPD    float64
	CRL    float64
}

var fetalData = map[int]FetalData{
	4:  {0.4, 0.5, 2.0, 0.3}, 5:  {0.7, 1.0, 3.0, 0.5},
	6:  {1.1, 2.0, 4.0, 0.7}, 7:  {1.5, 3.0, 6.0, 1.0},
	8:  {2.0, 5.0, 8.0, 1.5}, 9:  {2.5, 8.0, 10.0, 2.0},
	10: {3.1, 12.0, 12.0, 2.5}, 11: {4.1, 17.0, 15.0, 3.5},
	12: {5.4, 25.0, 18.0, 4.5}, 13: {6.7, 35.0, 21.0, 5.5},
	14: {8.1, 50.0, 24.0, 6.5}, 15: {9.4, 70.0, 27.0, 7.5},
	16: {10.8, 100.0, 30.0, 8.5}, 17: {12.2, 140.0, 33.0, 9.5},
	18: {13.6, 190.0, 36.0, 10.5}, 19: {15.0, 250.0, 39.0, 11.5},
	20: {16.4, 320.0, 42.0, 12.5}, 21: {17.8, 400.0, 45.0, 13.5},
	22: {19.2, 490.0, 48.0, 14.5}, 23: {20.6, 590.0, 51.0, 15.5},
	24: {22.0, 700.0, 54.0, 16.5}, 25: {23.4, 820.0, 57.0, 17.5},
	26: {24.8, 960.0, 60.0, 18.5}, 27: {26.2, 1110.0, 63.0, 19.5},
	28: {27.6, 1280.0, 66.0, 20.5}, 29: {29.0, 1460.0, 69.0, 21.5},
	30: {30.4, 1660.0, 72.0, 22.5}, 31: {31.8, 1880.0, 75.0, 23.5},
	32: {33.2, 2120.0, 78.0, 24.5}, 33: {34.6, 2380.0, 81.0, 25.5},
	34: {36.0, 2660.0, 84.0, 26.5}, 35: {37.4, 2960.0, 87.0, 27.5},
	36: {38.8, 3280.0, 90.0, 28.5}, 37: {40.2, 3620.0, 93.0, 29.5},
	38: {41.6, 3980.0, 96.0, 30.5}, 39: {43.0, 4360.0, 99.0, 31.5},
	40: {44.4, 4760.0, 102.0, 32.5}, 41: {45.8, 5180.0, 105.0, 33.5},
	42: {47.2, 5620.0, 108.0, 34.5},
}

func getWeekData(week int) *FetalData {
	if week < 1 || week > 42 {
		return nil
	}
	d, ok := fetalData[week]
	if !ok {
		return nil
	}
	return &d
}

func trimester(week int) string {
	if week < 13 {
		return "First"
	} else if week < 27 {
		return "Second"
	}
	return "Third"
}

func calculateEDD(lmp time.Time) time.Time {
	return lmp.AddDate(0, 0, 280)
}

func displayInfo(week, days int) {
	data := getWeekData(week)
	if data == nil {
		fmt.Println("Invalid week. Must be between 1 and 42.")
		return
	}
	tri := trimester(week)
	fmt.Println("\n🤰 Pregnancy Tracker")
	fmt.Printf("Week: %d + %d days\n", week, days)
	fmt.Printf("Trimester: %s\n", tri)
	fmt.Printf("Fetal length: %.1f cm\n", data.Length)
	fmt.Printf("Fetal weight: %.0f g\n", data.Weight)
	fmt.Printf("BPD: %.1f mm\n", data.BPD)
	fmt.Printf("CRL: %.1f cm\n", data.CRL)
}

func showAll() {
	fmt.Println("\n📊 Complete Fetal Growth Table (1-42 weeks)")
	fmt.Println("Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)")
	for w := 1; w <= 42; w++ {
		if d, ok := fetalData[w]; ok {
			fmt.Printf("%d\t%.1f\t\t%.0f\t\t%.1f\t%.1f\n", w, d.Length, d.Weight, d.BPD, d.CRL)
		}
	}
}

func main() {
	weeksPtr := flag.Int("weeks", 0, "Gestational weeks (1-42)")
	daysPtr := flag.Int("days", 0, "Additional days (0-6)")
	lmpPtr := flag.String("lmp", "", "Last menstrual period date (YYYY-MM-DD)")
	todayPtr := flag.Bool("today", false, "Use today's date")
	allPtr := flag.Bool("all", false, "Show all weeks table")
	flag.Parse()

	if *allPtr {
		showAll()
		return
	}

	if *lmpPtr != "" {
		lmp, err := time.Parse("2006-01-02", *lmpPtr)
		if err != nil {
			fmt.Println("Invalid date format. Use YYYY-MM-DD.")
			return
		}
		edd := calculateEDD(lmp)
		if *todayPtr {
			today := time.Now()
			if today.Before(lmp) {
				fmt.Println("LMP date cannot be in the future.")
				return
			}
			delta := today.Sub(lmp)
			days := int(delta.Hours() / 24)
			weeks := days / 7
			daysRem := days % 7
			displayInfo(weeks, daysRem)
		} else {
			fmt.Printf("\n📅 Estimated Due Date (from LMP): %s\n", edd.Format("2006-01-02"))
		}
		return
	}

	if *weeksPtr > 0 {
		displayInfo(*weeksPtr, *daysPtr)
	} else {
		fmt.Println("Please specify --weeks, --lmp, or --all")
	}
}

# pregnancy_tracker.py
import sys
import argparse
from datetime import datetime, timedelta

# Fetal growth data: week -> (length_cm, weight_g, BPD_mm, CRL_cm)
FETAL_DATA = {
    4: (0.4, 0.5, 2.0, 0.3), 5: (0.7, 1.0, 3.0, 0.5),
    6: (1.1, 2.0, 4.0, 0.7), 7: (1.5, 3.0, 6.0, 1.0),
    8: (2.0, 5.0, 8.0, 1.5), 9: (2.5, 8.0, 10.0, 2.0),
    10: (3.1, 12.0, 12.0, 2.5), 11: (4.1, 17.0, 15.0, 3.5),
    12: (5.4, 25.0, 18.0, 4.5), 13: (6.7, 35.0, 21.0, 5.5),
    14: (8.1, 50.0, 24.0, 6.5), 15: (9.4, 70.0, 27.0, 7.5),
    16: (10.8, 100.0, 30.0, 8.5), 17: (12.2, 140.0, 33.0, 9.5),
    18: (13.6, 190.0, 36.0, 10.5), 19: (15.0, 250.0, 39.0, 11.5),
    20: (16.4, 320.0, 42.0, 12.5), 21: (17.8, 400.0, 45.0, 13.5),
    22: (19.2, 490.0, 48.0, 14.5), 23: (20.6, 590.0, 51.0, 15.5),
    24: (22.0, 700.0, 54.0, 16.5), 25: (23.4, 820.0, 57.0, 17.5),
    26: (24.8, 960.0, 60.0, 18.5), 27: (26.2, 1110.0, 63.0, 19.5),
    28: (27.6, 1280.0, 66.0, 20.5), 29: (29.0, 1460.0, 69.0, 21.5),
    30: (30.4, 1660.0, 72.0, 22.5), 31: (31.8, 1880.0, 75.0, 23.5),
    32: (33.2, 2120.0, 78.0, 24.5), 33: (34.6, 2380.0, 81.0, 25.5),
    34: (36.0, 2660.0, 84.0, 26.5), 35: (37.4, 2960.0, 87.0, 27.5),
    36: (38.8, 3280.0, 90.0, 28.5), 37: (40.2, 3620.0, 93.0, 29.5),
    38: (41.6, 3980.0, 96.0, 30.5), 39: (43.0, 4360.0, 99.0, 31.5),
    40: (44.4, 4760.0, 102.0, 32.5), 41: (45.8, 5180.0, 105.0, 33.5),
    42: (47.2, 5620.0, 108.0, 34.5),
}

def get_week_data(week, days=0):
    if week < 1 or week > 42:
        return None
    # simple interpolation for days (not needed, just return week data)
    return FETAL_DATA.get(week)

def trimester(week):
    if week < 13:
        return "First"
    elif week < 27:
        return "Second"
    else:
        return "Third"

def calculate_edd(lmp_date):
    return lmp_date + timedelta(days=280)

def display_info(week, days=0):
    data = get_week_data(week)
    if not data:
        print("Invalid week. Must be between 1 and 42.")
        return
    length, weight, bpd, crl = data
    tri = trimester(week)
    print("\n🤰 Pregnancy Tracker")
    print(f"Week: {week} + {days} days")
    print(f"Trimester: {tri}")
    print(f"Fetal length: {length:.1f} cm")
    print(f"Fetal weight: {weight:.0f} g")
    print(f"BPD: {bpd:.1f} mm")
    print(f"CRL: {crl:.1f} cm")

def show_all():
    print("\n📊 Complete Fetal Growth Table (1-42 weeks)")
    print("Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)")
    for w in range(1, 43):
        d = FETAL_DATA.get(w)
        if d:
            print(f"{w}\t{d[0]:.1f}\t\t{d[1]:.0f}\t\t{d[2]:.1f}\t{d[3]:.1f}")

def main():
    parser = argparse.ArgumentParser(description="Pregnancy Tracker")
    parser.add_argument("--weeks", type=int, help="Gestational weeks (1-42)")
    parser.add_argument("--days", type=int, default=0, help="Additional days (0-6)")
    parser.add_argument("--lmp", help="Last menstrual period date (YYYY-MM-DD)")
    parser.add_argument("--today", action="store_true", help="Use today's date for LMP calculation")
    parser.add_argument("--all", action="store_true", help="Show all weeks table")
    args = parser.parse_args()

    if args.all:
        show_all()
        return

    if args.lmp:
        try:
            lmp = datetime.strptime(args.lmp, "%Y-%m-%d")
            edd = calculate_edd(lmp)
            if args.today:
                today = datetime.today()
                if today < lmp:
                    print("LMP date cannot be in the future.")
                    return
                delta = today - lmp
                weeks = delta.days // 7
                days = delta.days % 7
                display_info(weeks, days)
            else:
                print(f"\n📅 Estimated Due Date (from LMP): {edd.strftime('%Y-%m-%d')}")
                # Also show current gestation if today is given
                # Without --today, just print EDD
        except ValueError:
            print("Invalid date format. Use YYYY-MM-DD.")
        return

    if args.weeks:
        display_info(args.weeks, args.days)
    else:
        print("Please specify --weeks, --lmp, or --all")

if __name__ == "__main__":
    main()

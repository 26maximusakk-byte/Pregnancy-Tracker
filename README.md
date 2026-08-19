🤰 Pregnancy Tracker — Fetal Growth Diary
8 languages, one complete pregnancy tracker – calculate fetal size, weight, estimated due date, and trimester based on gestational age – right from your terminal.

✨ Features
📅 Calculate fetal measurements – length (cm), weight (g), BPD (mm), CRL (cm) by week of pregnancy

📆 Due date calculator – compute EDD from last menstrual period (LMP)

🗓️ Current gestational age – enter LMP date and see today's week + days

📊 Trimester info – shows first, second, or third trimester

📋 All‑weeks table – print the full growth chart (1‑42 weeks)

🎯 Precise input – specify weeks and optional days (e.g., --weeks 20 --days 3)

🚀 Common Usage
All implementations follow the same CLI pattern:

bash
# Show info for week 20
<command> --weeks 20

# Show info for 20 weeks and 3 days
<command> --weeks 20 --days 3

# Calculate EDD from LMP
<command> --lmp 2026-01-15

# Print all weeks table
<command> --all

# Use current date to compute gestational age from LMP
<command> --lmp 2026-01-15 --today
Arguments:

--weeks <n> – gestational weeks (1‑42)

--days <n> – additional days (0‑6)

--lmp <YYYY-MM-DD> – last menstrual period date

--today – use today's date for EDD calculation

--all – show complete growth table

📸 Example Output
text
🤰 Pregnancy Tracker
Week: 20 + 0 days
Trimester: Second
Fetal length: 25.6 cm
Fetal weight: 320 g
BPD: 48.0 mm
CRL: 16.5 cm
Estimated Due Date (from LMP): 2026-10-22
📁 Repository Structure
text
.
├── README.md
├── python/
│   └── pregnancy_tracker.py
├── go/
│   └── pregnancy_tracker.go
├── javascript/
│   └── pregnancy_tracker.js
├── ruby/
│   └── pregnancy_tracker.rb
├── php/
│   └── pregnancy_tracker.php
├── java/
│   └── PregnancyTracker.java
├── csharp/
│   └── PregnancyTracker.cs
└── cpp/
    └── pregnancy_tracker.cpp

// pregnancy_tracker.js
#!/usr/bin/env node
const { program } = require('commander');

const FETAL_DATA = {
    4: [0.4, 0.5, 2.0, 0.3], 5: [0.7, 1.0, 3.0, 0.5],
    6: [1.1, 2.0, 4.0, 0.7], 7: [1.5, 3.0, 6.0, 1.0],
    8: [2.0, 5.0, 8.0, 1.5], 9: [2.5, 8.0, 10.0, 2.0],
    10: [3.1, 12.0, 12.0, 2.5], 11: [4.1, 17.0, 15.0, 3.5],
    12: [5.4, 25.0, 18.0, 4.5], 13: [6.7, 35.0, 21.0, 5.5],
    14: [8.1, 50.0, 24.0, 6.5], 15: [9.4, 70.0, 27.0, 7.5],
    16: [10.8, 100.0, 30.0, 8.5], 17: [12.2, 140.0, 33.0, 9.5],
    18: [13.6, 190.0, 36.0, 10.5], 19: [15.0, 250.0, 39.0, 11.5],
    20: [16.4, 320.0, 42.0, 12.5], 21: [17.8, 400.0, 45.0, 13.5],
    22: [19.2, 490.0, 48.0, 14.5], 23: [20.6, 590.0, 51.0, 15.5],
    24: [22.0, 700.0, 54.0, 16.5], 25: [23.4, 820.0, 57.0, 17.5],
    26: [24.8, 960.0, 60.0, 18.5], 27: [26.2, 1110.0, 63.0, 19.5],
    28: [27.6, 1280.0, 66.0, 20.5], 29: [29.0, 1460.0, 69.0, 21.5],
    30: [30.4, 1660.0, 72.0, 22.5], 31: [31.8, 1880.0, 75.0, 23.5],
    32: [33.2, 2120.0, 78.0, 24.5], 33: [34.6, 2380.0, 81.0, 25.5],
    34: [36.0, 2660.0, 84.0, 26.5], 35: [37.4, 2960.0, 87.0, 27.5],
    36: [38.8, 3280.0, 90.0, 28.5], 37: [40.2, 3620.0, 93.0, 29.5],
    38: [41.6, 3980.0, 96.0, 30.5], 39: [43.0, 4360.0, 99.0, 31.5],
    40: [44.4, 4760.0, 102.0, 32.5], 41: [45.8, 5180.0, 105.0, 33.5],
    42: [47.2, 5620.0, 108.0, 34.5]
};

function getWeekData(week) {
    if (week < 1 || week > 42) return null;
    return FETAL_DATA[week] || null;
}

function trimester(week) {
    if (week < 13) return "First";
    if (week < 27) return "Second";
    return "Third";
}

function calculateEDD(lmp) {
    const d = new Date(lmp);
    d.setDate(d.getDate() + 280);
    return d;
}

function displayInfo(week, days) {
    const data = getWeekData(week);
    if (!data) {
        console.log("Invalid week. Must be between 1 and 42.");
        return;
    }
    const [length, weight, bpd, crl] = data;
    const tri = trimester(week);
    console.log("\n🤰 Pregnancy Tracker");
    console.log(`Week: ${week} + ${days} days`);
    console.log(`Trimester: ${tri}`);
    console.log(`Fetal length: ${length.toFixed(1)} cm`);
    console.log(`Fetal weight: ${weight.toFixed(0)} g`);
    console.log(`BPD: ${bpd.toFixed(1)} mm`);
    console.log(`CRL: ${crl.toFixed(1)} cm`);
}

function showAll() {
    console.log("\n📊 Complete Fetal Growth Table (1-42 weeks)");
    console.log("Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)");
    for (let w = 1; w <= 42; w++) {
        const d = FETAL_DATA[w];
        if (d) {
            console.log(`${w}\t${d[0].toFixed(1)}\t\t${d[1].toFixed(0)}\t\t${d[2].toFixed(1)}\t${d[3].toFixed(1)}`);
        }
    }
}

program
    .option('--weeks <n>', 'Gestational weeks (1-42)', parseInt)
    .option('--days <n>', 'Additional days (0-6)', parseInt, 0)
    .option('--lmp <date>', 'Last menstrual period date (YYYY-MM-DD)')
    .option('--today', 'Use today\'s date')
    .option('--all', 'Show all weeks table')
    .parse(process.argv);

const opts = program.opts();

if (opts.all) {
    showAll();
    process.exit(0);
}

if (opts.lmp) {
    const lmp = new Date(opts.lmp);
    if (isNaN(lmp)) {
        console.log("Invalid date format. Use YYYY-MM-DD.");
        process.exit(1);
    }
    const edd = calculateEDD(lmp);
    if (opts.today) {
        const today = new Date();
        if (today < lmp) {
            console.log("LMP date cannot be in the future.");
            process.exit(1);
        }
        const diff = today - lmp;
        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const weeks = Math.floor(days / 7);
        const rem = days % 7;
        displayInfo(weeks, rem);
    } else {
        console.log(`\n📅 Estimated Due Date (from LMP): ${edd.toISOString().slice(0,10)}`);
    }
    process.exit(0);
}

if (opts.weeks) {
    displayInfo(opts.weeks, opts.days || 0);
} else {
    console.log("Please specify --weeks, --lmp, or --all");
}

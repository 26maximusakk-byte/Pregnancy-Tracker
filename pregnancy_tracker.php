# pregnancy_tracker.php
#!/usr/bin/env php
<?php

$FETAL_DATA = [
    4 => [0.4, 0.5, 2.0, 0.3], 5 => [0.7, 1.0, 3.0, 0.5],
    6 => [1.1, 2.0, 4.0, 0.7], 7 => [1.5, 3.0, 6.0, 1.0],
    8 => [2.0, 5.0, 8.0, 1.5], 9 => [2.5, 8.0, 10.0, 2.0],
    10 => [3.1, 12.0, 12.0, 2.5], 11 => [4.1, 17.0, 15.0, 3.5],
    12 => [5.4, 25.0, 18.0, 4.5], 13 => [6.7, 35.0, 21.0, 5.5],
    14 => [8.1, 50.0, 24.0, 6.5], 15 => [9.4, 70.0, 27.0, 7.5],
    16 => [10.8, 100.0, 30.0, 8.5], 17 => [12.2, 140.0, 33.0, 9.5],
    18 => [13.6, 190.0, 36.0, 10.5], 19 => [15.0, 250.0, 39.0, 11.5],
    20 => [16.4, 320.0, 42.0, 12.5], 21 => [17.8, 400.0, 45.0, 13.5],
    22 => [19.2, 490.0, 48.0, 14.5], 23 => [20.6, 590.0, 51.0, 15.5],
    24 => [22.0, 700.0, 54.0, 16.5], 25 => [23.4, 820.0, 57.0, 17.5],
    26 => [24.8, 960.0, 60.0, 18.5], 27 => [26.2, 1110.0, 63.0, 19.5],
    28 => [27.6, 1280.0, 66.0, 20.5], 29 => [29.0, 1460.0, 69.0, 21.5],
    30 => [30.4, 1660.0, 72.0, 22.5], 31 => [31.8, 1880.0, 75.0, 23.5],
    32 => [33.2, 2120.0, 78.0, 24.5], 33 => [34.6, 2380.0, 81.0, 25.5],
    34 => [36.0, 2660.0, 84.0, 26.5], 35 => [37.4, 2960.0, 87.0, 27.5],
    36 => [38.8, 3280.0, 90.0, 28.5], 37 => [40.2, 3620.0, 93.0, 29.5],
    38 => [41.6, 3980.0, 96.0, 30.5], 39 => [43.0, 4360.0, 99.0, 31.5],
    40 => [44.4, 4760.0, 102.0, 32.5], 41 => [45.8, 5180.0, 105.0, 33.5],
    42 => [47.2, 5620.0, 108.0, 34.5]
];

function getWeekData($week) {
    global $FETAL_DATA;
    if ($week < 1 || $week > 42) return null;
    return isset($FETAL_DATA[$week]) ? $FETAL_DATA[$week] : null;
}

function trimester($week) {
    if ($week < 13) return "First";
    if ($week < 27) return "Second";
    return "Third";
}

function calculateEDD($lmp) {
    $date = DateTime::createFromFormat('Y-m-d', $lmp);
    if (!$date) return null;
    $date->add(new DateInterval('P280D'));
    return $date->format('Y-m-d');
}

function displayInfo($week, $days) {
    $data = getWeekData($week);
    if (!$data) {
        echo "Invalid week. Must be between 1 and 42.\n";
        return;
    }
    list($length, $weight, $bpd, $crl) = $data;
    $tri = trimester($week);
    echo "\n🤰 Pregnancy Tracker\n";
    echo "Week: $week + $days days\n";
    echo "Trimester: $tri\n";
    echo "Fetal length: " . number_format($length, 1) . " cm\n";
    echo "Fetal weight: " . number_format($weight, 0) . " g\n";
    echo "BPD: " . number_format($bpd, 1) . " mm\n";
    echo "CRL: " . number_format($crl, 1) . " cm\n";
}

function showAll() {
    global $FETAL_DATA;
    echo "\n📊 Complete Fetal Growth Table (1-42 weeks)\n";
    echo "Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)\n";
    for ($w = 1; $w <= 42; $w++) {
        if (isset($FETAL_DATA[$w])) {
            $d = $FETAL_DATA[$w];
            echo "$w\t" . number_format($d[0], 1) . "\t\t" . number_format($d[1], 0) . "\t\t" . number_format($d[2], 1) . "\t" . number_format($d[3], 1) . "\n";
        }
    }
}

$opts = getopt("", ["weeks:", "days:", "lmp:", "today", "all", "help"]);
if (isset($opts['help'])) {
    echo "Usage: php pregnancy_tracker.php [--weeks N] [--days N] [--lmp YYYY-MM-DD] [--today] [--all]\n";
    exit(0);
}

if (isset($opts['all'])) {
    showAll();
    exit(0);
}

if (isset($opts['lmp'])) {
    $lmp = $opts['lmp'];
    $edd = calculateEDD($lmp);
    if (!$edd) {
        echo "Invalid date format. Use YYYY-MM-DD.\n";
        exit(1);
    }
    if (isset($opts['today'])) {
        $today = new DateTime();
        $lmpDate = DateTime::createFromFormat('Y-m-d', $lmp);
        if ($today < $lmpDate) {
            echo "LMP date cannot be in the future.\n";
            exit(1);
        }
        $diff = $today->diff($lmpDate);
        $days = $diff->days;
        $weeks = intdiv($days, 7);
        $rem = $days % 7;
        displayInfo($weeks, $rem);
    } else {
        echo "\n📅 Estimated Due Date (from LMP): $edd\n";
    }
    exit(0);
}

if (isset($opts['weeks'])) {
    $week = (int)$opts['weeks'];
    $days = isset($opts['days']) ? (int)$opts['days'] : 0;
    displayInfo($week, $days);
} else {
    echo "Please specify --weeks, --lmp, or --all\n";
}
?>

// pregnancy_tracker.cpp
#include <iostream>
#include <map>
#include <string>
#include <vector>
#include <sstream>
#include <iomanip>
#include <ctime>
#include <getopt.h>

using namespace std;

struct FetalData {
    double length, weight, bpd, crl;
};

const map<int, FetalData> FETAL_DATA = {
    {4, {0.4, 0.5, 2.0, 0.3}}, {5, {0.7, 1.0, 3.0, 0.5}},
    {6, {1.1, 2.0, 4.0, 0.7}}, {7, {1.5, 3.0, 6.0, 1.0}},
    {8, {2.0, 5.0, 8.0, 1.5}}, {9, {2.5, 8.0, 10.0, 2.0}},
    {10, {3.1, 12.0, 12.0, 2.5}}, {11, {4.1, 17.0, 15.0, 3.5}},
    {12, {5.4, 25.0, 18.0, 4.5}}, {13, {6.7, 35.0, 21.0, 5.5}},
    {14, {8.1, 50.0, 24.0, 6.5}}, {15, {9.4, 70.0, 27.0, 7.5}},
    {16, {10.8, 100.0, 30.0, 8.5}}, {17, {12.2, 140.0, 33.0, 9.5}},
    {18, {13.6, 190.0, 36.0, 10.5}}, {19, {15.0, 250.0, 39.0, 11.5}},
    {20, {16.4, 320.0, 42.0, 12.5}}, {21, {17.8, 400.0, 45.0, 13.5}},
    {22, {19.2, 490.0, 48.0, 14.5}}, {23, {20.6, 590.0, 51.0, 15.5}},
    {24, {22.0, 700.0, 54.0, 16.5}}, {25, {23.4, 820.0, 57.0, 17.5}},
    {26, {24.8, 960.0, 60.0, 18.5}}, {27, {26.2, 1110.0, 63.0, 19.5}},
    {28, {27.6, 1280.0, 66.0, 20.5}}, {29, {29.0, 1460.0, 69.0, 21.5}},
    {30, {30.4, 1660.0, 72.0, 22.5}}, {31, {31.8, 1880.0, 75.0, 23.5}},
    {32, {33.2, 2120.0, 78.0, 24.5}}, {33, {34.6, 2380.0, 81.0, 25.5}},
    {34, {36.0, 2660.0, 84.0, 26.5}}, {35, {37.4, 2960.0, 87.0, 27.5}},
    {36, {38.8, 3280.0, 90.0, 28.5}}, {37, {40.2, 3620.0, 93.0, 29.5}},
    {38, {41.6, 3980.0, 96.0, 30.5}}, {39, {43.0, 4360.0, 99.0, 31.5}},
    {40, {44.4, 4760.0, 102.0, 32.5}}, {41, {45.8, 5180.0, 105.0, 33.5}},
    {42, {47.2, 5620.0, 108.0, 34.5}}
};

const FetalData* getWeekData(int week) {
    if (week < 1 || week > 42) return nullptr;
    auto it = FETAL_DATA.find(week);
    if (it != FETAL_DATA.end()) return &it->second;
    return nullptr;
}

string trimester(int week) {
    if (week < 13) return "First";
    if (week < 27) return "Second";
    return "Third";
}

time_t parseDate(const string& dateStr) {
    struct tm tm = {};
    if (strptime(dateStr.c_str(), "%Y-%m-%d", &tm) == nullptr) return -1;
    return mktime(&tm);
}

string formatDate(time_t t) {
    char buf[11];
    strftime(buf, sizeof(buf), "%Y-%m-%d", localtime(&t));
    return string(buf);
}

time_t calculateEDD(time_t lmp) {
    return lmp + 280 * 24 * 3600;
}

void displayInfo(int week, int days) {
    const FetalData* data = getWeekData(week);
    if (!data) {
        cout << "Invalid week. Must be between 1 and 42." << endl;
        return;
    }
    string tri = trimester(week);
    cout << "\n🤰 Pregnancy Tracker" << endl;
    cout << "Week: " << week << " + " << days << " days" << endl;
    cout << "Trimester: " << tri << endl;
    cout << fixed << setprecision(1);
    cout << "Fetal length: " << data->length << " cm" << endl;
    cout << fixed << setprecision(0);
    cout << "Fetal weight: " << data->weight << " g" << endl;
    cout << fixed << setprecision(1);
    cout << "BPD: " << data->bpd << " mm" << endl;
    cout << "CRL: " << data->crl << " cm" << endl;
}

void showAll() {
    cout << "\n📊 Complete Fetal Growth Table (1-42 weeks)" << endl;
    cout << "Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)" << endl;
    for (int w = 1; w <= 42; w++) {
        auto it = FETAL_DATA.find(w);
        if (it != FETAL_DATA.end()) {
            const auto& d = it->second;
            cout << w << "\t" << fixed << setprecision(1) << d.length << "\t\t"
                 << fixed << setprecision(0) << d.weight << "\t\t"
                 << fixed << setprecision(1) << d.bpd << "\t" << d.crl << endl;
        }
    }
}

int main(int argc, char* argv[]) {
    int opt;
    int week = 0, days = 0;
    string lmp;
    bool today = false, all = false;

    static struct option long_options[] = {
        {"weeks", required_argument, 0, 'w'},
        {"days", required_argument, 0, 'd'},
        {"lmp", required_argument, 0, 'l'},
        {"today", no_argument, 0, 't'},
        {"all", no_argument, 0, 'a'},
        {"help", no_argument, 0, 'h'},
        {0,0,0,0}
    };

    while ((opt = getopt_long(argc, argv, "w:d:l:tah", long_options, nullptr)) != -1) {
        switch (opt) {
            case 'w': week = stoi(optarg); break;
            case 'd': days = stoi(optarg); break;
            case 'l': lmp = optarg; break;
            case 't': today = true; break;
            case 'a': all = true; break;
            case 'h':
                cout << "Usage: pregnancy_tracker --weeks N [--days N] | --lmp YYYY-MM-DD [--today] | --all" << endl;
                return 0;
        }
    }

    if (all) {
        showAll();
        return 0;
    }

    if (!lmp.empty()) {
        time_t lmpTime = parseDate(lmp);
        if (lmpTime == -1) {
            cout << "Invalid date format. Use YYYY-MM-DD." << endl;
            return 1;
        }
        time_t edd = calculateEDD(lmpTime);
        if (today) {
            time_t now = time(nullptr);
            if (now < lmpTime) {
                cout << "LMP date cannot be in the future." << endl;
                return 1;
            }
            int diff = (int)difftime(now, lmpTime) / (24*3600);
            int w = diff / 7;
            int d = diff % 7;
            displayInfo(w, d);
        } else {
            cout << "\n📅 Estimated Due Date (from LMP): " << formatDate(edd) << endl;
        }
        return 0;
    }

    if (week > 0) {
        displayInfo(week, days);
    } else {
        cout << "Please specify --weeks, --lmp, or --all" << endl;
    }
    return 0;
}

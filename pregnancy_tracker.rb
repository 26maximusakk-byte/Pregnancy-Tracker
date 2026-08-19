# pregnancy_tracker.rb
#!/usr/bin/env ruby
require 'optparse'
require 'date'

FETAL_DATA = {
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
}

def get_week_data(week)
  FETAL_DATA[week]
end

def trimester(week)
  return "First" if week < 13
  return "Second" if week < 27
  "Third"
end

def calculate_edd(lmp)
  lmp + 280
end

def display_info(week, days)
  data = get_week_data(week)
  unless data
    puts "Invalid week. Must be between 1 and 42."
    return
  end
  length, weight, bpd, crl = data
  tri = trimester(week)
  puts "\n🤰 Pregnancy Tracker"
  puts "Week: #{week} + #{days} days"
  puts "Trimester: #{tri}"
  puts "Fetal length: #{length.round(1)} cm"
  puts "Fetal weight: #{weight.round(0)} g"
  puts "BPD: #{bpd.round(1)} mm"
  puts "CRL: #{crl.round(1)} cm"
end

def show_all
  puts "\n📊 Complete Fetal Growth Table (1-42 weeks)"
  puts "Week\tLength(cm)\tWeight(g)\tBPD(mm)\tCRL(cm)"
  (1..42).each do |w|
    d = FETAL_DATA[w]
    next unless d
    puts "#{w}\t#{d[0].round(1)}\t\t#{d[1].round(0)}\t\t#{d[2].round(1)}\t#{d[3].round(1)}"
  end
end

options = {}
OptionParser.new do |opts|
  opts.banner = "Usage: pregnancy_tracker.rb [options]"
  opts.on("--weeks N", Integer, "Gestational weeks (1-42)") { |v| options[:weeks] = v }
  opts.on("--days N", Integer, "Additional days (0-6)") { |v| options[:days] = v }
  opts.on("--lmp DATE", "Last menstrual period date (YYYY-MM-DD)") { |v| options[:lmp] = v }
  opts.on("--today", "Use today's date") { options[:today] = true }
  opts.on("--all", "Show all weeks table") { options[:all] = true }
  opts.on_tail("--help", "Show this message") { puts opts; exit }
end.parse!

if options[:all]
  show_all
  exit
end

if options[:lmp]
  begin
    lmp = Date.parse(options[:lmp])
  rescue ArgumentError
    puts "Invalid date format. Use YYYY-MM-DD."
    exit 1
  end
  edd = calculate_edd(lmp)
  if options[:today]
    today = Date.today
    if today < lmp
      puts "LMP date cannot be in the future."
      exit 1
    end
    delta = today - lmp
    weeks = delta.to_i / 7
    days = delta.to_i % 7
    display_info(weeks, days)
  else
    puts "\n📅 Estimated Due Date (from LMP): #{edd.strftime('%Y-%m-%d')}"
  end
  exit
end

if options[:weeks]
  display_info(options[:weeks], options[:days] || 0)
else
  puts "Please specify --weeks, --lmp, or --all"
end

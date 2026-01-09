# The initial lineup
lineup = [
    ("Code Play", "Indie", 30),
    ("The Pythonistas", "Rock", 45),
    ("Syntax Error", "Metal", 60)
]

# 1. Add the headliner using .append()
headliner = ("The Byte Beats", "Synthwave", 90)
lineup.append(headliner)

# 2. Emergency Swap: Move the first band to the end
late_band = lineup.pop(0) 
lineup.append(late_band)

# 3. Security Check: Remove a specific band by value using .remove()
band_to_remove = ("The Pythonistas", "Rock", 45)
lineup.remove(band_to_remove)

# 4. Calculate Total Time using a for loop and tuple unpacking
total_duration = 0
for name, genre, duration in lineup:
    total_duration += duration
print(lineup)
print(f"\nTotal festival duration: {total_duration} minutes")

lineup = [
    ("Code Play", "Indie", 30),
    ("The Pythonistas", "Rock", 45),
    ("Syntax Error", "Metal", 60)
]

("\n---Py-Fest 2026 Stage Manager---")
print("1. View Lineup & Total Time")
print("2. Add a New Band")
print("3. Move First Band to End (Late Arrival)")
print("4. Remove a Band by Name")
print("5. Move Band to Specific Position")
print("6. Exit")
choice = input("Select an option (1-6): ")

# 1. Find the Band and its current index
if choice == "1":
    print("\n--- Current Lineup ---")
    print(lineup)
    total_time = 0
    for i, (name, genre, duration) in enumerate(lineup, 1):
        total_time += duration
    print(f"Total Festival Duration: {total_time} minute")

# 2. Get the new position from user
elif choice == "2":
    name = input("Enter Band Name: ")
    genre = input("Enter Genre: ")
    duration = int(input("Enter Performance Duration (minutes): "))
    print(f"{name} added!")
#3. Remove from old spot, insert in new spot
elif choice == "3":

#4. 
elif choice == "4":


#5.
elif choice == "5":

#6. 
elif choice == "6":
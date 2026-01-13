lineup = [
    ("Code Play", "Indie", 30),
    ("The Pythonistas", "Rock", 45),
    ("Syntax Error", "Metal", 60)
]
while True:
    print("\n---Py-Fest 2026 Stage Manager---")
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
        print(f"Total Festival Duration: {total_time} minutes")

    # 2. Get the new position from user
    elif choice == "2":
        name = input("Enter Band Name: ")
        genre = input("Enter Genre: ")
        duration = int(input("Enter Performance Duration (minutes): "))
        lineup.append((name, genre, duration))
        print(f"{name} added!")
    #3. Remove from old spot, insert in new spot
    elif choice == "3":
        late_band = lineup.pop(0)
        lineup.append(late_band)
        print(f"{late_band[0]} moved to end!")

    #4. 
    elif choice == "4":
        name_to_remove = input("Enter the name of the band to remove: ")
        for band in lineup:
            if band[0] == name_to_remove:
                lineup.remove(band)
                print(f"{name_to_remove} has been removed from the lineup.")
                break
        else:
            print(f"{name_to_remove} not found in the lineup!")

    #5.
    elif choice == "5":
        if len(lineup) <= 1:
            print("Not enough bands to move!")
        else:
            target_artist = input("Enter the name of the band to move: ")
            new_pos = int(input(f"Enter new position (1-{len(lineup)}): "))
            if 1 <= new_pos <= len(lineup):
                for i, band in enumerate(lineup):
                    if band[0] == target_artist:
                        band_to_move = lineup.pop(i)
                        lineup.insert(new_pos - 1, band_to_move)
                        print(f"{target_artist} moved to position {new_pos}!")
                        break
                else:
                    print(f"{target_artist} not found in the lineup!")
            else:
                print("Invalid position!")
        
    #6. 
    elif choice == "6":
        print("Exiting Stage Manager. Have a great show!")
        break
    else:
        print("Invalid choice")
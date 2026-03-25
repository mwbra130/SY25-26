import datetime

v1 = "my_diary.txt"

while True:
    print("1. Write, 2. Read, 3. Clear, 4. Exit")
    v2 = input("Select (1-4):")

    if v2 == "1":
        v4 = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        v5 = input("Entry: ")
        v3 = open(v1, "a")
        v6 = v3.write(f"[{v4}] {v5}\n")
        v3.close()
    if v2 == "2":
        v3 = open(v1, "r")
        v7 = v3.readlines()
        v3.close()
        for v8 in v7:
            print(v8.strip())

    if v3 == "3":
        v3 = open(v1, "w")
        v1.write("")
        v3.close()
    if v4 == "4":
        break
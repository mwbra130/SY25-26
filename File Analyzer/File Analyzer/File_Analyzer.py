import glob

#Get all .txt files in the current directory

files = glob.glob("server_dump/*.txt")

'''
1. list all files with this error
2. this error
3. this error
'''
Count_Warning = 0
Count_Error = 0
Count_Ok = 0

for file in files:
    file = open(file, "r")
    if "WARN" in file.read():
        Count_Warning += 1
for file in files:
    file = open(file, "r")
    if "ERROR" in file.read():
        Count_Error += 1

for file in files:
    file = open(file, "r")
    if "OK" in file.read():
        Count_Ok += 1
file.close()

while True:
    print("1. List all files with WARN")
    print("2. List all files with ERROR")
    print("3. List all files with OK")
    select = input("Please enter (1-3): ")
    if select == "1":
        print("There are " + str(Count_Warning) + " files with WARN")
    elif select == "2":
        print("There are " + str(Count_Error) + " files with ERROR")
    elif select == "3":
        print("There are " + str(Count_Ok) + " files with OK")
    else:
        print("Please enter a valid number (1-3)")
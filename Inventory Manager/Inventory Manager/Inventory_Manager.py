print("---- Personal Inventory Manager ----")
items = {}
def display_menu():
    print("\nOptions: [1] Add    [2] Remove    [3] List    [4] Exit")
    option = input("Select an option (1-4): ")
    if option == "1":
        add_item()
    elif option == "2":
        remove_item()
    elif option == "3":
        list_items()
    elif option == "4":
        leave()
    else:
        print("Invalid option. Please select a number between 1 and 4.")


def add_item():
    item = input("Enter item name: ").strip().capitalize()
    try:
        quantity = int(input(f"How many {item}s: "))
        if item in items:
            items[item] += quantity
        else:
            items[item] = quantity
        print(f"Updated: {item} (Total: {items[item]})")
        print("--------------------------------------")
    except ValueError:
        print("Please enter a valid number for quantity.")
   

def remove_item():
    item = input("Enter item name to remove: ").strip().capitalize()
    if item in items:
        del items[item]
        print(f"{item} removed from inventory.")
    else:
        print(f"{item} not found in inventory.")

def list_items():
    for item in items:
        print(f"- {item}: {items[item]}")
    print("--------------------------------------")

def leave():
    print("Exiting Inventory Manager. Goodbye!")

while True:
    display_menu()

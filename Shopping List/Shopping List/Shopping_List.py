def print_menu():
    print("1. Add Item")
    print("2. Remove Items")
    print("3. View List")
    print("4. Exit")
    return int(input("Enter your choice: "))

def add_item():
    item = input("Enter the item to add: ")
    my_list.append(item)
    print(f"{item} has been added to your shopping list.")

def remove_items():
    item = input("Enter the item to remove: ")
    if item in my_list:
        my_list.remove(item)
        print(f"{item} has been removed from your shopping list.")
    else:
        print(f"{item} is not in your shopping list.")

def view_list():
    if not my_list:
        print("Your shopping list is empty.")
    else:
        print("Your shopping list contains:")
        for item in my_list:
            print(f"- {item}")


print("Welome to your Shopping List Manager!")
choice = 0
my_list = []

while choice != 4:
    choice = print_menu()
    if choice == 1:
        add_item()
    elif choice == 2:
        remove_items()
    elif choice == 3:
        view_list()

print("Thank you for using the Shopping List Manager. Goodbye!")
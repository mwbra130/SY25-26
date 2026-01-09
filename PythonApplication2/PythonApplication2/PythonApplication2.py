def process_enrollment(student_name, gpa, credits_owned, prerequisites_met, is_staff_child):
    print(f"--- Processing Enrollment for {student_name} ---")
    
    # ERROR 1: Identity (is) vs Equality (==)
    # Intent: Check if the name is "Admin"
    if student_name == "Admin":
        print("Administrative access granted.")

    # ERROR 2: Assignment vs Comparison
    # Intent: Check if GPA is exactly 4.0 for a scholarship
    # Note: In Python, this will actually cause a SyntaxError!
    if gpa == 4.0:
        print("Full Scholarship Awarded!")

    # ERROR 3: Truthiness and empty lists
    # Intent: If the student has no credits, they are a 'Freshman'
    if credits_owned == 0: 
        print("Status: Returning Student")
    else:
        print("Status: New Freshman")

    # ERROR 4: The "Dangling Else" / Indentation Trap
    # Intent: If prerequisites are met, check GPA for honors. 
    # If prerequisites AREN'T met, reject them.
    if prerequisites_met == True:
        print("Prerequisites verified.")
        if gpa > 3.8:
            print("Enrolled in Honors Program.")
    else:
        print("Enrollment Denied: Missing Prerequisites.")

    # ERROR 5: Logical Precedence (and/or)
    # Intent: Allow enrollment if (GPA > 2.0 AND prerequisites met) OR if they are a staff child.
    if (gpa > 2.0 and prerequisites_met) or is_staff_child:
        print("Enrollment Successful.")

    return "Process Complete"

# --- Test Run ---
# This student should be denied (GPA too low), but watch what happens...
process_enrollment("John Doe", 1.5, [], False, True)

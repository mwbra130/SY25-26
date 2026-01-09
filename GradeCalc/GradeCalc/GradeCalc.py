
ap = input("Enter Assessment Percentage: ")
cp = input("Enter Coursework Percentage: ")
fp = input("Enter Final Percentage: ")
print("Assessment", ap, "Coursework", cp, "Final", fp)

ag = input("Enter Assessment Grade: ")
cg = input("Enter Coursework Grade: ")
fg = input("Enter Final Grade: ")

print("Your grade is a: " , (float(ap) * float(ag)/100) + (float(cp) * float(cg)/100) + (float(fp) * float(fg)/100))

t1 = (ap,cp,fp)
l1 = [ag,cg,fg]
t1[1] = 10
import PyPDF2  
quantity = 0
files = []
while quantity <= 0:
    quantity = int(input("How many PDF files do you want to merge? "))

for i in range(int(quantity)):
    file = input("Enter the name of the first PDF file: ")
    files.append(file)

merger = PyPDF2.PdfMerger()

for pdf in files:
    try:
        merger.append(pdf)
        print(f"{pdf} has been added to the merger.")
    except FileNotFoundError:
        print(f"Error: {pdf} not found. Please check the file name and try again.")
    except PermissionError:
        print(f"Error: Permission denied for {pdf}. Please check the file permissions and try again.")
    except Exception as e:
        print(f"An error occurred while adding {pdf}: {e}")
output_filename = input("Enter the name for the merged PDF file (e.g., merged.pdf): ")

output_pdf = open(output_filename, "wb")
merger.write(output_pdf)
merger.close()
print(f"PDFs merged successfully into '{output_filename}'.")
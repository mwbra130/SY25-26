import subprocess

python_path = r"C:\Users\mwbra130\Documents\Python\WPy64-31401\python\python3.14t.exe"

def install(package):
    try:
        subprocess.check_call([python_path, "-m", "pip", "install", "--only-binary=:all:", package])
        print(f"✅ {package} installed successfully!")
    except Exception as e:
        print(f"❌ Failed to install {package}:", e)

print("📦 Installing compatible pandas and yfinance versions...")
install("pandas==2.1.4")
install("yfinance==0.2.28")

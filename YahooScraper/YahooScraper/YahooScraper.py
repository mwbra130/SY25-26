import yfinance as yf

def get_stock_price(ticker: str):
    """
    Fetch live stock data using the yfinance library.
    """
    stock = yf.Ticker(ticker)
    data = stock.info

    return {
        "ticker": data.get("symbol", ticker.upper()),
        "name": data.get("shortName", "N/A"),
        "price": data.get("currentPrice", "N/A"),
        "change": data.get("regularMarketChange", "N/A"),
        "percent_change": data.get("regularMarketChangePercent", "N/A"),
        "currency": data.get("currency", "USD"),
    }

if __name__ == "__main__":
    ticker = input("Enter stock ticker symbol: ").strip().upper()
    info = get_stock_price(ticker)

    print(f"\n{info['name']} ({info['ticker']})")
    print(f"Price: {info['price']} {info['currency']}")
    print(f"Change: {info['change']} ({info['percent_change']}%)")

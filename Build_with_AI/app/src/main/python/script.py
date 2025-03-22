import sys

def process_url(url):
    # Your URL processing logic here
    return f"Processed: {url}"

if __name__ == "__main__":
    if len(sys.argv) > 1:
        result = process_url(sys.argv[1])
        print(result)
    else:
        print("No URL provided")
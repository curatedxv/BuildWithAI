import requests
from bs4 import BeautifulSoup
import google.generativeai as genai
import os
from dotenv import load_dotenv

load_dotenv()

class module1:
    # Configure the Gemini API key
    genai.configure(api_key="AIzaSyAI932dJEhFJeXa3fuChRUxLLEEMxGQBAI")

    def __init__(self):
        pass

    def fetch_webpage_text(self, url):
        """Fetches the text content from a given URL.

        Args:
            url: The URL of the webpage.

        Returns:
            The extracted text content, or None if an error occurs.
        """
        try:
            response = requests.get(url)
            response.raise_for_status()  # Raise an exception for bad status codes

            soup = BeautifulSoup(response.content, "html.parser")
            text = soup.get_text(separator=" ", strip=True)
            # Extract the title
            title_tag = soup.find("title")
            if title_tag:
                title = title_tag.get_text(strip=True)
            else:
                title = "Title not found"
            return title, text
        except requests.exceptions.RequestException as e:
            print(f"Error fetching URL: {e}")
            return None, None
        except Exception as e:
            print(f"An unexpected error occurred: {e}")
            return None, None

    def summarize_text_with_gemini(self, text, prompt_instructions="Summarize the following text:"):
        """Summarizes the given text using the Gemini Pro model.

        Args:
            text: The text to summarize.
            prompt_instructions: Instructions to add to the prompt.

        Returns:
            The summary generated by Gemini, or None if an error occurs.
        """
        try:
            model = genai.GenerativeModel("gemini-2.0-flash")
            prompt = f"{prompt_instructions}\n\n{text}"
            response = model.generate_content(prompt)
            return response.text
        except Exception as e:
            print(f"Error generating summary: {e}")
            return None


if __name__ == "__main__":
    extractor = module1()
    url = "https://www.bbc.com/news/articles/c33706jy774o"  # Example URL

    # Fetch the text from the URL
    title, webpage_text = extractor.fetch_webpage_text(url)

    if webpage_text:
        # Summarize the text using Gemini
        summary = extractor.summarize_text_with_gemini(webpage_text)

        if summary:
            print("Summary:")
            print(summary)
        else:
            print("Failed to generate a summary.")
        if title:
            print("Title:")
            print(title)
        else:
            print("Failed to generate a title.")

    else:
        print("Failed to fetch webpage content.")

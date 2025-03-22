import requests
from bs4 import BeautifulSoup
import google.generativeai as genai
import os
from dotenv import load_dotenv

load_dotenv()

class module1:
    # Configure the Gemini API key
    my_api_key = None

    def __init__(self,api_key):
        self.my_api_key = api_key

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
                raise Exception("Title not found")
            return title, text
        except Exception as e:
            raise Exception("Failed to fetch the webpage text, reason: " + str(e))

    def summarize_text_with_gemini(self, text, prompt_instructions="Summarize the following text:"):
        """Summarizes the given text using the Gemini Pro model.

        Args:
            text: The text to summarize.
            prompt_instructions: Instructions to add to the prompt.

        Returns:
            The summary generated by Gemini, or None if an error occurs.
        """
        try:
            client = genai.configure(api_key = self.my_api_key)    
            model = genai.GenerativeModel("gemini-2.0-flash")
            prompt = f"{prompt_instructions}\n\n{text}"
            response = model.generate_content(prompt)
            return response.text
        except Exception as e:
            raise Exception("Failed to summarize the text with Gemini, reason: " + str(e))

    def whole_module(self,url):
        # Fetch the text from the URL
        try:
            title, webpage_text = self.fetch_webpage_text(url)
            if webpage_text:
                # Summarize the text using Gemini
                summary = self.summarize_text_with_gemini(webpage_text)
        except Exception as e:
            raise Exception("Issues found, reason: " + str(e))
        return title, summary




if __name__ == "__main__":
    ## test 1
    # extractor = module1("AIzaSyAI932dJEhFJeXa3fuChRUxLLEEMxGQBAI")
    # url = "https://www.bbc.com/news/articles/c33706jy774o"  # Example URL
    # title, summary = extractor.whole_module(url)
    # print(title)
    # print(summary)

    ## test 2 -- wrong url
    extractor = module1("AIzaSyAI932dJEhFJeXa3fuChRUxLLEEMxGQBAI")
    url = "https://www.bbc.com/news/articles/c3dsadsdadadsadsa3706jy774o"  # Example URL
    title, summary = extractor.whole_module(url)
    print(title)
    print(summary)


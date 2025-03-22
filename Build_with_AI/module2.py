from google import genai
import re
from module1 import module1 as module1

def module2(topic, my_api_key):
    client = genai.Client(api_key=my_api_key)

    prompt = f"""
    You are a knowledgeable AI assistant. The user is interested in the topic: "{topic}".

    Task:
    1. Provide exactly two relevant and working links about "{topic}".
    2. For each link, give a brief summary (1–2 sentences).
    3. Format your response as plain text:
    
    Link 1: https://...
    - Summary: ...

    Link 2: https://...
    - Summary: ...
    """

    response = client.models.generate_content(
        model="gemini-2.0-flash",
        contents=prompt
    )

    raw_text = response.result if hasattr(response, "result") else str(response)
    print("=== Raw Model Output ===")
    print(raw_text)

    # Find URLs
    links_found = re.findall(r'(?i)\bhttps?://\S+', raw_text)
    result = [link.rstrip('.,);]') for link in links_found[:2]]
    print("Filtered links:", result)

    end = []
    object1 = module1(api_key)

    for url in result:
        try:
            article = object1.whole_module(url)
            end.append(article)
        except Exception as e:
            print(f"Error fetching article from {url}: {e}")
            end.append(("N/A", "Could not fetch article."))

    return end

# if __name__ == "__main__":
#     topic = "Machine Learning"
#     print(module2(topic, api_key))

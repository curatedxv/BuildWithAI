from google import genai
import re
import os
from module1 import module1 as module1

#api_key = os.environ.get('YOUR_API_KEY')
client = genai.Client(api_key = 'AIzaSyAWKwn_82QHlXU4EGYs_2yYtARgS6NcQuw')

def module2(topic):

    prompt = f"""
    You are a knowledgeable AI assistant. The user is interested in the topic: "{topic}".
    
    Task:
    1. Provide exactly two relevant links about "{topic}".
    2. For each link, give a brief summary (1–2 sentences).
    3. Format your response as plain text, for example:
    
    Link 1: https://...
    - Summary: A short summary here.
    
    Link 2: https://...
    - Summary: Another short summary here.
    """


    response = client.models.generate_content(
        model="gemini-2.0-flash", contents=prompt
    )

    if response and hasattr(response, "result"):
        raw_text = response.result
        print("=== Raw Model Output ===")
        print(raw_text)
    else:
        raw_text = str(response)  # Fallback if the structure is different

    # 6. Use a regular expression to find URLs in the text.
    #    This looks for anything starting with http:// or https:// up to whitespace.
    links_found = re.findall(r'(?i)\bhttps?://\S+', raw_text)

    # 7. Keep only the first 2 links (in case more are found)
    result = links_found[:2]
    result = [s[:-3] if len(s) >= 3 else "" for s in result]
    print(result)
    object1 = module1('AIzaSyAWKwn_82QHlXU4EGYs_2yYtARgS6NcQuw')
    for i in result:
        end = []
        end.append(object1.summarize_text_with_gemini(i))
    return end

if __name__ == "__main__":
    topic = "Machine Learning"
    print(module2(topic))

from typing import Tuple
import google.generativeai as genai  # Correct import

# API key
api_key = os.environ.get('YOUR_API_KEY')
client = genai.Client(api_key = api_key)

# Configure Gemini
genai.configure(api_key=api_key)

def analyze_article_integrity(
    original: Tuple[str, str],
    similar1: Tuple[str, str],
    similar2: Tuple[str, str]
) -> str:
    """
    Uses Gemini to evaluate if the original article is fake or real
    by comparing it to two similar articles.
    """

    prompt = f"""
You are a fake news detection assistant.

Your task is to determine if the original article appears fake or real, based on comparison with two similar articles.

Use reasoning and focus on mismatches or confirmations between them. Respond in a clear, natural language message — like you're explaining to a user.

---

Original Article:
Title: {original[0]}
Summary: {original[1]}

Similar Article A:
Title: {similar1[0]}
Summary: {similar1[1]}

Similar Article B:
Title: {similar2[0]}
Summary: {similar2[1]}

What is your analysis? Is the original likely fake or real? Explain why.
"""

    model = genai.GenerativeModel("gemini-2.0-flash")
    response = model.generate_content(prompt)
    return response.text.strip()

from typing import Tuple, List
import os
import google.generativeai as genai

# API key from environment
api_key = os.environ.get('GOOGLE_API_KEY')
genai.configure(api_key=api_key)


def analyze_article_integrity(
    original: Tuple[str, str],
    similar_articles: List[Tuple[str, str]]
) -> str:
    """
    Uses Gemini to evaluate if the original article is fake or real
    by comparing it to a list of similar articles.
    """

    # Build dynamic prompt with all similar articles
    similar_articles_str = ""
    for idx, (title, summary) in enumerate(similar_articles, start=1):
        similar_articles_str += f"\nSimilar Article {idx}:\nTitle: {title}\nSummary: {summary}\n"

    prompt = f"""
You are a fake news detection assistant.

Your task is to determine if the original article appears fake or real, based on comparison with similar articles.

Use reasoning and focus on mismatches or confirmations between them. Respond in a clear, natural language message — like you're explaining to a user.

---

Original Article:
Title: {original[0]}
Summary: {original[1]}
{similar_articles_str}

What is your analysis? Is the original likely fake or real? Explain why.
"""

    model = genai.GenerativeModel("gemini-2.0-flash")
    response = model.generate_content(prompt)
    return response.text.strip()


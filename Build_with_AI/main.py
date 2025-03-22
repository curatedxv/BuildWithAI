from fastapi import FastAPI, Query
from pydantic import BaseModel
from typing import Optional
from module1 import module1
from module2 import module2
from module3 import analyze_article_integrity

app = FastAPI()


class URLRequest(BaseModel):
    url: str


@app.post("/check_fake_news")
def check_fake_news(request: URLRequest):
    url = request.url

    # Step 1: Get original article title & summary
    object1 = module1("AIzaSyAI932dJEhFJeXa3fuChRUxLLEEMxGQBAI")
    original_title, original_summary = object1.whole_module(url)

    # Step 2: Find similar articles and summarize them
    similar_articles = module2(original_title)
    if not similar_articles or len(similar_articles) < 2:
        return {"error": "Not enough similar articles found"}

    (title2, summary2), (title3, summary3) = similar_articles

    # Step 3: Run integrity check
    verdict = analyze_article_integrity(
        (original_title, original_summary),
        (title2, summary2),
        (title3, summary3)
    )

    return {
        "input_article": {
            "title": original_title,
            "summary": original_summary
        },
        "comparison_articles": [
            {"title": title2, "summary": summary2},
            {"title": title3, "summary": summary3}
        ],
        "verdict": verdict
    }

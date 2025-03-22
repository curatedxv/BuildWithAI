from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from module1 import module1
from module2 import module2
from module3 import analyze_article_integrity

app = FastAPI()

class URLRequest(BaseModel):
    url: str

@app.post("/check_fake_news")
def check_fake_news(request: URLRequest):
    try:
        url = request.url

        # Step 1: Get original article title & summary
        object1 = module1("AIzaSyAI932dJEhFJeXa3fuChRUxLLEEMxGQBAI")
        original_title, original_summary = object1.whole_module(url)

        # Step 2: Find similar articles and summarize them
        similar_articles = module2(original_title)

        # Fallback in case fewer than 2 articles are found
        default_article = ("N/A", "No similar article found")
        article2 = similar_articles[0] if len(similar_articles) > 0 else default_article
        article3 = similar_articles[1] if len(similar_articles) > 1 else default_article

        # ✅ Step 3: Run integrity check — pass both articles as a list
        verdict = analyze_article_integrity(
            (original_title, original_summary),
            [article2, article3]
        )

        return {
            "input_article": {
                "title": original_title,
                "summary": original_summary
            },
            "comparison_articles": [
                {"title": article2[0], "summary": article2[1]},
                {"title": article3[0], "summary": article3[1]}
            ],
            "verdict": verdict
        }

    except Exception as e:
        print(f"Error: {e}")
        raise HTTPException(status_code=500, detail=str(e))

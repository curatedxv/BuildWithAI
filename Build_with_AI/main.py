from module1 import get_summary_from_url
from module2 import find_similar_articles
from module3 import analyze_article_integrity

def run_fake_news_checker(url: str):
    # Step 1: Get original article title & summary
    original_title, original_summary = get_summary_from_url(url)

    # Step 2: Find similar articles and summarize them
    similar_articles = find_similar_articles(original_title, original_summary)
    (title2, summary2), (title3, summary3) = similar_articles

    # Step 3: Run integrity check
    verdict = analyze_article_integrity(
        (original_title, original_summary),
        (title2, summary2),
        (title3, summary3)
    )

    return verdict

# Test run
if __name__ == "__main__":
    test_url = "https://example.com/fake-news"
    print(run_fake_news_checker(test_url))

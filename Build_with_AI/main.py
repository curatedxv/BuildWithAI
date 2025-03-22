from module1 import module1
from module2 import module2
from module3 import analyze_article_integrity

def run_fake_news_checker(url: str):
    # Step 1: Get original article title & summary
    object1 = module1("AIzaSyAI932dJEhFJeXa3fuChRUxLLEEMxGQBAI")
    original_title, original_summary = object1.whole_module(url)

    # Step 2: Find similar articles and summarize them
    similar_articles = module2(original_title)
    print(similar_articles)
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
    test_url = "https://www.newsinlevels.com/products/trump-talks-with-putin-level-1/"
    print(run_fake_news_checker(test_url))

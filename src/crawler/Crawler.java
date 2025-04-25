package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Crawler {

    private Set<String> visitedPages = new HashSet<>();
    private Queue<String> fetchedPages = new LinkedList<>();
    private List<String> result = new ArrayList<>();
    private Map<String, String> pageTexts = new HashMap<>();

    private static final List<String> mainUrls = List.of(
            "https://en.wikipedia.org/wiki/List_of_pharaohs",
            "https://en.wikipedia.org/wiki/Pharaoh"
    );
    private static final int maxPages = 10;

    public void buildCrawler() {
        fetchedPages.addAll(mainUrls);

        while (!fetchedPages.isEmpty() && visitedPages.size() < maxPages) {
            String url = fetchedPages.poll();
            if (visitedPages.contains(url)) continue;

            try {
                Document pageContent = Jsoup.connect(url).get();
                visitedPages.add(url);
                result.add(url);
                String text = pageContent.body().text();
                pageTexts.put(url, text);

                Elements pageLinks = pageContent.select("a[href]");
                for (Element link : pageLinks) {
                    String href = link.attr("href");

                    if (href.startsWith("/wiki/") && !href.contains(":")) {
                        String baseUrl = "https://en.wikipedia.org";
                        String fullUrl = baseUrl + href;
                        if (!visitedPages.contains(fullUrl)) {
                            fetchedPages.add(fullUrl);
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("Failed to fetch: " + url);
            }
        }
    }

    public List<String> getCrawledPages() {
        return result;
    }

    public Map<String, String> getPageTexts() {
        return pageTexts;
    }
}

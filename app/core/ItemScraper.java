package core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.json.JsonHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemScraper {
  private static final ItemScraper INSTANCE = new ItemScraper();
  private static final Logger LOGGER = LoggerFactory.getLogger(ItemScraper.class);
  private static final String PRICES_URL = "http://warframe.wikia.com/wiki/Ducats/Prices";

  public Map<String, Integer> scrape() {
    try {
      Document doc = Jsoup.connect(PRICES_URL).get();
      Elements tableRows = doc.select("tr");



    } catch (IOException e) {
      LOGGER.error("Failed to connect", e);
    }


    Map<String, Integer> items = new HashMap<>();

    return items;
  }

  public static ItemScraper getInstance() {
    return INSTANCE;
  }
}

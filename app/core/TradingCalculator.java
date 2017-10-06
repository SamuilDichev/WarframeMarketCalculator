package core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import models.ApiResult;
import models.Item;
import models.Seller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.json.JsonHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TradingCalculator {
  private static final Logger LOGGER = LoggerFactory.getLogger(TradingCalculator.class);
  private static final String API = "https://warframe.market%s%s";
  private static final String BP_ROUTE = "/api/get_orders/Blueprint/";

  public static List<Item> calculate(List<Item> items) {
    long start = System.currentTimeMillis();

    ExecutorService executor = Executors.newFixedThreadPool(3);
    for (Item item : items) {
      executor.submit(() -> {
        List<Seller> ingameSellers = searchMarket(item.getName());
        if (!ingameSellers.isEmpty()) {
          Collections.sort(ingameSellers);
          item.setSeller(ingameSellers.get(0));
          item.setPlatinum(item.getSeller().getPrice());
          item.setDPP((double) item.getDucats() / item.getSeller().getPrice());
          item.setReturnRatio(getAveragePrice(ingameSellers) / item.getPlatinum());
        }
      });
    }

    executor.shutdown();
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      LOGGER.error("Executor failure: {}", e.getMessage());
    }

    LOGGER.info("Finished in {}", (System.currentTimeMillis() - start) / 1000 + " seconds.");

    return items;
  }

  private static double getAveragePrice(List<Seller> sellers) {
    int i;
    int total = 0;
    for (i = 1; i < Math.min(sellers.size(), 4); i++) {
      total += sellers.get(i).getPrice();
    }

    return total / (i - 1);
  }

  public static List<Seller> searchMarket(String item) {
    String url = String.format(API, BP_ROUTE, item.replaceAll(" ", "%20"));

    try {
      HttpResponse<String> response = Unirest.get(url).asString();
      if (response.getStatus() != 200) {
        LOGGER.error("{} responded with {}: {}", item, response.getStatus(), response.getStatusText());
        return new ArrayList<>();
      }
      return JsonHelper.fromJson(response.getBody(), new TypeReference<ApiResult>() {

      }).getResponse().getIngameSellers();
    } catch (UnirestException e) {
      LOGGER.error("Could not search market for {}. Exception msg: {}", item, e.getMessage());
      return new ArrayList<>();
    }
  }
}

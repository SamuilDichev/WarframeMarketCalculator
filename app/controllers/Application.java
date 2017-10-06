package controllers;

import core.TradingCalculator;
import models.Item;
import play.api.Play;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Option;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Application extends Controller {

  public Result index() {
    return ok(views.html.main.render("Some title", new ArrayList<>()));
  }

  public Result calculate() {
    Option<InputStream> is = Play.resourceAsStream("public/items.txt", Play.current());

    List<Item> items = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(is.get(), "UTF-8"));
      String line = "";
      while ((line = br.readLine()) != null) {
        String parts[] = line.split(", ");
        Item item = new Item();
        item.setName(parts[0]);
        item.setDucats(Integer.valueOf(parts[1]));
        items.add(item);
      }

      br.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    items = TradingCalculator.calculate(items);
    Collections.sort(items, Collections.reverseOrder());

    return ok(views.html.main.render("Items", items));
  }
}

package models;

import java.util.List;
import java.util.stream.Collectors;

public class Response {
  private List<Seller> sell;
  private List<Seller> buy;
  private boolean render_rank;

  public List<Seller> getSell() {
    return sell;
  }

  public List<Seller> getIngameSellers() {
    return sell.stream().filter(s -> s.isOnline_ingame() || s.isOnline_status())
            .filter(s -> !s.getIngame_name().startsWith("(PS4)"))
            .filter(s -> !s.getIngame_name().startsWith("(XB1)"))
            .collect(Collectors.toList());
  }

  public void setSell(List<Seller> sell) {
    this.sell = sell;
  }

  public List<Seller> getBuy() {
    return buy;
  }

  public void setBuy(List<Seller> buy) {
    this.buy = buy;
  }

  public boolean isRender_rank() {
    return render_rank;
  }

  public void setRender_rank(boolean render_rank) {
    this.render_rank = render_rank;
  }
}

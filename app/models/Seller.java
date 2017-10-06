package models;

public class Seller implements Comparable<Seller> {
  private boolean online_status;
  private int price;
  private int count;
  private boolean online_ingame;
  private String ingame_name;

  public boolean isOnline_status() {
    return online_status;
  }

  @Override
  public int compareTo(Seller o) {
    return Integer.compare(price, o.getPrice());
  }

  public void setOnline_status(boolean online_status) {
    this.online_status = online_status;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public boolean isOnline_ingame() {
    return online_ingame;
  }

  public void setOnline_ingame(boolean online_ingame) {
    this.online_ingame = online_ingame;
  }

  public String getIngame_name() {
    return ingame_name;
  }

  public void setIngame_name(String ingame_name) {
    this.ingame_name = ingame_name;
  }
}

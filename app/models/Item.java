package models;

public class Item implements Comparable<Item> {
  private String name;
  private int ducats;
  private int platinum;

  // Ducats per platinum
  private double DPP;
  private double returnRatio;
  private Seller seller;

  @Override
  public int compareTo(Item o) {
    return Double.compare(DPP, o.getDPP());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDucats() {
    return ducats;
  }

  public void setDucats(int ducats) {
    this.ducats = ducats;
  }

  public int getPlatinum() {
    return platinum;
  }

  public void setPlatinum(int platinum) {
    this.platinum = platinum;
  }

  public double getReturnRatio() {
    return returnRatio;
  }

  public void setReturnRatio(double returnRatio) {
    this.returnRatio = (double) Math.round(returnRatio * 100) / 100;
  }

  public double getDPP() {
    return DPP;
  }

  public void setDPP(double DPP) {
    this.DPP = (double) Math.round(DPP * 100) / 100;
  }

  public Seller getSeller() {
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }
}

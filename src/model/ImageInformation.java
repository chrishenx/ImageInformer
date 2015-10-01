package model;

public class ImageInformation {
  private int information;
  private double entropy;
  
  public ImageInformation(int information, double entropy) {
    this.information = information;
    this.entropy = entropy;
  }

  public int getInformation() {
    return information;
  }

  public void setInformation(int information) {
    this.information = information;
  }

  public double getEntropy() {
    return entropy;
  }

  public void setEntropy(double entropy) {
    this.entropy = entropy;
  }
}

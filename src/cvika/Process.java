package cvika;

import Jama.Matrix;
import ij.ImagePlus;

public class Process {
  public static final int RED = 1;
  public static final int GREEN = 2;
  public static final int BLUE = 3;
  public static final int Y = 4;
  public static final int CB = 5;
  public static final int CR = 6;
  public static final int A444 = 7;
  public static final int A422 = 8;
  public static final int A411 = 9;
  public static final int A420 = 10;

  private final ColorTransform colorTransform;
  private final ImagePlus imagePlus;

  public Process(ImagePlus imagePlus) {
    this.imagePlus = imagePlus;
    colorTransform = new ColorTransform(imagePlus.getBufferedImage());
    this.colorTransform.convertRgbToYcbcr();
    // test();
  }

  public ImagePlus getComponent(int component) {
    ImagePlus imagePlus = null;
    switch (component) {
      case (RED):
        imagePlus =
            colorTransform.setImageFromRGB(
                colorTransform.getImageWidth(),
                colorTransform.getImageHeight(),
                colorTransform.getRed(),
                "RED");
        break;
      case (GREEN):
        imagePlus =
            colorTransform.setImageFromRGB(
                colorTransform.getImageWidth(),
                colorTransform.getImageHeight(),
                colorTransform.getGreen(),
                "GREEN");
        break;
      case (BLUE):
        imagePlus =
            colorTransform.setImageFromRGB(
                colorTransform.getImageWidth(),
                colorTransform.getImageHeight(),
                colorTransform.getBlue(),
                "BLUE");
        break;
      case (Y):
        imagePlus =
            colorTransform.setImageFromRGB(
                colorTransform.getY().getColumnDimension(),
                colorTransform.getY().getRowDimension(),
                colorTransform.getY(),
                "Y");
        break;
      case (CB):
        imagePlus =
            colorTransform.setImageFromRGB(
                colorTransform.getcB().getColumnDimension(),
                colorTransform.getcB().getRowDimension(),
                colorTransform.getcB(),
                "CB");
        break;
      case (CR):
        imagePlus =
            colorTransform.setImageFromRGB(
                colorTransform.getcR().getColumnDimension(),
                colorTransform.getcR().getRowDimension(),
                colorTransform.getcR(),
                "CR");
        break;
      default:
        break;
    }
    return imagePlus;
  }

  public void downsample(int downsampleType) {
    Matrix cB = new Matrix(colorTransform.getcB().getArray());
    Matrix cR = new Matrix(colorTransform.getcR().getArray());
    switch (downsampleType) {
      case A444:
        break;
      case A422:
        colorTransform.setcB(colorTransform.downSample(cB));
        colorTransform.setcR(colorTransform.downSample(cR));
        break;
      case A411:
        colorTransform.setcB(colorTransform.downSample(colorTransform.downSample(cB)));
        colorTransform.setcR(colorTransform.downSample(colorTransform.downSample(cR)));
        break;
      case A420:
        colorTransform.setcB(
            colorTransform.downSample(colorTransform.downSample(cB).transpose()).transpose());
        colorTransform.setcR(
            colorTransform.downSample(colorTransform.downSample(cR).transpose()).transpose());
        break;
      default:
        break;
    }
    // switch case
  }

  private void test() {
    imagePlus.show("Original Image");
    this.colorTransform.convertRgbToYcbcr();
    this.colorTransform.convertYcbcrToRgb();
    colorTransform.setImageFromRGB().show();
  }
}

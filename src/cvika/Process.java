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
  private final ColorTransform colorTransformOrig;
  private final Quality quality;

  public Process(ImagePlus imagePlus) {
    this.imagePlus = imagePlus;
    this.quality = new Quality();
    colorTransform = new ColorTransform(imagePlus.getBufferedImage());
    colorTransformOrig = new ColorTransform(imagePlus.getBufferedImage());
    this.colorTransform.convertRgbToYcbcr();
    this.colorTransformOrig.convertRgbToYcbcr();
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

  public void oversample(int oversampleType) {
    Matrix cB = new Matrix(colorTransform.getcB().getArray());
    Matrix cR = new Matrix(colorTransform.getcR().getArray());
    switch (oversampleType) {
      case A444:
        break;
      case A422:
        colorTransform.setcB(colorTransform.overSample(cB));
        colorTransform.setcR(colorTransform.overSample(cR));
        break;
      case A411:
        colorTransform.setcB(colorTransform.overSample(colorTransform.overSample(cB)));
        colorTransform.setcR(colorTransform.overSample(colorTransform.overSample(cR)));
        break;
      case A420:
        colorTransform.setcB(
            colorTransform.overSample(colorTransform.overSample(cB).transpose()).transpose());
        colorTransform.setcR(
            colorTransform.overSample(colorTransform.overSample(cR).transpose()).transpose());
        break;
      default:
        break;
    }
    // switch case
  }

  public double getMse() {
    int imageHeight = colorTransform.getImageHeight();
    int imageWidth = colorTransform.getImageWidth();
    int cbHeight = colorTransform.getcB().getColumnDimension();
    int cbWidth = colorTransform.getcB().getRowDimension();
    int crHeight = colorTransform.getcB().getColumnDimension();
    int crWidth = colorTransform.getcB().getRowDimension();

    if (imageHeight != cbHeight
        || imageHeight != crHeight
        || imageWidth != cbWidth
        || imageWidth != crWidth) {
      return Double.NEGATIVE_INFINITY;
    }
    colorTransform.convertYcbcrToRgb();
    double a = quality.getMse(colorTransformOrig.getRed(), colorTransform.getRed());
    double b = quality.getMse(colorTransformOrig.getGreen(), colorTransform.getGreen());
    double c = quality.getMse(colorTransformOrig.getBlue(), colorTransform.getBlue());
    return (Math.round(100 * (a + b + c) / 3.0) / 100.0);
  }

  public double getPsnr() {
    int imageHeight = colorTransform.getImageHeight();
    int imageWidth = colorTransform.getImageWidth();
    int cbHeight = colorTransform.getcB().getColumnDimension();
    int cbWidth = colorTransform.getcB().getRowDimension();
    int crHeight = colorTransform.getcB().getColumnDimension();
    int crWidth = colorTransform.getcB().getRowDimension();

    if (imageHeight != cbHeight
        || imageHeight != crHeight
        || imageWidth != cbWidth
        || imageWidth != crWidth) {
      return Double.NEGATIVE_INFINITY;
    }

    colorTransform.convertYcbcrToRgb();
    double a = quality.getPsnr(colorTransformOrig.getRed(), colorTransform.getRed());
    double b = quality.getPsnr(colorTransformOrig.getGreen(), colorTransform.getGreen());
    double c = quality.getPsnr(colorTransformOrig.getBlue(), colorTransform.getBlue());
    double result = Math.round(100 * (a + b + c) / 3.0) / 100.0;
    if (result > 1000) result = Double.POSITIVE_INFINITY;

    return result;
  }
}

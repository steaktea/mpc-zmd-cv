package cvika;

import ij.ImagePlus;

public class Process {
  private final ColorTransform colorTransform;
  private final ImagePlus imagePlus;

  public Process(ImagePlus imagePlus) {
    this.imagePlus = imagePlus;
    colorTransform = new ColorTransform(imagePlus.getBufferedImage());
    test();
  }

  private void test() {
    imagePlus.show("Original Image");
    this.colorTransform.convertRgbToYcbcr();
    this.colorTransform.convertYcbcrToRgb();
    colorTransform.setImageFromRGB().show();
  }
}

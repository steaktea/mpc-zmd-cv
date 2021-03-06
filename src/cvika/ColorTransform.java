package cvika;

import Jama.Matrix;
import ij.ImagePlus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class ColorTransform {

  private final ColorModel colorModel;
  private final BufferedImage bImage;

  private final int imageHeight;
  private final int imageWidth;

  // Deklarace polí barevného prostoru RGB
  private final int[][] red;
  private final int[][] green;
  private final int[][] blue;

  // Deklarace matic barevného prostoru YCbCr
  private final Matrix y;
  private Matrix cB;
  private Matrix cR;

  public ColorTransform(BufferedImage bImage) {
    this.bImage = bImage;
    this.colorModel = bImage.getColorModel();
    this.imageHeight = bImage.getHeight();
    this.imageWidth = bImage.getWidth();

    this.red = new int[this.imageHeight][this.imageWidth];
    this.green = new int[this.imageHeight][this.imageWidth];
    this.blue = new int[this.imageHeight][this.imageWidth];

    this.y = new Matrix(this.imageHeight, this.imageWidth);
    this.cB = new Matrix(this.imageHeight, this.imageWidth);
    this.cR = new Matrix(this.imageHeight, this.imageWidth);

    getRGB();
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public int[][] getRed() {
    return red;
  }

  public int[][] getGreen() {
    return green;
  }

  public int[][] getBlue() {
    return blue;
  }

  public Matrix getY() {
    return y;
  }

  public Matrix getcB() {
    return cB;
  }

  public void setcB(Matrix cB) {
    this.cB = cB;
  }

  public Matrix getcR() {
    return cR;
  }

  public void setcR(Matrix cR) {
    this.cR = cR;
  }

  public void getRGB() {
    for (int i = 0; i < this.imageHeight; i++) {
      for (int j = 0; j < this.imageWidth; j++) {
        this.red[i][j] = colorModel.getRed(this.bImage.getRGB(j, i));
        this.green[i][j] = colorModel.getGreen(this.bImage.getRGB(j, i));
        this.blue[i][j] = colorModel.getBlue(this.bImage.getRGB(j, i));
      }
    }
  }

  // metoda pro vytvoreni barevneho obrazu z rgb
  public ImagePlus setImageFromRGB() {
    BufferedImage bImage =
        new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_RGB);
    int[][] rgb = new int[this.imageHeight][this.imageWidth];
    for (int i = 0; i < this.imageHeight; i++) {
      for (int j = 0; j < this.imageWidth; j++) {
        rgb[i][j] = new Color(this.red[i][j], this.green[i][j], this.blue[i][j]).getRGB();
        bImage.setRGB(j, i, rgb[i][j]);
      }
    }
    return (new ImagePlus("Rekonstruovany obraz", bImage));
  }

  // metoda pro vytvoreni obrazu z jedne slozky rgb
  public ImagePlus setImageFromRGB(int width, int height, int[][] x, String component) {
    BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    int[][] rgb = new int[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        rgb[i][j] = new Color(x[i][j], x[i][j], x[i][j]).getRGB();
        bImage.setRGB(j, i, rgb[i][j]);
      }
    }
    return (new ImagePlus(component, bImage));
  }

  // metoda pro vytvoreni barevneho obrazu z YCbCr
  public ImagePlus setImageFromRGB(int width, int height, Matrix x, String component) {
    BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    int[][] rgb = new int[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        rgb[i][j] = new Color((int) x.get(i, j), (int) x.get(i, j), (int) x.get(i, j)).getRGB();
        bImage.setRGB(j, i, rgb[i][j]);
      }
    }
    return (new ImagePlus(component, bImage));
  }

  public void convertRgbToYcbcr() {
    for (int i = 0; i < this.imageHeight; i++) {
      for (int j = 0; j < this.imageWidth; j++) {
        y.set(i, j, 0.257 * red[i][j] + 0.504 * green[i][j] + 0.098 * blue[i][j] + 16);
        cB.set(i, j, -0.148 * red[i][j] - 0.291 * green[i][j] + 0.439 * blue[i][j] + 128);
        cR.set(i, j, 0.439 * red[i][j] - 0.368 * green[i][j] - 0.071 * blue[i][j] + 128);
      }
    }
  }

  public void convertYcbcrToRgb() {
    for (int i = 0; i < this.imageHeight; i++) {
      for (int j = 0; j < this.imageWidth; j++) {
        red[i][j] = (int) Math.round(1.164 * (y.get(i, j) - 16) + 1.596 * (cR.get(i, j) - 128));
        if (red[i][j] > 255) red[i][j] = 255;
        if (red[i][j] < 0) red[i][j] = 0;
        green[i][j] =
            (int)
                Math.round(
                    1.164 * (y.get(i, j) - 16)
                        - 0.813 * (cR.get(i, j) - 128)
                        - 0.391 * (cB.get(i, j) - 128));
        if (green[i][j] > 255) green[i][j] = 255;
        if (green[i][j] < 0) green[i][j] = 0;
        blue[i][j] = (int) Math.round(1.164 * (y.get(i, j) - 16) + 2.018 * (cB.get(i, j) - 128));
        if (blue[i][j] > 255) blue[i][j] = 255;
        if (blue[i][j] < 0) blue[i][j] = 0;
      }
    }
  }

  public Matrix downSample(Matrix mat) {
    Matrix newMatrix = new Matrix(mat.getRowDimension(), mat.getColumnDimension() / 2);
    for (int i = 0; i < mat.getColumnDimension(); i += 2) {
      Matrix submatrix = mat.getMatrix(0, mat.getRowDimension() - 1, i, i);
      newMatrix.setMatrix(0, mat.getRowDimension() - 1, i / 2, i / 2, submatrix);
    }
    return newMatrix;
  }

  public Matrix overSample(Matrix mat) {
    Matrix newMatrix = new Matrix(mat.getRowDimension(), mat.getColumnDimension() * 2);
    for (int i = 0; i < mat.getColumnDimension(); i++) {
      Matrix submatrix = mat.getMatrix(0, mat.getRowDimension() - 1, i, i);
      newMatrix.setMatrix(0, newMatrix.getRowDimension() - 1, i * 2, i * 2, submatrix);
      newMatrix.setMatrix(0, newMatrix.getRowDimension() - 1, (i * 2) + 1, (i * 2) + 1, submatrix);
    }
    return newMatrix;
  }
}

package cvika;

public class Quality {

  public double getMse(int[][] original, int[][] edited) {
    int width = original.length;
    int height = original[0].length;
    double mse = 0;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        mse += Math.pow(original[i][j] - edited[i][j], 2);
      }
    }
    return mse / (width * height);
  }

  public double getPsnr(int[][] original, int[][] edited) {
    double mse = getMse(original, edited);
    return 10 * Math.log10(Math.pow(255, 2) / mse);
  }
}

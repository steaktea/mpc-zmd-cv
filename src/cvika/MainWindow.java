package cvika;

import ij.ImagePlus;

import javax.swing.*;

public class MainWindow {
  private JFrame frame;
  private JPanel mainPanel;
  private Process process;

  public MainWindow() {
    initialize();
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Aplikace pro zpracování obrazu");
    frame.setBounds(100, 100, 450, 300);
    frame.setContentPane(new cvika.MainWindow().mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
    new cvika.MainWindow();
  }

  private void initialize() {
    ImagePlus originalImage = new ImagePlus("Lenna.png");
    this.process = new Process(originalImage);
    originalImage.show();
  }
}

package cvika;

import ij.ImagePlus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
  private JFrame frame;
  private JPanel mainPanel;
  private JButton redButton;
  private JButton greenButton;
  private JButton blueButton;
  private JButton yButton;
  private JButton cbButton;
  private JButton crButton;
  private JButton a444Button;
  private JButton a422Button;
  private JButton a420Button;
  private JButton a411Button;
  private Process process;

  public MainWindow() {
    initialize();
    redButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.getComponent(Process.RED).show();
          }
        });
    greenButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.getComponent(Process.GREEN).show();
          }
        });
    blueButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.getComponent(Process.BLUE).show();
          }
        });
    yButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.getComponent(Process.Y).show();
          }
        });
    cbButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.getComponent(Process.CB).show();
          }
        });
    crButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.getComponent(Process.CR).show();
          }
        });
    a444Button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.downsample(Process.A444);
          }
        });
    a422Button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.downsample(Process.A422);
          }
        });
    a420Button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.downsample(Process.A420);
          }
        });
    a411Button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            process.downsample(Process.A411);
          }
        });
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
  }
}

package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import model.ImageInformation;
import model.ImageProcessor;

/**
 * GUI para mostrar informacion sobre una imagen.
 * @author Christian González León
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {
  public static final String APPLICATION_NAME = "Información de imagen";
  public static final Dimension windowSize;

  // Top controls
  private JButton selectFileButton;
  private JTextField pathTextField;
  
  // Bottom controls
  private JButton informationButton;
  private JTextField informationTextField;
  
  // Central controls
  private BufferedImage imageLoaded;
  private JLabel imageContainer;
  private Container mainContainer;  
  
  static {
    windowSize = new Dimension(680, 540);
  }
  
  public static void main(String[] args) {

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }
  
  public MainWindow() {
    JPanel pathPanel = new JPanel();
    pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    selectFileButton = new JButton("Seleccionar imagen");
    pathPanel.add(selectFileButton);
    
    pathTextField = new JTextField(50);
    pathTextField.setEditable(false);
    pathTextField.setHorizontalAlignment(JTextField.CENTER);
    pathPanel.add(pathTextField);
    
    mainContainer = getContentPane();
    
    mainContainer.add(pathPanel, BorderLayout.PAGE_START);
    
    imageContainer = new JLabel();
    imageContainer.setHorizontalAlignment(JLabel.CENTER);
    imageContainer.setVerticalAlignment(JLabel.CENTER);
    mainContainer.add(imageContainer, BorderLayout.CENTER);
    
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    informationButton = new JButton("Información");
    bottomPanel.add(informationButton);
    
    informationTextField = new JTextField(54);
    informationTextField.setEditable(false);
    informationTextField.setHorizontalAlignment(JTextField.CENTER);
    bottomPanel.add(informationTextField);
    
    mainContainer.add(bottomPanel, BorderLayout.PAGE_END);
    
    configureEventListeners();
  }
  
  private void configureEventListeners() {
    selectFileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Imagenes PNG y JPG", "jpg", "png");
        fileChooser.setFileFilter(filter);
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showOpenDialog(MainWindow.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          try {
            // Scale method from:
            //   http://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            Dimension dims = imageContainer.getSize();
            BufferedImage image = ImageIO.read(new File(imagePath));
            imageLoaded = new BufferedImage(dims.width, dims.height, image.getType());
            Graphics2D graphics = imageLoaded.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            graphics.drawImage(image, 0, 0, dims.width, dims.height, 0, 0, image.getWidth(),
                image.getHeight(), null);
            graphics.dispose();
            imageContainer.setIcon(new ImageIcon(imageLoaded));
            pathTextField.setText(imagePath);
          } catch (IOException e) {
            System.out.println("Error cargando imagen.");
          }
        }
      }
    });
    
    informationButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (imageLoaded == null)
          return;
        ImageProcessor imageProcessor = new ImageProcessor(imageLoaded);
        ImageInformation imageInformation = imageProcessor.calculateInformation();
        informationTextField.setText(
            "Cantidad de información: " + imageInformation.getInformation() + " bits  |||  " +
            "Entropia: " + imageInformation.getEntropy()
            );
      }
    });
  }
  
  public static void createAndShowGUI() {
    MainWindow mainWindow = new MainWindow();
    mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
    mainWindow.setTitle(APPLICATION_NAME);
    mainWindow.setMinimumSize(windowSize);
    mainWindow.setResizable(false);
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      System.out.println(e.getMessage());
    }
    SwingUtilities.updateComponentTreeUI(mainWindow);
    mainWindow.pack();
    mainWindow.setLocationRelativeTo(null);
    mainWindow.setVisible(true);
  }
}

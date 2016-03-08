import java.util.Observable;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;    
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.file.attribute.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.util.Date;

public class ImageCollectionModel extends Observable { 
    // the data in the model
    private ArrayList<ImageModel> m_imageModels;
    private JFileChooser m_fileChooser;
    private JFrame m_Jframe;

    public static int IMAGE_WIDTH = 250;
    public static int IMAGE_HEIGHT = 250;
    public static int META_DATA_OFFSET = 90;
    
    ImageCollectionModel() {
        m_imageModels = new ArrayList<ImageModel>();
        m_fileChooser = new JFileChooser();
        FileNameExtensionFilter filter_jpg = new FileNameExtensionFilter("*.jpg", "jpg");
        m_fileChooser.addChoosableFileFilter(filter_jpg);
        FileNameExtensionFilter filter_png = new FileNameExtensionFilter("*.png", "png");
        m_fileChooser.addChoosableFileFilter(filter_png);

        setChanged();
    }

    public void addImage(BufferedImage image, String fileName, String creationTime) {
        m_imageModels.add(new ImageModel(image, fileName, creationTime, m_Jframe));        
        setChanged();
        notifyObservers();
    }

    public void selectFile() {
        int result = m_fileChooser.showOpenDialog(m_Jframe);
        if (result == m_fileChooser.APPROVE_OPTION) {
            String path = m_fileChooser.getSelectedFile().getAbsolutePath();
            String file_name = m_fileChooser.getSelectedFile().getName();
            BufferedImage image = null;
            try {
                Path path_object = Paths.get(path);
                BasicFileAttributes attr = null;
                try {
                    attr = Files.readAttributes(path_object, BasicFileAttributes.class);
                }
                catch (IOException ex) {
                    System.out.println(ex);
                }
                String creation_time = attr == null ? "Not available" : new Date(attr.creationTime().toMillis()).toString();
                image = ImageIO.read(new File(path));
                addImage(image, file_name, creation_time);
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFrame(JFrame frame) {
        m_Jframe = frame;
    }

    public ArrayList<ImageModel> getImageModels() {
        return m_imageModels;
    }
}


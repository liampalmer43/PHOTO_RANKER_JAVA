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
import java.text.SimpleDateFormat;

public class ImageCollectionModel extends Observable { 
    // the data in the model
    private ArrayList<ImageModel> m_imageModels;
    private JFileChooser m_fileChooser;
    private JFrame m_Jframe;
    private boolean m_grid;
    private int m_rankingFilter;

    public static int IMAGE_WIDTH = 180;
    public static int IMAGE_HEIGHT = 180;
    public static int META_DATA_OFFSET = 70;
    
    ImageCollectionModel() {
        m_imageModels = new ArrayList<ImageModel>();
        m_fileChooser = new JFileChooser();
        FileNameExtensionFilter filter_jpg = new FileNameExtensionFilter("*.jpg", "jpg");
        m_fileChooser.addChoosableFileFilter(filter_jpg);
        FileNameExtensionFilter filter_png = new FileNameExtensionFilter("*.png", "png");
        m_fileChooser.addChoosableFileFilter(filter_png);
        // Initially we are in grid format.
        m_grid = true;
        // Initiall the ranking filter is zero.
        m_rankingFilter = 0;

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
                Date creation_time = attr == null ? null : new Date(attr.creationTime().toMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                String creation_date = attr == null ? "Not available" : sdf.format(creation_time);
                image = ImageIO.read(new File(path));
                addImage(image, file_name, creation_date);
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFrame(JFrame frame) {
        m_Jframe = frame;
    }

    public void setGrid() {
        if (m_grid) {
            return;
        }
        m_grid = true;
        setChanged();
        notifyObservers();
    }

    public void setList() {
        if (!m_grid) {
            return;
        }
        m_grid = false;
        setChanged();
        notifyObservers();
    }

    public void setRankingFilter(int rankingFilter) {
        m_rankingFilter = rankingFilter;
        setChanged();
        notifyObservers();
    }

    public int getRankingFilter() {
        return m_rankingFilter;
    }

    public boolean isGrid() {
        return m_grid;
    }

    public ArrayList<ImageModel> getImageModels() {
        return m_imageModels;
    }
}


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
    // The models corresponding to each individual image.
    private ArrayList<ImageModel> m_imageModels;
    // The file paths corresponding to each individual image.
    private ArrayList<String> m_filePaths;
    // The file chooser of the application.
    private JFileChooser m_fileChooser;
    // The JFrame of the application, used to center the file chooser and image popups.
    private JFrame m_Jframe;
    // Whether or not we are in grid view (vs list view).
    private boolean m_grid;
    // The current value of the ranking filter (set by the user).
    private int m_rankingFilter;

    // The desired width of the displayed images.
    public static int IMAGE_WIDTH = 180;
    // The desired height of the displayed images.
    public static int IMAGE_HEIGHT = 180;
    // The desired height of the meta data for each image.
    public static int META_DATA_OFFSET = 70;
    
    ImageCollectionModel() {
        // Initialize member variables.
        m_imageModels = new ArrayList<ImageModel>();
        m_filePaths = new ArrayList<String>();
        m_fileChooser = new JFileChooser();
        FileNameExtensionFilter filter_jpg = new FileNameExtensionFilter("*.jpg", "jpg");
        m_fileChooser.addChoosableFileFilter(filter_jpg);
        FileNameExtensionFilter filter_png = new FileNameExtensionFilter("*.png", "png");
        m_fileChooser.addChoosableFileFilter(filter_png);
        m_fileChooser.setMultiSelectionEnabled(true);
        // Initially we are in grid format.
        m_grid = true;
        // Initiall the ranking filter is zero.
        m_rankingFilter = 0;

        setChanged();
    }

    // Adding an image must happen through this interface.
    public void addImage(BufferedImage image, String fileName, String creationTime, String filePath, int ranking) {
        // If the path is already displayed in the application, don't add another copy of the image.
        for (int i = 0; i < m_filePaths.size(); ++i) {
            if (m_filePaths.get(i).equals(filePath)) {
                return;
            }
        }

        // Create the image model.
        m_imageModels.add(new ImageModel(image, fileName, creationTime, m_Jframe, this));
        // If the ranking is not zero, set the ranking.
        if (ranking != 0) {
            m_imageModels.get(m_imageModels.size() - 1).setRanking(ranking);
        }
        // Add the file path for later use.
        m_filePaths.add(filePath);
        setChanged();

        notifyObservers();
    }

    // Function for selecting one or more files to upload to the application.
    public void selectFile() {
        int result = m_fileChooser.showOpenDialog(m_Jframe);
        if (result == m_fileChooser.APPROVE_OPTION) {
            File[] files = m_fileChooser.getSelectedFiles();
            for (int i = 0; i < files.length; ++i) {
                String path = files[i].getAbsolutePath();
                String file_name = files[i].getName();
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
                    if (image != null) {
                        addImage(image, file_name, creation_date, path, 0);
                    }
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Function for saving the state of the application before exiting.
    // The state is stored in state.txt in the current directory.
    public void saveState() {
        try {
            FileWriter fileWriter = new FileWriter("state.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            try {
                bufferedWriter.write(String.valueOf(m_imageModels.size()));
                bufferedWriter.newLine();
                for (int i = 0; i < m_imageModels.size(); ++i) {
                    ImageModel image_model = m_imageModels.get(i);
                    bufferedWriter.write(image_model.getFileName());
                    bufferedWriter.newLine();
                    bufferedWriter.write(m_filePaths.get(i));
                    bufferedWriter.newLine();
                    bufferedWriter.write(String.valueOf(image_model.getRanking()));
                    bufferedWriter.newLine();
                }
            } finally {
                bufferedWriter.close();
            }
        } catch(IOException ex) {}
    }

    // Set the JFrame of the current application.
    public void setFrame(JFrame frame) {
        m_Jframe = frame;
    }

    // Simply make all the views update.
    // Used when we change view (grid or list) and the image views need to
    // adjust the position of their meta data.
    public void changeImageLayout() {
        for (int i = 0; i < m_imageModels.size(); ++i) {
            m_imageModels.get(i).changeLayout();
        }
    }

    // Change to grid view.
    public void setGrid() {
        if (m_grid) {
            return;
        }
        m_grid = true;
        setChanged();
        notifyObservers();
        changeImageLayout();
    }

    // Change to list view.
    public void setList() {
        if (!m_grid) {
            return;
        }
        m_grid = false;
        setChanged();
        notifyObservers();
        changeImageLayout();
    }

    // Used to cause the main view to update.
    public void newRanking(int ranking) {
        setChanged();
        notifyObservers();
    }

    // Set the ranking filter.
    public void setRankingFilter(int rankingFilter) {
        m_rankingFilter = rankingFilter;
        setChanged();
        notifyObservers();
    }

    // Public getters:    

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


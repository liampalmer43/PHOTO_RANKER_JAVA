import java.util.Observable;
import java.util.ArrayList;
import java.util.Date;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;    
import java.io.*;
import java.awt.image.BufferedImage;

public class ImageModel extends Observable { 
    // The image of the file.
    private BufferedImage m_image;
    // The name of the file.
    private String m_fileName;
    // The creation time of the file.
    private String m_creationTime;
    // The ranking of the file's image.
    private int m_ranking;
    // The JFrame of the application (used for centering popups).
    private JFrame m_Jframe;
    // We need this parameter to let the main model know that a specific ranking has changed.
    private ImageCollectionModel m_imageCollectionModel;

    ImageModel(BufferedImage image, String fileName, String creationTime, JFrame jframe, ImageCollectionModel model) {
        // Initialize member variables.
        m_image = image;
        m_fileName = fileName;
        m_creationTime = creationTime;
        m_ranking = 0;
        m_Jframe = jframe;
        m_imageCollectionModel = model;

        setChanged();
    }

    public void changeLayout() {
        // Simply notify the single view that the image layout has changed.
        setChanged();
        notifyObservers();
    }

    // Return the current layout type.
    public boolean isGrid() {
        return m_imageCollectionModel.isGrid();
    }

    // Set the ranking of the image.
    public void setRanking(int ranking) {
        if (m_ranking == ranking) {
            return;
        }
        m_ranking = ranking;
        setChanged();
        notifyObservers();
        // Tell the main model about the new ranking, so the filter can act accordingly.
        m_imageCollectionModel.newRanking(ranking);
    }

    // Public getters:

    public BufferedImage getImage() {
        return m_image;
    }

    public String getFileName() {
        return m_fileName;
    }

    public String getCreationTime() {
        return m_creationTime;
    }

    public int getRanking() {
        return m_ranking;
    }

    public JFrame getJframe() {
        return m_Jframe;
    }
}


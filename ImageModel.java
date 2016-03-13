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
    // the data in the model
    private BufferedImage m_image;
    private String m_fileName;
    private String m_creationTime;
    private int m_ranking;
    private JFrame m_Jframe;
    // We need this parameter to let the main model know that a specific ranking has changed.
    private ImageCollectionModel m_imageCollectionModel;

    ImageModel(BufferedImage image, String fileName, String creationTime, JFrame jframe, ImageCollectionModel model) {
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

    public boolean isGrid() {
        return m_imageCollectionModel.isGrid();
    }

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


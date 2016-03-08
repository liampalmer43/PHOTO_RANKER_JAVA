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
    private int m_rating;
    private JFrame m_Jframe;

    ImageModel(BufferedImage image, String fileName, String creationTime, JFrame jframe) {
        m_image = image;
        m_fileName = fileName;
        m_creationTime = creationTime;
        m_rating = 0;
        m_Jframe = jframe;
        setChanged();
    }

    public void setRating(int rating) {
        if (m_rating == rating) {
            return;
        }
        m_rating = rating;
        setChanged();
        notifyObservers();
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

    public int getRating() {
        return m_rating;
    }

    public JFrame getJframe() {
        return m_Jframe;
    }
}


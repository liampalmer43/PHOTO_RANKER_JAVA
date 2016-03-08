import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.*;
import java.awt.event.*;    
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Observable;
import java.util.Observer;
import java.io.File;
import java.io.IOException;

class ImageView extends JPanel implements Observer {

    // the view's main user interface
    
    // the model that this view is showing
    private ImageModel m_model;
    private JPanel m_parent;
    private Rating m_child;
    private JDialog m_pop;

    ImageView(ImageModel model) {
        // set the model 
        m_model = model;
        m_model.addObserver(this);

        // create the view UI
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT + ImageCollectionModel.META_DATA_OFFSET));
        this.setMaximumSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT + ImageCollectionModel.META_DATA_OFFSET));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        BufferedImage image = m_model.getImage();
        Image scaled_image = image.getScaledInstance(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        JLabel image_label = new JLabel();
        image_label.setIcon(new ImageIcon(scaled_image));
        this.add(image_label, BorderLayout.CENTER);

        m_pop = new JDialog();
        m_pop.setPreferredSize(new Dimension(400,400));
        Image scaled_pop = image.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaled_pop));
        m_pop.add(label);
        m_pop.pack();
        image_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_pop.setLocationRelativeTo(m_model.getJframe());
                m_pop.setVisible(true);
            }
        });

        JPanel description = new JPanel();
        description.setPreferredSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.META_DATA_OFFSET));
        description.setLayout(new GridLayout(3, 1, 5, 5));
        description.add(new JLabel(m_model.getFileName()));
        description.add(new JLabel(m_model.getCreationTime()));
        Rating rating = new Rating(0);
        description.add(rating);
        m_parent = description;
        m_child = rating;
        this.add(description, BorderLayout.PAGE_END);
    } 

    private class Rating extends JPanel {
        Rating(int rating) {
            Image scaled_filled_star = null;
            Image scaled_empty_star = null;
            try {
                BufferedImage filled_star = ImageIO.read(new File("filled.jpeg"));
                BufferedImage empty_star = ImageIO.read(new File("empty.png"));
                scaled_filled_star = filled_star.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                scaled_empty_star = empty_star.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            for (int i = 1; i <= 5; ++i) {
                final int count = i;
                if (count <= rating) {
                    JLabel filled = new JLabel();
                    filled.setIcon(new ImageIcon(scaled_filled_star));
                    filled.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            m_model.setRating(count);
                        }
                    });
                    this.add(filled);
                }
                else {
                    JLabel empty = new JLabel();
                    empty.setIcon(new ImageIcon(scaled_empty_star));
                    empty.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            m_model.setRating(count);
                        }
                    });
                    this.add(empty);
                }
            }
        }
    }

    // Observer interface 
    @Override
    public void update(Observable arg0, Object arg1) {
        int rating = m_model.getRating();
        m_parent.remove(m_child);
        m_child = new Rating(rating);
        m_parent.add(m_child);
        revalidate();
        repaint();
    }
} 

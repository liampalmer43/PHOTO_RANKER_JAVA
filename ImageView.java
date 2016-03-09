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
    private Ranking m_child;
    private JDialog m_pop;

    ImageView(ImageModel model) {
        // set the model 
        m_model = model;
        m_model.addObserver(this);

        // create the view UI
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT + ImageCollectionModel.META_DATA_OFFSET));
        this.setMaximumSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT + ImageCollectionModel.META_DATA_OFFSET));
        BufferedImage image = m_model.getImage();
        Image scaled_image = image.getScaledInstance(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        JLabel image_label = new JLabel();
        image_label.setIcon(new ImageIcon(scaled_image));
        this.add(image_label, BorderLayout.CENTER);

        m_pop = new JDialog();
        m_pop.setResizable(false);
        Image scaled_pop = image.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaled_pop));
        label.setPreferredSize(new Dimension(400,400));
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
        description.setMaximumSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.META_DATA_OFFSET));
        description.setLayout(new GridLayout(2, 1));
        JLabel name = new JLabel(m_model.getFileName());
        JLabel date = new JLabel(m_model.getCreationTime());
        name.setFont(new Font("Courier New", Font.BOLD, 10));
        date.setFont(new Font("Courier New", Font.BOLD, 10));
        JPanel words = new JPanel();
        words.setLayout(new BorderLayout());
        words.add(name, BorderLayout.LINE_START);
        words.add(date, BorderLayout.LINE_END);
        description.add(words);

        JButton clear_filter = null;
        try {
            BufferedImage icon = ImageIO.read(new File("broom.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            clear_filter = new JButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            clear_filter = new JButton("Clear");
        }
        clear_filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_model.setRanking(0);
            }
        }); 
        JPanel ranking_container = new JPanel();
        Ranking ranking = new Ranking(0);
        ranking_container.add(clear_filter);
        ranking_container.add(ranking);
        description.add(ranking_container);
        m_parent = ranking_container;
        m_child = ranking;
        this.add(description, BorderLayout.PAGE_END);
    } 

    private class Ranking extends JPanel {
        Ranking(int ranking) {
            Image scaled_filled_star = null;
            Image scaled_empty_star = null;
            try {
                BufferedImage filled_star = ImageIO.read(new File("filled.png"));
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
                if (count <= ranking) {
                    JLabel filled = new JLabel();
                    filled.setIcon(new ImageIcon(scaled_filled_star));
                    filled.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            m_model.setRanking(count);
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
                            m_model.setRanking(count);
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
        int ranking = m_model.getRanking();
        m_parent.remove(m_child);
        m_child = new Ranking(ranking);
        m_parent.add(m_child);
        m_parent.revalidate();
        m_parent.repaint();
    }
} 

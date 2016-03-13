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
    private JPanel m_gridParent;
    private Ranking m_gridChildRanking;
    private JButton m_gridChildFilter;
    private JPanel m_listParent;
    private Ranking m_listChildRanking;
    private JButton m_listChildFilter;
    private JDialog m_pop;
    private boolean m_grid;
    private int m_ranking;
    // The container for the grid layout ImageView.
    private JPanel m_gridContainer;
    // The container for the list layout ImageView.
    private JPanel m_listContainer;
    // Imaged
    private static Image m_filledStar = null;
    private static Image m_emptyStar = null;

    ImageView(ImageModel model) {
        if (m_filledStar == null || m_emptyStar == null) {
            try {
                BufferedImage filled_star = ImageIO.read(new File("filled.png"));
                BufferedImage empty_star = ImageIO.read(new File("empty.png"));
                m_filledStar = filled_star.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                m_emptyStar = empty_star.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // set the model 
        m_model = model;
        m_model.addObserver(this);
        // set state variables
        m_grid = true;
        m_ranking = 0;

        // create the view UI
        m_gridContainer = new JPanel();
        m_listContainer = new JPanel();

        m_gridContainer.setLayout(new BorderLayout());
        m_gridContainer.setPreferredSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT + ImageCollectionModel.META_DATA_OFFSET));
        m_gridContainer.setMaximumSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT + ImageCollectionModel.META_DATA_OFFSET));
        m_listContainer.setLayout(new BorderLayout());
        m_listContainer.setPreferredSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH * 2, ImageCollectionModel.IMAGE_HEIGHT));
        m_listContainer.setMaximumSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH * 2, ImageCollectionModel.IMAGE_HEIGHT));

        BufferedImage image = m_model.getImage();
        Image scaled_image = image.getScaledInstance(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        JLabel grid_image_label = new JLabel();
        grid_image_label.setIcon(new ImageIcon(scaled_image));
        JLabel list_image_label = new JLabel();
        list_image_label.setIcon(new ImageIcon(scaled_image));

        m_gridContainer.add(grid_image_label, BorderLayout.CENTER);
        m_listContainer.add(list_image_label, BorderLayout.CENTER);

        m_pop = new JDialog();
        m_pop.setResizable(false);
        Image scaled_pop = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaled_pop));
        label.setPreferredSize(new Dimension(500,500));
        m_pop.add(label);
        m_pop.pack();
        grid_image_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_pop.setLocationRelativeTo(m_model.getJframe());
                m_pop.setVisible(true);
            }
        });
        list_image_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_pop.setLocationRelativeTo(m_model.getJframe());
                m_pop.setVisible(true);
            }
        });

        // Create the description panel for the grid layout.
        JPanel grid_description = new JPanel();
        grid_description.setPreferredSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.META_DATA_OFFSET));
        grid_description.setMaximumSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH, ImageCollectionModel.META_DATA_OFFSET));
        grid_description.setLayout(new GridLayout(2, 1));
        JLabel grid_name = new JLabel(m_model.getFileName());
        JLabel grid_date = new JLabel(m_model.getCreationTime());
        grid_name.setFont(new Font("Courier New", Font.BOLD, 10));
        grid_date.setFont(new Font("Courier New", Font.BOLD, 10));
        JPanel grid_words = new JPanel();
        grid_words.setLayout(new BorderLayout());
        grid_words.add(grid_name, BorderLayout.LINE_START);
        grid_words.add(grid_date, BorderLayout.LINE_END);
        grid_description.add(grid_words);
        JButton grid_clear_filter = null;
        try {
            BufferedImage icon = ImageIO.read(new File("broom.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            grid_clear_filter = new JButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            grid_clear_filter = new JButton("Clear");
        }
        grid_clear_filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_model.setRanking(0);
            }
        }); 
        JPanel grid_ranking_container = new JPanel();
        Ranking grid_ranking = new Ranking(0);
        grid_ranking_container.add(grid_ranking);
        grid_ranking_container.add(grid_clear_filter);
        grid_description.add(grid_ranking_container);
        m_gridParent = grid_ranking_container;
        m_gridChildRanking = grid_ranking;
        m_gridChildFilter = grid_clear_filter;

        // Create the description panel for the list layout.
        JPanel list_description = new JPanel();
        list_description.setPreferredSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH - 10, ImageCollectionModel.META_DATA_OFFSET));
        list_description.setMaximumSize(new Dimension(ImageCollectionModel.IMAGE_WIDTH - 10, ImageCollectionModel.META_DATA_OFFSET));
        list_description.setLayout(new GridLayout(2, 1));
        JLabel list_name = new JLabel(m_model.getFileName());
        JLabel list_date = new JLabel(m_model.getCreationTime());
        list_name.setFont(new Font("Courier New", Font.BOLD, 10));
        list_date.setFont(new Font("Courier New", Font.BOLD, 10));
        JPanel list_words = new JPanel();
        list_words.setLayout(new BorderLayout());
        list_words.add(list_name, BorderLayout.LINE_START);
        list_words.add(list_date, BorderLayout.LINE_END);
        list_description.add(list_words);
        JButton list_clear_filter = null;
        try {
            BufferedImage icon = ImageIO.read(new File("broom.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            list_clear_filter = new JButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            list_clear_filter = new JButton("Clear");
        }
        list_clear_filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_model.setRanking(0);
            }
        }); 
        JPanel list_ranking_container = new JPanel();
        Ranking list_ranking = new Ranking(0);
        list_ranking_container.add(list_ranking);
        list_ranking_container.add(list_clear_filter);
        list_description.add(list_ranking_container);
        m_listParent = list_ranking_container;
        m_listChildRanking = list_ranking;
        m_listChildFilter = list_clear_filter;

        m_gridContainer.add(grid_description, BorderLayout.PAGE_END);
        m_listContainer.add(list_description, BorderLayout.LINE_END);

        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        this.add(m_model.isGrid() ? m_gridContainer : m_listContainer);
    } 

    private class Ranking extends JPanel {
        Ranking(int ranking) {
            for (int i = 1; i <= 5; ++i) {
                final int count = i;
                if (count <= ranking) {
                    JLabel filled = new JLabel();
                    filled.setIcon(new ImageIcon(m_filledStar));
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
                    empty.setIcon(new ImageIcon(m_emptyStar));
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
        boolean grid = m_model.isGrid();
        if (m_grid != grid) {
            m_grid = grid;
            this.removeAll();
            this.add(m_grid ? m_gridContainer : m_listContainer);
        }

        int ranking = m_model.getRanking();
        if (m_ranking != ranking) {
            m_gridParent.removeAll();
            m_gridChildRanking = new Ranking(ranking);
            m_gridParent.add(m_gridChildRanking);
            m_gridParent.add(m_gridChildFilter);
            m_gridParent.revalidate();
            m_gridParent.repaint();

            m_listParent.removeAll();
            m_listChildRanking = new Ranking(ranking);
            m_listParent.add(m_listChildRanking);
            m_listParent.add(m_listChildFilter);
            m_listParent.revalidate();
            m_listParent.repaint();
            m_ranking = ranking;
        }
    }
} 

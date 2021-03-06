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

class Toolbar extends JPanel implements Observer {

    // The load button.
    private JButton m_load;
    // The menu bar (set to be the frame's menu bar).
    private JMenuBar m_menuBar;
    // The toggle button pair for view toggling.
    private JToggleButton m_grid;
    private JToggleButton m_list;
    // The clear filter button.
    private JButton m_clearFilter;
    // The ranking child component that must be removed in place of a new ranking filter.
    private Ranking m_child;
    private JPanel m_parent;
    // Images used in the ranking object.
    private static Image m_filledStar = null;
    private static Image m_emptyStar = null;

    // The model that this view is showing.
    private ImageCollectionModel m_model;
    
    Toolbar(ImageCollectionModel model) {
        // Initialize the ranking object's images.
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

        // Create the view UI.
        // Start with the toolbar buttons.
        // The loading button.
        m_load = null;
        try {
            BufferedImage icon = ImageIO.read(new File("load.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            m_load = new JButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            m_load = new JButton("Load");
        }
        // The grid view button.
        m_grid = null;
        try {
            BufferedImage icon = ImageIO.read(new File("grid.gif"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            m_grid = new JToggleButton(new ImageIcon(scaled_icon), true);
        } catch (IOException e) {
            m_grid = new JToggleButton("Grid", true);
        }
        // The list view button.
        m_list = null;
        try {
            BufferedImage icon = ImageIO.read(new File("list.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            m_list = new JToggleButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            m_list = new JToggleButton("List");
        }
        // The clear filter button.
        m_clearFilter = null;
        try {
            BufferedImage icon = ImageIO.read(new File("broom.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            m_clearFilter = new JButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            m_clearFilter = new JButton("Clear");
        }
        
        // Set the model.
        m_model = model;
        
        m_grid.addActionListener(new ActionListener( ) {
            public void actionPerformed(ActionEvent e) {
                m_model.setGrid();
                m_list.setSelected(false);
                m_grid.setSelected(true);
            }
        });
        m_list.addActionListener(new ActionListener( ) {
            public void actionPerformed(ActionEvent e) {
                m_model.setList();
                m_list.setSelected(true);
                m_grid.setSelected(false);
            }
        });
        m_load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_model.selectFile();
            }
        }); 
        m_clearFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_model.setRankingFilter(0);
            }
        }); 

        JMenuBar bar = new JMenuBar();
        JPanel divider = new JPanel(new GridLayout(0, 2));
        JPanel left_container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel right_container = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        divider.add(left_container);
        divider.add(right_container);
        left_container.add(m_load);
        left_container.add(m_grid);
        left_container.add(m_list);
    
        m_child = new Ranking(0);
        right_container.add(m_child);
        right_container.add(m_clearFilter);
        m_parent = right_container;
        bar.add(divider);
        m_menuBar = bar;
        this.add(bar);
    } 

    public JMenuBar getMenuBar() {
        return m_menuBar;
    }

    // The toolbar's ranking object.
    private class Ranking extends JPanel {
        Ranking(int ranking) {
            this.add(new JLabel("FILTER BY: "));
            for (int i = 1; i <= 5; ++i) {
                final int count = i;
                if (count <= ranking) {
                    JLabel filled = new JLabel();
                    filled.setIcon(new ImageIcon(m_filledStar));
                    filled.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            m_model.setRankingFilter(count);
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
                            m_model.setRankingFilter(count);
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
        m_parent.removeAll();
        m_child = new Ranking(m_model.getRankingFilter());
        m_parent.add(m_child);
        m_parent.add(m_clearFilter);
        m_parent.revalidate();
        m_parent.repaint();
    }
} 

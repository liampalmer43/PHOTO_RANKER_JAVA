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

    // the view's main user interface
    private JButton m_load;
    private JMenuBar m_menuBar;
    private JToggleButton m_grid;
    private JToggleButton m_list;
    // The ranking child component that must be removed in place of a new ranking filter.
    private Ranking m_child;

    // the model that this view is showing
    private ImageCollectionModel m_model;
    
    Toolbar(ImageCollectionModel model) {
        // create the view UI
        m_load = null;
        try {
            BufferedImage icon = ImageIO.read(new File("load.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            m_load = new JButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            m_load = new JButton("Load");
        }
        m_grid = null;
        try {
            BufferedImage icon = ImageIO.read(new File("grid.gif"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            m_grid = new JToggleButton(new ImageIcon(scaled_icon), true);
        } catch (IOException e) {
            m_grid = new JToggleButton("Grid", true);
        }
        m_list = null;
        try {
            BufferedImage icon = ImageIO.read(new File("list.png"));
            Image scaled_icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            m_list = new JToggleButton(new ImageIcon(scaled_icon));
        } catch (IOException e) {
            m_list = new JToggleButton("List");
        }
        
        // set the model 
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

        JMenuBar bar = new JMenuBar();
        bar.add(m_load);
        bar.add(m_grid);
        bar.add(m_list);
        m_child = new Ranking(0);
        bar.add(m_child);
        m_menuBar = bar;
//        this.setLayout(new GridLayout(0, 5, 2, 2));
        this.add(bar);
    } 

    public JMenuBar getMenuBar() {
        return m_menuBar;
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
                            m_model.setRankingFilter(count);
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
        m_menuBar.remove(m_child);
        m_child = new Ranking(m_model.getRankingFilter());
        m_menuBar.add(m_child);
        m_menuBar.revalidate();
        m_menuBar.repaint();
    }
} 

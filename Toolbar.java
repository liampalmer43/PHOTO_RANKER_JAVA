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
        m_menuBar = bar;
        this.setLayout(new GridLayout(0, 5, 2, 2));
        this.add(bar);
    } 

    public JMenuBar getMenuBar() {
        return m_menuBar;
    }

    // Observer interface 
    @Override
    public void update(Observable arg0, Object arg1) {
        // Might be used if we want this componenet to watch the view.
    }
} 

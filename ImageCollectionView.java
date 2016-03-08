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

class ImageCollectionView extends JPanel implements Observer {

    // the view's main user interface
    private ArrayList<ImageView> m_imageViews;

    // the model that this view is showing
    private ImageCollectionModel m_model;
    
    ImageCollectionView(ImageCollectionModel model) {
        // set the model 
        m_model = model;

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resetLayout();
            }
        });
        m_imageViews = new ArrayList<ImageView>();
    }

    private void resetLayout() {
        int columns = getWidth() / (ImageCollectionModel.IMAGE_WIDTH + 30);
        int rows = (int)Math.ceil((float)m_imageViews.size() / columns);
        this.removeAll();
        this.setLayout(new GridLayout(rows, columns, 5, 5));
        for (int i = 0; i < rows * columns; ++i) {
            if (i < m_imageViews.size()) {
/*
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.add(new JPanel());
        p2.add(fixed_board);
        p2.add(new JPanel());
        p1.add(new JPanel());
        p1.add(p2);
        p1.add(new JPanel());
        
JScrollPane fixed_view = new JScrollPane(p1);
        fixed_view.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel full_view = new JPanel(new GridLayout(1,1));
        full_view.add(board);
        full_view.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
*/
                JPanel canvas = new JPanel();
                canvas.setLayout(new BoxLayout(canvas, BoxLayout.X_AXIS));
                JPanel inner_canvas = new JPanel();
                inner_canvas.setLayout(new BoxLayout(inner_canvas, BoxLayout.Y_AXIS));
                inner_canvas.add(new JPanel());
                inner_canvas.add(m_imageViews.get(i));
                inner_canvas.add(new JPanel());
                canvas.add(new JPanel());
                canvas.add(inner_canvas);
                canvas.add(new JPanel());
                this.add(canvas);
            }
            else {
                this.add(new JPanel());
            }
        }
        revalidate();
        repaint();
    }

    private void reCoordinate() {
        // Set the view appropriately.
        this.add(m_imageViews.get(m_imageViews.size() - 1));
        revalidate();
        repaint();
    }

    // Observer interface 
    @Override
    public void update(Observable arg0, Object arg1) {
        ArrayList<ImageModel> image_models = m_model.getImageModels();
        if (m_imageViews.size() != image_models.size()){
            m_imageViews.add(new ImageView(image_models.get(image_models.size() - 1)));
            resetLayout();
        }
    }
} 

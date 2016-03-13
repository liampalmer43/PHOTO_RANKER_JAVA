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
        this.removeAll();
        int ranking = m_model.getRankingFilter();
        ArrayList<ImageModel> image_models = m_model.getImageModels();
        ArrayList<ImageView> passing_views = new ArrayList<ImageView>();
        for (int i = 0; i < image_models.size(); ++i) {
            if (image_models.get(i).getRanking() >= ranking) {
                passing_views.add(m_imageViews.get(i));
            }
        }
                 
        if (m_model.isGrid()) {
            int columns = getWidth() / (ImageCollectionModel.IMAGE_WIDTH + 30);
            int rows = (int)Math.ceil((float)passing_views.size() / columns);
            if (columns == 0 && rows == 0) {
                return;
            }
            this.setLayout(new GridLayout(rows, columns, 5, 5));
            for (int i = 0; i < rows * columns; ++i) {
                if (i < passing_views.size()) {
                    JPanel canvas = new JPanel();
                    canvas.setLayout(new BoxLayout(canvas, BoxLayout.X_AXIS));
                    JPanel inner_canvas = new JPanel();
                    inner_canvas.setLayout(new BoxLayout(inner_canvas, BoxLayout.Y_AXIS));
                    inner_canvas.add(new JPanel());
                    inner_canvas.add(passing_views.get(i));
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
        }
        else {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            for (int i = 0; i < passing_views.size(); ++i) {
                // Make the list left aligned with a flow layout.
                JPanel container = new JPanel();
                container.setLayout(new FlowLayout(FlowLayout.LEFT));
                container.add(passing_views.get(i));
                container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                this.add(container);
            }
        }
        revalidate();
        repaint();
    }

    // Observer interface 
    @Override
    public void update(Observable arg0, Object arg1) {
        ArrayList<ImageModel> image_models = m_model.getImageModels();
        if (m_imageViews.size() != image_models.size()){
            m_imageViews.add(new ImageView(image_models.get(image_models.size() - 1)));
        }
        for (int i = 0; i < m_imageViews.size(); ++i) {
            m_imageViews.get(i).update(arg0, arg1);
        }
        resetLayout();
    }
} 

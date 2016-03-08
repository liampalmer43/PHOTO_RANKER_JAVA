import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.*;    

public class Main{

    public static void main(String[] args){ 
        JFrame frame = new JFrame("Fotag!");
        frame.setMinimumSize(new Dimension(400, 350));
        
        // create Model and initialize it
        ImageCollectionModel image_collection_model = new ImageCollectionModel();
        image_collection_model.setFrame(frame);
        
        ImageCollectionView image_collection_view = new ImageCollectionView(image_collection_model);
        image_collection_model.addObserver(image_collection_view);

        Toolbar tool_bar = new Toolbar(image_collection_model);
        image_collection_model.addObserver(tool_bar);
/*
        Menu menu = new Menu(model);
        model.addObserver(menu);
        frame.setJMenuBar(menu.getMenuBar());
*/ 
        // let all the views know that they're connected to the model
        image_collection_model.notifyObservers();
        
        // create the window
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(getParent().getWidth(), (int)super.getPreferredSize().getHeight());
            }
        };
        JScrollPane wrapper = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(wrapper);
        p.add(image_collection_view, BorderLayout.CENTER);
 
        frame.setJMenuBar(tool_bar.getMenuBar());
        frame.setPreferredSize(new Dimension(800,500));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } 
}


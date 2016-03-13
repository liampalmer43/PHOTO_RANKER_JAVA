import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.*;    
import java.io.File;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Main{

    public static void main(String[] args){ 
        JFrame frame = new JFrame("Fotag!");
        frame.setMinimumSize(new Dimension(410, 300));
        
        // create Model and initialize it
        ImageCollectionModel image_collection_model = new ImageCollectionModel();
        image_collection_model.setFrame(frame);
        
        ImageCollectionView image_collection_view = new ImageCollectionView(image_collection_model);
        image_collection_model.addObserver(image_collection_view);

        Toolbar tool_bar = new Toolbar(image_collection_model);
        image_collection_model.addObserver(tool_bar);
        
        // let all the views know that they're connected to the model
        image_collection_model.notifyObservers();

        // Try to restore previous state if the appropriate file exists.
        try {
            FileReader fileReader = new FileReader("state.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            try {
                int size = Integer.parseInt(bufferedReader.readLine());
                for (int i = 0; i < size; ++i) {
                    String name = bufferedReader.readLine();
                    String path = bufferedReader.readLine();
                    int ranking = Integer.parseInt(bufferedReader.readLine());
                    
                    BufferedImage image = null;
                    try {
                        Path path_object = Paths.get(path);
                        BasicFileAttributes attr = null;
                        try {
                            attr = Files.readAttributes(path_object, BasicFileAttributes.class);
                        }
                        catch (IOException ex) {
                            System.out.println(ex);
                        }
                        Date creation_time = attr == null ? null : new Date(attr.creationTime().toMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                        String creation_date = attr == null ? "Not available" : sdf.format(creation_time);
                        image = ImageIO.read(new File(path));
                        if (image != null) {
                            image_collection_model.addImage(image, name, creation_date, path, ranking);
                        }
                    } 
                    catch (IOException e) {}
                }
            } finally {
                bufferedReader.close();
            }
        } 
        catch(FileNotFoundException ex) {} 
        catch(IOException ex) {}
        
        // create the window
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(getParent().getWidth(), (int)super.getPreferredSize().getHeight());
            }
        };
        p.add(image_collection_view, BorderLayout.CENTER);
        JScrollPane wrapper = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(wrapper);
 
        frame.setJMenuBar(tool_bar.getMenuBar());
        frame.setPreferredSize(new Dimension(800,500));
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                image_collection_model.saveState();
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } 
}


package fr.mathieupassenaud.qrcodeclipboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */

        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // from https://docs.oracle.com/javase/tutorial/uiswing/misc/systemtray.html
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            //from https://stackoverflow.com/questions/7993000/need-to-use-joptionpane-error-message-type-of-jdialog-in-a-jframe/7993711
            JOptionPane optionPane = new JOptionPane("SystemTray is not supported", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            return;
        }
        final PopupMenu popup = new PopupMenu();
        JPopupMenu p = new JPopupMenu();

        final TrayIcon trayIcon =
                new TrayIcon(createImage("logo.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        trayIcon.setToolTip("Clipboard to qrcode");

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            JOptionPane optionPane = new JOptionPane("Can not add icon to tray", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            return;
        }

        trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                    Rectangle bounds = getSafeScreenBounds(e.getPoint());
                    final JFrame popup = new QrCodePanel();

                    popup.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            popup.dispose();
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                    popup.addWindowFocusListener(new WindowFocusListener() {
                        @Override
                        public void windowGainedFocus(WindowEvent e) {
                        }

                        @Override
                        public void windowLostFocus(WindowEvent e) {
                            popup.dispose();
                        }
                    });

                    // from https://stackoverflow.com/questions/51459612/how-to-add-text-field-in-a-pop-up-menu
                    Point point = e.getPoint();

                    int x = point.x;
                    int y = point.y;
                    if (y < bounds.y) {
                        y = bounds.y;
                    } else if (y > bounds.y + bounds.height) {
                        y = bounds.y + bounds.height;
                    }
                    if (x < bounds.x) {
                        x = bounds.x;
                    } else if (x > bounds.x + bounds.width) {
                        x = bounds.x + bounds.width;
                    }

                    if (x + popup.getPreferredSize().width > bounds.x + bounds.width) {
                        x = (bounds.x + bounds.width) - popup.getPreferredSize().width;
                    }
                    if (y + popup.getPreferredSize().height > bounds.y + bounds.height) {
                        y = (bounds.y + bounds.height) - popup.getPreferredSize().height;
                    }
                    popup.setLocation(x, y);
                    popup.setVisible(true);
                }
                if(e.getButton() == MouseEvent.BUTTON3){
                    popup.setEnabled(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    protected static Image createImage(String path, String description) {
        // from https://mkyong.com/java/java-read-a-file-from-resources-folder/
        ClassLoader classLoader = Main.class.getClassLoader();
        URL imageURL = classLoader.getResource(path);
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public static Rectangle getSafeScreenBounds(Point pos) {
        Rectangle bounds = getScreenBoundsAt(pos);
        Insets insets = getScreenInsetsAt(pos);
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= (insets.left + insets.right);
        bounds.height -= (insets.top + insets.bottom);
        return bounds;
    }

    public static Insets getScreenInsetsAt(Point pos) {
        GraphicsDevice gd = getGraphicsDeviceAt(pos);
        Insets insets = null;
        if (gd != null) {
            insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
        }
        return insets;
    }

    public static Rectangle getScreenBoundsAt(Point pos) {
        GraphicsDevice gd = getGraphicsDeviceAt(pos);
        Rectangle bounds = null;
        if (gd != null) {
            bounds = gd.getDefaultConfiguration().getBounds();
        }
        return bounds;
    }
    public static GraphicsDevice getGraphicsDeviceAt(Point pos) {
        GraphicsDevice device = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();
        ArrayList<GraphicsDevice> lstDevices = new ArrayList<GraphicsDevice>(lstGDs.length);
        for (GraphicsDevice gd : lstGDs) {
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            Rectangle screenBounds = gc.getBounds();
            if (screenBounds.contains(pos)) {
                lstDevices.add(gd);
            }
        }

        if (lstDevices.size() > 0) {
            device = lstDevices.get(0);
        } else {
            device = ge.getDefaultScreenDevice();
        }

        return device;
    }
}
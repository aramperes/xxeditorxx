package ca.momoperes.xxeditorxx.visual;

import ca.momoperes.xxeditorxx.Editor;
import ca.momoperes.xxeditorxx.EditorMap;
import ca.momoperes.xxeditorxx.EditorObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EditorFrame extends JFrame implements KeyListener {

    private JTree projectView;
    ProjectViewTreeNode projectViewTop = new ProjectViewTreeNode("Project", NodeType.FOLDER);
    ProjectViewTreeNode mapsNode = new ProjectViewTreeNode("Maps", NodeType.FOLDER);
    HashMap<String, ProjectViewTreeNode> mapNodes = new HashMap<>();
    private boolean setup = true;

    public EditorFrame() throws HeadlessException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        setLayout(null);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setTitle("xXEditorXx - v" + Editor.VERSION);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        addKeyListener(this);

        projectView = new JTree(projectViewTop);
        projectView.setCellRenderer(new ProjectViewTreeCellRenderer());
        projectView.setBackground(Color.WHITE);
        projectView.setSize(250, getHeight());
        projectView.setLocation(0, 0);
        projectView.setVisible(true);
        projectView.addKeyListener(this);

        projectView.addMouseListener(new ProjectViewTreeDoubleClickListener());
        add(projectView);
        projectView.expandPath(new TreePath(projectViewTop.getPath()));
    }

    public void clearNode(ProjectViewTreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            ProjectViewTreeNode child = (ProjectViewTreeNode) node.getChildAt(i);
            clearNode(child);
        }
        node.removeAllChildren();
    }

    public void addMap(EditorMap map) {
        ProjectViewTreeNode mapN = new ProjectViewTreeNode(map.getName() + " [" + map.getObjects().length + " Objects]", NodeType.MAP);
        mapN.add(new ProjectViewTreeNode("New object...", NodeType.NEW));
        for (EditorObject editorObject : map.getObjects()) {
            addObject(map, editorObject);
        }
        mapsNode.add(mapN);
        mapNodes.put(map.getName(), mapN);
        ((DefaultTreeModel) (projectView.getModel())).nodeStructureChanged(mapsNode);
    }

    public void addObject(EditorMap map, EditorObject object) {
        ProjectViewTreeNode objN = new ProjectViewTreeNode(object.getName() + " [" + object.getPoints().length + " Points]", NodeType.OBJECT);
        mapNodes.get(map.getName()).add(objN);
    }

    public void setupProjectView() {
        if (Editor.currentProject == null) {
            projectViewTop.add(new ProjectViewTreeNode("New project...", NodeType.NEW));
        } else {
            projectViewTop.setUserObject("Project [" + Editor.currentProject.getName() + "]");
            mapsNode.add(new ProjectViewTreeNode("New map...", NodeType.NEW));

            for (EditorMap map : Editor.currentProject.getMaps()) {
                addMap(map);
            }
            projectViewTop.add(mapsNode);

            ProjectViewTreeNode texturesNode = new ProjectViewTreeNode("Textures", NodeType.FOLDER);
            projectViewTop.add(texturesNode);
            texturesNode.add(new ProjectViewTreeNode("New texture...", NodeType.NEW));
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class ProjectViewTreeNode extends DefaultMutableTreeNode {
        private NodeType type;

        public ProjectViewTreeNode(Object userObject, NodeType type) {
            super(userObject);
            this.type = type;
        }

        public NodeType getType() {
            return type;
        }
    }

    class ProjectViewTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            ProjectViewTreeNode nodo = (ProjectViewTreeNode) value;
            if (nodo.getType() == NodeType.NEW) {
                try {
                    ClassLoader classLoader = getClass().getClassLoader();
                    File icon_new_file = new File(classLoader.getResource("icon_new.png").getFile());
                    Image icon_new_image = ImageIO.read(icon_new_file).getScaledInstance(10, 10, Image.SCALE_DEFAULT);
                    setIcon(new ImageIcon(icon_new_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (nodo.getType() == NodeType.MAP) {
                try {
                    ClassLoader classLoader = getClass().getClassLoader();
                    File icon_map_file = new File(classLoader.getResource("icon_map.png").getFile());
                    Image icon_map_image = ImageIO.read(icon_map_file).getScaledInstance(10, 10, Image.SCALE_DEFAULT);
                    setIcon(new ImageIcon(icon_map_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }
    }

    class ProjectViewTreeDoubleClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                return;
            }
            if (e.getClickCount() == 2) {
                ProjectViewTreeNode node = (ProjectViewTreeNode) projectView.getLastSelectedPathComponent();
                if (node == null) return;

                if (node.getType() == NodeType.NEW) {
                    ProjectViewTreeNode parentNode = (ProjectViewTreeNode) node.getParent();
                    String parent = (String) parentNode.getUserObject();
                    if (parent.equals("Project")) {
                        System.out.println("Create new project window.");
                    } else if (parent.equals("Maps")) {
                        System.out.println("Create new map window.");
                        EditorMap map = new EditorMap("Testmap", "test.xxm");
                        map.setObjects(new EditorObject[0]);
                        if (Editor.currentProject.addMap(map)) {
                            Editor.currentProject.save();
                            addMap(map);
                        }
                    } else if (parent.equals("Textures")) {
                        System.out.println("Create new texture window.");
                    } else if (parentNode.getType() == NodeType.MAP) {
                        System.out.println("Create new object.");
                    }
                }
            }
        }
    }

    enum NodeType {
        FOLDER, NEW, MAP, OBJECT, TEXTURE;
    }
}

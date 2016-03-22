package ca.momoperes.xxeditorxx;

import ca.momoperes.xxeditorxx.visual.EditorFrame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class Editor {

    public static final String VERSION = "1.0-SNAPSHOT";
    public static Editor instance;
    public static EditorFrame frame;
    public static EditorProject currentProject;

    public Editor() {
        instance = this;

        frame = new EditorFrame();

        // Testing
        File projectFolder = new File("C:\\Users\\csa\\Desktop\\xxproject");
        String name = "My project";

        EditorProject project = EditorProject.create(name, projectFolder);

        if (project == null) {
            System.out.println("Loading project...");
            project = EditorProject.load(projectFolder);
            System.out.println("Found project: " + project.getName());
            System.out.println("Maps: " + project.getMaps().length);

            for (EditorMap map : project.getMaps()) {
                System.out.println("===================");
                System.out.println("Map name: " + map.getName());
                System.out.println("Objects: " + map.getObjects().length);
                for (EditorObject object : map.getObjects()) {
                    System.out.println("  - Object \"" + object.getName() + "\" with " + object.getPoints().length + " points.");
                }
            }

            if (project == null) {
                System.out.println("Could not load project.");
                return;
            }
        } else {
            System.out.println("Created project.");
        }

        currentProject = project;
        frame.setupProjectView();
        project.save();
    }

    public static void main(String[] args) {
        new Editor();
    }

    public static void writeFile(File file, String content) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        try {
            Files.write(file.toPath(), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(File file) {
        if (!file.isFile())
            return null;

        try {
            List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

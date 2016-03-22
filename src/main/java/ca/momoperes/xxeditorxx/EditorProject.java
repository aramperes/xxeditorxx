package ca.momoperes.xxeditorxx;

import ca.momothereal.mojangson.ex.MojangsonParseException;
import ca.momothereal.mojangson.value.MojangsonCompound;
import ca.momothereal.mojangson.value.MojangsonString;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EditorProject {
    private String name;
    private File location;
    private EditorMap[] maps;
    private EditorTexture[] textures;
    private String version = Editor.VERSION;

    public EditorProject(String name, File location) {
        this.name = name;
        this.location = location;
    }

    public static EditorProject create(String name, File location) {
        if (name == null || location == null) {
            return null;
        }

        if (!location.isDirectory()) {
            return null;
        }

        if (!(location.listFiles() == null || location.listFiles().length == 0)) {
            return null;
        }

        File dirTextures = new File(location, "textures");
        File dirMaps = new File(location, "maps");
        dirTextures.mkdirs();
        dirMaps.mkdirs();

        File projectDesc = new File(location, "project.xxp");

        MojangsonCompound cp = new MojangsonCompound();
        cp.put("name", new MojangsonString(name));
        cp.put("version", new MojangsonString(Editor.VERSION));
        StringBuilder builder = new StringBuilder();
        cp.write(builder);

        Editor.writeFile(projectDesc, builder.toString());
        return new EditorProject(name, location);
    }

    public static EditorProject load(File location) {
        File folder = null;
        if (location.isFile()) {
            if (location.getName().equals("project.xxp"))
                folder = location.getParentFile();
        } else {
            folder = location;
        }

        if (folder == null)
            return null;

        File projectFile = new File(folder, "project.xxp");
        String projectFileContent = Editor.readFile(projectFile);

        MojangsonCompound cp = new MojangsonCompound();
        try {
            cp.read(projectFileContent);
        } catch (MojangsonParseException e) {
            e.printStackTrace();
        }

        String projectName = ((MojangsonString) cp.get("name")).getValue();
        String projectVersion = ((MojangsonString) cp.get("version")).getValue();
        EditorProject project = new EditorProject(projectName, folder);
        project.setVersion(projectVersion);
        project.setMaps(getMaps(project));

        return project;
    }

    public static EditorMap[] getMaps(EditorProject project) {
        File mapFolder = new File(project.getLocation(), "maps");
        File[] mapFiles = mapFolder.listFiles();
        List<EditorMap> maps = new ArrayList<>();

        for (File mapFile : mapFiles) {
            EditorMap map = EditorMap.load(mapFile);
            if (map != null) {
                maps.add(map);
            }
        }

        return maps.toArray(new EditorMap[maps.size()]);
    }

    public String getName() {
        return name;
    }

    public File getLocation() {
        return location;
    }

    public EditorMap[] getMaps() {
        return maps;
    }

    public EditorTexture[] getTextures() {
        return textures;
    }

    public void setMaps(EditorMap[] maps) {
        this.maps = maps;
    }

    public void setTextures(EditorTexture[] textures) {
        this.textures = textures;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void save() {
        for (EditorMap map : getMaps()) {
            map.save(this);
        }
    }

    public void addMap(EditorMap map) {
        List<EditorMap> mapsL = new ArrayList<>();
        Collections.addAll(mapsL, maps);
        mapsL.add(map);
        maps = mapsL.toArray(new EditorMap[mapsL.size()]);
    }
}

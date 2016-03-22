package ca.momoperes.xxeditorxx;

import ca.momothereal.mojangson.ex.MojangsonParseException;
import ca.momothereal.mojangson.value.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditorMap {

    private String name;
    private String source;
    private EditorObject[] objects;

    public EditorMap(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public EditorObject[] getObjects() {
        return objects;
    }

    public void setObjects(EditorObject[] objects) {
        this.objects = objects;
    }

    public static EditorMap load(File file) {
        if (!file.isFile())
            return null;

        if (!file.getName().endsWith(".xxm")) {
            return null;
        }

        String content = Editor.readFile(file);
        MojangsonCompound cp = new MojangsonCompound();
        try {
            cp.read(content);
        } catch (MojangsonParseException e) {
            e.printStackTrace();
            return null;
        }

        String mapName = ((MojangsonString) cp.get("name")).getValue();
        EditorMap map = new EditorMap(mapName, file.getName());
        List<EditorObject> objects = new ArrayList<>();
        MojangsonArray<MojangsonCompound> objectsA = (MojangsonArray<MojangsonCompound>) cp.get("objects");
        for (MojangsonCompound objectC : objectsA) {
            EditorObject object = EditorObject.load(objectC);
            if (object != null)
                objects.add(object);
        }
        EditorObject[] eos = objects.toArray(new EditorObject[objects.size()]);
        map.setObjects(eos);

        return map;
    }

    public void save(EditorProject project) {
        File mapFolder = new File(project.getLocation(), "maps");
        MojangsonCompound top = new MojangsonCompound();
        top.put("name", new MojangsonString(getName()));
        MojangsonArray<MojangsonCompound> objects = new MojangsonArray<>();
        for (EditorObject object : getObjects()) {
            MojangsonCompound objC = new MojangsonCompound();
            objC.put("name", new MojangsonString(object.getName()));
            MojangsonArray<MojangsonCompound> points = new MojangsonArray<>();
            for (EditorPoint point : object.getPoints()) {
                MojangsonCompound pointC = new MojangsonCompound();
                pointC.put("x", new MojangsonDouble(point.getX()));
                pointC.put("y", new MojangsonDouble(point.getY()));
                points.add(pointC);
            }
            objC.put("points", points);
            objects.add(objC);
        }
        top.put("objects", objects);

        StringBuilder builder = new StringBuilder();
        top.write(builder);
        Editor.writeFile(new File(mapFolder, getSource()), builder.toString());
    }
}

package ca.momoperes.xxeditorxx;

import ca.momothereal.mojangson.value.MojangsonArray;
import ca.momothereal.mojangson.value.MojangsonCompound;
import ca.momothereal.mojangson.value.MojangsonDouble;
import ca.momothereal.mojangson.value.MojangsonString;

import java.util.ArrayList;
import java.util.List;

public class EditorObject {
    private String name;
    private EditorPoint[] points;

    public EditorObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public EditorPoint[] getPoints() {
        return points;
    }

    public void setPoints(EditorPoint[] points) {
        this.points = points;
    }

    public static EditorObject load(MojangsonCompound cp) {
        List<EditorPoint> points = new ArrayList<>();
        String name = ((MojangsonString) cp.get("name")).getValue();
        MojangsonArray<MojangsonCompound> pointsA = (MojangsonArray<MojangsonCompound>) cp.get("points");
        for (MojangsonCompound pointC : pointsA) {
            double x = ((MojangsonDouble) pointC.get("x")).getValue();
            double y = ((MojangsonDouble) pointC.get("y")).getValue();
            points.add(new EditorPoint(x, y));
        }

        EditorPoint[] finalPoints = points.toArray(new EditorPoint[points.size()]);
        EditorObject object = new EditorObject(name);
        object.setPoints(finalPoints);

        return object;
    }
}

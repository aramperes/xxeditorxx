package ca.momoperes.xxeditorxx;

public class EditorTexture {
    private String name;
    private String source;

    public EditorTexture(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }
}

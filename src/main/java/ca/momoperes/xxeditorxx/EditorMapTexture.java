package ca.momoperes.xxeditorxx;

public class EditorMapTexture {
    private EditorTexture texture;
    private double xScale, yScale;

    public EditorMapTexture(EditorTexture texture, double xScale, double yScale) {
        this.texture = texture;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public EditorTexture getTexture() {
        return texture;
    }

    public double getScaleX() {
        return xScale;
    }

    public void setScaleX(double xScale) {
        this.xScale = xScale;
    }

    public double getScaleY() {
        return yScale;
    }

    public void setScaleY(double yScale) {
        this.yScale = yScale;
    }
}

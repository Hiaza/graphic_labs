import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Mjolnir extends JFrame {
    static SimpleUniverse universe;
    static Scene scene;
    static Map<String, Shape3D> nameMap;
    static BranchGroup root;
    static Canvas3D canvas;

    static TransformGroup mjolnir;
    static Transform3D transform3D;

    public Mjolnir() throws IOException {
        configureWindow();
        configureCanvas();
        configureUniverse();

        root= new BranchGroup();
        addImageBackground();

        addDirectionalLightToUniverse();
        addAmbientLightToUniverse();

        ChangeViewAngle();

        mjolnir = getMjolnirGroup();
        root.addChild(mjolnir);

        addAppearance();
        root.compile();
        universe.addBranchGraph(root);
    }

    private void configureWindow()  {
        setTitle("Месники");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configureCanvas(){
        canvas=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);
    }

    private void configureUniverse(){
        universe= new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    private void addModelToUniverse() throws IOException{
        scene = getSceneFromFile("source_folder//thor.obj");
        root=scene.getSceneGroup();
    }

    private void addDirectionalLightToUniverse() {
        BoundingSphere bounds = new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 2000000.0);
        DirectionalLight light = new DirectionalLight(new Color3f(1, 1, 1), new Vector3f(1, -1, -1));
        light.setInfluencingBounds(bounds);

        root.addChild(light);
    }
    private void addAmbientLightToUniverse() {
        AmbientLight light = new AmbientLight(new Color3f(1, 1, 1));
        light.setInfluencingBounds(new BoundingSphere());
        root.addChild(light);
    }

//    private void addLightToUniverse(){
//        Bounds bounds = new BoundingSphere();
//        Color3f color = new Color3f(65/255f, 30/255f, 25/255f);
//        Vector3f lightdirection = new Vector3f(-1f,-1f,-1f);
//        DirectionalLight dirlight = new DirectionalLight(color,lightdirection);
//        dirlight.setInfluencingBounds(bounds);
//        root.addChild(dirlight);
//    }

    private void printModelElementsList(Map<String,Shape3D> nameMap){
        for (String name : nameMap.keySet()) {
            System.out.printf("Name: %s\n", name);}
    }
    private TransformGroup getMjolnirGroup() throws IOException {
        scene = getSceneFromFile("source_folder//thor.obj");
        nameMap=scene.getNamedObjects();
        //Print elements of your model:
        printModelElementsList(nameMap);
        mjolnir = new TransformGroup();
        mjolnir.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        for(var entity: nameMap.entrySet()){
            scene.getSceneGroup().removeChild(nameMap.get(entity.getKey()));
            mjolnir.addChild(nameMap.get(entity.getKey()));
        }

        transform3D = new Transform3D();
        transform3D.setScale(new Vector3d(0.4, 0.4, 0.4));
        Transform3D transform3D2 = new Transform3D();
        transform3D2.rotX(-0.3*Math.PI);
        Transform3D rot2 = new Transform3D();
        rot2.rotX(Math.PI / 4);
        transform3D2.mul(rot2);

        mjolnir.removeAllChildren();
        TransformGroup tg = new TransformGroup();
        tg.setTransform(transform3D2);
        mjolnir.setTransform(transform3D);
        mjolnir.addChild(tg);
        for(var entity: nameMap.entrySet()){
            tg.addChild(nameMap.get(entity.getKey()));
        }
        return mjolnir;
    }


    private void setDuckElementsList() {

        nameMap=scene.getNamedObjects();
        //Print elements of your model:
        printModelElementsList(nameMap);

//        mjolnir = new TransformGroup();
//        transform3D = new Transform3D();
//        transform3D.setScale(0.5f);
//        mjolnir.setTransform(transform3D);
//
//        for(var entity: nameMap.entrySet()){
//            scene.getSceneGroup().removeChild(nameMap.get(entity.getKey()));
//            mjolnir.addChild(nameMap.get(entity.getKey()));
//        }
//        mjolnir.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        root.addChild(mjolnir);
    }

    Texture getTexture(String path) {
        TextureLoader textureLoader = new TextureLoader(path,"LUMINANCE",canvas);
        Texture texture = textureLoader.getTexture();
        return texture;
    }

    Material getMaterialForBox() {
        Material material = new Material();
        material.setAmbientColor ( new Color3f(new Color(0xB3727274, true)) );
        material.setDiffuseColor ( new Color3f(new Color(0xB3737376, true)) );
        material.setSpecularColor( new Color3f(new Color(0xC0000000, true)) );
        material.setShininess( 0.3f );
        material.setLightingEnable(true);
        return material;
    }

    Material getMaterialForStick() {
        Material material = new Material();
        material.setAmbientColor ( new Color3f(new Color(0xFE000000, true)) );
        material.setDiffuseColor ( new Color3f(new Color(0xFE000000, true)) );
        material.setSpecularColor( new Color3f(new Color(0xFE000000, true)) );
        material.setShininess( 0.3f );
        material.setLightingEnable(true);
        return material;
    }

    Material getMaterialForCircles() {
        Material material = new Material();
        material.setAmbientColor ( new Color3f(new Color(0xF3592500, true)) );
        material.setDiffuseColor ( new Color3f(new Color(0xF3592500, true)) );
        material.setSpecularColor( new Color3f(new Color(0xF3592500, true)) );
        material.setShininess( 0.3f );
        material.setLightingEnable(true);
        return material;
    }

    Material getMaterialForShapes() {
        Material material = new Material();
        material.setAmbientColor ( new Color3f(new Color(0xFFAF8E1B, true)) );
        material.setDiffuseColor ( new Color3f(new Color(0xFFAF8E1B, true)) );
        material.setSpecularColor( new Color3f(new Color(0xFFAF8E1B, true)) );
        material.setShininess( 0.3f );
        material.setLightingEnable(true);
        return material;
    }

    Material getMaterialForBelt() {
        Material material = new Material();
        material.setAmbientColor ( new Color3f(new Color(0xFF321807, true)) );
        material.setDiffuseColor ( new Color3f(new Color(0xFF321807, true)) );
        material.setSpecularColor( new Color3f(new Color(0xFF321807, true)) );
        material.setShininess( 0.3f );
        material.setLightingEnable(true);
        return material;
    }

    private Appearance getForBox(){
        Appearance appearance = new Appearance();
        appearance.setTexture(getTexture("source_folder//iron.jpg"));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(getMaterialForBox());
        return appearance;
    }

    private Appearance getForShapes(){
        Appearance appearance = new Appearance();
        appearance.setTexture(getTexture("source_folder//text.jpg"));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(getMaterialForShapes());
        return appearance;
    }

    private Appearance getForStick(){
        Appearance appearance = new Appearance();
        appearance.setTexture(getTexture("source_folder//black.jpg"));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(getMaterialForStick());
        return appearance;
    }

    private Appearance getForCircles(){
        Appearance appearance = new Appearance();
        appearance.setTexture(getTexture("source_folder//brown.jpg"));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(getMaterialForCircles());
        return appearance;
    }

    private Appearance getForBelt(){
        Appearance appearance = new Appearance();
        appearance.setTexture(getTexture("source_folder//dark_brown.jpg"));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(getMaterialForBelt());
        return appearance;
    }

    private void addAppearance(){

        Shape3D box = nameMap.get("box001");
        box.setAppearance(getForBox());

        for (var entity: nameMap.entrySet()){
            if(entity.getKey().contains("shape")){
                Shape3D shape = nameMap.get(entity.getKey());
                shape.setAppearance(getForShapes());
            }
            if(entity.getKey().contains("ag")){
                Shape3D shape = nameMap.get(entity.getKey());
                shape.setAppearance(getForCircles());
            }
            if(entity.getKey().contains("line")){
                Shape3D shape = nameMap.get(entity.getKey());
                shape.setAppearance(getForBelt());
            }
            if(entity.getKey().contains("cylinder")){
                Shape3D shape = nameMap.get(entity.getKey());
                shape.setAppearance(getForStick());
            }
            if(entity.getKey().contains("circle")){
                Shape3D shape = nameMap.get(entity.getKey());
                shape.setAppearance(getForStick());
            }
        }
    }


    private void addImageBackground(){
        TextureLoader t = new TextureLoader("source_folder//background.png", canvas);
        Background background = new Background(t.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
        background.setApplicationBounds(bounds);
        root.addChild(background);

    }

    private void ChangeViewAngle(){
        ViewingPlatform vp = universe.getViewingPlatform();
        TransformGroup vpGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTranslation = new Transform3D();
        Vector3f translationVector = new Vector3f(0F, 0F, 6F);
        vpTranslation.setTranslation(translationVector);
        vpGroup.setTransform(vpTranslation);
    }

    private void addOtherLight(){
        Color3f directionalLightColor = new Color3f(Color.BLACK);
        Color3f ambientLightColor = new Color3f(Color.WHITE);
        Vector3f lightDirection = new Vector3f(-1F, -1F, -1F);

        AmbientLight ambientLight = new AmbientLight(ambientLightColor);
        DirectionalLight directionalLight = new DirectionalLight(directionalLightColor, lightDirection);

        Bounds influenceRegion = new BoundingSphere();

        ambientLight.setInfluencingBounds(influenceRegion);
        directionalLight.setInfluencingBounds(influenceRegion);
        root.addChild(ambientLight);
        root.addChild(directionalLight);
    }

    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load(new FileReader(location));
    }


    public static void main(String[]args){
        try {
            Mjolnir window = new Mjolnir();
            AnimationMjolnir movement = new AnimationMjolnir(mjolnir, transform3D, window);
            window.setVisible(true);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
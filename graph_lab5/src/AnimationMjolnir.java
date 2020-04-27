import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.*;

public class AnimationMjolnir implements ActionListener, KeyListener {
    private Button go;
    private TransformGroup mjolnir;
    private Transform3D translateTransform;
    private Transform3D rotateTransformX;
    private Transform3D rotateTransformY;
    private Transform3D rotateTransformZ;

    private JFrame mainFrame;

    private float signx=1.0f;
    private float signy=1.0f;
    private float zoom=0.7f;
    private float xloc=0.0f;
    private float yloc=0.0f;
    private float zloc=0.0f;
    private Timer timer;

    public AnimationMjolnir(TransformGroup mjolnir, Transform3D trans, JFrame frame){
        go = new Button("Go");
        this.mjolnir =mjolnir;
        this.translateTransform=trans;
        this.mainFrame=frame;

        rotateTransformX= new Transform3D();
        rotateTransformY= new Transform3D();
        rotateTransformZ= new Transform3D();

        Mjolnir.canvas.addKeyListener(this);
        timer = new Timer(100, this);

        Panel p =new Panel();
        p.add(go);
        mainFrame.add("North",p);
        go.addActionListener(this);
        go.addKeyListener(this);
    }

    private void initialBoteState(){
        xloc=0.0f;
        yloc=0.0f;
        zloc=0.0f;
        zoom=0.7f;
        signx=1.0f;
        signy=1.0f;
        if(timer.isRunning()){timer.stop();}
        go.setLabel("Go");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // start timer when button is pressed
        if (e.getSource()==go){
            if (!timer.isRunning()) {
                timer.start();
                go.setLabel("Stop");
            }
            else {
                timer.stop();
                go.setLabel("Go");
            }
        }
        else {
            translateTransform.setScale(new Vector3d(zoom, zoom, zoom));
            translateTransform.setTranslation(new Vector3f(xloc,yloc,zloc));
            mjolnir.setTransform(translateTransform);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Invoked when a key has been typed.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar()=='1') {

            rotateTransformZ.rotZ(Math.PI/6);
            translateTransform.mul(rotateTransformZ);
        }
        if (e.getKeyChar()=='2') {

            rotateTransformY.rotY(Math.PI/2);
            translateTransform.mul(rotateTransformY);
        }
        if (e.getKeyChar()=='3') {
            rotateTransformX.rotX(Math.PI/2);
            translateTransform.mul(rotateTransformX);
        }
        if (e.getKeyChar()=='0'){
            rotateTransformY.rotY(Math.PI/2.8);
            translateTransform.mul(rotateTransformY);
        }

        if (e.getKeyChar()=='4'){
            xloc += 0.1 * signx;
            if (Math.abs(xloc *2) >= 2 ) {
                signx = -1.0f * signx;
                rotateTransformZ.rotZ(Math.PI);
                translateTransform.mul(rotateTransformZ);
            }
            translateTransform.setScale(new Vector3d(zoom, zoom, zoom));
            translateTransform.setTranslation(new Vector3f(xloc,yloc,zloc));
            mjolnir.setTransform(translateTransform);
        }

        if (e.getKeyChar()=='5'){
            yloc += 0.1 * signy;
            if (Math.abs(yloc *2) >= 2 ) {
                signy = -1.0f * signy;
                rotateTransformZ.rotZ(Math.PI);
                translateTransform.mul(rotateTransformZ);
            }
            translateTransform.setScale(new Vector3d(zoom, zoom, zoom));
            translateTransform.setTranslation(new Vector3f(xloc,yloc,zloc));
            mjolnir.setTransform(translateTransform);
        }

        if (e.getKeyChar()=='6'){
            zloc += 0.1;
            translateTransform.setScale(new Vector3d(zoom, zoom, zoom));
            translateTransform.setTranslation(new Vector3f(xloc,yloc,zloc));
            mjolnir.setTransform(translateTransform);
        }

        if (e.getKeyChar()=='7'){
            zloc -= 0.1;
            translateTransform.setScale(new Vector3d(zoom, zoom, zoom));
            translateTransform.setTranslation(new Vector3f(xloc,yloc,zloc));
            mjolnir.setTransform(translateTransform);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Invoked when a key has been released.
    }
}
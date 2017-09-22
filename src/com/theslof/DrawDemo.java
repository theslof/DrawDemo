package com.theslof;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class DrawDemo extends JPanel{
    //Konstanter
    public static final int FPS = 60;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    public static JFrame window;

    //Håll data i en Vector, vilket är en långsammare ArrayList. Den har dock inbyggt stöd för multitrådning.
    private Vector<Graphical> graphics;
    private Thread addThread;

    // --- Main method, körs först ---
    public static void main(String[] args) {
        //Skapa vårt GUI
        EventQueue.invokeLater(() -> {
            //Bygg vårt GUI
            window = new JFrame();
            window.setTitle("Draw Demo"); //Titel på fönstret
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            //Lägg till vårt objekt som ritar saker
            window.add(new DrawDemo());

            window.pack();
            window.setVisible(true);
        });
    }

    // --- Konstruktor ---
    public DrawDemo(){
        //Sätt default-data
        graphics = new Vector<>();

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.WHITE);

        //Lägg till grafiska objekt i en egen tråd. Denna tråden låser logik/grafik.
        addThread = new Thread(this::initializeObjects);
        addThread.start();

        //Logiktråden, ska hantera alla beräkningar
        new Thread(this::logicThread).start();

        //Renderingstråden, ritar ut vår grafik
        new Thread(this::updateGraphics).start();
    }

    private void initializeObjects() {
        //graphics.add(new Ball(variabler));
    }

    //Denna loopas konstant på sin egna tråd, sköter logiken
    private void logicThread() {
        //Initiera en klocka för att hålla reda på tiden mellan uppdateringarna
        long lastTime = System.currentTimeMillis();

        while(true){
            try {
                addThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long currentTime = System.currentTimeMillis();
            //Beräkna dt, vilket håller reda på hur mycket tid som har passerat relativt till en frame.
            double dt = (double)(currentTime - lastTime) / FPS;
            updateObjects(dt);
            lastTime = currentTime;
        }
    }

    //Uppdatera x/y för varje objekt och kolla kollisioner
    private void updateObjects(double dt) {
        for(Graphical g : graphics){
            if(g instanceof Ball){
                Ball b = (Ball) g;
                //Uppdatera vx/vy
                b.update(dt);

                //Kolla kollisioner och updatera vx/vy samt x/y vid behov
                //Börja med väggar, kollision mellan objekt är mycket svårare matematiskt
                //checkCollisions(b)

            }
        }
    }

    //Uppdatera grafiken. Denna ska köras 60 gånger per sekund, samma som vår FPS-konstant
    private void updateGraphics() {
        while(true){
            try {
                addThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint(); //Måla om vår instans av DrawDemo. Denna kallar på drawComponent() vilket vi måste överladda
            try {
                Thread.sleep(1000 / FPS); //Vänta i en frame, dvs. 1000 millisekunder delat med FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Denna kallas av repaint() och sköter uppritning av våra objekt
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        //Skapa ett nytt grafikobjekt vi kan rita på
        Graphics2D g2d = (Graphics2D) g.create();
        //Rita upp alla komponenter
        for(Graphical o: graphics){
            o.paint(g2d);
        }
        g2d.dispose();
    }
}

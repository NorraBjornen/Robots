package logic;

import gui.GameWindow;
import gui.LogWindow;
import gui.MainApplicationFrame;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;

public class StateSaver {
    private final String homePath = System.getProperty("user.home");

    public void save(LogWindow logWindow, GameWindow gameWindow){
        HashMap<String, Integer> state = new HashMap<>();

        state.put("log.width", logWindow.getWidth());
        state.put("log.height", logWindow.getHeight());
        state.put("log.x", logWindow.getX());
        state.put("log.y", logWindow.getY());

        int lVis = -1;
        if(logWindow.isDisplayable())
            lVis = 0;

        state.put("log.visible", lVis);



        state.put("mod.width", gameWindow.getWidth());
        state.put("mod.height", gameWindow.getHeight());
        state.put("mod.x", gameWindow.getX());
        state.put("mod.y", gameWindow.getY());


        int mVis = -1;
        if(gameWindow.isDisplayable())
            mVis = 0;

        state.put("mod.visible", mVis);
        saveState(state);
    }

    private void saveState(Serializable s){
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(homePath + "\\data");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(s);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in /tmp/employee.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public MinimizationParam restoreState(MainApplicationFrame frame, LogWindow logWindow, GameWindow gameWindow){
        HashMap<String, Integer> s = getSavedState();

        boolean lHide = false;
        boolean mHide = false;

        if(s != null){
            logWindow.setBounds(s.get("log.x"), s.get("log.y"), s.get("log.width"), s.get("log.height"));
            int lVis = s.get("log.visible");
            if(lVis == 0)
                logWindow.setVisible(true);
            else{
                lHide = true;
            }

            gameWindow.setBounds(s.get("mod.x"), s.get("mod.y"), s.get("mod.width"), s.get("mod.height"));
            int mVis = s.get("mod.visible");
            if(mVis == 0)
                gameWindow.setVisible(true);
            else
                mHide = true;

        } else
            gameWindow.setSize(400,  400);


        frame.addWindow(logWindow);
        frame.addWindow(gameWindow);

        if(lHide){
            try{
                logWindow.setIcon(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if(mHide){
            try{
                gameWindow.setIcon(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }


        return new MinimizationParam(lHide, mHide);
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Integer> getSavedState(){
        try(FileInputStream fileIn = new FileInputStream(homePath + "\\data");
                ObjectInputStream in = new ObjectInputStream(fileIn)){

            return (HashMap<String, Integer>) in.readObject();

        } catch (IOException | ClassNotFoundException i) {
            return null;
        }
    }
}

package logic;

import gui.GameWindow;
import gui.LogWindow;
import gui.MainApplicationFrame;
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

        int logVisible = -1;
        if(logWindow.isDisplayable())
            logVisible = 0;

        state.put("log.visible", logVisible);


        state.put("mod.width", gameWindow.getWidth());
        state.put("mod.height", gameWindow.getHeight());
        state.put("mod.x", gameWindow.getX());
        state.put("mod.y", gameWindow.getY());


        int modVisible = -1;
        if(gameWindow.isDisplayable())
            modVisible = 0;

        state.put("mod.visible", modVisible);
        saveState(state);
    }

    private void saveState(Serializable s){
        try {
            String path = homePath + "\\data";
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(s);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + path);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void restoreState(MainApplicationFrame frame, LogWindow logWindow, GameWindow gameWindow){
        HashMap<String, Integer> savedState = getSavedState();

        boolean isLogHidden = false;
        boolean isGameHidden = false;

        if(savedState != null){
            logWindow.setBounds(savedState.get("log.x"), savedState.get("log.y"), savedState.get("log.width"), savedState.get("log.height"));
            if(savedState.get("log.visible") == 0)
                logWindow.setVisible(true);
            else{
                isLogHidden = true;
            }

            gameWindow.setBounds(savedState.get("mod.x"), savedState.get("mod.y"), savedState.get("mod.width"), savedState.get("mod.height"));
            if(savedState.get("mod.visible") == 0)
                gameWindow.setVisible(true);
            else
                isGameHidden = true;

        } else
            gameWindow.setSize(400,  400);


        frame.addWindow(logWindow);
        frame.addWindow(gameWindow);

        if(isLogHidden){
            try{
                logWindow.setIcon(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if(isGameHidden){
            try{
                gameWindow.setIcon(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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

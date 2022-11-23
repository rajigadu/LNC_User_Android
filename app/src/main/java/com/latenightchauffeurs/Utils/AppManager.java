package com.latenightchauffeurs.Utils;

public class AppManager {
    private static AppManager ourInstance = null;
    private IonAppListners ionAppListners;

    public static AppManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new AppManager();
        }
        return ourInstance;
    }

    public IonAppListners getIonAppListners() {
        return ionAppListners;
    }

    public void setIonAppListners(IonAppListners ionAppListners) {
        this.ionAppListners = ionAppListners;
    }
}

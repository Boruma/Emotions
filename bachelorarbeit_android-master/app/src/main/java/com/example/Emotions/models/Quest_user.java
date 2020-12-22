package com.example.Emotions.models;

public class Quest_user {

           private  int user_id;
           private  int quest_id;
           private  String progress;

           private Quest questobj;



    public Quest_user() {
    }

    public Quest_user(int user_id, int quest_id, String progress) {
        this.user_id = user_id;
        this.quest_id = quest_id;
        this.progress = progress;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuest_id() {
        return quest_id;
    }

    public void setQuest_id(int quest_id) {
        this.quest_id = quest_id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
    public Quest getQuestobj() {
        return questobj;
    }

    public void setQuestobj(Quest questobj) {
        this.questobj = questobj;
    }

    @Override
    public String toString() {
        return "Quest_user{" +
                "user_id=" + user_id +
                ", quest_id=" + quest_id +
                ", progress='" + progress + '\'' +
                ", questobj=" + questobj.toString() +
                '}';
    }
}

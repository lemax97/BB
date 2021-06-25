package com.mygdx.rpgame.quest;

import com.badlogic.gdx.utils.ObjectMap;

public class QuestTask {

    public static enum QuestType{
        FETCH,
        KILL,
        DELIVERY,
        GUARD,
        ESCORT,
        RETURN,
        DISCOVER
    }

    public static enum QuestTaskPropertyType{
        IS_TASK_COMPLETE,
        TARGET_TYPE,
        TARGET_NUM,
        TARGET_LOCATION,
        NONE
    }

    private ObjectMap<String, Object> taskProperties;
    private String id;
    private String taskPhrase;
    private QuestType questType;

    public QuestTask(){
        taskProperties = new ObjectMap<String, Object>();
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTaskPhrase(){
        return taskPhrase;
    }

    public void setTaskPhrase(String taskPhrase) {
        this.taskPhrase = taskPhrase;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public ObjectMap<String, Object> getTaskProperties() {
        return taskProperties;
    }

    public void setTaskProperties(ObjectMap<String, Object> taskProperties) {
        this.taskProperties = taskProperties;
    }

    public boolean isTaskComplete(){

    }
}

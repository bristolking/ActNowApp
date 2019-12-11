package com.actnow.android.sdk.responses;

public  class AdavancedTaskRecords {
    private String task_id;
    private String name;
    private String task_code;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask_code() {
        return task_code;
    }

    public void setTask_code(String task_code) {
        this.task_code = task_code;
    }

    @Override
    public String toString() {
        return "AdavancedTaskRecords{" +
                "task_id='" + task_id + '\'' +
                ", name='" + name + '\'' +
                ", task_code='" + task_code + '\'' +
                '}';
    }
}

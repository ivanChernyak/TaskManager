package com.taskmanager.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class can create tasks.
 * Tasks can be active and inactive.
 * Also tasks can be repeated at intervals.
 */
public class Task implements Cloneable, Serializable {
    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;
    private boolean isActive;

    /**
     * This constructor creates an inactive, not repeatable task
     *
     * @param title task title
     * @param time  must be greater than zero
     */
    public Task(String title, Date time) {
        this.title = title;
        this.time = time;
        this.isActive = false;
        this.interval = 0;
    }

    /**
     * This constructor creates an inactive, repeatable task
     *
     * @param title    task title
     * @param start    must be greater than zero
     * @param end      must be greater than zero
     * @param interval must be greater than zero
     */
    public Task(String title, Date start, Date end, int interval) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.isActive = false;
    }

    /**
     * This method returns the name of the task
     *
     * @return task title value
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method sets the value of the task name
     *
     * @param title task title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method checks the activity of the task
     *
     * @return true if task is active, false if task is non active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * This method sets activity of the task
     *
     * @param active must be true or false
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * This method returns task time
     *
     * @return time if task is not repeatable, start if task is repeatable
     */
    public Date getTime() {
        if (this.isRepeated()) {
            return start;
        } else
            return time;
    }

    /**
     * This method sets the time
     * If the task was repeatable, makes it non-repeatable
     *
     * @param time must be greater than zero
     */
    public void setTime(Date time) {
        if (this.isRepeated()) {
            this.setTime(null, null, 0);
        }
        this.time = time;
    }

    /**
     * This method returns start
     *
     * @return start if task is repeatable, time if task is not repeatable
     */
    public Date getStartTime() {
        if (this.isRepeated()) {
            return start;
        } else
            return time;
    }

    /**
     * This method returns end
     *
     * @return end if task is repeatable, time if task is not repeatable
     */
    public Date getEndTime() {
        if (this.isRepeated()) {
            return end;
        } else
            return time;
    }

    /**
     * This method returns interval
     *
     * @return interval if task is repeatable, 0 if task is not repeatable
     */
    public int getRepeatInterval() {
        if (this.isRepeated()) {
            return interval;
        } else
            return 0;
    }

    /**
     * This method makes the task repeatable
     *
     * @param start    must be greater than zero
     * @param end      must be greater than zero
     * @param interval must be greater than zero
     */
    public void setTime(Date start, Date end, int interval) {
        this.start = start;
        this.end = end;
        this.interval = interval;
    }

    /**
     * This method checks the repeatability of the task
     *
     * @return true if task is repeatable, false if task is non repeatable
     */
    public boolean isRepeated() {
        return (interval != 0) ? true : false;
    }

    /**
     * This method returns the time of the next task execution.
     *
     * @param current time to test for repeatability, must be greater than zero
     * @return -1 if task is non active;
     * -1 if task is not repeatable and current < time;
     * in other cases returns time of the next task execution
     */
    public Date nextTimeAfter(Date current) {
        if (!this.isActive) {
            return null;
        } else {
            if (!this.isRepeated()) {
                if (current.compareTo(this.time) < 0) {
                    return this.time;
                } else {
                    return null;
                }
            } else {
                if (current.compareTo(this.end) >= 0) {
                    return null;
                } else {
                    if (current.compareTo(this.start) < 0) {
                        return this.start;
                    } else {
                        long nextTime = start.getTime();
                        while (nextTime <= current.getTime()) {
                            nextTime += interval * 1000;
                        }
                        return new Date(nextTime);
                    }
                }
            }
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param other another object to compare.
     * @return true if other object is "equal to" this one,
     * false if other object is "not equal to" this one.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Task task = (Task) other;
        if (interval != task.interval) {
            return false;
        }
        if (isActive != task.isActive) {
            return false;
        }
        if (title != null ? !title.equals(task.title) : task.title != null) {
            return false;
        }
        if (time != null ? !time.equals(task.time) : task.time != null) {
            return false;
        }
        if (start != null ? !start.equals(task.start) : task.start != null) {
            return false;
        }
        return end != null ? end.equals(task.end) : task.end == null;
    }

    /**
     * Returns a hash code value for the Task.
     */
    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + interval;
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    /**
     * Returns a string representation of the Task.
     */
    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", start=" + start +
                ", end=" + end +
                ", interval=" + interval +
                ", isActive=" + isActive +
                '}';
    }

    /**
     * Creates and returns a copy of Task object.
     *
     * @return a copy of Task object.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the Cloneable interface.
     */
    @Override
    public Task clone() throws CloneNotSupportedException {
        Task cloned = (Task) super.clone();
        cloned.time = (Date) time.clone();
        if (this.isRepeated()) {
            cloned.start = (Date) start.clone();
            cloned.end = (Date) end.clone();
        }
        return cloned;
    }
}

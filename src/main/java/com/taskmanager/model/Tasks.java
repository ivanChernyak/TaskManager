package com.taskmanager.model;

import java.util.*;

public class Tasks {

    /**
     * This method returns a subset of tasks that are
     * scheduled to execute in the interval
     *
     * @param start left border of interval
     * @param end   right border of interval
     * @return a subset of tasks that are scheduled to execute in the interval
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks,
                                          Date start, Date end) {
        Set<Task> incoming = new HashSet<>();
        Iterator iterator = tasks.iterator();
        Task current;

        while (iterator.hasNext()) {
            current = (Task) iterator.next();
            if (current.nextTimeAfter(start) != null) {
                if (current.nextTimeAfter(start).compareTo(end) <= 0
                        && current.nextTimeAfter(start).compareTo(start) > 0) {
                    incoming.add(current);
                }
            }
        }
        return incoming;
    }

    /**
     * This method builds a task calendar for a given period - a table
     * where each date corresponds to a set of tasks.
     *
     * @param tasks the tasks on which the calendar is built.
     * @param start left border of period.
     * @param end   right border of period.
     * @return a task calendar for a given period.
     */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks,
                                                      Date start, Date end) {
        SortedMap<Date, Set<Task>> calendar = new TreeMap<>();
        Date currentDate;
        Task currentTask;
        Iterable<Task> incoming = Tasks.incoming(tasks, start, end);
        Iterator taskIterator = incoming.iterator();
        while (taskIterator.hasNext()) {
            currentTask = (Task) taskIterator.next();
            if (currentTask.isRepeated()) {
                long currentTime = currentTask.getStartTime().getTime();
                while (currentTime <= end.getTime()) {
                    currentDate = new Date(currentTime);
                    if (currentDate.compareTo(start) >= 0) {
                        updateSetInMap(calendar, currentDate, currentTask);
                    }
                    currentTime += currentTask.getRepeatInterval() * 1000;
                }
            } else {
                currentDate = currentTask.getTime();
                updateSetInMap(calendar, currentDate, currentTask);
            }
        }
        return calendar;
    }

    /**
     * This is helper method for the calendar().
     * This method updates the set of tasks in the calendar by key.
     * If the calendar contains a key, then add a new task to the set of tasks.
     * Otherwise, create a new set of tasks with this key.
     *
     * @param calendar calendar - Map where key is Date, value is set of tasks.
     * @param dateKey  the key for which we are looking for a set of tasks.
     * @param tempTask the task to be added.
     */
    private static void updateSetInMap(SortedMap<Date, Set<Task>> calendar,
                                       Date dateKey, Task tempTask) {
        Set<Task> tempSet;
        if (calendar.containsKey(dateKey)) {
            tempSet = calendar.get(dateKey);
            tempSet.add(tempTask);
            calendar.put(dateKey, tempSet);
        } else {
            tempSet = new HashSet<>();
            tempSet.add(tempTask);
            calendar.put(dateKey, tempSet);
        }
    }
}

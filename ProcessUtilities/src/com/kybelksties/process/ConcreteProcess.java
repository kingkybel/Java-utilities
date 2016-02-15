
/*
 * Copyright (C) 2015 Dieter J Kybelksties
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @date: 2015-12-14
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import com.kybelksties.general.EnvironmentVar;
import com.kybelksties.general.EnvironmentVarSets;
import com.kybelksties.general.EnvironmentVarTableModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Wrapper around Process that encapsulates process state, environment variables
 * and others to create repeatable processes with repeatable configuration.
 *
 * @author kybelksd
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ConcreteProcess extends Process
{

    private static final String CLASS_NAME = ConcreteProcess.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private String environmentVarFile;

    /**
     * List of listeners to state event changes.
     */
    protected EventListenerList stateChangeListenerList =
                                new EventListenerList();

    transient private Process process = null;
    transient private final ProcessBuilder builder;

    transient State state = null;
    private String[] command = null;
    private EnvironmentVarSets environmentVarSets = new EnvironmentVarSets();

    /**
     * Default Construct.
     */
    public ConcreteProcess()
    {
        builder = new ProcessBuilder();
        setState(State.NotStarted);

    }

    /**
     * Construct.
     *
     * @param environmentVarSets set of environment variables
     */
    public ConcreteProcess(EnvironmentVarSets environmentVarSets)
    {
        this();
        cloneEnvironment(environmentVarSets);
    }

    /**
     * Construct.
     *
     * @param command            command-line as string
     * @param environmentVarSets set of environment variables
     */
    public ConcreteProcess(String command, EnvironmentVarSets environmentVarSets)
    {
        this(environmentVarSets);
        this.command = new String[1];
        this.command[0] = command;
        builder.command(command);
        cloneEnvironment(environmentVarSets);
    }

    /**
     * Construct.
     *
     * @param command            command-line as string array
     * @param environmentVarSets set of environment variables
     */
    public ConcreteProcess(String[] command,
                           EnvironmentVarSets environmentVarSets)
    {
        this(environmentVarSets);
        this.command = command;
        builder.command(command);
        cloneEnvironment(environmentVarSets);
    }

    /**
     * Add a listener to receive update messages.
     *
     * @param listener the object to receive update messages
     */
    public void addStateChangeEventListener(StateEventListener listener)
    {
        stateChangeListenerList.add(StateEventListener.class, listener);
    }

    void fireProcessStateEvent()
    {
        fireProcessStateEvent(new StateEvent(this));
    }

    void fireProcessStateEvent(StateEvent evt)
    {
        Object[] listeners = stateChangeListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2)
        {
            // stateChangeListenerList is a list of Class-objects alternating
            // with objects of that class
            if (listeners[i] == StateEventListener.class)
            {
                ((StateEventListener) listeners[i + 1]).
                        processStateChanged(evt);
            }
        }
    }

    @Override
    public String toString()
    {
        return state.toString();
    }

    /**
     * Set the state of the process.
     *
     * @param state the new state of the process
     */
    public final void setState(State state)
    {
        LOGGER.log(Level.FINE, "Change state from ''{0}'' to ''{1}''",
                   new Object[]
                   {
                       this.state == null ? "<null>" : this.state.toString(),
                       state
                   });
        this.state = state;
        fireProcessStateEvent();
    }

    /**
     * Initialize the environment using a flat file.
     *
     * @param fileName csv file that contains the environment
     * @throws IOException
     */
    public void initEnvironment(String fileName) throws IOException
    {
        environmentVarFile = fileName;
        environmentVarSets.initialiseVarsFromFile(fileName);
        clearEnvironment();
        for (String category : environmentVarSets.getCategoryNameSet())
        {
            updateEnvironment(category);
        }
    }

    /**
     * Initialize the environment.
     *
     * @throws IOException
     */
    public void initEnvironment() throws IOException
    {
        if (environmentVarFile != null && !environmentVarFile.isEmpty())
        {
            initEnvironment(environmentVarFile);
        }
    }

    /**
     * Clear all environment variables from the process's environment.
     */
    public void clearEnvironment()
    {
        builder.environment().clear();
    }

    /**
     * Clone the given environment into this process.
     *
     * @param environmentVarSets the environment to clone
     */
    public final void cloneEnvironment(EnvironmentVarSets environmentVarSets)
    {
        if (environmentVarSets == null)
        {
            return;
        }
        this.environmentVarSets = new EnvironmentVarSets(environmentVarSets);
        updateEnvironments();
    }

    /**
     * Get all defined variables as CategorisedEnvironment-object.
     *
     * @return the categorized environment object
     */
    public EnvironmentVarSets getCategorisedEnvironment()
    {
        return environmentVarSets;
    }

    /**
     * Get the defined environment variables as a key-value map.
     *
     * @return a string-to-string map as process builder maintains
     */
    public Map<String, String> getEnvironmentMap()
    {
        return builder.environment();
    }

    /**
     * Retrieve the command-line of the process as String array.
     *
     * @return string array defining the command
     */
    public String[] command()
    {
        return this.command;
    }

    /**
     * Set the command-line of the process from a String.
     *
     * @param command a single string that might contain spaces
     */
    public void command(String command)
    {
        this.command = command.split(" ");
        builder.command(this.command);
    }

    /**
     * Set the command-line of the process from a String array.
     *
     * @param command string array; individual strings might contain spaces
     */
    public void command(String[] command)
    {
        this.command = command;
        builder.command(command);
    }

    /**
     * Get a model to enable display in a table component.
     *
     * @param category
     * @return the table model for the category
     */
    public TableModel getTableModel(String category)
    {
        return environmentVarSets.getTableModel(category);
    }

    /**
     * Define all environment variables in a category.
     *
     * @param category
     */
    public void defineAll(String category)
    {
        environmentVarSets.getTableModel(category).defineAll();
    }

    /**
     * Undefine all environment variables in a category.
     *
     * @param category the category for which all values will be reset
     */
    public void undefineAll(String category)
    {
        environmentVarSets.getTableModel(category).undefineAll();
    }

    /**
     * Switch all boolean values in a category of environment values on or off.
     *
     * @param category the category for which the booleans will be set
     * @param on       true means that all booleans will be true, false mens
     *                 they will be set to false
     */
    public void setAllBooleans(String category, Boolean on)
    {
        environmentVarSets.getTableModel(category).setBooleans(on);
    }

    /**
     * Copy environment settings from another process.
     *
     * @param rhs the right-hand-side concrete process
     */
    public void copyEnvironmentFrom(ConcreteProcess rhs)
    {
        getEnvironmentMap().clear();
        cloneEnvironment(rhs.environmentVarSets);
    }

    /**
     * Updates the environment variables maintained in the category.
     *
     * @param category the category for which the values will be updated
     */
    public void updateEnvironment(String category)
    {
        EnvironmentVarTableModel model =
                                 environmentVarSets.getTableModel(category);
        for (int row = 0; row < model.getRowCount(); row++)
        {
            EnvironmentVar entry = model.get(row);
            if (entry.getDefined())
            {
                getEnvironmentMap().put(entry.getName(),
                                        entry.getValue().toString());
            }
            else
            {
                getEnvironmentMap().remove(entry.getName());
            }
        }
    }

    private void updateEnvironments()
    {
        getEnvironmentMap().clear();
        for (String category : environmentVarSets.getCategoryNameSet())
        {
            updateEnvironment(category);
        }
    }

    /**
     * Start the process.
     *
     * @return this process after attempted start.
     */
    public ConcreteProcess start()
    {
        updateEnvironments();
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    builder.directory(directory());
                    process = builder.start();
                    setState(State.Running);
                    waitFor();
                    if (state == State.Running)
                    {
                        exitValue();
                    }
                }
                catch (IOException | InterruptedException ex)
                {
                    setState(State.StartFailed);
                }
            }
        });
        thread.start();
        return this;
    }

    @Override
    public OutputStream getOutputStream()
    {
        return process != null ? process.getOutputStream() : null;
    }

    @Override
    public InputStream getInputStream()
    {
        return process != null ? process.getInputStream() : null;
    }

    @Override
    public InputStream getErrorStream()
    {
        return process != null ? process.getErrorStream() : null;
    }

    @Override
    public int waitFor() throws InterruptedException
    {
        return process != null ? process.waitFor() : 0;
    }

    @Override
    public int exitValue()
    {
        int reval = process.exitValue();
        setState(reval == 0 ? State.Finished : State.Terminated);
        return reval;
    }

    @Override
    public void destroy()
    {
        process.destroy();
        state = State.Terminated;
    }

    /**
     * Check the state if the process is running.
     *
     * @return true if and only if the process is running.
     */
    public boolean isRunning()
    {
        return State.Running.equals(state);
    }

    /**
     * Retrieve the working directory.
     *
     * @return current the working directory as File object.
     */
    public File directory()
    {
        File runDir = builder.directory();
        if (runDir == null && command != null && command.length > 0)
        {
            String exeDir = command[0];
            int lastSlashPos = exeDir.lastIndexOf('/');
            if (lastSlashPos != -1)
            {
                runDir = new File(exeDir.substring(0, lastSlashPos));
            }
        }
        if (runDir == null)
        {

            runDir = new File(".");
        }

        return runDir;
    }

    /**
     * Set the working directory.
     *
     * @param dir a string description of the directory
     * @return the modified ProcessBuilder with the new working directory
     */
    public ProcessBuilder directory(String dir)
    {
        if (dir == null)
        {
            dir = ".";
        }
        return builder.directory(new File(dir));
    }

    /**
     * Set the working directory.
     *
     * @param dir the new working directory
     * @return the modified ProcessBuilder with the new working directory
     */
    public ProcessBuilder directory(File dir)
    {
        if (dir == null)
        {
            dir = new File(".");
        }
        return builder.directory(dir);
    }

    /**
     * An EventObject extension that triggers changes in the running state of
     * the process.
     */
    public static class StateEvent extends EventObject
    {

        /**
         * Construct with the event source.
         *
         * @param source the event source object
         */
        public StateEvent(Object source)
        {
            super(source);
        }
    }

    /**
     * Interface that listeners to State-events have to implement.
     */
    public interface StateEventListener extends EventListener
    {

        /**
         * The implementation of this method allows the listener to handle the
         * event.
         *
         * @param evt the triggering event
         */
        public void processStateChanged(StateEvent evt);
    }

    /**
     * Process states.
     */
    @XmlType
    @XmlEnum(value = String.class)
    public enum State
    {

        /**
         * Process has not yet been started.
         */
        NotStarted,
        /**
         * Process has been started.
         */
        Running,
        /**
         * Start of the process has failed.
         */
        StartFailed,
        /**
         * Process has was terminated or finished with code != 0.
         */
        Terminated,
        /**
         * Process has finished.
         */
        Finished
    }
}

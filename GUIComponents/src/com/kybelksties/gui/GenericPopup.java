
/*
 * Copyright (C) 2015 Dieter J Kybelksties
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @date: 2015-12-16
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * A Pop-up menu that takes care of some of the boring plumbing. Menus can be
 * created by collections of string/action-object pairs.
 *
 * @author kybelksd
 */
public class GenericPopup extends JPopupMenu
{

    private static final String CLASS_NAME = GenericPopup.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    GenericActionListener actionListener = new GenericActionListener();

    /**
     * Default constructor.
     */
    public GenericPopup()
    {
    }

    /**
     * Construct with one initial item/action.
     *
     * @param menuItem
     * @param action
     */
    public GenericPopup(String menuItem, Action action)
    {
        actionListener = new GenericActionListener(menuItem, action);
    }

    /**
     * Construct with a map of item/action - pairs.
     *
     * @param map
     */
    public GenericPopup(Map<? extends String, ? extends Action> map)
    {
        actionListener = new GenericActionListener(map);
    }

    /**
     * Generic way to pass back information to the action of a menu item.
     *
     * @param object
     */
    public void updateMenu(Object... object)
    {
        for (String menuItemName : actionListener.actions.keySet())
        {
            Action action = actionListener.actions.get(menuItemName);
            action.updateActionParameters(object);
        }
    }

    /**
     * Append a menu item/action pair.
     *
     * @param menuItemName
     * @param action
     */
    public final void add(String menuItemName, Action action)
    {
        actionListener.add(menuItemName, action);
        JMenuItem menuItem = new JMenuItem(menuItemName);
        menuItem.addActionListener(actionListener);
        super.add(menuItem);
    }

    /**
     * Append all menu item/action pairs from a map.
     *
     * @param map
     */
    public final void addAll(Map<? extends String, ? extends Action> map)
    {
        actionListener.addAll(map);
        for (String menuItemName : map.keySet())
        {
            JMenuItem menuItem = new JMenuItem(menuItemName);
            menuItem.addActionListener(actionListener);
            super.add(menuItem);
        }
    }

    /**
     * Retrieve the action handler of an item.
     *
     * @param menuItemName
     * @return the specific handler associated with the menu item
     */
    public Action getActionHandler(String menuItemName)
    {
        return actionListener.actions.get(menuItemName);
    }

    /**
     * Remove all action listeners and menu items.
     */
    public void clear()
    {
        actionListener.clear();
        super.removeAll();
    }

    /**
     * Remove one action listeners and menu item.
     *
     * @param menuItem
     */
    public void remove(String menuItem)
    {
        actionListener.remove(menuItem);
        for (Component c : super.getComponents())
        {
            if (c.getName().equals(menuItem))
            {
                super.remove(c);
            }
        }
    }

    /**
     * Polymorphous actions.
     */
    public interface Action
    {

        /**
         * Execute the action.
         */
        public void execute();

        /**
         * This way parameters can be passed back from the Action listener to
         * the Action class.
         *
         * @param object variant list of parameters for the execution. It's the
         *               responsibility of the overriding class to vet and
         *               interpret the objects.
         *
         */
        public void updateActionParameters(Object... object);
    }

    /**
     * Generic action listener for the use with GenericPopup-objects.
     */
    public class GenericActionListener implements ActionListener
    {

        HashMap<String, Action> actions = new HashMap<>();

        /**
         * Default constructor.
         */
        public GenericActionListener()
        {
        }

        /**
         * Constructs with one item/action pair.
         *
         * @param menuItem
         * @param action
         */
        public GenericActionListener(String menuItem, Action action)
        {
            add(menuItem, action);
        }

        /**
         * Constructs with a map of item/action pairs.
         *
         * @param map
         */
        public GenericActionListener(Map<? extends String, ? extends Action> map)
        {
            addAll(map);
        }

        /**
         * Append an item/action pair.
         *
         * @param menuItemName
         * @param action
         */
        public final void add(String menuItemName, Action action)
        {
            actions.put(menuItemName, action);
        }

        /**
         * Append all item/action pairs in a map.
         *
         * @param map
         */
        public final void addAll(Map<? extends String, ? extends Action> map)
        {
            actions.putAll(map);
        }

        /**
         * Remove all actions from the action map.
         */
        public void clear()
        {
            actions.clear();
        }

        /**
         * Remove one action from the action map.
         *
         * @param menuItem
         */
        public void remove(String menuItem)
        {
            actions.remove(menuItem);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Action action = actions.get(e.getActionCommand());
            if (action != null)
            {
                action.execute();
            }
            else
            {
                throw new UnsupportedOperationException(CLASS_NAME +
                                                        ": " +
                                                        e.getActionCommand() +
                                                        " has no action attached.");
            }
        }
    }

}

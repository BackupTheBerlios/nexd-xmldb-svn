/*
 * Project: xmldb-manager 
 * Copyright (C) 2005  Manuel Pichler <manuel.pichler@xplib.de>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * $Log: ResourceTableModel.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.resource;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.QueryResultObject;
import de.xplib.xdbm.ui.model.QueryResultSetObject;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.ResourceObjectFactory;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VirtualCollectionObject;
import de.xplib.xdbm.ui.model.XapiCollectionObject;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ResourceTableModel 
    extends AbstractTableModel
    implements I18NObserver, UIObjectObserver {
    
    private static final String[] COLUMN_NAME_KEYS = {
            "resource.column.id",
            "resource.column.type",
            "resource.column.content"
    };
    
    protected ArrayList rows = new ArrayList();
        
    private String[] columnNames = {" ", "", "", ""};
    
    private ResourceObjectFactory factory = new ResourceObjectFactory();

    /**
     * 
     */
    public ResourceTableModel() {
        super();
        
        I18N i18n = I18N.getInstance();
        i18n.addObserver(this);
        this.updateI18N(i18n);
        
        Application.getInstance().addObserver((UIObjectObserver) this);
    }
    
    

    /**
     * <Some description here>
     * 
     * @return
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 4;
    }
    
    /**
     * <Some description here>
     * 
     * @param columnName
     * @return
     * @see javax.swing.table.AbstractTableModel#findColumn(java.lang.String)
     */
    public int findColumn(String columnName) {
        for (int i = 0; i < 4; i++) {
            if (columnName.equals(this.columnNames[i])) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * <Some description here>
     * 
     * @param columnIndex
     * @return
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    /**
     * <Some description here>
     * 
     * @param column
     * @return
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        return this.columnNames[column];
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return this.rows.size();
    }
    
    /**
     * <Some description here>
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        if (rowIndex >= this.rows.size()) {
            return null;
        }
        
        ResourceObject row = (ResourceObject) this.rows.get(rowIndex);
        if (columnIndex == 1) {
            return row.getId();
        } else if (columnIndex == 2) {
            return row.getType();
        } else if (columnIndex == 3) {
            return row.getContent();
        }
        return null;
    }
    
    public ResourceObject getRow(final int rowIndexIn) {
        if (this.rows.size() <= rowIndexIn) {
            return null;
        }
        ResourceObject ro = (ResourceObject) this.rows.get(rowIndexIn);
        if (ro instanceof EmptyResourceObject) {
            return null;
        }
        return ro;
    }
    
    /**
     * @param rowIn
     */
    public void addRow(final ResourceObject rowIn) {
        this.rows.add(rowIn);
    }
    
    
    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(final I18N i18nIn) {
        for (int i = 0; i < 3; i++) {
            this.columnNames[i + 1] = i18nIn.getTitle(COLUMN_NAME_KEYS[i]);
        }
    }
    
    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(
     *      de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
       
        if (objectIn instanceof QueryResultSetObject) {

            this.rows.clear();
            
            QueryResultObject[] objects = ((QueryResultSetObject) objectIn)
                                                  .getResultObjects();
            
            for (int i = 0; i < objects.length; i++) {
                this.rows.add(objects[i]);
            }
            
            this.fireTableDataChanged();
        } else if (objectIn instanceof XapiCollectionObject 
                || objectIn instanceof VirtualCollectionObject) {
            
            this.rows.clear();
            
            Collection coll = (Collection) objectIn.getUserObject();
            
            try {
                String[] names = coll.listResources();
                for (int i = 0; i < names.length; i++) {
                    ResourceObject row = this.factory.createResourceRow(
                            coll, coll.getResource(names[i]));
                    
                    this.rows.add(row);
                }
            } catch (XMLDBException e) {
                e.printStackTrace(Application.err);
            }
            
            this.fireTableDataChanged();
        } if (objectIn instanceof ResourceObject) {
            if (objectIn.isNew()) {
            
                Object o = null;
                Iterator it = this.rows.iterator();
                while (it.hasNext()) {
                    o = it.next();
                    if (o instanceof EmptyResourceObject) {
                        break;
                    } else {
                        o = null;
                    }
                }
                
                if (o != null) {
                    this.rows.add(this.rows.indexOf(o), objectIn);
                    this.rows.remove(o);
                } else {
                    this.rows.add(objectIn);
                }
                
                this.fireTableDataChanged();
            } else if (objectIn.isDeleted()) {
                
                Object o    = objectIn.getUserObject();
                Iterator it = this.rows.iterator();
                while (it.hasNext()) {
                    UIObject uio = (UIObject) it.next();
                    if (uio.getUserObject() == o) {
                        this.rows.remove(uio);
                        this.fireTableDataChanged();
                        break;
                    }
                }
            }
        }
    }
}

package com.example.tele.openmapshapefile;

// Note:
// This is my modified version for educational purpose by Kiichi Takeuchi. I removed swing dependency and changed class name.
//

// **********************************************************************
//
// <copyright>
//
//  BBN Technologies, a Verizon Company
//  10 Moulton Street
//  Cambridge, MA 02138
//  (617) 873-8000
//
//  Copyright (C) BBNT Solutions LLC. All rights reserved.
//
// </copyright>
// **********************************************************************
//
// $Source: /cvs/distapps/openmap/com/bbn/openmap/dataAccess/shape/input/DbfInputStream.java,v $
// $RCSfile: DbfInputStream.java,v $
// $Revision: 1.1.2.1 $
// $Date: 2002/10/25 17:43:42 $
// $Author: dietrick $
//
// **********************************************************************


import com.bbn.openmap.dataAccess.shape.input.LittleEndianInputStream;

//package com.bbn.openmap.dataAccess.shape.input;
import com.bbn.openmap.dataAccess.shape.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Reads the contents of a DBF file and provides access to what it has read
 * through several get methods
 * @author Doug Van Auken
 */
public class DbfInputStream2 {
    /** An input stream to process primitives in Little Endian or Big Endian */
    private LittleEndianInputStream _leis = null;

    /** An array of column names, as read from the field descripter array */
    private String[] _columnNames = null;

    /** An array of column lengths, as read from the field descripter array */
    private byte[] _lengths = null;

    /** An array of decimal counts, as read from the field descripter array */
    private byte[] _decimalCounts = null;

    /** An array of column types, as read from the field descripter array */
    private byte[] _types = null;

    /** The number of columns */
    private int _columnCount = -1;

    /** The number of rows */
    private int _rowCount = -1;

    /** The header length */
    private short _headerLength = -1;

    /** The record length */
    private short _recordLength = -1;

    /** An ArrayList with each element representing a record, which itself is an ArrayList */
    private ArrayList _records = null;

    /**
     * Creates a LittleEndianInputStream then uses it to read the contents
     * of the DBF file
     * @param is An inputstream used to create a LittleEndianInputStream
     */
    public DbfInputStream2(InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        _leis = new LittleEndianInputStream(bis);
        readHeader();
        readFieldDescripters();
        readData();
    }

    /**
     * Returns an array of column names
     * @return An array of column names
     */
    public String[] getColumnNames(){
        return _columnNames;
    }

    /**
     * Returns an array of character lengths
     * @return An array of character lengths
     */
    public byte[] getLengths(){
        return _lengths;
    }

    /**
     * Returns an array of decimal counts
     * @return An array of decimal counts
     */
    public byte[] getDecimalCounts(){
        return _decimalCounts;
    }

    /**
     * Returns an array of field types
     * @return An array of field types
     */
    public byte[] getTypes() {
        return _types;
    }

    /**
     * Returns an ArrayList of records
     * @return An ArrayList of recrods
     */
    public ArrayList getRecords(){
        return _records;
    }

    /**
     * Returns the number of columns
     * @return The nunber of columns
     */
    public int getColumnCount(){
        return _columnCount;
    }

    /**
     * Returns the number of rows
     * @return The number of rows
     */
    public int getRowCount(){
        return _rowCount;

    }

    /**
     * Reads the header
     */
    private void readHeader() throws IOException {
        byte description = _leis.readByte();
        byte year = _leis.readByte();
        byte month = _leis.readByte();
        byte day = _leis.readByte();
        _rowCount = _leis.readLEInt();
        _headerLength = _leis.readLEShort();
        _recordLength = _leis.readLEShort();
        _columnCount = (_headerLength - 32 - 1) / 32;
        _leis.skipBytes(20);
    }

    /**
     * Initializes arrays that hold column names, column types, character
     * lengths, and decimal counts, then populates them
     */
    private void readFieldDescripters() throws IOException {
        _columnNames = new String[_columnCount];
        _types = new byte[_columnCount];
        _lengths = new byte[_columnCount];
        _decimalCounts = new byte[_columnCount];

        for (int n=0; n<=_columnCount-1; n++) {
            _columnNames[n] = _leis.readString(11);
            _types[n] = (byte)_leis.readByte();
            _leis.skipBytes(4);
            _lengths[n] = _leis.readByte();
            _decimalCounts[n] = _leis.readByte();
            _leis.skipBytes(14);
        }
    }

    /**
     * Reads the data and places data in a class scope ArrayList of records
     */
    public void readData() throws IOException {
        _leis.skipBytes(2);
        _records = new ArrayList();
        for (int r=0; r <= _rowCount - 1; r++) {
            ArrayList record = new ArrayList();
            for (int c = 0; c <= _columnCount - 1; c++) {
                int length = _lengths[c];
                int type = _types[c];
                String cell = _leis.readString(length);
                if (type == DbfTableModel.TYPE_NUMERIC && !cell.equals("")) {
                    record.add(c, new Double(cell));
                } else {
                    record.add(c, cell);
                }
            }
            _records.add(record);
            _leis.skipBytes(1);
        }
    }
}
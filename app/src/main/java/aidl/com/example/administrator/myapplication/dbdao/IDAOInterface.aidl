// IDAOInterface.aidl
package com.example.administrator.myapplication.dbdao;

// Declare any non-default types here with import statements

interface IDAOInterface {

       	String initRecordDB();
       	String getResult(String sql);

        String executeRawSQL(String sql);
        void closeDB();
}

package server.controller;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;

public class SQLDataAdapter extends XmlAdapter<java.util.Date, java.sql.Date>{
    @Override
    public Date unmarshal(java.util.Date v) throws Exception {
        if (v==null) return null;
        return new Date(v.getTime());
    }

    @Override
    public java.util.Date marshal(Date v) throws Exception {
        if (v == null) return null;
        return new java.util.Date(v.getTime());
    }
}

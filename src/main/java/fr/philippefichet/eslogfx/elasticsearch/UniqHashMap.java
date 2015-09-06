/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.philippefichet.eslogfx.elasticsearch;

import java.util.HashMap;

/**
 *
 * @author glopinous
 */
public class UniqHashMap extends HashMap<String, String>{

    @Override
    public int hashCode() {
        String id = get("_id");
        System.out.println("hashCode _id = " + id);
        return id != null ? id.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        String id = get("_id");
        if (o instanceof UniqHashMap && id != null) {
            UniqHashMap u = (UniqHashMap)o;
            return id.equals(u.get("_id"));
        }
        return false;
        
    }
}

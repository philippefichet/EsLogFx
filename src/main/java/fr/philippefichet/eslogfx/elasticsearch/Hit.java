/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.philippefichet.eslogfx.elasticsearch;

import java.util.Map;

/**
 *
 * @author philippefichet
 */
public class Hit {
    private String _index;
    private String _type;
    private String _id;
    private Map<String, String> _source;

    public String getIndex() {
        return _index;
    }

    public void setIndex(String _index) {
        this._index = _index;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Map<String, String> getSource() {
        return _source;
    }

    public void setSource(Map<String, String> _source) {
        this._source = _source;
    }
    
}

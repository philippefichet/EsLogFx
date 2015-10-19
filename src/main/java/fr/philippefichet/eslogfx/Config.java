/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.philippefichet.eslogfx;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author philippefichet
 */
public class Config {
    private static Config instance;
    public final static String DATE_FORMAT_TIMESTAMP = "timestamp";
    private String url;
    private String login;
    private String password;
    private String dateFormat;
    private String dateFormatShow;
    private String fieldDate;
    private String fieldLevel;
    private String fieldMessage;
    private Integer fieldMessageNumberLine = 1;
    private Long rewindSecond = 7200L;
    private String authScope;
    private List<String> fieldExclude = new ArrayList<>();

    public static Config getInstance() {
        if (instance == null) {
            String configFile = System.getProperty("eslogfx.configfile");
            if (configFile != null) {
                Gson gson = new Gson();
                File file = new File(configFile);
                if (file.exists()) {
                    try {
                        instance = gson.fromJson(Files.newBufferedReader(file.toPath()), Config.class);
                    } catch(IOException | JsonSyntaxException | JsonIOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (instance == null) {
                instance = new Config();
            }
        }
        return instance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getFieldDate() {
        return fieldDate;
    }

    public void setFieldDate(String fieldDate) {
        this.fieldDate = fieldDate;
    }

    public String getFieldLevel() {
        return fieldLevel;
    }

    public void setFieldLevel(String fieldLevel) {
        this.fieldLevel = fieldLevel;
    }

    public Long getRewindSecond() {
        return rewindSecond;
    }

    public void setRewindSecond(Long rewindSecond) {
        this.rewindSecond = rewindSecond;
    }

    public String getAuthScope() {
        return authScope;
    }

    public void setAuthScope(String authScope) {
        this.authScope = authScope;
    }

    public String getFieldMessage() {
        return fieldMessage;
    }

    public void setFieldMessage(String fieldMessage) {
        this.fieldMessage = fieldMessage;
    }

    public Integer getFieldMessageNumberLine() {
        return fieldMessageNumberLine;
    }

    public void setFieldMessageNumberLine(Integer fieldMessageNumberLine) {
        this.fieldMessageNumberLine = fieldMessageNumberLine;
    }

    public List<String> getFieldExclude() {
        return fieldExclude;
    }

    public void setFieldExclude(List<String>fieldExlude) {
        this.fieldExclude = fieldExlude;
    }

    public String getDateFormatShow() {
        return dateFormatShow;
    }

    public void setDateFormatShow(String dateFormatShow) {
        this.dateFormatShow = dateFormatShow;
    }

    @Override
    public String toString() {
        return "Config{" + "url=" + url + ", login=" + login + ", password=" + password + ", dateFormat=" + dateFormat + ", fieldDate=" + fieldDate + ", fieldLevel=" + fieldLevel + ", rewindSecond=" + rewindSecond + ", authScope=" + authScope + '}';
    }
}
